package com.mediguard.central.enums;

import lombok.Getter;

/**
 * 患者状态枚举
 */
@Getter
public enum PatientStatus {
    NORMAL("NORMAL", "正常"),
    WARNING("WARNING", "警告"),
    CRITICAL("CRITICAL", "危急");

    private final String code;
    private final String label;

    PatientStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
