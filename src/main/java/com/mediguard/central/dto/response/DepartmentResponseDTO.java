package com.mediguard.central.dto.response;

import lombok.Data;

/**
 * 科室响应 DTO
 */
@Data
public class DepartmentResponseDTO {

    /**
     * 科室ID
     */
    private String id;

    /**
     * 科室代码
     */
    private String code;

    /**
     * 科室名称
     */
    private String name;

    /**
     * 床位容量
     */
    private Integer capacity;
}
