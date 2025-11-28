package com.mediguard.central.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 登录响应 DTO
 */
@Data
public class LoginResponseDTO {

    /**
     * Token
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfoDTO user;

    @Data
    public static class UserInfoDTO {
        private String id;
        private String username;
        private String name;
        private String role;
        private List<DepartmentDTO> departments;
    }

    @Data
    public static class DepartmentDTO {
        private String id;
        private String code;
        private String name;
        private Integer capacity;
    }
}
