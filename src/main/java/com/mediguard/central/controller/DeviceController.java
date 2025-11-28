package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.response.DeviceResponseDTO;
import com.mediguard.central.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 获取可用设备列表
     */
    @GetMapping("/available")
    public Result<List<DeviceResponseDTO>> getAvailableDevices() {
        log.info("【设备管理】接收到获取可用设备请求");
        return Result.success(deviceService.getAvailableDevices());
    }
}
