package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Club;
import cn.pcs.studentclubmanagement.entity.Membership;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    /**
     * 加入社团
     */
    @PostMapping("/join")
    public Result<?> joinClub(@RequestParam Long userId,
            @RequestParam Long clubId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String position) {
        boolean success = membershipService.joinClub(userId, clubId, departmentId, position);
        return success ? Result.success("成功加入社团") : Result.error("加入失败，可能已经是成员");
    }

    /**
     * 查询社团的所有成员
     */
    @GetMapping("/club/{clubId}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<List<Membership>> getClubMembers(@PathVariable Long clubId) {
        return Result.success(membershipService.getMembershipsByClub(clubId));
    }

    /**
     * 查询用户加入的社团（每人只能加入一个社团）
     */
    @GetMapping("/user/{userId}")
    public Result<Membership> getUserMembership(@PathVariable Long userId) {
        return Result.success(membershipService.getSingleMembershipByUser(userId));
    }

    /**
     * 退出社团
     */
    @DeleteMapping("/{membershipId}")
    public Result<?> leaveClub(@PathVariable Long membershipId) {
        boolean success = membershipService.removeById(membershipId);
        return success ? Result.success("成功退出社团") : Result.error("退出失败");
    }

    /**
     * 查询所有membership记录，返回所有字段和内容（无权限限制）
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEADER','MEMBER')")
    public Result<Map<String, Object>> getAllMemberships() {
        List<Membership> membership = membershipService.list();
        Map<String, Object> result = new HashMap<>();
        result.put("total", membership.size());
        result.put("list", membership);
        return Result.success(result);
    }

    /**
     * 更新指定membership的状态
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<?> updateMembershipStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || (!"APPROVED".equals(status) && !"REJECTED".equals(status))) {
            return Result.error("参数错误");
        }
        boolean success = membershipService.reviewMembership(id, status);
        if (success) {
            return Result.success("操作成功");
        } else {
            return Result.error("操作失败");
        }
    }
}