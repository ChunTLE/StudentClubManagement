package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Club;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ClubService extends IService<Club> {
    // 可扩展自定义方法
    Club getClubByName(String name);
}
