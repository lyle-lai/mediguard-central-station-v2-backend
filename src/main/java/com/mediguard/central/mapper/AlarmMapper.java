package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.AlarmEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警 Mapper 接口
 */
@Mapper
public interface AlarmMapper extends BaseMapper<AlarmEntity> {
}
