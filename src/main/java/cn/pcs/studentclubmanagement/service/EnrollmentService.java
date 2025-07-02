package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Enrollment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface EnrollmentService extends IService<Enrollment> {
    boolean enroll(Long activityId, Long userId);

    List<Enrollment> getByUserId(Long userId);

    List<Enrollment> getByActivityId(Long activityId);

    boolean deleteEnrollment(Long activityId, Long userId);

    /**
     * 根据用户名查询该用户所有报名信息
     */
    List<Enrollment> getByUsername(String username);
}