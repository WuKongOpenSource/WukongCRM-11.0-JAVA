package com.kakarote.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.entity.BO.SetChangeTemplateBO;
import com.kakarote.hrm.entity.PO.HrmSalaryChangeTemplate;
import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import com.kakarote.hrm.entity.VO.QueryChangeTemplateListVO;
import com.kakarote.hrm.mapper.HrmSalaryChangeTemplateMapper;
import com.kakarote.hrm.service.IHrmSalaryChangeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 调薪模板 服务实现类
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
@Service
public class HrmSalaryChangeTemplateServiceImpl extends BaseServiceImpl<HrmSalaryChangeTemplateMapper, HrmSalaryChangeTemplate> implements IHrmSalaryChangeTemplateService {


    @Autowired
    private HrmSalaryChangeTemplateMapper salaryChangeTemplateMapper;

    @Override
    public List<ChangeSalaryOptionVO> queryChangeSalaryOption() {
        return salaryChangeTemplateMapper.queryChangeSalaryOption();
    }

    @Override
    public void setChangeTemplate(SetChangeTemplateBO setChangeTemplateBO) {
        HrmSalaryChangeTemplate salaryChangeTemplate = new HrmSalaryChangeTemplate();
        salaryChangeTemplate.setId(setChangeTemplateBO.getId());
        salaryChangeTemplate.setTemplateName(setChangeTemplateBO.getTemplateName());
        salaryChangeTemplate.setValue(JSON.toJSONString(setChangeTemplateBO.getValue()));
        saveOrUpdate(salaryChangeTemplate);
    }

    @Override
    public List<QueryChangeTemplateListVO> queryChangeTemplateList() {
        List<HrmSalaryChangeTemplate> list = lambdaQuery().list();
        return list.stream().map(template->{
            QueryChangeTemplateListVO changeTemplateListVO = new QueryChangeTemplateListVO();
            changeTemplateListVO.setId(template.getId());
            changeTemplateListVO.setTemplateName(template.getTemplateName());
            changeTemplateListVO.setIsDefault(template.getIsDefault());
            changeTemplateListVO.setValue(JSON.parseArray(template.getValue(),ChangeSalaryOptionVO.class));
            return changeTemplateListVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteChangeTemplate(Integer id) {
        HrmSalaryChangeTemplate template = getById(id);
        if (template.getIsDefault() == 1){
            throw new CrmException(HrmCodeEnum.DEFAULT_TEMPLATE_CANNOT_BE_DELETED);
        }
        removeById(id);
    }
}
