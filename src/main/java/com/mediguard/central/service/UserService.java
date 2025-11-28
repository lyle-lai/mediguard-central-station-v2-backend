package com.mediguard.central.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mediguard.central.dto.LoginDTO;
import com.mediguard.central.dto.response.LoginResponseDTO;
import com.mediguard.central.entity.UserEntity;

/**
 * 用户服务接口
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 用户登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    LoginResponseDTO login(LoginDTO loginDTO);
}
