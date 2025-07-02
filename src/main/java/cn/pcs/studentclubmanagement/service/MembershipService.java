package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Membership;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MembershipService extends IService<Membership> {
    boolean joinClub(Long userId, Long clubId, Long departmentId, String position);
    List<Membership> getMembershipsByClub(Long clubId);
    List<Membership> getMembershipsByUser(Long userId);
    Membership getSingleMembershipByUser(Long userId);
} 