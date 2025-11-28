package com.mediguard.central.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediguard.central.dto.response.AlarmSnapshotResponseDTO;
import com.mediguard.central.entity.AlarmEntity;
import com.mediguard.central.mapper.AlarmMapper;
import com.mediguard.central.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报警服务实现类
 */
@Slf4j
@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, AlarmEntity> implements AlarmService {

    @Override
    public List<AlarmEntity> getHistory(String department) {
        log.info("【报警管理】获取报警历史，科室={}", department);
        LambdaQueryWrapper<AlarmEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(department)) {
            wrapper.eq(AlarmEntity::getDepartmentCode, department);
        }
        wrapper.orderByDesc(AlarmEntity::getAlarmTime);
        return this.list(wrapper);
    }

    @Override
    public AlarmSnapshotResponseDTO getSnapshot(String alarmId) {
        log.info("【报警管理】获取报警快照，ID={}", alarmId);
        AlarmEntity alarm = this.getById(alarmId);
        if (alarm == null) {
            throw new RuntimeException("报警记录不存在");
        }

        AlarmSnapshotResponseDTO response = new AlarmSnapshotResponseDTO();
        response.setId(alarm.getId());
        response.setTimestamp(alarm.getAlarmTime().toString());

        // 从快照 JSON 中提取参数和波形（如果存在）
        // 结构参考 api.md 6.2 节
        if (alarm.getSnapshot() != null) {
            Map<String, Object> snapshot = alarm.getSnapshot();

            // 解析参数列表
            if (snapshot.containsKey("parameters")) {
                List<Map<String, Object>> paramList = (List<Map<String, Object>>) snapshot.get("parameters");
                List<AlarmSnapshotResponseDTO.ParameterDTO> parameters = paramList.stream().map(p -> {
                    AlarmSnapshotResponseDTO.ParameterDTO dto = new AlarmSnapshotResponseDTO.ParameterDTO();
                    dto.setId((String) p.get("id"));
                    dto.setLabel((String) p.get("label"));
                    // value 可以是数值或字符串 (e.g. "130/85")
                    dto.setValue(p.get("value"));
                    dto.setUnit((String) p.get("unit"));
                    dto.setIsAlarm((Boolean) p.get("isAlarm"));
                    return dto;
                }).collect(Collectors.toList());
                response.setParameters(parameters);
            }

            // 解析波形列表
            if (snapshot.containsKey("waveforms")) {
                List<Map<String, Object>> waveList = (List<Map<String, Object>>) snapshot.get("waveforms");
                List<AlarmSnapshotResponseDTO.WaveformDTO> waveforms = waveList.stream().map(w -> {
                    AlarmSnapshotResponseDTO.WaveformDTO dto = new AlarmSnapshotResponseDTO.WaveformDTO();
                    dto.setId((String) w.get("id"));
                    dto.setLabel((String) w.get("label"));
                    dto.setColor((String) w.get("color"));
                    // data 是数值数组
                    dto.setData((List<Number>) w.get("data"));
                    return dto;
                }).collect(Collectors.toList());
                response.setWaveforms(waveforms);
            }
        }

        return response;
    }
}
