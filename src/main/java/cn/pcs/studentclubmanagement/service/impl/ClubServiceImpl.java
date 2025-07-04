package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Club;
import cn.pcs.studentclubmanagement.entity.Membership;
import cn.pcs.studentclubmanagement.entity.Department;
import cn.pcs.studentclubmanagement.mapper.ClubMapper;
import cn.pcs.studentclubmanagement.service.ClubService;
import cn.pcs.studentclubmanagement.service.MembershipService;
import cn.pcs.studentclubmanagement.service.DepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements ClubService {
    @Autowired
    private MembershipService membershipService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public Club getClubByName(String name) {
        return baseMapper.selectByName(name);
    }

    @Override
    public List<Map<String, Object>> getClubMemberCounts() {
        List<Club> clubs = this.list();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Club club : clubs) {
            long count = membershipService.count(
                new QueryWrapper<Membership>().eq("club_id", club.getId()).eq("status", "APPROVED")
            );
            Map<String, Object> map = new HashMap<>();
            map.put("name", club.getName());
            map.put("value", count);
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getClubMemberAndDepartmentCounts() {
        List<Club> clubs = this.list();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Club club : clubs) {
            long clubCount = membershipService.count(
                new QueryWrapper<Membership>().eq("club_id", club.getId()).eq("status", "APPROVED")
            );
            Map<String, Object> clubMap = new HashMap<>();
            clubMap.put("name", club.getName());
            clubMap.put("value", clubCount);

            // 查询该社团下所有部门
            List<Department> departments = departmentService.list(
                new QueryWrapper<Department>().eq("club_id", club.getId())
            );
            List<Map<String, Object>> deptList = new ArrayList<>();
            for (Department dept : departments) {
                long deptCount = membershipService.count(
                    new QueryWrapper<Membership>().eq("club_id", club.getId())
                        .eq("department_id", dept.getId())
                        .eq("status", "APPROVED")
                );
                Map<String, Object> deptMap = new HashMap<>();
                deptMap.put("name", dept.getName());
                deptMap.put("value", deptCount);
                deptList.add(deptMap);
            }
            clubMap.put("departments", deptList);
            result.add(clubMap);
        }
        return result;
    }
}
