package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    // 示例：获取个人信息
    @GetMapping("/profile")
    public Result<String> getProfile() {
        return Result.success("所有登录用户都能访问此接口");
    }
}