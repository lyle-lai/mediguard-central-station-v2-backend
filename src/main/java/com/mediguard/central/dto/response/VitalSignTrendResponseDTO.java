package com.mediguard.central.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 生命体征趋势响应 DTO
 */
@Data
public class VitalSignTrendResponseDTO {

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 动态参数值（key: 参数ID, value: 参数值）
     */
    private Map<String, String> parameters;
}
