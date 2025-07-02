package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.MailLog;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.MailLogService;
import cn.pcs.studentclubmanagement.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mail")
public class MailLogController {
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private MailLogService mailLogService;

    // 发送邮件并记录日志
    @PostMapping("/send")
    public Result<?> sendMail(@RequestBody MailLog mailLog) {
        boolean success = mailUtil.sendMail(mailLog.getToEmail(), mailLog.getSubject(), mailLog.getContent());
        mailLog.setStatus(success ? 1 : 0);
        mailLog.setSentTime(LocalDateTime.now());
        mailLogService.save(mailLog);
        return success ? Result.success() : Result.error("邮件发送失败");
    }

    // 查询所有邮件日志
    @GetMapping("/logs")
    public Result<List<MailLog>> getAllLogs() {
        return Result.success(mailLogService.list());
    }
} 