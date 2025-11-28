package com.mediguard.central.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 生命体征数据推送 DTO
 */
@Data
public class VitalSignsPushDTO {

    /**
     * 患者ID
     */
    private String patientId;

    /**
     * 床位号
     */
    private String bedNumber;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 参数数据
     */
    private Map<String, Object> parameters;

    /**
     * 波形数据
     */
    private Map<String, List<Number>> waveforms;
}
