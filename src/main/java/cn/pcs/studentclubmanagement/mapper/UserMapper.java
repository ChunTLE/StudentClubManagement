package cn.pcs.studentclubmanagement.mapper;

import cn.pcs.studentclubmanagement.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT id FROM user WHERE username = #{username} LIMIT 1")
    Long selectIdByUsername(String username);

    @Select("SELECT id FROM user WHERE username LIKE CONCAT('%', #{username}, '%')")
    List<Long> selectIdsByUsernameLike(String username);
}
