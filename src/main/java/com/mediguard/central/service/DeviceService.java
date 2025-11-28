package com.mediguard.central.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mediguard.central.dto.response.DeviceResponseDTO;
import com.mediguard.central.entity.DeviceEntity;

import java.util.List;

/**
 * 设备服务接口
 */
public interface DeviceService extends IService<DeviceEntity> {

    /**
     * 获取可用设备列表
     * 
     * @return 设备列表
     */
    List<DeviceResponseDTO> getAvailableDevices();
}
