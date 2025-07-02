package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Activity;
import cn.pcs.studentclubmanagement.entity.Enrollment;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    // 分页获取活动列表，可选clubId
    @GetMapping
    public Result<?> getActivities(@RequestParam(defaultValue = "1") int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize,
                                   @RequestParam(required = false) Long clubId) {
        return Result.success(activityService.getActivityPage(pageNum, pageSize, clubId));
    }

    // 获取活动详情（通过title）
    @GetMapping("/detail")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER','MEMBER')")
    public Result<Activity> getActivityByTitle(@RequestParam String title) {
        Activity activity = activityService.getActivityByTitle(title);
        return activity != null ? Result.success(activity) : Result.error("未找到该活动");
    }

    // 新建活动（仅LEADER/ADMIN）
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<Activity> createActivity(@RequestBody Activity activity) {
        activityService.save(activity);
        return Result.success(activity);
    }

    // 编辑活动（仅LEADER/ADMIN）
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        activity.setId(id);
        activityService.updateById(activity);
        return Result.success(activity);
    }

    // 删除活动（仅ADMIN）
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteActivity(@PathVariable Long id) {
        activityService.removeById(id);
        return Result.success();
    }

    // 活动报名（仅MEMBER）
    @PostMapping("/{id}/enroll")
    @PreAuthorize("hasRole('MEMBER')")
    public Result<?> enrollActivity(@PathVariable Long id, @RequestParam Long userId) {
        boolean success = activityService.enrollActivity(id, userId);
        return success ? Result.success() : Result.error("已报名或报名失败");
    }

    // 查询某活动的报名用户
    @GetMapping("/{id}/enrollments")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<List<Enrollment>> getEnrollments(@PathVariable Long id) {
        return Result.success(activityService.getEnrollmentsByActivity(id));
    }

    // 查询某用户报名的活动
    @GetMapping("/user/{userId}")
    public Result<List<Activity>> getUserActivities(@PathVariable Long userId) {
        return Result.success(activityService.getActivitiesByUser(userId));
    }
}
