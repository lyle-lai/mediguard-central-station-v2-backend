package com.mediguard.central.enums;

import lombok.Getter;

/**
 * 设备类型枚举
 */
@Getter
public enum DeviceType {
    MONITOR("MONITOR", "监护仪"),
    VENTILATOR("VENTILATOR", "呼吸机"),
    ANESTHESIA("ANESTHESIA", "麻醉机");

    private final String code;
    private final String label;

    DeviceType(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
