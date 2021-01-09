package com.kakarote.crm.common.log;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmActivity;

public class CrmActivityLog {
    private SysLogUtil sysLogUtil = ApplicationContextHolder.getBean(SysLogUtil.class);


    public Content addCrmActivityRecord(CrmActivity crmActivity) {
        CrmEnum crmEnum = CrmEnum.parse(crmActivity.getActivityType());
        return new Content(crmEnum.getRemarks(),"","新建了跟进记录");
    }
}
