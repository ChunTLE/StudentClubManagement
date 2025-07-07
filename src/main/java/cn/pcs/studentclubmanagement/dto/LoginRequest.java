package cn.pcs.studentclubmanagement.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String captcha; // 用户输入的验证码
    private String captchaId; // 前端传回的uuid
}