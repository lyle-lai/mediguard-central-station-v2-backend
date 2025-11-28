package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 科室实体类（简化版，配置通过关联表管理）
 */
@Data
@TableName("sys_department")
public class DepartmentEntity {

    /**
     * 科室ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 科室代码
     */
    private String code;

    /**
     * 科室名称
     */
    private String name;

    /**
     * 床位容量
     */
    private Integer capacity;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
