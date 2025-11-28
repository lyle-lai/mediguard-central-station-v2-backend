package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.DictionaryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典 Mapper 接口
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<DictionaryEntity> {
}
