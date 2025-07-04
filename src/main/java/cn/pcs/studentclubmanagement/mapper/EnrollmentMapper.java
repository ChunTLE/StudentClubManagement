package cn.pcs.studentclubmanagement.mapper;

import cn.pcs.studentclubmanagement.entity.Enrollment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.pcs.studentclubmanagement.entity.EnrollmentInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnrollmentMapper extends BaseMapper<Enrollment> {
    List<EnrollmentInfoVO> searchEnrollmentInfo(@Param("realName") String realName, @Param("title") String title);
}
