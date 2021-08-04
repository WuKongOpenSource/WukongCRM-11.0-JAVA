package com.kakarote.hrm.service.actionrecord.impl;

import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthRecord;
import com.kakarote.hrm.service.actionrecord.AbstractHrmActionRecordService;
import org.springframework.stereotype.Service;

@Service("insuranceActionRecordService")
@SysLog(subModel = SubModelType.HRM_SOCIAL_SECURITY)
public class insuranceActionRecordServiceImpl extends AbstractHrmActionRecordService {

    @SysLogHandler(isReturn = true)
    public Content computeInsuranceDataLog(HrmInsuranceMonthRecord insuranceMonthRecord){
        String detail = "生成"+insuranceMonthRecord.getYear()+"="+insuranceMonthRecord.getTitle();
        return new Content(insuranceMonthRecord.getTitle(),detail, BehaviorEnum.SAVE);
    }

    @SysLogHandler(isReturn = true)
    public Content deleteInsurance(HrmInsuranceMonthRecord insuranceMonthRecord) {
        String detail = "删除了"+insuranceMonthRecord.getYear()+"="+insuranceMonthRecord.getTitle();
        return new Content(insuranceMonthRecord.getTitle(),detail, BehaviorEnum.DELETE);
    }
}
