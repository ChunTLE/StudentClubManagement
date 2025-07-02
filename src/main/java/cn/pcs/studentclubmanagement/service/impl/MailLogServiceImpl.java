package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.MailLog;
import cn.pcs.studentclubmanagement.mapper.MailLogMapper;
import cn.pcs.studentclubmanagement.service.MailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements MailLogService {
} 