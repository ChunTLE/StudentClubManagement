package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.entity.UserExportVO;
import cn.pcs.studentclubmanagement.mapper.UserMapper;
import cn.pcs.studentclubmanagement.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User findByUsername(String username) {
        return this.getOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public java.util.List<UserExportVO> getUserExportList() {
        java.util.List<User> users = this.list();
        java.util.List<UserExportVO> exportList = new java.util.ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (User user : users) {
            UserExportVO vo = new UserExportVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setEmail(user.getEmail());
            vo.setRealName(user.getRealName());
            // 角色转换
            String role = user.getRole();
            if ("ADMIN".equals(role)) {
                vo.setRole("管理员");
            } else if ("LEADER".equals(role)) {
                vo.setRole("社团负责人");
            } else if ("MEMBER".equals(role)) {
                vo.setRole("干事");
            } else {
                vo.setRole(role != null ? role : "");
            }
            // 账号状态转换
            if (user.getStatus() != null) {
                vo.setStatus(user.getStatus() == 1 ? "账号正常" : "账号封禁");
            } else {
                vo.setStatus("");
            }
            vo.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : "");
            exportList.add(vo);
        }
        return exportList;
    }
}