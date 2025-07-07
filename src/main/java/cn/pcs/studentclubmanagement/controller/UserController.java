package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.User;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.UserService;
import cn.pcs.studentclubmanagement.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import cn.pcs.studentclubmanagement.entity.UserExportVO;
import com.alibaba.excel.support.ExcelTypeEnum;
import cn.pcs.studentclubmanagement.entity.UserImportVO;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 查询所有用户
    @GetMapping
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }

    // 根据用户名查询用户
    @GetMapping("/search")
    public Result<User> getUserByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("未找到该用户");
        }
    }

    //修改用户信息（仅ADMIN和LEADER可用）
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER', 'MEMBER')")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        // 如果有新密码，进行加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.encode(user.getPassword()));
        }
        boolean updated = userService.updateById(user);
        return updated ? Result.success(user) : Result.error("修改失败");
    }

    //管理账号状态：禁用、启用（仅ADMIN可用）
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        boolean updated = userService.updateById(user);
        return updated ? Result.success() : Result.error("状态更新失败");
    }

    // 获取当前登录用户信息
    @GetMapping("/me")
    public Result<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("未找到当前用户信息");
        }
    }

    // 用户数据导出接口
    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public void exportUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer status,
            HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        
        // 构建文件名，包含筛选条件信息
        StringBuilder fileNameBuilder = new StringBuilder("用户数据");
        if (role != null && !role.isEmpty()) {
            String roleName = "";
            switch (role) {
                case "ADMIN": roleName = "管理员"; break;
                case "LEADER": roleName = "社团负责人"; break;
                case "MEMBER": roleName = "干事"; break;
                default: roleName = role;
            }
            fileNameBuilder.append("_").append(roleName);
        }
        if (status != null) {
            fileNameBuilder.append("_").append(status == 1 ? "正常" : "封禁");
        }
        
        String fileName = URLEncoder.encode(fileNameBuilder.toString(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        
        // 根据筛选条件获取数据
        java.util.List<UserExportVO> exportData = userService.getUserExportList(role, status);
        EasyExcel.write(response.getOutputStream(), cn.pcs.studentclubmanagement.entity.UserExportVO.class)
                .sheet("用户列表").doWrite(exportData);
    }

    // 用户数据导入接口
    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<?> importUsers(@RequestParam("file") MultipartFile file) {
        try {
            List<UserImportVO> userList = EasyExcel.read(file.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(UserImportVO.class)
                    .sheet()
                    .doReadSync();
            int success = 0, fail = 0;
            StringBuilder failMsg = new StringBuilder();
            for (UserImportVO vo : userList) {
                try {
                    cn.pcs.studentclubmanagement.entity.User user = new cn.pcs.studentclubmanagement.entity.User();
                    user.setUsername(vo.getUsername());
                    user.setEmail(vo.getEmail());
                    user.setRealName(vo.getRealName());
                    // 密码处理
                    if (vo.getPassword() != null && !vo.getPassword().isEmpty()) {
                        user.setPassword(PasswordUtil.encode(vo.getPassword()));
                    } else {
                        user.setPassword(PasswordUtil.encode("123456"));
                    }
                    // 角色转换
                    String role = vo.getRole();
                    if ("管理员".equals(role)) user.setRole("ADMIN");
                    else if ("社团负责人".equals(role)) user.setRole("LEADER");
                    else if ("干事".equals(role)) user.setRole("MEMBER");
                    else user.setRole(role);
                    // 账号状态转换
                    String status = vo.getStatus();
                    if ("账号正常".equals(status)) user.setStatus(1);
                    else if ("账号封禁".equals(status)) user.setStatus(0);
                    else user.setStatus(null);
                    // 创建时间
                    user.setCreatedAt(vo.getCreatedAt() != null && !vo.getCreatedAt().isEmpty() ? java.time.LocalDateTime.parse(vo.getCreatedAt(), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
                    boolean saved = userService.save(user);
                    if (saved) success++;
                    else {
                        fail++;
                        failMsg.append("用户名").append(vo.getUsername()).append("导入失败；");
                    }
                } catch (Exception e) {
                    fail++;
                    failMsg.append("用户名").append(vo.getUsername()).append("导入异常；");
                }
            }
            return Result.success("导入成功：" + success + "条，失败：" + fail + "条。" + (fail > 0 ? failMsg.toString() : ""));
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }
}
