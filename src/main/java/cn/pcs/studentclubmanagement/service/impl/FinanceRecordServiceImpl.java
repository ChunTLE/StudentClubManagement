package cn.pcs.studentclubmanagement.service.impl;

import cn.pcs.studentclubmanagement.entity.FinanceRecord;
import cn.pcs.studentclubmanagement.mapper.FinanceRecordMapper;
import cn.pcs.studentclubmanagement.service.FinanceRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FinanceRecordServiceImpl extends ServiceImpl<FinanceRecordMapper, FinanceRecord>
        implements FinanceRecordService {
}