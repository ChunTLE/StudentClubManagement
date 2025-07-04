package cn.pcs.studentclubmanagement.mapper;

import cn.pcs.studentclubmanagement.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    // 可扩展自定义方法
} 