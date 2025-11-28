package com.mediguard.central.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 系统字典响应 DTO
 */
@Data
public class SystemDictionaryResponseDTO {

    /**
     * 设备类型列表
     */
    private List<DictItemDTO> deviceTypes;

    /**
     * 科室列表
     */
    private List<DepartmentItemDTO> departments;

    /**
     * 报警级别列表
     */
    private List<DictItemDTO> alarmLevels;

    @Data
    public static class DictItemDTO {
        private String code;
        private String label;
    }

    @Data
    public static class DepartmentItemDTO {
        private String code;
        private String label;
        private Integer capacity;
    }
}
