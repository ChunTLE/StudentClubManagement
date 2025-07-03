package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Enrollment;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    // 活动报名
    @PostMapping
    public Result<?> enroll(@RequestBody Map<String, Long> body) {
        Long activityId = body.get("activityId");
        Long userId = body.get("userId");
        if (activityId == null || userId == null) {
            return Result.error("参数不能为空");
        }
        boolean success = enrollmentService.enroll(activityId, userId);
        return success ? Result.success("报名成功") : Result.error("已报名或报名失败");
    }

    // 按用户ID查询报名
    @GetMapping("/user/{userId}")
    public Result<List<Enrollment>> getByUserId(@PathVariable Long userId) {
        return Result.success(enrollmentService.getByUserId(userId));
    }

    // 按活动ID查询报名
    @GetMapping("/activity/{activityId}")
    public Result<List<Enrollment>> getByActivityId(@PathVariable Long activityId) {
        return Result.success(enrollmentService.getByActivityId(activityId));
    }

    // 删除报名
    @DeleteMapping
    public Result<?> deleteEnrollment(@RequestParam Long activityId, @RequestParam Long userId) {
        boolean success = enrollmentService.deleteEnrollment(activityId, userId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    // 根据用户名查询报名活动
    @GetMapping("/username/{username}")
    public Result<List<Enrollment>> getByUsername(@PathVariable String username) {
        return Result.success(enrollmentService.getByUsername(username));
    }

    /**
     * 查询所有报名信息
     */
    @GetMapping
    public Result<?> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.list();
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("list", enrollments);
        result.put("total", enrollments.size());
        return Result.success(result);
    }

    /**
     * 修改报名信息
     */
    @PutMapping("/{id}")
    public Result<?> updateEnrollment(@PathVariable Long id, @RequestBody Enrollment enrollment) {
        enrollment.setId(id);
        boolean success = enrollmentService.updateById(enrollment);
        return success ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 根据活动名称查询报名信息
     */
    @GetMapping("/activityName/{activityName}")
    public Result<List<Enrollment>> getByActivityName(@PathVariable String activityName) {
        return Result.success(enrollmentService.getByActivityName(activityName));
    }
}