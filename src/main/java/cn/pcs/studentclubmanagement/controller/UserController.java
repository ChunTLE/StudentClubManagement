package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.UserService;
import cn.pcs.studentclubmanagement.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 查询所有用户
    @GetMapping
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }

    // 根据用户名查询用户
    @GetMapping("/search")
    public Result<User> getUserByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("未找到该用户");
        }
    }

    //修改用户信息（仅ADMIN和LEADER可用）
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER', 'MEMBER')")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        // 如果有新密码，进行加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.encode(user.getPassword()));
        }
        boolean updated = userService.updateById(user);
        return updated ? Result.success(user) : Result.error("修改失败");
    }

    //管理账号状态：禁用、启用（仅ADMIN可用）
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        boolean updated = userService.updateById(user);
        return updated ? Result.success() : Result.error("状态更新失败");
    }

    // 获取当前登录用户信息
    @GetMapping("/me")
    public Result<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("未找到当前用户信息");
        }
    }
}
