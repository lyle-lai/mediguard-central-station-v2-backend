package com.mediguard.central.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediguard.central.dto.response.SystemDictionaryResponseDTO;
import com.mediguard.central.entity.DepartmentEntity;
import com.mediguard.central.entity.DictionaryEntity;
import com.mediguard.central.enums.AlarmLevel;
import com.mediguard.central.enums.DeviceType;
import com.mediguard.central.mapper.DepartmentMapper;
import com.mediguard.central.mapper.DictionaryMapper;
import com.mediguard.central.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统服务实现类（使用枚举）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemServiceImpl extends ServiceImpl<DictionaryMapper, DictionaryEntity> implements SystemService {

    private final DepartmentMapper departmentMapper;

    @Override
    public SystemDictionaryResponseDTO getDictionaries() {
        log.info("【系统配置】获取系统字典数据");
        SystemDictionaryResponseDTO response = new SystemDictionaryResponseDTO();

        // 1. 设备类型（从枚举获取）
        List<SystemDictionaryResponseDTO.DictItemDTO> deviceTypes = Arrays.stream(DeviceType.values())
                .map(type -> {
                    SystemDictionaryResponseDTO.DictItemDTO dto = new SystemDictionaryResponseDTO.DictItemDTO();
                    dto.setCode(type.getCode());
                    dto.setLabel(type.getLabel());
                    return dto;
                }).collect(Collectors.toList());
        response.setDeviceTypes(deviceTypes);

        // 2. 报警级别（从枚举获取）
        List<SystemDictionaryResponseDTO.DictItemDTO> alarmLevels = Arrays.stream(AlarmLevel.values())
                .map(level -> {
                    SystemDictionaryResponseDTO.DictItemDTO dto = new SystemDictionaryResponseDTO.DictItemDTO();
                    dto.setCode(level.getCode());
                    dto.setLabel(level.getLabel());
                    return dto;
                }).collect(Collectors.toList());
        response.setAlarmLevels(alarmLevels);

        // 3. 科室列表
        List<DepartmentEntity> departments = departmentMapper.selectList(null);
        List<SystemDictionaryResponseDTO.DepartmentItemDTO> deptList = departments.stream()
                .map(d -> {
                    SystemDictionaryResponseDTO.DepartmentItemDTO dto = new SystemDictionaryResponseDTO.DepartmentItemDTO();
                    dto.setCode(d.getCode());
                    dto.setLabel(d.getName());
                    dto.setCapacity(d.getCapacity());
                    return dto;
                }).collect(Collectors.toList());
        response.setDepartments(deptList);

        return response;
    }
}
