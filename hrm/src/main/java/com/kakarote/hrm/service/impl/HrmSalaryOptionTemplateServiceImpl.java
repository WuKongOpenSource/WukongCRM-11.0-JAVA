package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.RecursionUtil;
import com.kakarote.hrm.entity.PO.HrmSalaryOptionTemplate;
import com.kakarote.hrm.entity.VO.SalaryOptionVO;
import com.kakarote.hrm.mapper.HrmSalaryOptionMapper;
import com.kakarote.hrm.mapper.HrmSalaryOptionTemplateMapper;
import com.kakarote.hrm.service.IHrmSalaryOptionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统薪资项模板 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class HrmSalaryOptionTemplateServiceImpl extends BaseServiceImpl<HrmSalaryOptionTemplateMapper, HrmSalaryOptionTemplate> implements IHrmSalaryOptionTemplateService {

    @Autowired
    private HrmSalaryOptionMapper salaryOptionMapper;

    @Override
    public List<SalaryOptionVO> querySalaryOptionTemplateList() {
        List<HrmSalaryOptionTemplate> list = salaryOptionMapper.querySalaryOptionTemplateList();
        return RecursionUtil.getChildListTree(list, "parentCode", 0, "code", "children", SalaryOptionVO.class);
    }
}
