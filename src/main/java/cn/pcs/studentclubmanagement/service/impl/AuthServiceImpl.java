package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.dto.LoginRequest;
import cn.pcs.studentclubmanagement.dto.LoginResponse;
import cn.pcs.studentclubmanagement.dto.RegisterRequest;
import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.service.AuthService;
import cn.pcs.studentclubmanagement.service.UserService;
import cn.pcs.studentclubmanagement.util.JwtUtil;
import cn.pcs.studentclubmanagement.util.RedisTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTokenUtil redisTokenUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 根据用户名查找用户
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 使用BCrypt验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 将token存储到Redis中，设置5分钟有效期
        redisTokenUtil.storeToken(token, user.getId());

        // 创建登录响应
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRole(user.getRole());
        response.setToken(token);

        return response;
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        User existingUser = userService.findByUsername(registerRequest.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 使用BCrypt加密
        user.setEmail(registerRequest.getEmail());
        user.setRealName(registerRequest.getRealName());
        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "MEMBER");
        user.setStatus(1); // 默认启用
        user.setCreatedAt(LocalDateTime.now());

        // 保存用户
        userService.save(user);

        return user;
    }
}