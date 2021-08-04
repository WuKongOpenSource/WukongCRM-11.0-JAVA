package com.kakarote.hrm.service.actionrecord.impl;

import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.hrm.constant.HrmActionBehaviorEnum;
import com.kakarote.hrm.constant.HrmActionTypeEnum;
import com.kakarote.hrm.entity.BO.UpdateScheduleBO;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeSeg;
import com.kakarote.hrm.service.actionrecord.AbstractHrmActionRecordService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("employeeAppraisalActionRecordService")
@SysLog(subModel = SubModelType.HRM_APPRAISAL)
public class EmployeeAppraisalActionRecordServiceImpl extends AbstractHrmActionRecordService {

    private static HrmActionTypeEnum actionTypeEnum = HrmActionTypeEnum.EMPLOYEE_APPRAISAL;
    private static HrmActionBehaviorEnum behaviorEnum = HrmActionBehaviorEnum.UPDATE;


    /**
     * 开启考核记录
     */
    @SysLogHandler(isReturn = true)
    public Content startAppraisalRecord(String appraisalName, List<Integer> employeeAppraisalIds){
        String content =  "开启了考核";
        employeeAppraisalIds.forEach(employeeAppraisalId->{
            save(content,employeeAppraisalId);
        });
        return new Content(appraisalName,content+":"+appraisalName, BehaviorEnum.UPDATE);
    }

    /**
     * 提交考核
     */
    @SysLogHandler(isReturn = true)
    public Content submitAppraisalRecord(Integer employeeAppraisalId){
        String content =  "提交了目标";
        save(content,employeeAppraisalId);
        return new Content("",content, BehaviorEnum.UPDATE);
    }

    /**
     * 确认目标
     */
    @SysLogHandler(isReturn = true)
    public Content confirmAppraisalRecord(Integer employeeAppraisalId){
        String content =  "确认了目标";
        save(content,employeeAppraisalId);
        return new Content("",content, BehaviorEnum.UPDATE);
    }
    /**
     * 提交评定
     */
    @SysLogHandler(isReturn = true)
    public Content submitEvaluatoRecord(Integer employeeAppraisalId){
        String content =  "提交了评定";
        save(content,employeeAppraisalId);
        return new Content("",content, BehaviorEnum.UPDATE);
    }

    /**
     * 确认考核结果，考核完成
     */
    @SysLogHandler(isReturn = true)
    public Content confirmResult(String appraisalName, List<Integer> employeeAppraisalIds, boolean isLast){
        String content;
        if (isLast){
            content =  "确认考核结果，考核完成";
        }else {
            content =  "确认考核结果";
        }
        employeeAppraisalIds.forEach(employeeAppraisalId->{
            save(content,employeeAppraisalId);
        });
        return new Content(appraisalName,content, BehaviorEnum.UPDATE);
    }

    /**
     * 修改目标进度
     */
    @SysLogHandler(isReturn = true)
    public Content updateSchedule(String appraisalName, UpdateScheduleBO updateScheduleBO, HrmAchievementEmployeeSeg seg){
        String content = "修改了目标"+seg.getSegName()+"进度为"+updateScheduleBO.getSchedule()+"%,完成情况说明:"+updateScheduleBO.getExplainDesc();
        save(content,seg.getEmployeeAppraisalId());
        return new Content(appraisalName,content, BehaviorEnum.UPDATE);
    }

    /**
     * 拒绝
     * @param appraisalName
     * @param status 2 驳回目标 3 驳回评定
     * @param reason 原因
     */
    @SysLogHandler(isReturn = true)
    public Content reject(String appraisalName, Integer employeeAppraisalId, Integer status, String reason){
        String content;
        if (status == 2){
            content =  "驳回了目标,驳回理由："+reason;
        }else{
            content =  "驳回了评定,驳回理由："+reason;
        }
        save(content,employeeAppraisalId);

        return new Content(appraisalName,content, BehaviorEnum.UPDATE);
    }

    /**
     *终止考核
     */
    @SysLogHandler(isReturn = true)
    public Content stopAppraisal(String appraisalName, List<Integer> employeeAppraisalIds){
        String content =  "终止了考核";
        employeeAppraisalIds.forEach(employeeAppraisalId->{
            save(content,employeeAppraisalId);
        });
        return new Content(appraisalName,content, BehaviorEnum.UPDATE);
    }


    /**
     * 保存操作记录
     * @param content
     * @param employeeAppraisalId
     */
    private void save(String content,Integer employeeAppraisalId){
        actionRecordService.saveRecord(actionTypeEnum,behaviorEnum, Collections.singletonList(content),employeeAppraisalId);
    }

}
