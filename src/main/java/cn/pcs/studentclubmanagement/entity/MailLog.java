package cn.pcs.studentclubmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("mail_log")
public class MailLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("to_email")
    private String toEmail;

    private String subject;

    private String content;

    private Integer status; // 0失败，1成功

    @TableField("sent_time")
    private LocalDateTime sentTime;
}
