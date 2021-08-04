package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.SetSalaryGroupBO;
import com.kakarote.hrm.entity.PO.HrmSalaryGroup;
import com.kakarote.hrm.entity.PO.HrmSalaryTaxRule;
import com.kakarote.hrm.entity.VO.SalaryGroupPageListVO;

import java.util.List;

/**
 * <p>
 * 薪资组 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmSalaryGroupService extends BaseService<HrmSalaryGroup> {

    /**
     * 查询薪资组列表
     * @param pageEntity
     * @return
     */
    BasePage<SalaryGroupPageListVO> querySalaryGroupPageList(PageEntity pageEntity);

    /**
     * 查询员工计税规则
     * @param employeeId
     * @return
     */
    HrmSalaryTaxRule queryEmployeeTaxRule(Integer employeeId);

    /**
     * 添加修改薪资组
     * @param salaryGroup
     */
    void setSalaryGroup(SetSalaryGroupBO salaryGroup);

    /**
     * 根据计税类型查询薪资组
     * @param taxType
     * @return
     */
    List<HrmSalaryGroup> querySalaryGroupByTaxType(int taxType);
}
