package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Department;
import cn.pcs.studentclubmanagement.mapper.DepartmentMapper;
import cn.pcs.studentclubmanagement.service.DepartmentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public Department getDepartmentByName(String name) {
        return baseMapper.selectByName(name);
    }

    @Override
    public IPage<Department> getDepartmentPage(int pageNum, int pageSize, Long clubId) {
        Page<Department> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectDepartmentPage(page, clubId);
    }
}