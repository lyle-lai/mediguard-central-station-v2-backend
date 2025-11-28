package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户科室关联实体类
 */
@Data
@TableName("sys_user_department")
public class UserDepartmentEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 科室代码
     */
    private String departmentCode;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
