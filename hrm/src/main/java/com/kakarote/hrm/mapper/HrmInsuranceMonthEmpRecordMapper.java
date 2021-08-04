package com.kakarote.hrm.mapper;

import cn.hutool.core.date.DateTime;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryEmpInsuranceMonthBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceRecordListBO;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthEmpRecord;
import com.kakarote.hrm.entity.VO.QueryEmpInsuranceMonthVO;
import com.kakarote.hrm.entity.VO.QueryInsurancePageListVO;
import com.kakarote.hrm.entity.VO.SimpleHrmEmployeeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 员工每月社保记录 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface HrmInsuranceMonthEmpRecordMapper extends BaseMapper<HrmInsuranceMonthEmpRecord> {

    BasePage<QueryEmpInsuranceMonthVO> queryEmpInsuranceMonth(BasePage<QueryEmpInsuranceMonthVO> parse,@Param("data") QueryEmpInsuranceMonthBO queryEmpInsuranceMonthBO);

    List<SimpleHrmEmployeeVO> queryNoInsuranceEmp(@Param("iRecordId") Integer iRecordId,@Param("dateTime") DateTime dateTime);

    BasePage<QueryInsurancePageListVO> myInsurancePageList(BasePage<QueryInsurancePageListVO> parse,@Param("data") QueryInsuranceRecordListBO recordListBO);

}
