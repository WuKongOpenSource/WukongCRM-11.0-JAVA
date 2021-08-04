package com.kakarote.hrm.cron;

import cn.hutool.core.collection.CollUtil;
import com.kakarote.hrm.constant.EmployeeEntryStatus;
import com.kakarote.hrm.constant.EmployeeStatusEnum;
import com.kakarote.hrm.constant.HrmActionBehaviorEnum;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.entity.PO.HrmEmployeeChangeRecord;
import com.kakarote.hrm.entity.PO.HrmEmployeeQuitInfo;
import com.kakarote.hrm.service.IHrmEmployeeChangeRecordService;
import com.kakarote.hrm.service.IHrmEmployeeQuitInfoService;
import com.kakarote.hrm.service.IHrmEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EmployeeChangeCron {

    @Autowired
    private IHrmEmployeeChangeRecordService changeRecordService;

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private IHrmEmployeeQuitInfoService quitInfoService;

    /**
     * 调岗晋升转正
     *
     */
    public void employeeChangeRecords() {
        List<HrmEmployee> employeeList = new ArrayList<>();
        List<HrmEmployeeChangeRecord> hrmEmployeeChangeRecords = changeRecordService.lambdaQuery()
                .apply("to_days(effect_time) = to_days(now())")
                .groupBy(HrmEmployeeChangeRecord::getEmployeeId)
                .having("max(create_time)").list();
        hrmEmployeeChangeRecords.forEach(changeRecord -> {
            employeeList.add(employeeChangeRecord(changeRecord));
        });
        List<HrmEmployee> list = employeeService.lambdaQuery()
                .ne(HrmEmployee::getStatus,EmployeeStatusEnum.OFFICIAL.getValue())
                .apply("to_days(become_time) = to_days(now())")
                .list();
        list.forEach(employee -> {
            employee.setBecomeTime(new Date());
            employee.setStatus(EmployeeStatusEnum.OFFICIAL.getValue());
            employee.setUpdateTime(new Date());
        });
        employeeList.addAll(list);
        if (CollUtil.isNotEmpty(employeeList)){
            employeeService.saveOrUpdateBatch(employeeList);
        }
    }

    /**
     * 员工离职变更状态
     *
     */
    public void employeeQuit() {
        List<HrmEmployeeQuitInfo> quitInfoList = quitInfoService.lambdaQuery().select(HrmEmployeeQuitInfo::getEmployeeId)
                .apply("to_days(plan_quit_time) = to_days(now())").list();
        List<HrmEmployee> employeeList = quitInfoList.stream().map(quitInfo -> {
            HrmEmployee employee = new HrmEmployee();
            employee.setEmployeeId(quitInfo.getEmployeeId());
            employee.setEntryStatus(EmployeeEntryStatus.ALREADY_LEAVE.getValue());
            return employee;
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(employeeList)){
            employeeService.saveOrUpdateBatch(employeeList);
        }
    }

    public static HrmEmployee employeeChangeRecord(HrmEmployeeChangeRecord changeRecord) {
        Integer changeType = changeRecord.getChangeType();
        HrmEmployee employee = new HrmEmployee();
        employee.setEmployeeId(changeRecord.getEmployeeId());
        employee.setDeptId(changeRecord.getNewDept());
        employee.setPost(changeRecord.getNewPost());
        employee.setPostLevel(changeRecord.getNewPostLevel());
        if (changeType.equals(HrmActionBehaviorEnum.CHANGE_POST.getValue())
                || changeType.equals(HrmActionBehaviorEnum.PROMOTED.getValue())
                || changeType.equals(HrmActionBehaviorEnum.DEGRADE.getValue())
                || changeType.equals(HrmActionBehaviorEnum.CHANGE_FULL_TIME_EMPLOYEE.getValue())) {
            employee.setWorkAddress(changeRecord.getNewWorkAddress());
            if (changeType.equals(HrmActionBehaviorEnum.CHANGE_FULL_TIME_EMPLOYEE.getValue())) {
                Integer probation = changeRecord.getProbation();
                employee.setProbation(probation);
                if (probation == 0) {
                    employee.setBecomeTime(new Date());
                    employee.setStatus(EmployeeStatusEnum.OFFICIAL.getValue());
                } else {
                    employee.setStatus(EmployeeStatusEnum.TRY_OUT.getValue());
                }
            }
        } else if (changeType.equals(HrmActionBehaviorEnum.BECOME.getValue())) {
            employee.setBecomeTime(new Date());
            employee.setStatus(EmployeeStatusEnum.OFFICIAL.getValue());
        }
        employee.setUpdateTime(new Date());
        return employee;
    }
}
