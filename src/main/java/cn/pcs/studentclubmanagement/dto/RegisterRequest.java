package cn.pcs.studentclubmanagement.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String realName;
    private String role;
}