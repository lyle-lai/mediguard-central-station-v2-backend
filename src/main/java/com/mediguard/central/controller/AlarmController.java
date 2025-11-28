package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.response.AlarmSnapshotResponseDTO;
import com.mediguard.central.entity.AlarmEntity;
import com.mediguard.central.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报警管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * 获取报警历史
     */
    @GetMapping
    public Result<List<AlarmEntity>> getHistory(@RequestParam(required = false) String department) {
        log.info("【报警管理】接收到获取历史请求，科室={}", department);
        return Result.success(alarmService.getHistory(department));
    }

    /**
     * 获取报警快照详情
     */
    @GetMapping("/{alarmId}/snapshot")
    public Result<AlarmSnapshotResponseDTO> getSnapshot(@PathVariable String alarmId) {
        log.info("【报警管理】接收到获取快照请求，ID={}", alarmId);
        return Result.success(alarmService.getSnapshot(alarmId));
    }
}
