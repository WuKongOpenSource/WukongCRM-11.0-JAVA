package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryInsurancePageListBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceRecordListBO;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthRecord;
import com.kakarote.hrm.entity.VO.QueryInsurancePageListVO;
import com.kakarote.hrm.entity.VO.QueryInsuranceRecordListVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每月社保记录 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface HrmInsuranceMonthRecordMapper extends BaseMapper<HrmInsuranceMonthRecord> {

    /**
     * 查询参保员工id
     * @return
     */
    List<Map<String,Integer>> queryInsuranceEmployee();

    BasePage<QueryInsuranceRecordListVO> queryInsuranceRecordList(BasePage<QueryInsuranceRecordListVO> parse,
                                                                  @Param("data") QueryInsuranceRecordListBO recordListBO,
                                                                  @Param("employeeIds") Collection<Integer> employeeIds,@Param("isAll") boolean isAll);

    BasePage<QueryInsurancePageListVO> queryInsurancePageList(BasePage<QueryInsurancePageListVO> parse,@Param("data") QueryInsurancePageListBO queryInsurancePageListBO,
                                                              @Param("employeeIds")Collection<Integer> employeeIds);

    QueryInsuranceRecordListVO queryInsuranceRecord(@Param("iRecordId") String iRecordId,@Param("employeeIds")Collection<Integer> employeeIds);

    List<Integer> queryDeleteEmpRecordIds(Integer iRecordId);
}
