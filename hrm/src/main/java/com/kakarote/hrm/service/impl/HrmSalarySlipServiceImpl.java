package com.kakarote.hrm.service.impl;


import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.entity.BO.QuerySalarySlipListBO;
import com.kakarote.hrm.entity.PO.HrmSalarySlip;
import com.kakarote.hrm.entity.VO.QuerySalarySlipListVO;
import com.kakarote.hrm.mapper.HrmSalarySlipMapper;
import com.kakarote.hrm.service.IHrmSalarySlipOptionService;
import com.kakarote.hrm.service.IHrmSalarySlipRecordService;
import com.kakarote.hrm.service.IHrmSalarySlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工资条 服务实现类
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@Service
public class HrmSalarySlipServiceImpl extends BaseServiceImpl<HrmSalarySlipMapper, HrmSalarySlip> implements IHrmSalarySlipService {


    @Autowired
    private HrmSalarySlipMapper salarySlipMapper;

    @Autowired
    private IHrmSalarySlipOptionService salarySlipOptionService;

    @Autowired
    private IHrmSalarySlipRecordService slipRecordService;

    @Override
    public BasePage<QuerySalarySlipListVO> querySalarySlipList(QuerySalarySlipListBO querySalarySlipListBO) {
        BasePage<QuerySalarySlipListVO> page = salarySlipMapper.querySalarySlipList(querySalarySlipListBO.parse(), querySalarySlipListBO, EmployeeHolder.getEmployeeId());
        page.getList().forEach(slip -> {
            if (slip.getReadStatus() == 0){
                lambdaUpdate().set(HrmSalarySlip::getReadStatus,1).eq(HrmSalarySlip::getId,slip.getId()).update();
            }
            slip.setSalarySlipOptionList(slipRecordService.querySlipDetail(slip.getId()));
        });
        return page;
    }
}
