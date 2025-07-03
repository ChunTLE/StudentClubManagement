package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Membership;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MembershipService extends IService<Membership> {
    boolean joinClub(Long userId, Long clubId, Long departmentId, String position);

    List<Membership> getMembershipsByClub(Long clubId);

    List<Membership> getMembershipsByUser(Long userId);

    Membership getSingleMembershipByUser(Long userId);

    boolean reviewMembership(Long membershipId, String status);

    /**
     * 根据状态查询membership记录
     */
    List<Membership> getMembershipsByStatus(String status);
}