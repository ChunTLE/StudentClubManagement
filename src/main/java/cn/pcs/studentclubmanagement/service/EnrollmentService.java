package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Enrollment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import cn.pcs.studentclubmanagement.entity.EnrollmentInfoVO;

public interface EnrollmentService extends IService<Enrollment> {
    boolean enroll(Long activityId, Long userId);

    List<Enrollment> getByUserId(Long userId);

    List<Enrollment> getByActivityId(Long activityId);

    boolean deleteEnrollment(Long activityId, Long userId);

    /**
     * 根据用户名查询该用户所有报名信息
     */
    List<Enrollment> getByUsername(String username);

    /**
     * 根据活动名称查询报名信息
     */
    List<Enrollment> getByActivityName(String activityName);

    /**
     * 多条件、模糊查询报名信息
     */
    List<Enrollment> searchEnrollments(String username, String activityTitle);

    List<EnrollmentInfoVO> searchEnrollmentInfo(String realName, String title);
}