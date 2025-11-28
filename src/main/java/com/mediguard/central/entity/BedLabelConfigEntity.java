package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 床位标签配置实体类
 */
@Data
@TableName("cfg_bed_label")
public class BedLabelConfigEntity {

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
     * 自定义标签
     */
    private String customLabel;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
