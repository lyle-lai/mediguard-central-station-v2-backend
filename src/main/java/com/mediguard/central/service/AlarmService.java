package com.mediguard.central.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mediguard.central.dto.response.AlarmSnapshotResponseDTO;
import com.mediguard.central.entity.AlarmEntity;

import java.util.List;

/**
 * 报警服务接口
 */
public interface AlarmService extends IService<AlarmEntity> {

    /**
     * 获取报警历史
     * 
     * @param department 科室代码（可选）
     * @return 报警列表
     */
    List<AlarmEntity> getHistory(String department);

    /**
     * 获取报警快照详情
     * 
     * @param alarmId 报警ID
     * @return 快照详情
     */
    AlarmSnapshotResponseDTO getSnapshot(String alarmId);
}
