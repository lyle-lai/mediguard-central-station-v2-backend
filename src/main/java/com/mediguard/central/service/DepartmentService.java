package com.mediguard.central.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mediguard.central.dto.AlarmThresholdConfigDTO;
import com.mediguard.central.dto.DepartmentCapacityDTO;
import com.mediguard.central.dto.DeviceDisplayConfigDTO;
import com.mediguard.central.dto.response.DepartmentConfigResponseDTO;
import com.mediguard.central.entity.DepartmentEntity;

import java.util.List;

/**
 * 科室服务接口
 */
public interface DepartmentService extends IService<DepartmentEntity> {

    /**
     * 获取科室完整配置
     * 
     * @param departmentCode 科室代码
     * @return 配置信息
     */
    DepartmentConfigResponseDTO getConfig(String departmentCode);

    /**
     * 更新床位容量与标签
     * 
     * @param departmentCode 科室代码
     * @param dto            参数
     */
    void updateCapacity(String departmentCode, DepartmentCapacityDTO dto);

    /**
     * 更新设备显示模板
     * 
     * @param departmentCode 科室代码
     * @param configs        配置列表
     */
    void updateDeviceConfigs(String departmentCode, List<DeviceDisplayConfigDTO> configs);

    /**
     * 更新报警阈值策略
     * 
     * @param departmentCode 科室代码
     * @param thresholds     阈值列表
     */
    void updateAlarmThresholds(String departmentCode, List<AlarmThresholdConfigDTO> thresholds);
}
