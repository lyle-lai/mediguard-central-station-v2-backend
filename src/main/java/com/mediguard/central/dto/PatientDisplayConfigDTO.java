package com.mediguard.central.dto;

import lombok.Data;

import java.util.List;

/**
 * 患者显示配置参数
 */
@Data
public class PatientDisplayConfigDTO {

    /**
     * 可见波形ID列表
     */
    private List<String> visibleWaveformIds;

    /**
     * 可见参数ID列表
     */
    private List<String> visibleParameterIds;
}
