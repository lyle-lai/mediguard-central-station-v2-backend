package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.mediguard.central.enums.DeviceType;
import com.mediguard.central.enums.PatientStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 患者实体类（移除displayConfig，改为床位绑定）
 */
@Data
@TableName("pat_patient")
public class PatientEntity {

    /**
     * 患者ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 住院号
     */
    private String admissionId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String gender;

    /**
     * 所属科室
     */
    private String departmentCode;

    /**
     * 床位号
     */
    private String bedNumber;

    /**
     * 绑定设备ID
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private DeviceType deviceType;

    /**
     * 状态 (NORMAL/WARNING/CRITICAL)
     */
    private PatientStatus status;

    /**
     * 入科时间
     */
    private LocalDateTime admissionTime;

    /**
     * 出科时间
     */
    private LocalDateTime dischargeTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
