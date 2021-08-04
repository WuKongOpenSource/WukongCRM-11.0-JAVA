package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryEmployeePageListBO;
import com.kakarote.hrm.entity.BO.QueryNotesStatusBO;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.entity.PO.HrmEmployeeField;
import com.kakarote.hrm.entity.VO.EmployeeInfo;
import com.kakarote.hrm.entity.VO.SimpleHrmEmployeeVO;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * <p>
 * 员工表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmEmployeeMapper extends BaseMapper<HrmEmployee> {

    BasePage<Map<String,Object>> queryPageList(BasePage<Map<String,Object>> page,
                                               @Param("data") QueryEmployeePageListBO employeePageListBO,
                                               @Param("birthdayEmpList") List<Integer> birthdayEmpList,
                                               @Param("employeeIds")Collection<Integer> employeeIds);

    EmployeeInfo queryEmployeeInfoByMobile(String mobile);

    List<Map<String, Object>> queryNoPaySalaryEmployee(@Param("employeeIds") List<Integer> employeeIds);

    Integer queryFieldValueNoDelete(@Param("uniqueList") List<HrmEmployeeField> uniqueList);

    List<Integer> queryToInByMonth(@Param("year") int year,@Param("month") int month);

    List<Integer> queryToLeaveByMonth(@Param("year") int year,@Param("month") int month);


    /**
     * 查询生日员工数量
     * @param lunarList 农历
     * @param solarList 阳历
     * @return
     */
    List<Integer> queryBirthdayEmp(@Param("lunarList") List<String> lunarList,@Param("solarList") List<String> solarList);

    /**
     * 查询生日员工
     * @param lunarBirthday 农历
     * @param solarBirthday 阳历
     * @param employeeIds
     * @return
     */
    List<HrmEmployee> queryBirthdayListByTime(@Param("lunarBirthday") String lunarBirthday, @Param("solarBirthday") String solarBirthday,
                                              @Param("employeeIds") Collection<Integer> employeeIds);

    List<HrmEmployee> queryEntryEmpListByTime(@Param("time") Date time,@Param("employeeIds") Collection<Integer> employeeIds);

    List<HrmEmployee> queryBecomeEmpListByTime(@Param("time") Date time,@Param("employeeIds") Collection<Integer> employeeIds);

    List<HrmEmployee> queryLeaveEmpListByTime(@Param("time") Date time,@Param("employeeIds") Collection<Integer> employeeIds);

    List<Integer> queryToCorrectCount();

    List<Integer> queryToInCount();

    List<SimpleHrmEmployeeVO> querySimpleEmpByDeptId(Integer deptId);

    List<Map<String, Object>> export(@Param("data") QueryEmployeePageListBO employeePageListBO);

    Set<Integer> filterDeleteEmployeeIds(@Param("employeeIds") Set<Integer> employeeIds);

    Set<String> queryEntryStatusList(@Param("data") QueryNotesStatusBO queryNotesStatusBO,@Param("employeeIds") Collection<Integer> employeeIds);

    Set<String> queryBecomeStatusList(@Param("data") QueryNotesStatusBO queryNotesStatusBO,@Param("employeeIds") Collection<Integer> employeeIds);

    Set<String> queryLeaveStatusList(@Param("data") QueryNotesStatusBO queryNotesStatusBO,@Param("employeeIds") Collection<Integer> employeeIds);
}
