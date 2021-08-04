package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryDeptListBO;
import com.kakarote.hrm.entity.BO.QueryEmployeeByDeptIdBO;
import com.kakarote.hrm.entity.PO.HrmDept;
import com.kakarote.hrm.entity.VO.DeptEmployeeVO;
import com.kakarote.hrm.entity.VO.DeptVO;
import com.kakarote.hrm.entity.VO.QueryEmployeeListByDeptIdVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmDeptMapper extends BaseMapper<HrmDept> {

    DeptVO queryById(@Param("deptId") Integer deptId, @Param("employeeIds")Collection<Integer> employeeIds);

    List<DeptVO> queryList(@Param("queryDeptListBO") QueryDeptListBO queryDeptListBO);

    BasePage<QueryEmployeeListByDeptIdVO> queryEmployeeByDeptId(BasePage<QueryEmployeeListByDeptIdVO> parse,@Param("data") QueryEmployeeByDeptIdBO employeeByDeptIdBO,
                                                                @Param("employeeIds")Collection<Integer> employeeIds);

    List<DeptEmployeeVO> queryDeptEmployeeList();

    List<DeptVO> queryDeptByEmpIds(@Param("employeeIds") Collection<Integer> employeeIds);
}
