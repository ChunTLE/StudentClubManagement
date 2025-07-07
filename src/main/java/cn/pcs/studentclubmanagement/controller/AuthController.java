package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.dto.LoginRequest;
import cn.pcs.studentclubmanagement.dto.LoginResponse;
import cn.pcs.studentclubmanagement.dto.RegisterRequest;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.service.AuthService;
import cn.pcs.studentclubmanagement.util.JwtUtil;
import cn.pcs.studentclubmanagement.util.RedisTokenUtil;
import cn.pcs.studentclubmanagement.util.SimpleCaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTokenUtil redisTokenUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取验证码接口
     * 
     * @return 验证码信息
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> getCaptcha() {
        SimpleCaptchaUtil.CaptchaResult captcha = SimpleCaptchaUtil.createCaptcha();
        String captchaId = UUID.randomUUID().toString();
        // 存入Redis，5分钟有效
        redisTemplate.opsForValue().set("captcha:" + captchaId, captcha.code, 5, TimeUnit.MINUTES);

        Map<String, String> result = new HashMap<>();
        result.put("captchaId", captchaId);
        result.put("captchaImage", "data:image/png;base64," + captcha.base64);
        return Result.success(result);
    }

    /**
     * 用户登录
     * 
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String redisKey = "captcha:" + loginRequest.getCaptchaId();
        String realCaptcha = redisTemplate.opsForValue().get(redisKey);

        if (realCaptcha == null) {
            return Result.error("验证码已过期，请刷新重试");
        }
        if (!realCaptcha.equalsIgnoreCase(loginRequest.getCaptcha())) {
            return Result.error("验证码错误");
        }
        // 校验通过后删除验证码，防止复用
        redisTemplate.delete(redisKey);

        try {
            LoginResponse response = authService.login(loginRequest);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     * 
     * @param request HTTP请求
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        try {
            // 从请求属性中获取用户ID
            Long userId = (Long) request.getAttribute("userId");
            if (userId != null) {
                // 使token失效
                redisTokenUtil.invalidateToken(userId);
                return Result.success("登出成功");
            } else {
                return Result.error("用户未登录");
            }
        } catch (Exception e) {
            return Result.error("登出失败: " + e.getMessage());
        }
    }

    /**
     * 获取token状态信息
     * 
     * @param request HTTP请求
     * @return token状态信息
     */
    @GetMapping("/token-status")
    public Result<Map<String, Object>> getTokenStatus(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error("用户未登录");
            }

            Map<String, Object> status = new HashMap<>();
            status.put("userId", userId);
            status.put("username", request.getAttribute("username"));
            status.put("role", request.getAttribute("role"));

            // 获取token剩余有效期
            long expiration = redisTokenUtil.getTokenExpiration(userId);
            status.put("expirationSeconds", expiration);
            status.put("isValid", expiration > 0);

            return Result.success(status);
        } catch (Exception e) {
            return Result.error("获取token状态失败: " + e.getMessage());
        }
    }

    /**
     * 测试接口 - 验证token是否有效
     * 
     * @param request HTTP请求
     * @return 测试结果
     */
    @GetMapping("/test-token")
    public Result<Map<String, Object>> testToken(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            String username = (String) request.getAttribute("username");
            String role = (String) request.getAttribute("role");

            Map<String, Object> result = new HashMap<>();
            result.put("message", "Token验证成功");
            result.put("userId", userId);
            result.put("username", username);
            result.put("role", role);
            result.put("timestamp", System.currentTimeMillis());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("Token验证失败: " + e.getMessage());
        }
    }

    /**
     * 用户注册
     * 
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authService.register(registerRequest);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}