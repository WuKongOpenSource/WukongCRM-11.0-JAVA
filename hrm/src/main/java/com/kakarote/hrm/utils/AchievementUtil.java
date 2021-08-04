package com.kakarote.hrm.utils;

import cn.hutool.core.date.DateUtil;
import com.kakarote.core.exception.CrmException;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.EmployeeEntryStatus;
import com.kakarote.hrm.constant.achievement.AppraisalEmployeeType;
import com.kakarote.hrm.entity.PO.HrmDept;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.service.IHrmDeptService;
import com.kakarote.hrm.service.IHrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Description:    绩效考核工具类
 * @author: hmb
 * @date:  2020-05-11 11:56
 */
@Component
public class AchievementUtil {

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private IHrmDeptService deptService;

    /**
     * 绩效时间格式化
     * @param startTime
     * @param endTime
     * @return
     */
    public static String appraisalTimeFormat(Date startTime,Date endTime){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM.dd");
        return DateUtil.format(startTime,dateFormat)+"-"+ DateUtil.format(endTime,dateFormat);
    }

    /**
     *  根据类型查找员工id
     * @param type 1 员工本人 2 直属上级 3 所在部门负责人 4 上级部门负责人 5 指定目标确认人
     * @param employeeId 当前员工id
     * @param designationEmployeeId 指定员工id
     * @return
     */
    public Integer findEmployeeIdByType(Integer type,Integer employeeId,Integer designationEmployeeId,String desc){
            HrmEmployee employee;
            HrmDept dept;
            AppraisalEmployeeType employeeType = AppraisalEmployeeType.parse(type);
            switch (AppraisalEmployeeType.parse(type)){
                case MYSELF:
                    return   employeeId;
                case PARENT:
                    employee = employeeService.getById(employeeId);
                    HrmEmployee parentEmployee = employeeService.getById(employee.getParentId());
                    if (parentEmployee == null || parentEmployee.getEntryStatus().equals(EmployeeEntryStatus.ALREADY_LEAVE.getValue()) || parentEmployee.getIsDel() == 1){
                        throw new CrmException(HrmCodeEnum.PARENT_DOES_NOT_EXIST,employee.getEmployeeName(),desc);
                    }
                    return parentEmployee.getEmployeeId();
                case MYSELF_DEPT_MAIN:
                    employee = employeeService.getById(employeeId);
                    dept = deptService.getById(employee.getDeptId());
                    if (dept == null){
                        throw new CrmException(HrmCodeEnum.DEPT_DOES_NOT_EXIST,employee.getEmployeeName(),desc);
                    }
                    HrmEmployee deptEmployee = employeeService.getById(dept.getMainEmployeeId());
                    if (deptEmployee == null || deptEmployee.getEntryStatus().equals(EmployeeEntryStatus.ALREADY_LEAVE.getValue()) || deptEmployee.getIsDel() == 1){
                        throw new CrmException(HrmCodeEnum.DEPT_MAIN_EMPLOYEE_DOES_NOT_EXIST,employee.getEmployeeName(),desc);
                    }
                    return deptEmployee.getEmployeeId();
                case PARENT_DEPT_MAIN:
                    employee = employeeService.getById(employeeId);
                    dept = deptService.getById(employee.getDeptId());
                    if (dept == null){
                        throw new CrmException(HrmCodeEnum.DEPT_DOES_NOT_EXIST,employee.getEmployeeName(),desc);
                    }
                    HrmDept parentDept = deptService.getById(dept.getPid());
                    if (parentDept != null){
                        //上级部门存在
                        HrmEmployee parentDeptEmployee = employeeService.getById(parentDept.getMainEmployeeId());
                        if (parentDeptEmployee == null || parentDeptEmployee.getEntryStatus().equals(EmployeeEntryStatus.ALREADY_LEAVE.getValue()) || parentDeptEmployee.getIsDel() == 1){
                            throw new CrmException(HrmCodeEnum.PARENT_MAIN_EMPLOYEE_DEPT_DOES_NOT_EXIST,employee.getEmployeeName(),desc);
                        }
                        return parentDeptEmployee.getEmployeeId();
                    }else {
//                        不存在直接找本机部门负责人
                        HrmEmployee deptEmployee1 = employeeService.getById(dept.getMainEmployeeId());
                        if (deptEmployee1 == null || deptEmployee1.getEntryStatus().equals(EmployeeEntryStatus.ALREADY_LEAVE.getValue()) || deptEmployee1.getIsDel() == 1){
                            throw new CrmException(HrmCodeEnum.DEPT_MAIN_EMPLOYEE_DOES_NOT_EXIST,employee.getEmployeeName(),desc);
                        }
                        return deptEmployee1.getEmployeeId();
                    }
                case DESIGNATION:
                    HrmEmployee designationEmployee = employeeService.getById(designationEmployeeId);
                    if (designationEmployee.getEntryStatus().equals(EmployeeEntryStatus.ALREADY_LEAVE.getValue()) || designationEmployee.getIsDel() == 1){
                        throw new CrmException(HrmCodeEnum.DESIGNATION_EMPLOYEE_DEPT_DOES_NOT_EXIST,designationEmployee.getEmployeeName(),desc);
                    }
                    return designationEmployee.getEmployeeId();
                default:return null;
            }
    }
}
