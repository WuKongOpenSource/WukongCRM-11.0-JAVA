package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmSalaryGroup;
import com.kakarote.hrm.entity.VO.SalaryGroupPageListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 薪资组 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface HrmSalaryGroupMapper extends BaseMapper<HrmSalaryGroup> {

    BasePage<SalaryGroupPageListVO> querySalaryGroupPageList(BasePage<SalaryGroupPageListVO> parse);

    /**
     * 通过员工id查询薪资组
     * @param employeeId
     * @return
     */
    HrmSalaryGroup queryEmployeeSalaryGroupByEmpId(@Param("employeeId")Integer employeeId);

    /**
     * 通过部门id查询薪资组
     * @param deptIds
     * @return
     */
    HrmSalaryGroup queryEmployeeSalaryGroupByDId(@Param("deptIds") Set<Integer> deptIds);

    List<HrmSalaryGroup> querySalaryGroupByTaxType(int taxType);
}
