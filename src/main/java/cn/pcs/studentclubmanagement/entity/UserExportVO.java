package cn.pcs.studentclubmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserExportVO {
    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("真实姓名")
    private String realName;

    @ExcelProperty("角色")
    private String role;

    @ExcelProperty("账号状态")
    private String status;

    @ExcelProperty("创建时间")
    private String createdAt;
} 