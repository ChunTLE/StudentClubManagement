package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User findByUsername(String username);
    java.util.List<cn.pcs.studentclubmanagement.entity.UserExportVO> getUserExportList();
    java.util.List<cn.pcs.studentclubmanagement.entity.UserExportVO> getUserExportList(String role, Integer status);
}