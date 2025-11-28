package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.AlarmThresholdConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警阈值配置 Mapper 接口
 */
@Mapper
public interface AlarmThresholdConfigMapper extends BaseMapper<AlarmThresholdConfigEntity> {
}
