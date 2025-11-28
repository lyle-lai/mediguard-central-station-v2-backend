package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.DeviceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备 Mapper 接口
 */
@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntity> {
}
