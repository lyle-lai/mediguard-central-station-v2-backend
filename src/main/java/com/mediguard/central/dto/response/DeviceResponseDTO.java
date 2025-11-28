package com.mediguard.central.dto.response;

import lombok.Data;

/**
 * 设备响应 DTO
 */
@Data
public class DeviceResponseDTO {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 所属科室
     */
    private String departmentCode;

    /**
     * 状态
     */
    private String status;

    /**
     * IP地址
     */
    private String ipAddress;
}
