package com.mediguard.central.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 科室配置响应 DTO
 */
@Data
public class DepartmentConfigResponseDTO {

    /**
     * 科室床位容量
     */
    private Map<String, Integer> deptCapacity;

    /**
     * 床位标签
     */
    private Map<String, Map<String, String>> bedLabels;

    /**
     * 设备配置
     */
    private Map<String, Map<String, DeviceConfigDTO>> deviceConfigs;

    /**
     * 报警阈值
     */
    private Map<String, List<AlarmThresholdDTO>> alarmThresholds;

    /**
     * 科室偏好设置
     */
    private com.mediguard.central.dto.DepartmentPreferencesDTO preferences;

    @Data
    public static class DeviceConfigDTO {
        private List<WaveformItemDTO> waveforms;
        private List<ParameterItemDTO> parameters;
    }

    @Data
    public static class WaveformItemDTO {
        private String id;
        private String label;
        private Boolean visible;
        private String color;
        private Integer order;
    }

    @Data
    public static class ParameterItemDTO {
        private String id;
        private String label;
        private String unit;
        private Boolean visible;
        private Integer order;
    }

    @Data
    public static class AlarmThresholdDTO {
        private String paramId;
        private String label;
        private Integer min;
        private Integer max;
        private Integer delay;
        private String priority;
        private Boolean enabled;
    }
}
