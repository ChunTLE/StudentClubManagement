package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Club;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ClubService extends IService<Club> {
    // 可扩展自定义方法
    Club getClubByName(String name);
    List<Map<String, Object>> getClubMemberCounts();
    List<Map<String, Object>> getClubMemberAndDepartmentCounts();
}
