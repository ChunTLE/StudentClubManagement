package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Message;
import cn.pcs.studentclubmanagement.service.MessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.pcs.studentclubmanagement.util.MailUtil;
import cn.pcs.studentclubmanagement.entity.Result;
import java.time.LocalDateTime;

import java.security.Principal;
import cn.pcs.studentclubmanagement.service.UserService;
import cn.pcs.studentclubmanagement.entity.User;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UserService userService;

    // 分页获取当前用户的消息
    @GetMapping("/list")
    public Result<IPage<Message>> list(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Principal principal) {
        // 假设principal.getName()为用户名，需要根据用户名查userId
        // 这里建议你根据实际项目获取userId
        Long userId = getUserIdFromPrincipal(principal);
        IPage<Message> pageResult = messageService.getMessagesByUserId(userId, new Page<>(page, size));
        return Result.success(pageResult);
    }

    // TODO: 实现根据principal获取userId的方法
    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal == null) return null;
        String username = principal.getName();
        User user = userService.findByUsername(username);
        return user != null ? user.getId() : null;
    }

    // 发送消息并可选发送邮件
    @PostMapping("/send")
    public Result<?> sendMessage(@RequestBody Message message, @RequestParam(required = false) String toEmail) {
        // 如果有邮件地址，先发邮件
        if (toEmail != null && !toEmail.isEmpty()) {
            boolean success = mailUtil.sendMail(toEmail, message.getTitle(), message.getContent());
            if (!success) {
                return Result.error("邮件发送失败，消息未保存");
            }
        }
        // 保存消息
        message.setCreateTime(LocalDateTime.now());
        message.setIsRead(false);
        boolean saved = messageService.save(message);
        return saved ? Result.success() : Result.error("消息保存失败");
    }
} 