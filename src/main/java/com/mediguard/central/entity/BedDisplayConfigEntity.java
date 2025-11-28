package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 床位显示配置实体类（绑定床位而非患者）
 */
@Data
@TableName("cfg_bed_display")
public class BedDisplayConfigEntity {

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
     * 床位号
     */
    private String bedNumber;

    /**
     * 波形ID
     */
    private String waveformId;

    /**
     * 参数ID
     */
    private String parameterId;

    /**
     * 是否可见
     */
    private Boolean visible;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
