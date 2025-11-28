package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.LoginDTO;
import com.mediguard.central.dto.response.LoginResponseDTO;
import com.mediguard.central.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@RequestBody @Validated LoginDTO loginDTO) {
        log.info("【认证模块】接收到登录请求，参数={}", loginDTO);
        LoginResponseDTO data = userService.login(loginDTO);
        return Result.success(data);
    }
}
