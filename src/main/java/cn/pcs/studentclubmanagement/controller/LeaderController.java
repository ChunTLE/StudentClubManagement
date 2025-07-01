package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.entity.Club;
import cn.pcs.studentclubmanagement.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leader")
public class LeaderController {

    // 示例：获取所有社团（实际应注入ClubService）
    @GetMapping("/clubs")
    public Result<String> getAllClubs() {
        // 这里只做演示，实际应返回社团列表
        return Result.success("只有LEADER和ADMIN可以访问此接口");
    }
}