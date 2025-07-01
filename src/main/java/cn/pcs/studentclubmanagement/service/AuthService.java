package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.dto.LoginRequest;
import cn.pcs.studentclubmanagement.dto.LoginResponse;
import cn.pcs.studentclubmanagement.dto.RegisterRequest;
import cn.pcs.studentclubmanagement.entity.User;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

    User register(RegisterRequest registerRequest);
}