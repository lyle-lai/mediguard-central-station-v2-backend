package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.response.SystemDictionaryResponseDTO;
import com.mediguard.central.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统配置控制器
 */
@Slf4j
@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemController {

    private final SystemService systemService;

    /**
     * 获取系统字典
     */
    @GetMapping("/dictionaries")
    public Result<SystemDictionaryResponseDTO> getDictionaries() {
        log.info("【系统配置】接收到获取字典请求");
        return Result.success(systemService.getDictionaries());
    }
}
