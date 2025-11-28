package com.mediguard.central.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("【系统异常】发生未知错误", e);
        return Result.error(500, "系统内部错误: " + e.getMessage());
    }

    // 可以扩展其他自定义异常处理
}
