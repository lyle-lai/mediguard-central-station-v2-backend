package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
