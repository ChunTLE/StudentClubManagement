package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
