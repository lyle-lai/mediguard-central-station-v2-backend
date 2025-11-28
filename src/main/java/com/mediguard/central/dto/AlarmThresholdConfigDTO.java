package com.mediguard.central.dto;

import lombok.Data;

/**
 * 报警阈值配置 DTO
 */
@Data
public class AlarmThresholdConfigDTO {
    /**
     * 参数ID
     */
    private String paramId;

    /**
     * 参数标签
     */
    private String label;

    /**
     * 最小值
     */
    private Integer min;

    /**
     * 最大值
     */
    private Integer max;

    /**
     * 延迟时间(秒)
     */
    private Integer delay;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
