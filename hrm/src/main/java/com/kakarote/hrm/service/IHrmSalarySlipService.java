package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.QuerySalarySlipListBO;
import com.kakarote.hrm.entity.PO.HrmSalarySlip;
import com.kakarote.hrm.entity.VO.QuerySalarySlipListVO;

/**
 * <p>
 * 工资条 服务类
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
public interface IHrmSalarySlipService extends BaseService<HrmSalarySlip> {

    /**
     * 查询工资条列表
     * @param querySalarySlipListBO
     * @return
     */
    BasePage<QuerySalarySlipListVO> querySalarySlipList(QuerySalarySlipListBO querySalarySlipListBO);
}
