package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.AlarmThresholdConfigDTO;
import com.mediguard.central.dto.DepartmentCapacityDTO;
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
     * 更新设备波形配置
     */
    @PutMapping("/{departmentCode}/device-configs/{deviceType}/waveforms")
    public Result<Void> updateWaveformConfigs(
            @PathVariable String departmentCode,
            @PathVariable String deviceType,
            @RequestBody List<com.mediguard.central.dto.WaveformConfigDTO> waveforms) {
        log.info("【科室配置】接收到更新设备波形配置请求，科室={}，设备类型={}", departmentCode, deviceType);
        departmentService.updateWaveformConfigs(departmentCode, deviceType, waveforms);
        return Result.success();
    }

    /**
     * 更新设备参数配置
     */
    @PutMapping("/{departmentCode}/device-configs/{deviceType}/parameters")
    public Result<Void> updateParameterConfigs(
            @PathVariable String departmentCode,
            @PathVariable String deviceType,
            @RequestBody List<com.mediguard.central.dto.ParameterConfigDTO> parameters) {
        log.info("【科室配置】接收到更新设备参数配置请求，科室={}，设备类型={}", departmentCode, deviceType);
        departmentService.updateParameterConfigs(departmentCode, deviceType, parameters);
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

    /**
     * 更新科室偏好设置
     */
    @PutMapping("/{departmentCode}/preferences")
    public Result<Void> updatePreferences(@PathVariable String departmentCode,
            @RequestBody com.mediguard.central.dto.DepartmentPreferencesDTO preferences) {
        log.info("【科室配置】接收到更新偏好设置请求，科室={}", departmentCode);
        departmentService.updatePreferences(departmentCode, preferences);
        return Result.success();
    }
}
