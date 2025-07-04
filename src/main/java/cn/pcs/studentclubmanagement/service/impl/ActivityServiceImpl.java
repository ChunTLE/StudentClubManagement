package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Activity;
import cn.pcs.studentclubmanagement.entity.Enrollment;
import cn.pcs.studentclubmanagement.entity.ActivityExportVO;
import cn.pcs.studentclubmanagement.entity.Club;
import cn.pcs.studentclubmanagement.mapper.ActivityEnrollmentMapper;
import cn.pcs.studentclubmanagement.mapper.ActivityMapper;
import cn.pcs.studentclubmanagement.service.ActivityService;
import cn.pcs.studentclubmanagement.service.ClubService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.format.DateTimeFormatter;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Autowired
    private ActivityEnrollmentMapper activityEnrollmentMapper;

    @Autowired
    private ClubService clubService;

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
        QueryWrapper<Enrollment> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId).eq("user_id", userId);
        if (activityEnrollmentMapper.selectCount(wrapper) > 0) {
            return false; // 已报名
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setActivityId(activityId);
        enrollment.setUserId(userId);
        enrollment.setEnrolledAt(java.time.LocalDateTime.now());
        return activityEnrollmentMapper.insert(enrollment) > 0;
    }

    @Override
    public List<Enrollment> getEnrollmentsByActivity(Long activityId) {
        QueryWrapper<Enrollment> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId);
        return activityEnrollmentMapper.selectList(wrapper);
    }

    @Override
    public List<Activity> getActivitiesByUser(Long userId) {
        List<Enrollment> enrollments = activityEnrollmentMapper.selectList(
                new QueryWrapper<Enrollment>().eq("user_id", userId)
        );
        if (enrollments.isEmpty()) return java.util.Collections.emptyList();
        List<Long> activityIds = enrollments.stream().map(Enrollment::getActivityId).toList();
        return this.listByIds(activityIds);
    }

    @Override
    public Activity getActivityByTitle(String title) {
        return baseMapper.selectByTitle(title);
    }

    @Override
    public List<ActivityExportVO> getActivityExportList() {
        List<Activity> activities = this.list();
        List<ActivityExportVO> exportList = new java.util.ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Activity activity : activities) {
            ActivityExportVO vo = new ActivityExportVO();
            vo.setId(activity.getId());
            vo.setContent(activity.getContent());
            vo.setLocation(activity.getLocation());
            Club club = clubService.getById(activity.getClubId());
            vo.setClubName(club != null ? club.getName() : "");
            vo.setStartTime(activity.getStartTime() != null ? activity.getStartTime().format(formatter) : "");
            vo.setEndTime(activity.getEndTime() != null ? activity.getEndTime().format(formatter) : "");
            exportList.add(vo);
        }
        return exportList;
    }
}
