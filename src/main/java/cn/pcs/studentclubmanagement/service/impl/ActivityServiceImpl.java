package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Activity;
import cn.pcs.studentclubmanagement.entity.ActivityEnrollment;
import cn.pcs.studentclubmanagement.mapper.ActivityEnrollmentMapper;
import cn.pcs.studentclubmanagement.mapper.ActivityMapper;
import cn.pcs.studentclubmanagement.service.ActivityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Autowired
    private ActivityEnrollmentMapper activityEnrollmentMapper;

    @Override
    public IPage<Activity> getActivityPage(int pageNum, int pageSize, Long clubId) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        if (clubId != null) {
            wrapper.eq("club_id", clubId);
        }
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public boolean enrollActivity(Long activityId, Long userId) {
        QueryWrapper<ActivityEnrollment> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId).eq("user_id", userId);
        if (activityEnrollmentMapper.selectCount(wrapper) > 0) {
            return false; // 已报名
        }
        ActivityEnrollment enrollment = new ActivityEnrollment();
        enrollment.setActivityId(activityId);
        enrollment.setUserId(userId);
        enrollment.setEnrolledAt(java.time.LocalDateTime.now());
        return activityEnrollmentMapper.insert(enrollment) > 0;
    }

    @Override
    public List<ActivityEnrollment> getEnrollmentsByActivity(Long activityId) {
        QueryWrapper<ActivityEnrollment> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId);
        return activityEnrollmentMapper.selectList(wrapper);
    }

    @Override
    public List<Activity> getActivitiesByUser(Long userId) {
        List<ActivityEnrollment> enrollments = activityEnrollmentMapper.selectList(
                new QueryWrapper<ActivityEnrollment>().eq("user_id", userId)
        );
        if (enrollments.isEmpty()) return java.util.Collections.emptyList();
        List<Long> activityIds = enrollments.stream().map(ActivityEnrollment::getActivityId).toList();
        return this.listByIds(activityIds);
    }
    @Override
    public Activity getActivityByTitle(String title) {
        return baseMapper.selectByTitle(title);
    }
}
