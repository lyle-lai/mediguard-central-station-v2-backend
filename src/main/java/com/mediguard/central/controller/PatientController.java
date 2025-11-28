package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.BedDisplayConfigDTO;
import com.mediguard.central.dto.PatientAdmissionDTO;
import com.mediguard.central.dto.response.PatientResponseDTO;
import com.mediguard.central.dto.response.VitalSignTrendResponseDTO;
import com.mediguard.central.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * 获取所有患者
     */
    @GetMapping
    public Result<List<PatientResponseDTO>> getAllPatients() {
        log.info("【患者管理】接收到获取所有患者请求");
        return Result.success(patientService.getAllPatients());
    }

    /**
     * 患者入科
     */
    @PostMapping
    public Result<String> admitPatient(@RequestBody @Validated PatientAdmissionDTO dto) {
        log.info("【患者管理】接收到患者入科请求，参数={}", dto);
        return Result.success(patientService.admitPatient(dto));
    }

    /**
     * 患者出科
     */
    @DeleteMapping("/{patientId}")
    public Result<Void> dischargePatient(@PathVariable String patientId) {
        log.info("【患者管理】接收到患者出科请求，ID={}", patientId);
        patientService.dischargePatient(patientId);
        return Result.success();
    }

    /**
     * 更新床位显示配置
     */
    @PutMapping("/beds/{bedNumber}/config")
    public Result<Void> updateDisplayConfig(@PathVariable String bedNumber, @RequestBody BedDisplayConfigDTO dto) {
        log.info("【患者管理】接收到更新床位显示配置请求，床位号={}", bedNumber);
        patientService.updateDisplayConfig(bedNumber, dto);
        return Result.success();
    }

    /**
     * 获取历史趋势数据
     */
    @GetMapping("/{patientId}/trends")
    public Result<List<VitalSignTrendResponseDTO>> getTrends(@PathVariable String patientId,
            @RequestParam Integer range) {
        log.info("【患者管理】接收到获取趋势请求，ID={}，范围={}小时", patientId, range);
        return Result.success(patientService.getTrends(patientId, range));
    }
}
