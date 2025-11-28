package com.mediguard.central.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建科室响应 DTO（简化版）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDepartmentResponseDTO {

    /**
     * 科室ID
     */
    private String id;

    /**
     * 科室代码
     */
    private String code;
}
