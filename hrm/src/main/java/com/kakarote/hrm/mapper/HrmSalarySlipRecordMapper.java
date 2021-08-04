package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QuerySendDetailListBO;
import com.kakarote.hrm.entity.BO.QuerySendRecordListBO;
import com.kakarote.hrm.entity.BO.QuerySlipEmployeePageListBO;
import com.kakarote.hrm.entity.BO.SendSalarySlipBO;
import com.kakarote.hrm.entity.PO.HrmSalarySlipRecord;
import com.kakarote.hrm.entity.VO.QuerySendDetailListVO;
import com.kakarote.hrm.entity.VO.QuerySendRecordListVO;
import com.kakarote.hrm.entity.VO.SlipEmployeeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发工资条记录 Mapper 接口
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
public interface HrmSalarySlipRecordMapper extends BaseMapper<HrmSalarySlipRecord> {

    BasePage<SlipEmployeeVO> querySlipEmployeePageList(BasePage<SlipEmployeeVO> page, @Param("sRecordId") Integer sRecordId,@Param("data") QuerySlipEmployeePageListBO slipEmployeePageListBO);

    BasePage<QuerySendRecordListVO> querySendRecordList(BasePage<QuerySendRecordListVO> page,@Param("data") QuerySendRecordListBO querySendRecordListBO);

    BasePage<QuerySendDetailListVO> querySendDetailList(BasePage<QuerySendDetailListVO> page,@Param("data") QuerySendDetailListBO querySendRecordListBO);

    List<Integer> querySlipEmployeeIds(@Param("sRecordId") Integer sRecordId,@Param("data") SendSalarySlipBO sendSalarySlipBO);
}
