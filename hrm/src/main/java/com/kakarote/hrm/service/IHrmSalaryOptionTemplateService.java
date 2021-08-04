package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmSalaryOptionTemplate;
import com.kakarote.hrm.entity.VO.SalaryOptionVO;

import java.util.List;

/**
 * <p>
 * 系统薪资项模板 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmSalaryOptionTemplateService extends BaseService<HrmSalaryOptionTemplate> {

    /**
     * 查询薪资模板列表
     * @return
     */
    List<SalaryOptionVO> querySalaryOptionTemplateList();
}
