package com.mediguard.central.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediguard.central.dto.BedDisplayConfigDTO;
import com.mediguard.central.dto.PatientAdmissionDTO;
import com.mediguard.central.dto.response.PatientResponseDTO;
import com.mediguard.central.dto.response.VitalSignTrendResponseDTO;
import com.mediguard.central.entity.BedDisplayConfigEntity;
import com.mediguard.central.entity.PatientEntity;
import com.mediguard.central.entity.VitalSignDataEntity;
import com.mediguard.central.enums.PatientStatus;
import com.mediguard.central.mapper.BedDisplayConfigMapper;
import com.mediguard.central.mapper.PatientMapper;
import com.mediguard.central.mapper.VitalSignDataMapper;
import com.mediguard.central.service.PatientService;
import com.mediguard.central.service.StompVitalSignsPushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 患者服务实现类（重构版）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl extends ServiceImpl<PatientMapper, PatientEntity> implements PatientService {

    private final VitalSignDataMapper vitalSignDataMapper;
    private final BedDisplayConfigMapper bedDisplayConfigMapper;
    private final StompVitalSignsPushService pushService;

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        log.info("【患者管理】获取所有在床患者信息");
        List<PatientEntity> patients = this.list();

        return patients.stream().map(p -> {
            PatientResponseDTO dto = new PatientResponseDTO();
            dto.setId(p.getId());
            dto.setDeviceId(p.getDeviceId());
            dto.setBedNumber(p.getBedNumber());
            dto.setDepartment(p.getDepartmentCode());
            dto.setDeviceType(p.getDeviceType().getCode());
            dto.setConnectedDevices(List.of(p.getDeviceType().getCode()));
            dto.setName(p.getName());
            dto.setAge(p.getAge());
            dto.setGender(p.getGender());
            dto.setAdmissionId(p.getAdmissionId());
            dto.setStatus(p.getStatus().getCode());
            dto.setActiveAlarm(null);
            dto.setParameters(new ArrayList<>());
            dto.setWaveforms(new ArrayList<>());
            dto.setAlarmDuration(0);
            dto.setViolationCounters(new HashMap<>());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String admitPatient(PatientAdmissionDTO dto) {
        log.info("【患者管理】患者入科，参数={}", dto);

        PatientEntity patient = new PatientEntity();
        patient.setName(dto.getName());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setAdmissionId(dto.getAdmissionId());
        patient.setBedNumber(dto.getBedNumber());
        patient.setDepartmentCode(dto.getDepartment());
        patient.setDeviceId(dto.getDeviceId());
        patient.setDeviceType(com.mediguard.central.enums.DeviceType.valueOf(dto.getDeviceType()));
        patient.setStatus(PatientStatus.NORMAL);
        patient.setAdmissionTime(LocalDateTime.now());

        this.save(patient);
        log.info("【患者管理】患者入科成功，ID={}", patient.getId());

        // 触发推送服务刷新缓存
        pushService.forceRefreshCache();

        return patient.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dischargePatient(String patientId) {
        log.info("【患者管理】患者出科，ID={}", patientId);
        PatientEntity patient = this.getById(patientId);
        if (patient == null)
            throw new RuntimeException("患者不存在");

        this.removeById(patientId);

        // 触发推送服务刷新缓存
        pushService.forceRefreshCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDisplayConfig(String bedNumber, BedDisplayConfigDTO dto) {
        log.info("【患者管理】更新床位显示配置，床位号={}，配置={}", bedNumber, dto);

        // 假设当前上下文中有科室信息，或者从入参获取。这里简化处理，假设所有床位号唯一或通过其他方式获取科室
        // 实际场景可能需要 departmentCode + bedNumber 才能唯一确定
        // 这里暂时通过查询患者来获取科室代码，如果床位空闲可能需要额外处理
        // 或者入参 DTO 应该包含 departmentCode

        // 查找该床位的患者以获取科室代码
        PatientEntity patient = this.getOne(new LambdaQueryWrapper<PatientEntity>()
                .eq(PatientEntity::getBedNumber, bedNumber)
                .last("LIMIT 1"));

        String departmentCode = (patient != null) ? patient.getDepartmentCode() : "ICU"; // 默认或错误处理

        // 删除该床位的旧配置
        bedDisplayConfigMapper.delete(new LambdaQueryWrapper<BedDisplayConfigEntity>()
                .eq(BedDisplayConfigEntity::getDepartmentCode, departmentCode)
                .eq(BedDisplayConfigEntity::getBedNumber, bedNumber));

        // 插入新配置（波形）
        if (dto.getVisibleWaveformIds() != null) {
            for (String waveformId : dto.getVisibleWaveformIds()) {
                BedDisplayConfigEntity config = new BedDisplayConfigEntity();
                config.setDepartmentCode(departmentCode);
                config.setBedNumber(bedNumber);
                config.setWaveformId(waveformId);
                config.setVisible(true);
                bedDisplayConfigMapper.insert(config);
            }
        }

        // 插入新配置（参数）
        if (dto.getVisibleParameterIds() != null) {
            for (String parameterId : dto.getVisibleParameterIds()) {
                BedDisplayConfigEntity config = new BedDisplayConfigEntity();
                config.setDepartmentCode(departmentCode);
                config.setBedNumber(bedNumber);
                config.setParameterId(parameterId);
                config.setVisible(true);
                bedDisplayConfigMapper.insert(config);
            }
        }
    }

    @Override
    public List<VitalSignTrendResponseDTO> getTrends(String patientId, Integer rangeHours) {
        log.info("【患者管理】获取历史趋势，ID={}，范围={}小时", patientId, rangeHours);
        LocalDateTime startTime = LocalDateTime.now().minusHours(rangeHours);

        // 查询动态参数数据
        List<VitalSignDataEntity> dataList = vitalSignDataMapper.selectList(
                new LambdaQueryWrapper<VitalSignDataEntity>()
                        .eq(VitalSignDataEntity::getPatientId, patientId)
                        .ge(VitalSignDataEntity::getTimestamp, startTime)
                        .orderByAsc(VitalSignDataEntity::getTimestamp));

        // 按时间戳分组
        Map<LocalDateTime, Map<String, String>> groupedData = new LinkedHashMap<>();
        for (VitalSignDataEntity data : dataList) {
            groupedData.computeIfAbsent(data.getTimestamp(), k -> new HashMap<>())
                    .put(data.getParameterId(), data.getParameterValue());
        }

        // 转换为响应 DTO
        return groupedData.entrySet().stream().map(entry -> {
            VitalSignTrendResponseDTO dto = new VitalSignTrendResponseDTO();
            dto.setTimestamp(entry.getKey());
            dto.setParameters(entry.getValue());
            return dto;
        }).collect(Collectors.toList());
    }
}
