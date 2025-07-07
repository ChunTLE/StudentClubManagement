package cn.pcs.studentclubmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    @TableField("real_name")
    private String realName;

    private String role; // 可用 Enum 管理：ADMIN, LEADER, MEMBER

    private Integer status; // 0 禁用，1 正常

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("avatar_url")
    private String avatarUrl; // 头像图片URL
}
