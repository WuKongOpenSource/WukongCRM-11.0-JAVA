package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QuerySalarySlipListBO;
import com.kakarote.hrm.entity.PO.HrmSalarySlip;
import com.kakarote.hrm.entity.VO.QuerySalarySlipListVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 工资条 Mapper 接口
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
public interface HrmSalarySlipMapper extends BaseMapper<HrmSalarySlip> {

    BasePage<QuerySalarySlipListVO> querySalarySlipList(BasePage<QuerySalarySlipListVO> parse, @Param("data") QuerySalarySlipListBO querySalarySlipListBO,@Param("employeeId") Integer employeeId);
}
