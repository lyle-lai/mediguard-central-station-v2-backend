package com.mediguard.central.dto;

import lombok.Data;

/**
 * 波形配置 DTO
 */
@Data
public class WaveformConfigDTO {

    /**
     * 波形ID
     */
    private String id;

    /**
     * 波形标签
     */
    private String label;

    /**
     * 是否可见
     */
    private Boolean visible;

    /**
     * 颜色
     */
    private String color;

    /**
     * 显示顺序
     */
    private Integer order;
}
