package com.mediguard.central.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 报警快照响应 DTO
 */
@Data
public class AlarmSnapshotResponseDTO {

    /**
     * 报警ID
     */
    private String id;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 参数列表
     */
    private List<ParameterDTO> parameters;

    /**
     * 波形列表
     */
    private List<WaveformDTO> waveforms;

    @Data
    public static class ParameterDTO {
        private String id;
        private String label;
        private Object value;
        private String unit;
        private Boolean isAlarm;
    }

    @Data
    public static class WaveformDTO {
        private String id;
        private String label;
        private String color;
        private List<Number> data;
    }
}
