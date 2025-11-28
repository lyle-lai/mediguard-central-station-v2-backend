package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mediguard.central.enums.DeviceType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备波形配置实体类
 */
@Data
@TableName("cfg_device_waveform")
public class DeviceWaveformConfigEntity {

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
     * 设备类型
     */
    private DeviceType deviceType;

    /**
     * 波形ID
     */
    private String waveformId;

    /**
     * 波形标签
     */
    private String label;

    /**
     * 是否可见
     */
    private Boolean visible;

    /**
     * 颜色
     */
    private String color;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
