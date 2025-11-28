package com.mediguard.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mediguard.central.entity.UserDepartmentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户科室关联 Mapper
 */
@Mapper
public interface UserDepartmentMapper extends BaseMapper<UserDepartmentEntity> {
}
