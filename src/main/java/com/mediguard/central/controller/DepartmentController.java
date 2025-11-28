package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.AlarmThresholdConfigDTO;
import com.mediguard.central.dto.DepartmentCapacityDTO;
import com.mediguard.central.dto.DeviceDisplayConfigDTO;
import com.mediguard.central.dto.response.DepartmentConfigResponseDTO;
import com.mediguard.central.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室配置控制器
 */
@Slf4j
@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 获取科室完整配置
     */
    @GetMapping("/{departmentCode}/config")
    public Result<DepartmentConfigResponseDTO> getConfig(@PathVariable String departmentCode) {
        log.info("【科室配置】接收到获取配置请求，科室={}", departmentCode);
        return Result.success(departmentService.getConfig(departmentCode));
    }

    /**
     * 更新床位容量与标签
     */
    @PutMapping("/{departmentCode}/capacity")
    public Result<Void> updateCapacity(@PathVariable String departmentCode, @RequestBody DepartmentCapacityDTO dto) {
        log.info("【科室配置】接收到更新容量请求，科室={}", departmentCode);
        departmentService.updateCapacity(departmentCode, dto);
        return Result.success();
    }

    /**
     * 更新设备显示模板
     */
    @PutMapping("/{departmentCode}/device-configs")
    public Result<Void> updateDeviceConfigs(@PathVariable String departmentCode,
            @RequestBody List<DeviceDisplayConfigDTO> configs) {
        log.info("【科室配置】接收到更新设备模板请求，科室={}", departmentCode);
        departmentService.updateDeviceConfigs(departmentCode, configs);
        return Result.success();
    }

    /**
     * 更新报警阈值策略
     */
    @PutMapping("/{departmentCode}/alarm-thresholds")
    public Result<Void> updateAlarmThresholds(@PathVariable String departmentCode,
            @RequestBody List<AlarmThresholdConfigDTO> thresholds) {
        log.info("【科室配置】接收到更新报警阈值请求，科室={}", departmentCode);
        departmentService.updateAlarmThresholds(departmentCode, thresholds);
        return Result.success();
    }
}
