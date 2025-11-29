package com.mediguard.central.dto;

import lombok.Data;

/**
 * 参数配置 DTO
 */
@Data
public class ParameterConfigDTO {

    /**
     * 参数ID
     */
    private String id;

    /**
     * 参数标签
     */
    private String label;

    /**
     * 单位
     */
    private String unit;

    /**
     * 是否可见
     */
    private Boolean visible;

    /**
     * 显示顺序
     */
    private Integer order;
}
