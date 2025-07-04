package cn.pcs.studentclubmanagement.service;

import cn.pcs.studentclubmanagement.entity.Message;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface MessageService {
    IPage<Message> getMessagesByUserId(Long userId, Page<Message> page);
} 