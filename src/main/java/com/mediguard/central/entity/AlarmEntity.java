package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.mediguard.central.enums.AlarmCategory;
import com.mediguard.central.enums.AlarmLevel;
import com.mediguard.central.enums.DeviceType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 报警实体类
 */
@Data
@TableName(value = "alm_alarm", autoResultMap = true)
public class AlarmEntity {

    /**
     * 报警ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 报警时间
     */
    private LocalDateTime alarmTime;

    /**
     * 患者ID
     */
    private String patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 床位号
     */
    private String bedNumber;

    /**
     * 科室代码
     */
    private String departmentCode;

    /**
     * 设备类型
     */
    private DeviceType deviceType;

    /**
     * 报警分类 (PHYSIOLOGICAL/TECHNICAL)
     */
    private AlarmCategory category;

    /**
     * 报警级别 (CRITICAL/WARNING/NORMAL)
     */
    private AlarmLevel level;

    /**
     * 报警信息
     */
    private String message;

    /**
     * 是否确认
     */
    private Boolean acknowledged;

    /**
     * 报警快照 (JSON)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> snapshot;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
