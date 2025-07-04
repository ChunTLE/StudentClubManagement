package cn.pcs.studentclubmanagement.mapper;

import cn.pcs.studentclubmanagement.entity.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    Activity selectByTitle(String title);

    @Select("SELECT id FROM activity WHERE title = #{title} LIMIT 1")
    Long selectIdByTitle(String title);

    @Select("SELECT id FROM activity WHERE title LIKE CONCAT('%', #{title}, '%')")
    List<Long> selectIdsByTitleLike(String title);
}
