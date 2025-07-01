package cn.pcs.studentclubmanagement.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long userId;
    private String username;
    private String realName;
    private String role;
    private String token;
}