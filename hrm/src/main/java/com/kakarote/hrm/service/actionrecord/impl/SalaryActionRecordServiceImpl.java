package com.kakarote.hrm.service.actionrecord.impl;

import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthRecord;
import com.kakarote.hrm.service.actionrecord.AbstractHrmActionRecordService;
import org.springframework.stereotype.Service;

@Service("salaryActionRecordService")
@SysLog(subModel = SubModelType.HRM_SALARY)
public class SalaryActionRecordServiceImpl extends AbstractHrmActionRecordService {

    @SysLogHandler(isReturn = true)
    public Content addNextMonthSalaryLog(HrmSalaryMonthRecord salaryMonthRecord){
        String detail = "添加"+salaryMonthRecord.getYear()+"-"+salaryMonthRecord.getTitle();
        return new Content(salaryMonthRecord.getTitle(),detail, BehaviorEnum.SAVE);
    }

    @SysLogHandler(isReturn = true)
    public Content computeSalaryDataLog(HrmSalaryMonthRecord salaryMonthRecord) {
        String detail = "核算"+salaryMonthRecord.getYear()+"-"+salaryMonthRecord.getTitle();
        return new Content(salaryMonthRecord.getTitle(),detail, BehaviorEnum.SAVE);
    }

    @SysLogHandler(isReturn = true)
    public Content deleteSalaryLog(HrmSalaryMonthRecord salaryMonthRecord) {
        String detail = "删除"+salaryMonthRecord.getYear()+"-"+salaryMonthRecord.getTitle();
        return new Content(salaryMonthRecord.getTitle(),detail, BehaviorEnum.SAVE);
    }
}
