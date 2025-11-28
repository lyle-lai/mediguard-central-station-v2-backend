package com.mediguard.central.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediguard.central.dto.AlarmThresholdConfigDTO;
import com.mediguard.central.dto.DepartmentCapacityDTO;
import com.mediguard.central.dto.DeviceDisplayConfigDTO;
import com.mediguard.central.dto.response.DepartmentConfigResponseDTO;
import com.mediguard.central.entity.*;
import com.mediguard.central.enums.AlarmLevel;
import com.mediguard.central.enums.DeviceType;
import com.mediguard.central.mapper.*;
import com.mediguard.central.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 科室服务实现类（重构版）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity>
        implements DepartmentService {

    private final DeviceWaveformConfigMapper waveformConfigMapper;
    private final DeviceParameterConfigMapper parameterConfigMapper;
    private final AlarmThresholdConfigMapper thresholdConfigMapper;
    private final BedLabelConfigMapper bedLabelConfigMapper;

    @Override
    public DepartmentConfigResponseDTO getConfig(String departmentCode) {
        log.info("【科室配置】获取科室配置，科室={}", departmentCode);
        DepartmentEntity dept = this.getOne(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, departmentCode));

        if (dept == null) {
            throw new RuntimeException("科室不存在");
        }

        DepartmentConfigResponseDTO response = new DepartmentConfigResponseDTO();

        // 1. 床位容量
        response.setDeptCapacity(Map.of(dept.getCode(), dept.getCapacity()));

        // 2. 床位标签
        List<BedLabelConfigEntity> bedLabels = bedLabelConfigMapper.selectList(
                new LambdaQueryWrapper<BedLabelConfigEntity>()
                        .eq(BedLabelConfigEntity::getDepartmentCode, departmentCode));
        Map<String, String> bedLabelMap = bedLabels.stream()
                .collect(Collectors.toMap(BedLabelConfigEntity::getBedNumber, BedLabelConfigEntity::getCustomLabel));
        response.setBedLabels(Map.of(dept.getCode(), bedLabelMap));

        // 3. 设备配置（波形和参数）
        Map<String, DepartmentConfigResponseDTO.DeviceConfigDTO> deviceConfigs = new HashMap<>();
        for (DeviceType deviceType : DeviceType.values()) {
            DepartmentConfigResponseDTO.DeviceConfigDTO config = new DepartmentConfigResponseDTO.DeviceConfigDTO();

            // 波形配置
            List<DeviceWaveformConfigEntity> waveforms = waveformConfigMapper.selectList(
                    new LambdaQueryWrapper<DeviceWaveformConfigEntity>()
                            .eq(DeviceWaveformConfigEntity::getDepartmentCode, departmentCode)
                            .eq(DeviceWaveformConfigEntity::getDeviceType, deviceType)
                            .orderByAsc(DeviceWaveformConfigEntity::getDisplayOrder));
            List<DepartmentConfigResponseDTO.WaveformItemDTO> waveformList = waveforms.stream().map(w -> {
                DepartmentConfigResponseDTO.WaveformItemDTO dto = new DepartmentConfigResponseDTO.WaveformItemDTO();
                dto.setId(w.getWaveformId());
                dto.setLabel(w.getLabel());
                dto.setVisible(w.getVisible());
                dto.setColor(w.getColor());
                dto.setOrder(w.getDisplayOrder());
                return dto;
            }).collect(Collectors.toList());
            config.setWaveforms(waveformList);

            // 参数配置
            List<DeviceParameterConfigEntity> parameters = parameterConfigMapper.selectList(
                    new LambdaQueryWrapper<DeviceParameterConfigEntity>()
                            .eq(DeviceParameterConfigEntity::getDepartmentCode, departmentCode)
                            .eq(DeviceParameterConfigEntity::getDeviceType, deviceType)
                            .orderByAsc(DeviceParameterConfigEntity::getDisplayOrder));
            List<DepartmentConfigResponseDTO.ParameterItemDTO> parameterList = parameters.stream().map(p -> {
                DepartmentConfigResponseDTO.ParameterItemDTO dto = new DepartmentConfigResponseDTO.ParameterItemDTO();
                dto.setId(p.getParameterId());
                dto.setLabel(p.getLabel());
                dto.setUnit(p.getUnit());
                dto.setVisible(p.getVisible());
                dto.setOrder(p.getDisplayOrder());
                return dto;
            }).collect(Collectors.toList());
            config.setParameters(parameterList);

            deviceConfigs.put(deviceType.getCode(), config);
        }
        response.setDeviceConfigs(Map.of(dept.getCode(), deviceConfigs));

        // 4. 报警阈值
        List<AlarmThresholdConfigEntity> thresholds = thresholdConfigMapper.selectList(
                new LambdaQueryWrapper<AlarmThresholdConfigEntity>()
                        .eq(AlarmThresholdConfigEntity::getDepartmentCode, departmentCode));
        List<DepartmentConfigResponseDTO.AlarmThresholdDTO> thresholdList = thresholds.stream().map(t -> {
            DepartmentConfigResponseDTO.AlarmThresholdDTO dto = new DepartmentConfigResponseDTO.AlarmThresholdDTO();
            dto.setParamId(t.getParamId());
            dto.setLabel(t.getLabel());
            dto.setMin(t.getMinValue());
            dto.setMax(t.getMaxValue());
            dto.setDelay(t.getDelaySeconds());
            dto.setPriority(t.getPriority().getCode());
            dto.setEnabled(t.getEnabled());
            return dto;
        }).collect(Collectors.toList());
        response.setAlarmThresholds(Map.of(dept.getCode(), thresholdList));

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCapacity(String departmentCode, DepartmentCapacityDTO dto) {
        log.info("【科室配置】更新床位容量，科室={}，参数={}", departmentCode, dto);
        DepartmentEntity dept = this.getOne(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, departmentCode));
        if (dept == null)
            throw new RuntimeException("科室不存在");

        if (dto.getCapacity() != null) {
            dept.setCapacity(dto.getCapacity());
            this.updateById(dept);
        }

        // 更新床位标签
        if (dto.getBedLabels() != null) {
            for (Map.Entry<String, String> entry : dto.getBedLabels().entrySet()) {
                BedLabelConfigEntity label = bedLabelConfigMapper.selectOne(
                        new LambdaQueryWrapper<BedLabelConfigEntity>()
                                .eq(BedLabelConfigEntity::getDepartmentCode, departmentCode)
                                .eq(BedLabelConfigEntity::getBedNumber, entry.getKey()));
                if (label == null) {
                    label = new BedLabelConfigEntity();
                    label.setDepartmentCode(departmentCode);
                    label.setBedNumber(entry.getKey());
                    label.setCustomLabel(entry.getValue());
                    bedLabelConfigMapper.insert(label);
                } else {
                    label.setCustomLabel(entry.getValue());
                    bedLabelConfigMapper.updateById(label);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceConfigs(String departmentCode, List<DeviceDisplayConfigDTO> configs) {
        log.info("【科室配置】更新设备显示模板，科室={}", departmentCode);

        for (DeviceDisplayConfigDTO config : configs) {
            DeviceType deviceType = DeviceType.valueOf(config.getDeviceType());

            // 1. 更新波形配置
            if (config.getWaveforms() != null) {
                // 删除旧配置
                waveformConfigMapper.delete(new LambdaQueryWrapper<DeviceWaveformConfigEntity>()
                        .eq(DeviceWaveformConfigEntity::getDepartmentCode, departmentCode)
                        .eq(DeviceWaveformConfigEntity::getDeviceType, deviceType));

                // 插入新配置
                for (DeviceDisplayConfigDTO.WaveformConfigDTO waveform : config.getWaveforms()) {
                    DeviceWaveformConfigEntity entity = new DeviceWaveformConfigEntity();
                    entity.setDepartmentCode(departmentCode);
                    entity.setDeviceType(deviceType);
                    entity.setWaveformId(waveform.getId());
                    entity.setLabel(waveform.getLabel());
                    entity.setVisible(waveform.getVisible());
                    entity.setColor(waveform.getColor());
                    entity.setDisplayOrder(waveform.getOrder());
                    waveformConfigMapper.insert(entity);
                }
            }

            // 2. 更新参数配置
            if (config.getParameters() != null) {
                // 删除旧配置
                parameterConfigMapper.delete(new LambdaQueryWrapper<DeviceParameterConfigEntity>()
                        .eq(DeviceParameterConfigEntity::getDepartmentCode, departmentCode)
                        .eq(DeviceParameterConfigEntity::getDeviceType, deviceType));

                // 插入新配置
                for (DeviceDisplayConfigDTO.ParameterConfigDTO parameter : config.getParameters()) {
                    DeviceParameterConfigEntity entity = new DeviceParameterConfigEntity();
                    entity.setDepartmentCode(departmentCode);
                    entity.setDeviceType(deviceType);
                    entity.setParameterId(parameter.getId());
                    entity.setLabel(parameter.getLabel());
                    entity.setUnit(parameter.getUnit());
                    entity.setVisible(parameter.getVisible());
                    entity.setDisplayOrder(parameter.getOrder());
                    parameterConfigMapper.insert(entity);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAlarmThresholds(String departmentCode, List<AlarmThresholdConfigDTO> thresholds) {
        log.info("【科室配置】更新报警阈值，科室={}", departmentCode);
        // 删除旧配置
        thresholdConfigMapper.delete(new LambdaQueryWrapper<AlarmThresholdConfigEntity>()
                .eq(AlarmThresholdConfigEntity::getDepartmentCode, departmentCode));

        // 插入新配置
        for (AlarmThresholdConfigDTO threshold : thresholds) {
            AlarmThresholdConfigEntity entity = new AlarmThresholdConfigEntity();
            entity.setDepartmentCode(departmentCode);
            entity.setParamId(threshold.getParamId());
            entity.setLabel(threshold.getLabel());
            entity.setMinValue(threshold.getMin());
            entity.setMaxValue(threshold.getMax());
            entity.setDelaySeconds(threshold.getDelay());
            entity.setPriority(AlarmLevel.valueOf(threshold.getPriority()));
            entity.setEnabled(threshold.getEnabled());
            thresholdConfigMapper.insert(entity);
        }
    }
}
