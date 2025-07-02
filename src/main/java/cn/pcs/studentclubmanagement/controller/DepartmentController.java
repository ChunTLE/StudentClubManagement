package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.Department;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // 1. 查询所有部门，所有人可访问
    @GetMapping
    public Result<?> getDepartments(@RequestParam(defaultValue = "1") int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize,
                                   @RequestParam(required = false) Long clubId) {
        return Result.success(departmentService.getDepartmentPage(pageNum, pageSize, clubId));
    }
    // 2. 查询部门详情（通过name字段）
    @GetMapping("/detail")
    public Result<Department> getDepartmentByName(@RequestParam String name) {
        Department department = departmentService.getDepartmentByName(name);
        return department != null ? Result.success(department) : Result.error("未找到该部门");
    }

    // 3. 新建部门(仅‘ADMIN’和‘LEADER’可访问)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<Department> createDepartment(@RequestBody Department department) {
        departmentService.save(department);
        return Result.success(department);
    }

    // 4. 修改部门信息(仅‘ADMIN’和‘LEADER’可访问)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        department.setId(id);
        departmentService.updateById(department);
        return Result.success(department);
    }

    // 5. 删除部门(仅ADMIN可访问)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteDepartment(@PathVariable Long id) {
        departmentService.removeById(id);
        return Result.success();
    }
}
