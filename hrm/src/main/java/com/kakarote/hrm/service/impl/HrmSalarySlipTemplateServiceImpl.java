package com.kakarote.hrm.service.impl;


import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.BO.AddSlipTemplateBO;
import com.kakarote.hrm.entity.PO.HrmSalarySlipTemplate;
import com.kakarote.hrm.entity.PO.HrmSalarySlipTemplateOption;
import com.kakarote.hrm.mapper.HrmSalarySlipTemplateMapper;
import com.kakarote.hrm.service.IHrmSalarySlipTemplateOptionService;
import com.kakarote.hrm.service.IHrmSalarySlipTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 工资表模板 服务实现类
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@Service
public class HrmSalarySlipTemplateServiceImpl extends BaseServiceImpl<HrmSalarySlipTemplateMapper, HrmSalarySlipTemplate> implements IHrmSalarySlipTemplateService {

    @Autowired
    private IHrmSalarySlipTemplateOptionService slipTemplateOptionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSlipTemplate(AddSlipTemplateBO addSlipTemplateBO) {
        HrmSalarySlipTemplate slipTemplate = new HrmSalarySlipTemplate();
        slipTemplate.setTemplateName(addSlipTemplateBO.getTemplateName());
        slipTemplate.setHideEmpty(addSlipTemplateBO.getHideEmpty());
        save(slipTemplate);
        List<HrmSalarySlipTemplateOption> slipTemplateOption = addSlipTemplateBO.getSlipTemplateOption();
        for (int i = 0; i < slipTemplateOption.size(); i++) {
            HrmSalarySlipTemplateOption salarySlipTemplateOption = slipTemplateOption.get(i);
            salarySlipTemplateOption.setTemplateId(slipTemplate.getId());
            salarySlipTemplateOption.setType(1);
            salarySlipTemplateOption.setPid(0);
            salarySlipTemplateOption.setSort(i+1);
            salarySlipTemplateOption.setId(null);
            slipTemplateOptionService.save(salarySlipTemplateOption);
            List<HrmSalarySlipTemplateOption> optionList = salarySlipTemplateOption.getOptionList();
            for (int j = 0; j < optionList.size(); j++) {
                HrmSalarySlipTemplateOption option = optionList.get(j);
                option.setId(null);
                option.setTemplateId(slipTemplate.getId());
                option.setType(2);
                option.setSort(j+1);
                option.setPid(salarySlipTemplateOption.getId());
            }
            slipTemplateOptionService.saveBatch(optionList,optionList.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSlipTemplate(Integer templateId) {
        slipTemplateOptionService.lambdaUpdate().eq(HrmSalarySlipTemplateOption::getTemplateId,templateId).remove();
        removeById(templateId);
    }
}
