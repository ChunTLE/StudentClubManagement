package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.Message;
import cn.pcs.studentclubmanagement.mapper.MessageMapper;
import cn.pcs.studentclubmanagement.service.MessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public IPage<Message> getMessagesByUserId(Long userId, Page<Message> page) {
        return messageMapper.selectPage(page,
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Message>()
                        .eq("user_id", userId)
                        .orderByDesc("create_time"));
    }

    @Override
    public boolean save(Message message) {
        return messageMapper.insert(message) > 0;
    }
} 