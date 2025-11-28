package com.mediguard.central.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 患者信息响应 DTO
 */
@Data
public class PatientResponseDTO {

    /**
     * 患者ID
     */
    private String id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 床位号
     */
    private String bedNumber;

    /**
     * 科室
     */
    private String department;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 连接的设备列表
     */
    private List<String> connectedDevices;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String gender;

    /**
     * 住院号
     */
    private String admissionId;

    /**
     * 状态
     */
    private String status;

    /**
     * 当前报警
     */
    private ActiveAlarmDTO activeAlarm;

    /**
     * 参数列表
     */
    private List<ParameterDTO> parameters;

    /**
     * 波形列表
     */
    private List<WaveformDTO> waveforms;

    /**
     * 报警持续时间
     */
    private Integer alarmDuration;

    /**
     * 违规计数器
     */
    private Map<String, Integer> violationCounters;

    @Data
    public static class ActiveAlarmDTO {
        private String message;
        private String category;
        private String timestamp;
        private String priority;
        private Boolean isAcknowledged;
    }

    @Data
    public static class ParameterDTO {
        private String id;
        private String label;
        private Object value;
        private String unit;
    }

    @Data
    public static class WaveformDTO {
        private String id;
        private String label;
        private String color;
        private List<Number> data;
    }
}
