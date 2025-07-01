package cn.pcs.studentclubmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("department")
public class Department {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("club_id")
    private Long clubId;

    private String name;

    private String description;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
