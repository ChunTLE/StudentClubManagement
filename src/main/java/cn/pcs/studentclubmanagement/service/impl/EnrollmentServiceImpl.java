package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Enrollment;
import cn.pcs.studentclubmanagement.mapper.EnrollmentMapper;
import cn.pcs.studentclubmanagement.mapper.UserMapper;
import cn.pcs.studentclubmanagement.mapper.ActivityMapper;
import cn.pcs.studentclubmanagement.service.EnrollmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {
    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public boolean enroll(Long activityId, Long userId) {
        QueryWrapper<Enrollment> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId).eq("user_id", userId);
        if (enrollmentMapper.selectCount(wrapper) > 0) {
            return false; // 已报名
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setActivityId(activityId);
        enrollment.setUserId(userId);
        enrollment.setEnrolledAt(LocalDateTime.now());
        return enrollmentMapper.insert(enrollment) > 0;
    }

    @Override
    public List<Enrollment> getByUserId(Long userId) {
        return enrollmentMapper.selectList(new QueryWrapper<Enrollment>().eq("user_id", userId));
    }

    @Override
    public List<Enrollment> getByActivityId(Long activityId) {
        return enrollmentMapper.selectList(new QueryWrapper<Enrollment>().eq("activity_id", activityId));
    }

    @Override
    public boolean deleteEnrollment(Long activityId, Long userId) {
        QueryWrapper<Enrollment> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId).eq("user_id", userId);
        return enrollmentMapper.delete(wrapper) > 0;
    }

    @Override
    public List<Enrollment> getByUsername(String username) {
        // 通过用户名查找userId
        Long userId = userMapper.selectIdByUsername(username);
        if (userId == null) {
            return java.util.Collections.emptyList();
        }
        return enrollmentMapper.selectList(new QueryWrapper<Enrollment>().eq("user_id", userId));
    }

    @Override
    public List<Enrollment> getByActivityName(String activityName) {
        // 通过活动名称查找activityId
        Long activityId = activityMapper.selectIdByTitle(activityName);
        if (activityId == null) {
            return java.util.Collections.emptyList();
        }
        return enrollmentMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Enrollment>()
                .eq("activity_id", activityId));
    }
}