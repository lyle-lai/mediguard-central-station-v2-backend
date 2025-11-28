package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mediguard.central.enums.AlarmLevel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报警阈值配置实体类
 */
@Data
@TableName("cfg_alarm_threshold")
public class AlarmThresholdConfigEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 科室代码
     */
    private String departmentCode;

    /**
     * 参数ID
     */
    private String paramId;

    /**
     * 参数标签
     */
    private String label;

    /**
     * 最小值
     */
    private Integer minValue;

    /**
     * 最大值
     */
    private Integer maxValue;

    /**
     * 延迟时间（秒）
     */
    private Integer delaySeconds;

    /**
     * 优先级
     */
    private AlarmLevel priority;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
