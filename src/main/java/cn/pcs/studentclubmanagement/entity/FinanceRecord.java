package cn.pcs.studentclubmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("finance_record")
public class FinanceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("club_id")
    private Long clubId;

    private String type; // INCOME, EXPENSE

    private BigDecimal amount;

    private String description;

    @TableField("record_time")
    private LocalDateTime recordTime;

    @TableField("recorded_by")
    private Long recordedBy;
}
