package com.mediguard.central.dto;

import lombok.Data;

/**
 * 科室偏好设置 DTO
 */
@Data
public class DepartmentPreferencesDTO {

    /**
     * 是否演示模式
     */
    private Boolean isDemoMode;

    /**
     * 模拟速度（倍数）
     */
    private Integer simulationSpeed;

    /**
     * 夜间模式
     */
    private Boolean nightMode;

    /**
     * 音频启用
     */
    private Boolean audioEnabled;
}
