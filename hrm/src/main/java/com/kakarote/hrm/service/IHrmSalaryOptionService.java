package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.SetSalaryOptionBO;
import com.kakarote.hrm.entity.PO.HrmSalaryOption;
import com.kakarote.hrm.entity.VO.SalaryOptionDetailVO;
import com.kakarote.hrm.entity.VO.SalaryOptionVO;

import java.util.List;

/**
 * <p>
 * 系统薪资项 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmSalaryOptionService extends BaseService<HrmSalaryOption> {

    /**
     * 查询薪资项详情
     * @return
     */
    SalaryOptionDetailVO querySalaryOptionDetail();

    /**
     * 查询薪资项列表
     * @return
     */
    List<SalaryOptionVO> querySalaryOptionList();


    /**
     * 修改薪资项
     * @param setSalaryOptionBO
     */
    void setSalaryOption(SetSalaryOptionBO setSalaryOptionBO);

}
