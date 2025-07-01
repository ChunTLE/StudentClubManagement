package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.mapper.UserMapper;
import cn.pcs.studentclubmanagement.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User findByUsername(String username) {
        return this.getOne(new QueryWrapper<User>().eq("username", username));
    }
}