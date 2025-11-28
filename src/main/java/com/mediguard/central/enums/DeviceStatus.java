package com.mediguard.central.enums;

import lombok.Getter;

/**
 * 设备状态枚举
 */
@Getter
public enum DeviceStatus {
    ONLINE("ONLINE", "在线"),
    OFFLINE("OFFLINE", "离线");

    private final String code;
    private final String label;

    DeviceStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
