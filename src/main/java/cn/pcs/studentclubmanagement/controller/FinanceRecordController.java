package cn.pcs.studentclubmanagement.controller;

import cn.pcs.studentclubmanagement.entity.FinanceRecord;
import cn.pcs.studentclubmanagement.entity.Result;
import cn.pcs.studentclubmanagement.service.FinanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/finance-records")
public class FinanceRecordController {

    @Autowired
    private FinanceRecordService financeRecordService;

    // 查询所有财务记录（仅社团负责人和管理员可见）
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<Map<String, Object>> getAllFinanceRecords() {
        List<FinanceRecord> records = financeRecordService.list();
        Map<String, Object> result = new HashMap<>();
        result.put("total", records.size());
        result.put("list", records);
        return Result.success(result);
    }

    // 查询单条财务记录
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<FinanceRecord> getFinanceRecordById(@PathVariable Long id) {
        FinanceRecord record = financeRecordService.getById(id);
        return record != null ? Result.success(record) : Result.error("未找到该财务记录");
    }

    // 新增财务记录（仅社团负责人和管理员可见）
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<FinanceRecord> createFinanceRecord(@RequestBody FinanceRecord record) {
        financeRecordService.save(record);
        return Result.success(record);
    }

    // 修改财务记录（仅社团负责人和管理员可见）
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public Result<FinanceRecord> updateFinanceRecord(@PathVariable Long id, @RequestBody FinanceRecord record) {
        record.setId(id);
        financeRecordService.updateById(record);
        return Result.success(record);
    }

    // 删除财务记录（仅管理员可见）
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteFinanceRecord(@PathVariable Long id) {
        financeRecordService.removeById(id);
        return Result.success();
    }
}