package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Club;
import cn.pcs.studentclubmanagement.entity.Membership;
import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.mapper.MembershipMapper;
import cn.pcs.studentclubmanagement.service.ClubService;
import cn.pcs.studentclubmanagement.service.MembershipService;
import cn.pcs.studentclubmanagement.service.UserService;
import cn.pcs.studentclubmanagement.util.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MembershipServiceImpl extends ServiceImpl<MembershipMapper, Membership> implements MembershipService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private MailUtil mailUtil;

    @Override
    public boolean joinClub(Long userId, Long clubId, Long departmentId, String position) {
        // 检查是否已经加入任何社团
        QueryWrapper<Membership> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (this.count(wrapper) > 0) {
            return false; // 已经加入社团，不能再加入
        }
        // 创建成员关系
        Membership membership = new Membership();
        membership.setUserId(userId);
        membership.setClubId(clubId);
        membership.setDepartmentId(departmentId);
        membership.setPosition(position != null && !position.trim().isEmpty() ? position : "干事");
        membership.setStatus("PENDING"); // 设置为待审核状态
        membership.setJoinedAt(LocalDateTime.now());
        boolean success = this.save(membership);
        if (success) {
            sendJoinApplicationNotification(userId, clubId);
        }
        return success;
    }

    @Override
    public List<Membership> getMembershipsByClub(Long clubId) {
        QueryWrapper<Membership> wrapper = new QueryWrapper<>();
        wrapper.eq("club_id", clubId);
        return this.list(wrapper);
    }

    @Override
    public List<Membership> getMembershipsByUser(Long userId) {
        QueryWrapper<Membership> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.list(wrapper);
    }

    @Override
    public Membership getSingleMembershipByUser(Long userId) {
        QueryWrapper<Membership> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return this.getOne(wrapper);
    }

    /**
     * 发送加入社团通知邮件
     */
    private void sendJoinNotification(Long userId, Long clubId) {
        try {
            User user = userService.getById(userId);
            Club club = clubService.getById(clubId);

            if (user != null && club != null && user.getEmail() != null) {
                String subject = "欢迎加入社团 - " + club.getName();
                String content = String.format(
                        "亲爱的 %s：\n\n" +
                                "恭喜您成功加入社团：%s\n" +
                                "加入时间：%s\n\n" +
                                "祝您在社团中度过愉快的时光！\n\n" +
                                "此邮件由系统自动发送，请勿回复。",
                        user.getRealName() != null ? user.getRealName() : user.getUsername(),
                        club.getName(),
                        LocalDateTime.now().toString());

                mailUtil.sendMail(user.getEmail(), subject, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 邮件发送失败不影响加入社团的主要流程
        }
    }

    /**
     * 发送申请提交通知邮件
     */
    private void sendJoinApplicationNotification(Long userId, Long clubId) {
        try {
            User user = userService.getById(userId);
            Club club = clubService.getById(clubId);

            if (user != null && club != null && user.getEmail() != null) {
                String subject = "社团申请已提交 - " + club.getName();
                String content = String.format(
                        "亲爱的 %s：\n\n" +
                                "您的社团申请已成功提交。\n\n" +
                                "社团名称：%s\n" +
                                "申请时间：%s\n" +
                                "申请状态：待审核\n\n" +
                                "我们将在3-5个工作日内完成审核，请耐心等待。\n" +
                                "审核结果将通过邮件通知您。\n\n" +
                                "此邮件由系统自动发送，请勿回复。",
                        user.getRealName() != null ? user.getRealName() : user.getUsername(),
                        club.getName(),
                        LocalDateTime.now().toString());

                mailUtil.sendMail(user.getEmail(), subject, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 邮件发送失败不影响申请提交的主要流程
        }
    }

    @Override
    public boolean reviewMembership(Long membershipId, String status) {
        Membership membership = this.getById(membershipId);
        if (membership == null || !"PENDING".equals(membership.getStatus())) {
            return false;
        }
        membership.setStatus(status);
        if ("APPROVED".equals(status)) {
            membership.setJoinedAt(java.time.LocalDateTime.now());
        }
        boolean success = this.updateById(membership);
        if (success) {
            if ("APPROVED".equals(status)) {
                sendJoinNotification(membership.getUserId(), membership.getClubId());
            } else {
                sendReviewNotification(membership, status);
            }
        }
        return success;
    }

    /**
     * 发送审核结果通知邮件
     */
    private void sendReviewNotification(Membership membership, String status) {
        try {
            User user = userService.getById(membership.getUserId());
            Club club = clubService.getById(membership.getClubId());

            if (user != null && club != null && user.getEmail() != null) {
                String subject;
                String content;

                if ("APPROVED".equals(status)) {
                    subject = "社团申请审核通过 - " + club.getName();
                    content = String.format(
                            "亲爱的 %s：\n\n" +
                                    "恭喜您！您的社团申请已审核通过。\n\n" +
                                    "社团名称：%s\n" +
                                    "申请职位：%s\n" +
                                    "审核时间：%s\n" +
                                    "审核结果：通过\n\n" +
                                    "欢迎您正式成为社团的一员！\n\n" +
                                    "此邮件由系统自动发送，请勿回复。",
                            user.getRealName() != null ? user.getRealName() : user.getUsername(),
                            club.getName(),
                            membership.getPosition() != null ? membership.getPosition() : "普通成员",
                            LocalDateTime.now().toString());
                } else {
                    subject = "社团申请审核结果 - " + club.getName();
                    content = String.format(
                            "亲爱的 %s：\n\n" +
                                    "很抱歉，您的社团申请未能通过审核。\n\n" +
                                    "社团名称：%s\n" +
                                    "申请职位：%s\n" +
                                    "审核时间：%s\n" +
                                    "审核结果：未通过\n\n" +
                                    "感谢您对社团的关注，欢迎您继续关注我们的其他活动。\n\n" +
                                    "此邮件由系统自动发送，请勿回复。",
                            user.getRealName() != null ? user.getRealName() : user.getUsername(),
                            club.getName(),
                            membership.getPosition() != null ? membership.getPosition() : "普通成员",
                            LocalDateTime.now().toString());
                }

                mailUtil.sendMail(user.getEmail(), subject, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 邮件发送失败不影响审核的主要流程
        }
    }

    @Override
    public List<Membership> getMembershipsByStatus(String status) {
        QueryWrapper<Membership> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return this.list(wrapper);
    }
}