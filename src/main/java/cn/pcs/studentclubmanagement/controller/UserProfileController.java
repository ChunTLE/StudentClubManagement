package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.service.UserService;
import cn.pcs.studentclubmanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户资料控制器
 * 需要JWT认证
 */
@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户信息
     * 
     * @param request HTTP请求
     * @return 用户信息
     */
    @GetMapping("/me")
    public Result<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        // 从请求属性中获取用户信息（由JWT拦截器设置）
        Long userId = (Long) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        // 从数据库获取完整的用户信息
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 构建响应数据
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("realName", user.getRealName());
        userInfo.put("role", user.getRole());
        userInfo.put("status", user.getStatus());
        userInfo.put("createdAt", user.getCreatedAt());

        return Result.success(userInfo);
    }

    /**
     * 更新用户信息
     * 
     * @param request HTTP请求
     * @param user    更新的用户信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<User> updateProfile(HttpServletRequest request, @RequestBody User user) {
        Long userId = (Long) request.getAttribute("userId");

        // 确保只能更新自己的信息
        if (!userId.equals(user.getId())) {
            return Result.error("只能更新自己的信息");
        }

        // 获取原用户信息
        User existingUser = userService.getById(userId);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }

        // 只允许更新某些字段
        existingUser.setEmail(user.getEmail());
        existingUser.setRealName(user.getRealName());

        // 保存更新
        userService.updateById(existingUser);

        return Result.success(existingUser);
    }

    /**
     * 验证令牌
     * 
     * @param request HTTP请求
     * @return 验证结果
     */
    @GetMapping("/verify")
    public Result<Map<String, Object>> verifyToken(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("userId", userId);
        tokenInfo.put("username", username);
        tokenInfo.put("role", role);
        tokenInfo.put("valid", true);

        return Result.success(tokenInfo);
    }
}