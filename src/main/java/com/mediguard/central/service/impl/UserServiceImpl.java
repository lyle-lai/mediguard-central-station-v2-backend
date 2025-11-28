package com.mediguard.central.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediguard.central.config.JwtUtil;
import com.mediguard.central.dto.LoginDTO;
import com.mediguard.central.dto.response.LoginResponseDTO;
import com.mediguard.central.entity.DepartmentEntity;
import com.mediguard.central.entity.UserDepartmentEntity;
import com.mediguard.central.entity.UserEntity;
import com.mediguard.central.mapper.DepartmentMapper;
import com.mediguard.central.mapper.UserDepartmentMapper;
import com.mediguard.central.mapper.UserMapper;
import com.mediguard.central.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDepartmentMapper userDepartmentMapper;
    private final DepartmentMapper departmentMapper;

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        log.info("【用户认证】用户尝试登录，用户名={}", loginDTO.getUsername());

        try {
            // 使用 Spring Security 进行认证
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            // 认证成功，生成 JWT Token（无过期时间）
            String token = jwtUtil.generateToken(loginDTO.getUsername());

            // 获取用户信息
            UserEntity user = this.getOne(new LambdaQueryWrapper<UserEntity>()
                    .eq(UserEntity::getUsername, loginDTO.getUsername()));

            // 构建返回数据
            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);

            LoginResponseDTO.UserInfoDTO userInfo = new LoginResponseDTO.UserInfoDTO();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setName(user.getName());
            userInfo.setRole(user.getRole());

            // 查询用户关联的科室列表
            List<UserDepartmentEntity> userDepts = userDepartmentMapper.selectList(
                    new LambdaQueryWrapper<UserDepartmentEntity>()
                            .eq(UserDepartmentEntity::getUserId, user.getId()));

            if (!userDepts.isEmpty()) {
                // 查询科室详细信息
                List<String> deptCodes = userDepts.stream()
                        .map(UserDepartmentEntity::getDepartmentCode)
                        .collect(Collectors.toList());

                List<DepartmentEntity> departments = departmentMapper.selectList(
                        new LambdaQueryWrapper<DepartmentEntity>()
                                .in(DepartmentEntity::getCode, deptCodes));

                List<LoginResponseDTO.DepartmentDTO> departmentDTOs = departments.stream()
                        .map(dept -> {
                            LoginResponseDTO.DepartmentDTO dto = new LoginResponseDTO.DepartmentDTO();
                            dto.setId(dept.getId());
                            dto.setCode(dept.getCode());
                            dto.setName(dept.getName());
                            return dto;
                        })
                        .collect(Collectors.toList());
                userInfo.setDepartments(departmentDTOs);
            }

            response.setUser(userInfo);

            log.info("【用户认证】登录成功，用户名={}，关联科室数={}", user.getUsername(), userDepts.size());
            return response;
        } catch (Exception e) {
            log.warn("【用户认证】登录失败，用户名={}, 错误={}", loginDTO.getUsername(), e.getMessage());
            throw new RuntimeException("用户名或密码错误");
        }
    }
}
