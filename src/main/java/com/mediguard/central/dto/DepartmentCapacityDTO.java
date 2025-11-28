package com.mediguard.central.dto;

import lombok.Data;

import java.util.Map;

/**
 * 科室容量更新请求参数
 */
@Data
public class DepartmentCapacityDTO {

    /**
     * 床位容量
     */
    private Integer capacity;

    /**
     * 床位标签
     */
    private Map<String, String> bedLabels;
}
