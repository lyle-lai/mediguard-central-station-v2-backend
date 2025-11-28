package com.mediguard.central.dto;

import lombok.Data;
import java.util.List;

/**
 * 床位显示配置 DTO
 */
@Data
public class BedDisplayConfigDTO {
    /**
     * 床位号
     */
    private String bedNumber;

    /**
     * 可见波形ID列表
     */
    private List<String> visibleWaveformIds;

    /**
     * 可见参数ID列表
     */
    private List<String> visibleParameterIds;
}
