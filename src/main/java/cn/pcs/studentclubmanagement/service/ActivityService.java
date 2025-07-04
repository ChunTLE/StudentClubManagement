package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Activity;
import cn.pcs.studentclubmanagement.entity.Enrollment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ActivityService extends IService<Activity> {
    Activity getActivityByTitle(String title);
    IPage<Activity> getActivityPage(int pageNum, int pageSize, Long clubId);
    boolean enrollActivity(Long activityId, Long userId);
    List<Enrollment> getEnrollmentsByActivity(Long activityId);
    List<Activity> getActivitiesByUser(Long userId);
    List<cn.pcs.studentclubmanagement.entity.ActivityExportVO> getActivityExportList();
}
