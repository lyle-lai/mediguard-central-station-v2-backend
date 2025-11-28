package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.PatientEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 患者 Mapper 接口
 */
@Mapper
public interface PatientMapper extends BaseMapper<PatientEntity> {
}
