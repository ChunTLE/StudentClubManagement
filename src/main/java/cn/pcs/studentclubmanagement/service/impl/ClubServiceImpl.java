package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Club;
import cn.pcs.studentclubmanagement.mapper.ClubMapper;
import cn.pcs.studentclubmanagement.service.ClubService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements ClubService {
    @Override
    public Club getClubByName(String name) {
        return baseMapper.selectByName(name);
    }
}
