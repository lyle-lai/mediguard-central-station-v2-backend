package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.VitalSignDataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 生命体征数据 Mapper 接口
 */
@Mapper
public interface VitalSignDataMapper extends BaseMapper<VitalSignDataEntity> {
}
