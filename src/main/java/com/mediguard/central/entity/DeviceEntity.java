package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mediguard.central.enums.DeviceStatus;
import com.mediguard.central.enums.DeviceType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备实体类
 */
@Data
@TableName("dev_device")
public class DeviceEntity {

    /**
     * 设备ID
     */
    @TableId(type = IdType.INPUT)
    private String deviceId;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 设备类型
     */
    private DeviceType deviceType;

    /**
     * 所属科室
     */
    private String departmentCode;

    /**
     * 状态 (ONLINE/OFFLINE)
     */
    private DeviceStatus status;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
