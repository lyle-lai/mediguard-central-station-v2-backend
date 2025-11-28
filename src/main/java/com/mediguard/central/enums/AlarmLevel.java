package com.mediguard.central.enums;

import lombok.Getter;

/**
 * 报警级别枚举
 */
@Getter
public enum AlarmLevel {
    CRITICAL("CRITICAL", "危急"),
    WARNING("WARNING", "警告"),
    NORMAL("NORMAL", "正常");

    private final String code;
    private final String label;

    AlarmLevel(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
