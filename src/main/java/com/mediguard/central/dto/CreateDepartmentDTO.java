package com.mediguard.central.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 创建科室请求 DTO
 */
@Data
public class CreateDepartmentDTO {

    /**
     * 科室代码
     */
    @NotBlank(message = "科室代码不能为空")
    private String code;

    /**
     * 科室名称
     */
    @NotBlank(message = "科室名称不能为空")
    private String name;

    /**
     * 床位容量
     */
    @NotNull(message = "床位容量不能为空")
    private Integer capacity;
}
