package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Message;
import cn.pcs.studentclubmanagement.service.MessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    // 分页获取当前用户的消息
    @GetMapping("/list")
    public IPage<Message> list(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Principal principal) {
        // 假设principal.getName()为用户名，需要根据用户名查userId
        // 这里建议你根据实际项目获取userId
        Long userId = getUserIdFromPrincipal(principal);
        return messageService.getMessagesByUserId(userId, new Page<>(page, size));
    }

    // TODO: 实现根据principal获取userId的方法
    private Long getUserIdFromPrincipal(Principal principal) {
        // 这里需要你根据实际项目实现
        // 比如通过用户名查User表
        return 1L; // 示例返回
    }
} 