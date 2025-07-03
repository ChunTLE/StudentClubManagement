package cn.pcs.studentclubmanagement.service;


import cn.pcs.studentclubmanagement.entity.Activity;
import cn.pcs.studentclubmanagement.entity.Department;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DepartmentService extends IService<Department> {

    Department getDepartmentByName(String name);
    IPage<Department> getDepartmentPage(int pageNum, int pageSize, Long clubId);
    List<Department> getAllDepartments();

}
