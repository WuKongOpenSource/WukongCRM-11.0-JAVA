package com.kakarote.crm.common.log;

import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmFieldBO;

public class CrmFieldLog {

    public Content saveField(CrmFieldBO crmFieldBO) {
        CrmEnum crmEnum = CrmEnum.parse(crmFieldBO.getLabel());
        return new Content(crmEnum.getRemarks(),"保存了自定义字段:"+crmEnum.getRemarks(), BehaviorEnum.SAVE);
    }
}
