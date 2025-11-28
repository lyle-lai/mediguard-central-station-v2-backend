package com.mediguard.central.controller;

import com.mediguard.central.common.Result;
import com.mediguard.central.dto.CreateDepartmentDTO;
import com.mediguard.central.dto.response.CreateDepartmentResponseDTO;
import com.mediguard.central.dto.response.DepartmentResponseDTO;
import com.mediguard.central.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理员科室管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/departments")
@RequiredArgsConstructor
@Tag(name = "Admin - 科室管理", description = "管理员科室CRUD操作")
public class AdminDepartmentController {

    private final DepartmentService departmentService;

    /**
     * 获取所有科室列表
     */
    @GetMapping
    @Operation(summary = "获取所有科室列表", description = "获取系统定义的科室列表（Admin视图）")
    public Result<List<DepartmentResponseDTO>> getAllDepartments() {
        log.info("【Admin】获取所有科室列表");
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return Result.success(departments);
    }

    /**
     * 创建新科室
     */
    @PostMapping
    @Operation(summary = "新增科室", description = "创建一个新的监护科室，并初始化默认配置")
    public Result<CreateDepartmentResponseDTO> createDepartment(@Valid @RequestBody CreateDepartmentDTO dto) {
        log.info("【Admin】创建新科室，code={}", dto.getCode());

        DepartmentResponseDTO department = departmentService.createDepartment(dto);
        CreateDepartmentResponseDTO response = new CreateDepartmentResponseDTO(
                department.getId(),
                department.getCode());

        return Result.success(response);
    }

    /**
     * 删除科室
     */
    @DeleteMapping("/{code}")
    @Operation(summary = "删除科室", description = "移除一个科室及其所有配置")
    public Result<Void> deleteDepartment(@PathVariable String code) {
        log.info("【Admin】删除科室，code={}", code);
        departmentService.deleteDepartment(code);
        return Result.success();
    }
}
