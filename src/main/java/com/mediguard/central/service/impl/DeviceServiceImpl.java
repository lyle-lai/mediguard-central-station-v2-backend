package com.mediguard.central.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediguard.central.dto.response.DeviceResponseDTO;
import com.mediguard.central.entity.DeviceEntity;
import com.mediguard.central.entity.PatientEntity;
import com.mediguard.central.enums.DeviceStatus;
import com.mediguard.central.mapper.DeviceMapper;
import com.mediguard.central.mapper.PatientMapper;
import com.mediguard.central.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceService {

        private final PatientMapper patientMapper;

        @Override
        public List<DeviceResponseDTO> getAvailableDevices() {
                log.info("【设备管理】获取可用设备列表");

                // 获取所有在线设备
                List<DeviceEntity> onlineDevices = this.list(new LambdaQueryWrapper<DeviceEntity>()
                                .eq(DeviceEntity::getStatus, DeviceStatus.ONLINE));

                // 获取已分配的设备ID
                List<PatientEntity> patients = patientMapper.selectList(null);
                Set<String> assignedDeviceIds = patients.stream()
                                .map(PatientEntity::getDeviceId)
                                .collect(Collectors.toSet());

                // 过滤出未分配的设备
                return onlineDevices.stream()
                                .filter(device -> !assignedDeviceIds.contains(device.getDeviceId()))
                                .map(device -> {
                                        DeviceResponseDTO dto = new DeviceResponseDTO();
                                        dto.setDeviceId(device.getDeviceId());
                                        dto.setSerialNumber(device.getSerialNumber());
                                        dto.setDeviceType(device.getDeviceType().getCode());
                                        dto.setDepartmentCode(device.getDepartmentCode());
                                        dto.setStatus(device.getStatus().getCode());
                                        dto.setIpAddress(device.getIpAddress());
                                        return dto;
                                }).collect(Collectors.toList());
        }
}
