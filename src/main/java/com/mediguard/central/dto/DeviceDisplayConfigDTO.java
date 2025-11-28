package com.mediguard.central.dto;

import lombok.Data;
import java.util.List;

/**
 * 设备显示配置 DTO
 */
@Data
public class DeviceDisplayConfigDTO {
    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 波形配置列表
     */
    private List<WaveformConfigDTO> waveforms;

    /**
     * 参数配置列表
     */
    private List<ParameterConfigDTO> parameters;

    @Data
    public static class WaveformConfigDTO {
        private String id;
        private String label;
        private Boolean visible;
        private String color;
        private Integer order;
    }

    @Data
    public static class ParameterConfigDTO {
        private String id;
        private String label;
        private String unit;
        private Boolean visible;
        private Integer order;
    }
}
