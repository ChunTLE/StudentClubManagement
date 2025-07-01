package cn.pcs.studentclubmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("membership")
public class Membership {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("club_id")
    private Long clubId;

    @TableField("department_id")
    private Long departmentId;

    private String position;

    private String status; // PENDING, APPROVED, REJECTED

    @TableField("joined_at")
    private LocalDateTime joinedAt;
}
