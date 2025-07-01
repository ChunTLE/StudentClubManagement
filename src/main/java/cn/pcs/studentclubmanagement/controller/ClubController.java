package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Club;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    @Autowired
    private ClubService clubService;

    // 查询所有社团（所有登录用户可访问）
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEADER','MEMBER')")
    public Result<Map<String, Object>> getAllClubs() {
        List<Club> clubs = clubService.list();
        Map<String, Object> result = new HashMap<>();
        result.put("total", clubs.size());
        result.put("list", clubs);
        return Result.success(result);
    }

    // 查询社团详情
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN','LEADER','MEMBER')")
//    public Result<Club> getClubById(@PathVariable Long id) {
//        Club club = clubService.getById(id);
//        return club != null ? Result.success(club) : Result.error("未找到该社团");
//    }

    // 查询社团详情（通过name）
    @GetMapping("/detail")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER','MEMBER')")
    public Result<Club> getClubByName(@RequestParam String name) {
        Club club = clubService.getClubByName(name);
        return club != null ? Result.success(club) : Result.error("未找到该社团");
    }

    // 新建社团（仅LEADER和ADMIN可访问）
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<Club> createClub(@RequestBody Club club) {
        clubService.save(club);
        return Result.success(club);
    }

    // 修改社团（仅LEADER和ADMIN可访问）
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<Club> updateClub(@PathVariable Long id, @RequestBody Club club) {
        club.setId(id);
        clubService.updateById(club);
        return Result.success(club);
    }

    // 删除社团（仅ADMIN可访问）
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteClub(@PathVariable Long id) {
        clubService.removeById(id);
        return Result.success();
    }
}
