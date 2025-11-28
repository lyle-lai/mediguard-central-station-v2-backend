package com.mediguard.central.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 生命体征数据实体类（动态参数）
 */
@Data
@TableName("pat_vital_sign_data")
public class VitalSignDataEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 患者ID
     */
    private String patientId;

    /**
     * 采集时间
     */
    private LocalDateTime timestamp;

    /**
     * 参数ID
     */
    private String parameterId;

    /**
     * 参数值
     */
    private String parameterValue;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
