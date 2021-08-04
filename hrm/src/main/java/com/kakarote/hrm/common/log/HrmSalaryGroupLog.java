package com.kakarote.hrm.common.log;

import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.hrm.entity.PO.HrmSalaryGroup;
import com.kakarote.hrm.service.IHrmSalaryGroupService;
import org.springframework.web.bind.annotation.PathVariable;

public class HrmSalaryGroupLog {

    private IHrmSalaryGroupService salaryGroupService = ApplicationContextHolder.getBean(IHrmSalaryGroupService.class);

    public Content deleteSalaryGroup(@PathVariable Integer groupId){
        HrmSalaryGroup salaryGroup = salaryGroupService.getById(groupId);
        return new Content(salaryGroup.getGroupName(),"删除了薪资组:"+salaryGroup.getGroupName(), BehaviorEnum.DELETE);
    }
}
