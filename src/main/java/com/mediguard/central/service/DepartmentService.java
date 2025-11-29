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
         * 更新设备波形配置
         * 
         * @param departmentCode 科室代码
         * @param deviceType     设备类型
         * @param waveforms      波形配置列表
         */
        void updateWaveformConfigs(String departmentCode, String deviceType,
                        List<com.mediguard.central.dto.WaveformConfigDTO> waveforms);

        /**
         * 更新设备参数配置
         * 
         * @param departmentCode 科室代码
         * @param deviceType     设备类型
         * @param parameters     参数配置列表
         */
        void updateParameterConfigs(String departmentCode, String deviceType,
                        List<com.mediguard.central.dto.ParameterConfigDTO> parameters);

        /**
         * 更新报警阈值策略
         * 
         * @param departmentCode 科室代码
         * @param thresholds     阈值列表
         */
        void updateAlarmThresholds(String departmentCode, List<AlarmThresholdConfigDTO> thresholds);

        /**
         * 获取所有科室列表（Admin）
         * 
         * @return 科室列表
         */
        List<com.mediguard.central.dto.response.DepartmentResponseDTO> getAllDepartments();

        /**
         * 创建新科室（Admin）
         * 
         * @param dto 创建参数
         * @return 创建的科室信息
         */
        com.mediguard.central.dto.response.DepartmentResponseDTO createDepartment(
                        com.mediguard.central.dto.CreateDepartmentDTO dto);

        /**
         * 更新科室偏好设置
         * 
         * @param departmentCode 科室代码
         * @param preferences    偏好设置
         */
        void updatePreferences(String departmentCode, com.mediguard.central.dto.DepartmentPreferencesDTO preferences);

        /**
         * 删除科室（Admin）
         * 
         * @param code 科室代码
         */
        void deleteDepartment(String code);
}
