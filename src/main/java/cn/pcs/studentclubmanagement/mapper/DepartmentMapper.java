package cn.pcs.studentclubmanagement.mapper;

import cn.pcs.studentclubmanagement.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    Department selectByName(@Param("name") String name);

    IPage<Department> selectDepartmentPage(IPage<Department> page, @Param("clubId") Long clubId);
}