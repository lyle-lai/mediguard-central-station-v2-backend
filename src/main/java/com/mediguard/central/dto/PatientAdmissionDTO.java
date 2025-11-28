package com.mediguard.central.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 患者入科请求参数
 */
@Data
public class PatientAdmissionDTO {

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    private Integer age;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    private String gender;

    /**
     * 住院号
     */
    @NotBlank(message = "住院号不能为空")
    private String admissionId;

    /**
     * 床位号
     */
    @NotBlank(message = "床位号不能为空")
    private String bedNumber;

    /**
     * 所属科室
     */
    @NotBlank(message = "所属科室不能为空")
    private String department;

    /**
     * 设备ID
     */
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 设备类型
     */
    @NotBlank(message = "设备类型不能为空")
    private String deviceType;
}
