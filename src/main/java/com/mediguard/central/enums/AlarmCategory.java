package com.mediguard.central.enums;

import lombok.Getter;

/**
 * 报警分类枚举
 */
@Getter
public enum AlarmCategory {
    PHYSIOLOGICAL("PHYSIOLOGICAL", "生理报警"),
    TECHNICAL("TECHNICAL", "技术报警");

    private final String code;
    private final String label;

    AlarmCategory(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
