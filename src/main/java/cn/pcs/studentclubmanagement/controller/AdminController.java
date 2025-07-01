package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // 只有ADMIN角色可以访问
    @GetMapping("/users")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }
}