package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryHistorySalaryListBO;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthRecord;
import com.kakarote.hrm.entity.VO.QueryHistorySalaryDetailVO;
import com.kakarote.hrm.entity.VO.QueryHistorySalaryListVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每月薪资记录 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface HrmSalaryMonthRecordMapper extends BaseMapper<HrmSalaryMonthRecord> {

    BigDecimal queryBeforeTaxTotalSalary(@Param("employeeId") Integer employeeId,@Param("year") Integer year,@Param("month") Integer month);

    /**
     * 查询每月薪资合计(生成历史薪资使用)
     * @param sRecordId
     * @return
     */
    Map<String, Object> queryMonthSalaryCount(Integer sRecordId);

    BasePage<QueryHistorySalaryListVO> queryHistorySalaryList(BasePage<Object> parse, @Param("data") QueryHistorySalaryListBO queryHistorySalaryListBO,
                                                              @Param("employeeIds") Collection<Integer> employeeIds);

    List<Map<String, Object>> querySalaryOptionCount(String sRecordId);

    List<Map<String, Object>> querySalaryByIds(@Param("sEmpRecordIds") List<Integer> sEmpRecordIds,@Param("sRecordId") Integer sRecordId);

    List<Integer> queryDeleteEmpRecordIds(Integer sRecordId);

    QueryHistorySalaryDetailVO queryHistorySalaryDetail(@Param("sRecordId")Integer sRecordId,@Param("employeeIds") Collection<Integer> employeeIds);

}
