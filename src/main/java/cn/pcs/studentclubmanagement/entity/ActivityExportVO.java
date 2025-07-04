package cn.pcs.studentclubmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ActivityExportVO {
    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("简介")
    private String content;

    @ExcelProperty("地点")
    private String location;

    @ExcelProperty("所属社团")
    private String clubName;

    @ExcelProperty("开始时间")
    private String startTime;

    @ExcelProperty("结束时间")
    private String endTime;
} 