package com.mediguard.central.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mediguard.central.dto.BedDisplayConfigDTO;
import com.mediguard.central.dto.PatientAdmissionDTO;
import com.mediguard.central.dto.response.PatientResponseDTO;
import com.mediguard.central.dto.response.VitalSignTrendResponseDTO;
import com.mediguard.central.entity.PatientEntity;

import java.util.List;

/**
 * 患者服务接口
 */
public interface PatientService extends IService<PatientEntity> {

    /**
     * 获取所有患者
     * 
     * @return 患者列表
     */
    List<PatientResponseDTO> getAllPatients();

    /**
     * 患者入科
     * 
     * @param dto 入科信息
     * @return 患者ID
     */
    String admitPatient(PatientAdmissionDTO dto);

    /**
     * 患者出科
     * 
     * @param patientId 患者ID
     */
    void dischargePatient(String patientId);

    /**
     * 更新床位显示配置（绑定床位而非患者）
     * 
     * @param bedNumber 床位号
     * @param dto       配置信息
     */
    void updateDisplayConfig(String bedNumber, BedDisplayConfigDTO dto);

    /**
     * 获取历史趋势数据（动态参数）
     * 
     * @param patientId  患者ID
     * @param rangeHours 时间范围（小时）
     * @return 趋势数据列表
     */
    List<VitalSignTrendResponseDTO> getTrends(String patientId, Integer rangeHours);
}
