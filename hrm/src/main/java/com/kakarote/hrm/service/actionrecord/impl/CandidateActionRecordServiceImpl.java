package com.kakarote.hrm.service.actionrecord.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.*;
import com.kakarote.hrm.entity.BO.EliminateCandidateBO;
import com.kakarote.hrm.entity.BO.UpdateCandidatePostBO;
import com.kakarote.hrm.entity.BO.UpdateCandidateRecruitChannelBO;
import com.kakarote.hrm.entity.BO.UpdateCandidateStatusBO;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.service.actionrecord.AbstractHrmActionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 候选人操作记录
 * @author hmb
 */
@Service("candidateActionRecordService")
@SysLog(subModel = SubModelType.HRM_CANDIDATE)
public class CandidateActionRecordServiceImpl extends AbstractHrmActionRecordService {

    private static HrmActionTypeEnum actionTypeEnum = HrmActionTypeEnum.RECRUIT_CANDIDATE;
    private static LabelGroupEnum labelGroupEnum = LabelGroupEnum.RECRUIT_CANDIDATE;


    @Autowired
    private IHrmRecruitPostService recruitPostService;

    @Autowired
    private IHrmRecruitCandidateService recruitCandidateService;

    @Autowired
    private IHrmRecruitChannelService recruitChannelService;

    @Autowired
    private  IHrmEmployeeService employeeService;

    @Autowired
    private  IHrmRecruitInterviewService interviewService;



    /**
     * 属性kv
     */
    private static Dict properties = Dict.create().set("candidateName", "候选人名称").set("mobile", "手机").set("sex", "性别").set("age", "年龄")
            .set("postId", "职位").set("workTime", "工作年限").set("education", "学历").set("graduateSchool", "毕业院校").set("latestWorkPlace", "最近工作单位")
            .set("channelId", "招聘渠道").set("remark", "备注");


    /**
     * 新建/删除操作记录
     */
    @SysLogHandler(isReturn = true)
    public Content addOrDeleteRecord(HrmActionBehaviorEnum behaviorEnum, Integer candidateId){
        String content = behaviorEnum.getName()+"了" + labelGroupEnum.getDesc();
        actionRecordService.saveRecord(actionTypeEnum,behaviorEnum, Collections.singletonList(content),candidateId);
        HrmRecruitCandidate candidate = recruitCandidateService.getById(candidateId);
        if (HrmActionBehaviorEnum.ADD.equals(behaviorEnum)){
            return new Content(candidate.getCandidateName(),content, BehaviorEnum.SAVE);
        }else {
            return new Content(candidate.getCandidateName(),content,BehaviorEnum.DELETE);
        }
    }


    /**
     * 职位实体类修改
     */
    @SysLogHandler(isReturn = true)
    public Content entityUpdateRecord(Map<String, Object> oldRecord, Map<String, Object> newRecord, Integer candidateId, String candidateName) {
        List<String> contentList = entityCommonUpdateRecord(labelGroupEnum,properties, oldRecord, newRecord);
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, contentList, candidateId);
        return new Content(candidateName, CollUtil.join(contentList,","),BehaviorEnum.UPDATE);
    }

    @Override
    protected String compare(LabelGroupEnum labelGroupEnum, Dict properties, String newFieldKey, String oldValue, String newValue) {
        String content = super.compare(labelGroupEnum, properties, newFieldKey, oldValue, newValue);
        if ("postId".equals(newFieldKey)) {
            HrmRecruitPost oldPost = recruitPostService.getById(oldValue);
            HrmRecruitPost newPost = recruitPostService.getById(newValue);
            if (oldPost != null){
                oldValue = oldPost.getPostName();
            }
            if (newPost != null){
                newValue = newPost.getPostName();
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        } else if ("sex".equals(newFieldKey)) {
            if (!"空".equals(oldValue)){
                oldValue = SexEnum.parseName(Integer.parseInt(oldValue));
            }
            if (!"空".equals(newValue)){
                newValue = SexEnum.parseName(Integer.parseInt(newValue));
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("education".equals(newFieldKey)) {
            if (!"空".equals(oldValue)){
                oldValue = CandidateEducationEnum.parseName(Integer.parseInt(oldValue));
            }
            if (!"空".equals(newValue)){
                newValue = CandidateEducationEnum.parseName(Integer.parseInt(newValue));
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("channelId".equals(newFieldKey)) {
            HrmRecruitChannel oldChannel = recruitChannelService.getById(oldValue);
            HrmRecruitChannel newChannel = recruitChannelService.getById(newValue);
            if (oldChannel != null){
                oldValue = oldChannel.getValue();
            }
            if (newChannel != null){
                newValue = newChannel.getValue();
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }
        return content;
    }

    /**
     * 批量修改候选人状态 操作记录
     *   --移动到初选通过   2
     *   --移动到面试通过  4
     *   --移动到待入职   6
     *   --恢复到新候选人  1
     */
    @SysLogHandler(isReturn = true)
    public List<Content> updateCandidateStatusRecord(UpdateCandidateStatusBO updateCandidateStatusBO){
        List<Content> contentList = new ArrayList<>();
        List<Integer> candidateIds = updateCandidateStatusBO.getCandidateIds();
        Integer status = updateCandidateStatusBO.getStatus();
        String  content = "将候选人{}移到"+CandidateStatusEnum.parseName(status);
        for (Integer candidateId : candidateIds) {
            HrmRecruitCandidate candidate = recruitCandidateService.getById(candidateId);
            String format = StrUtil.format(content, candidate.getCandidateName());
            actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(format), candidateId);
            contentList.add(new Content(candidate.getCandidateName(), format,BehaviorEnum.UPDATE));
        }
        return contentList;
    }

    @SysLogHandler(isReturn = true)
    public List<Content> eliminateCandidateBORecord(EliminateCandidateBO eliminateCandidateBO){
        List<Content> contentList = new ArrayList<>();
        String content = "淘汰了候选人{},淘汰原因:"+eliminateCandidateBO.getEliminate();
        if (StrUtil.isNotEmpty(eliminateCandidateBO.getRemarks())){
            content += ",备注:"+eliminateCandidateBO.getRemarks();
        }
        for (Integer candidateId : eliminateCandidateBO.getCandidateIds()) {
            HrmRecruitCandidate candidate = recruitCandidateService.getById(candidateId);
            String format = StrUtil.format(content, candidate.getCandidateName());
            actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(format), candidateId);
            contentList.add(new Content(candidate.getCandidateName(), format,BehaviorEnum.UPDATE));
        }
        return contentList;
    }


    /**
     * 修改应聘职位操作记录
     */
    @SysLogHandler(isReturn = true)
    public  List<Content> updateCandidatePostRecord(UpdateCandidatePostBO updateCandidatePostBO){
        List<Content> contentList = new ArrayList<>();
        List<Integer> candidateIds = updateCandidatePostBO.getCandidateIds();
        Integer postId = updateCandidatePostBO.getPostId();
        HrmRecruitPost recruitPost = recruitPostService.getById(postId);
        String content = "为候选人{}更改了应聘职位:"+recruitPost.getPostName();
        for (Integer candidateId : candidateIds) {
            HrmRecruitCandidate candidate = recruitCandidateService.getById(candidateId);
            String format = StrUtil.format(content, candidate.getCandidateName());
            actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(format), candidateId);
            contentList.add(new Content(candidate.getCandidateName(), format,BehaviorEnum.UPDATE));
        }
        return contentList;
    }

    /**
     * 修改应聘渠道操作记录
     */
    @SysLogHandler(isReturn = true)
    public  List<Content> updateCandidateRecruitChannel(UpdateCandidateRecruitChannelBO updateCandidateRecruitChannelBO){
        List<Content> contentList = new ArrayList<>();
        List<Integer> candidateIds = updateCandidateRecruitChannelBO.getCandidateIds();
        HrmRecruitChannel channel = recruitChannelService.getById(updateCandidateRecruitChannelBO.getChannelId());
        String recruitChannel = channel.getValue();
        String content = "为候选人{}更改了应聘渠道:"+recruitChannel;
        for (Integer candidateId : candidateIds) {
            HrmRecruitCandidate candidate = recruitCandidateService.getById(candidateId);
            String format = StrUtil.format(content, candidate.getCandidateName());
            actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(format), candidateId);
            contentList.add(new Content(candidate.getCandidateName(), format,BehaviorEnum.UPDATE));
        }
        return contentList;
    }

    /**
     * 一键清理候选人操作记录
     * @param jsonObject
     */
    public  void cleanCandidateRecord(JSONObject jsonObject){
        List<Integer> candidateIds = jsonObject.getJSONArray("candidateIds").toJavaList(Integer.class);
        String eliminate = jsonObject.getString("eliminate");
        String content = "清理了候选人,原因"+eliminate;
        for (Integer candidateId : candidateIds) {
            actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(content), candidateId);
        }
    }

    /**
     * 安排面试操作记录
     * @param interview
     */
    @SysLogHandler(isReturn = true)
    public Content addInterviewRecord(HrmRecruitInterview interview){
        HrmRecruitCandidate candidate = recruitCandidateService.getById(interview.getCandidateId());
        HrmEmployee interviewEmployee = employeeService.getById(interview.getInterviewEmployeeId());
        String interviewEmployeeName = "";
        String content = "为候选人"+candidate.getCandidateName()+"安排了面试："+ DateUtil.formatDateTime(interview.getInterviewTime());
        if(interview.getType()!=null){
            content+="，面试方式:"+InterviewType.parseName(interview.getType());
        }
        if (interviewEmployee!=null){
            interviewEmployeeName = interviewEmployee.getEmployeeName();
           content +="，面试官："+ interviewEmployeeName;
        }
        Set<Integer> employeeIds = TagUtil.toSet(interview.getOtherInterviewEmployeeIds());
        StringBuilder  otherInterviewEmployeeName= new StringBuilder();
        if (CollectionUtil.isNotEmpty(employeeIds)){
            employeeIds.forEach(employeeId-> {
                HrmEmployee employee = employeeService.getById(employeeId);
                //判断员工不存在 || 删除 || 已离职
                if (employee.getIsDel() == 1 || employee.getEntryStatus() == EmployeeEntryStatus.ALREADY_LEAVE.getValue()) {
                    throw new CrmException(HrmCodeEnum.RESULT_CONFIRM_EMPLOYEE_NO_EXIST, employee.getEmployeeName());
                }
                otherInterviewEmployeeName.append(employee.getEmployeeName()).append("、");
            });
        }
        if (StrUtil.isNotEmpty(otherInterviewEmployeeName.toString())){
            content+="，其他面试官："+ otherInterviewEmployeeName.deleteCharAt(otherInterviewEmployeeName.length()-1).toString();
        }
        content+="，面试轮次：第"+interview.getStageNum()+"轮";
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(content), interview.getCandidateId());
        return new Content(candidate.getCandidateName(), content,BehaviorEnum.UPDATE);
    }
    /**
     * 取消面试操作记录
     * @param interview
     */
    @SysLogHandler(isReturn = true)
    public Content cancelInterviewRecord(HrmRecruitInterview interview){
        HrmRecruitCandidate candidate = recruitCandidateService.getById(interview.getCandidateId());
        HrmEmployee interviewEmployee = employeeService.getById(interview.getInterviewEmployeeId());
        String interviewEmployeeName = "";
        if (interviewEmployee!=null){
            interviewEmployeeName = interviewEmployee.getEmployeeName();
        }
        Set<Integer> employeeIds = TagUtil.toSet(interview.getOtherInterviewEmployeeIds());
        StringBuilder  otherInterviewEmployeeName= new StringBuilder();
        if (CollectionUtil.isNotEmpty(employeeIds)){
            employeeIds.forEach(employeeId-> {
              HrmEmployee employee = employeeService.getById(employeeId);
              //判断员工不存在 || 删除 || 已离职
              if (employee.getIsDel() == 1 || employee.getEntryStatus() == EmployeeEntryStatus.ALREADY_LEAVE.getValue()) {
                  throw new CrmException(HrmCodeEnum.RESULT_CONFIRM_EMPLOYEE_NO_EXIST, employee.getEmployeeName());
              }
                otherInterviewEmployeeName.append(employee.getEmployeeName()).append("、");
          });
        }
        String content = "为候选人"+candidate.getCandidateName()+"取消了面试："+ DateUtil.formatDateTime(interview.getInterviewTime());
        if(interview.getType()!=null){
            content+="，面试方式："+InterviewType.parseName(interview.getType());
        }
        if (StrUtil.isNotEmpty(interview.getCancelReason())){
            content+="，取消原因："+interview.getCancelReason();
        }
        if (StrUtil.isNotEmpty(interviewEmployeeName)){
            content+="，原面试官："+ interviewEmployeeName;
        }
        if (StrUtil.isNotEmpty(otherInterviewEmployeeName.toString())){
            content+="，原其他面试官："+ otherInterviewEmployeeName.deleteCharAt(otherInterviewEmployeeName.length()-1).toString();
        }
        content+= "，原面试轮次：第"+interview.getStageNum()+"轮";
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(content), interview.getCandidateId());
        return new Content(candidate.getCandidateName(), content,BehaviorEnum.UPDATE);
    }

    /**
     * 更改面试安排记录
     * @param interview
     * @param operateType
     * @return
     */
    @SysLogHandler(isReturn = true)
    public Content updateInterviewRecord(HrmRecruitInterview interview,Integer operateType){
        HrmRecruitCandidate candidate = recruitCandidateService.getById(interview.getCandidateId());
        HrmRecruitInterview hrmRecruitInterview=interviewService.getById(interview.getInterviewId());
        StringBuilder content = new StringBuilder();
         content.append("为候选人"+candidate.getCandidateName()+"更改了面试安排") ;
         if(operateType==3){
           if(null!=interview.getInterviewTime()){
               content.append("，面试时间："+"从无更改为"+DateUtil.formatDateTime(interview.getInterviewTime()));
           }
           if(interview.getType()!=null){
                 content.append("，面试方式："+"从无更改为"+InterviewType.parseName(interview.getType()));
           }
           if(null!=interview.getInterviewEmployeeId()){
               HrmEmployee interviewEmployee = employeeService.getById(interview.getInterviewEmployeeId());
               if (interviewEmployee!=null){
                     content.append("，面试官："+"从无更改为"+interviewEmployee.getEmployeeName());
               }
           }
             Set<Integer> employeeIds = TagUtil.toSet(interview.getOtherInterviewEmployeeIds());
             StringBuilder  otherInterviewEmployeeName= new StringBuilder();
             if(CollectionUtil.isNotEmpty(employeeIds)){
                 employeeIds.forEach(employeeId -> {
                     HrmEmployee employee = employeeService.getById(employeeId);
                     //判断员工不存在 || 删除 || 已离职
                     if (employee.getIsDel() == 1 || employee.getEntryStatus() == EmployeeEntryStatus.ALREADY_LEAVE.getValue()) {
                         throw new CrmException(HrmCodeEnum.RESULT_CONFIRM_EMPLOYEE_NO_EXIST, employee.getEmployeeName());
                     }
                     otherInterviewEmployeeName.append(employee.getEmployeeName()).append("、");
                 });
                 content.append("，其他面试官：" + "从无更改为" + otherInterviewEmployeeName.deleteCharAt(otherInterviewEmployeeName.length()-1).toString());
             }
         }else{
             if(!interview.getInterviewTime().equals(hrmRecruitInterview.getInterviewTime())){
                 content.append("，面试时间："+"从"+DateUtil.formatDateTime(hrmRecruitInterview.getInterviewTime())+"更改为"+DateUtil.formatDateTime(interview.getInterviewTime()));
             }
             if(interview.getType()!=hrmRecruitInterview.getType()){
                 String oldInterViewType="无";
                 String interViewType="无";
                 if (hrmRecruitInterview.getType()!=null){
                     oldInterViewType= InterviewType.parseName(hrmRecruitInterview.getType());
                 }
                 if(interview.getType()!=null){
                     interViewType=  InterviewType.parseName(interview.getType());
                 }
                 content.append("，面试方式："+"从"+oldInterViewType+"更改为"+interViewType);
             }
             String  oldInterviewEmployeeName="无";
             String interviewEmployeeName = "无";
             if(null!=hrmRecruitInterview.getInterviewEmployeeId()){
                 HrmEmployee  oldInterviewEmployee=employeeService.getById(hrmRecruitInterview.getInterviewEmployeeId());
                 if (oldInterviewEmployee!=null){
                     oldInterviewEmployeeName = oldInterviewEmployee.getEmployeeName();
                 }
             }
             if(null!=interview.getInterviewEmployeeId()){
                 HrmEmployee interviewEmployee = employeeService.getById(interview.getInterviewEmployeeId());
                 if (interviewEmployee!=null){
                     interviewEmployeeName = interviewEmployee.getEmployeeName();
                 }
             }
             if(!oldInterviewEmployeeName.equals(interviewEmployeeName)){
                 content.append("，面试官："+"从"+oldInterviewEmployeeName+"更改为"+interviewEmployeeName);
             }

             StringBuilder oldOtherInterviewEmployeeName = new StringBuilder();
             StringBuilder  otherInterviewEmployeeName= new StringBuilder();
             Set<Integer>oldEmployeeIds = TagUtil.toSet(hrmRecruitInterview.getOtherInterviewEmployeeIds());
             Set<Integer> employeeIds = TagUtil.toSet(interview.getOtherInterviewEmployeeIds());
             if(CollectionUtil.isNotEmpty(oldEmployeeIds)){
                 oldEmployeeIds.forEach(employeeId -> {
                     HrmEmployee employee = employeeService.getById(employeeId);
                     //判断员工不存在 || 删除 || 已离职
                     if (employee.getIsDel() == 1 || employee.getEntryStatus() == EmployeeEntryStatus.ALREADY_LEAVE.getValue()) {
                         throw new CrmException(HrmCodeEnum.RESULT_CONFIRM_EMPLOYEE_NO_EXIST, employee.getEmployeeName());
                     }
                     oldOtherInterviewEmployeeName.append(employee.getEmployeeName()).append("、");
                 });
             }
             if(CollectionUtil.isNotEmpty(employeeIds)){
                 employeeIds.forEach(employeeId -> {
                     HrmEmployee employee = employeeService.getById(employeeId);
                     //判断员工不存在 || 删除 || 已离职
                     if (employee.getIsDel() == 1 || employee.getEntryStatus() == EmployeeEntryStatus.ALREADY_LEAVE.getValue()) {
                         throw new CrmException(HrmCodeEnum.RESULT_CONFIRM_EMPLOYEE_NO_EXIST, employee.getEmployeeName());
                     }
                     otherInterviewEmployeeName.append(employee.getEmployeeName()).append("、");
                 });
             }
             if (CollectionUtil.isNotEmpty(employeeIds)&&CollectionUtil.isNotEmpty(oldEmployeeIds)&&!TagUtil.isSetEqual(oldEmployeeIds,employeeIds)) {
                 content.append("，其他面试官：" + "从" + oldOtherInterviewEmployeeName.deleteCharAt(oldOtherInterviewEmployeeName.length()-1).toString() + "更改为" + otherInterviewEmployeeName.deleteCharAt(otherInterviewEmployeeName.length()-1).toString());
             }else if (CollectionUtil.isNotEmpty(employeeIds)&&CollectionUtil.isEmpty(oldEmployeeIds)){
                 content.append("，其他面试官：" + "从" + "无" + "更改为" + otherInterviewEmployeeName.deleteCharAt(otherInterviewEmployeeName.length()-1).toString());
             }else if(CollectionUtil.isEmpty(employeeIds)&&CollectionUtil.isNotEmpty(oldEmployeeIds)){
                 content.append("，其他面试官：" + "从" + oldOtherInterviewEmployeeName.deleteCharAt(oldOtherInterviewEmployeeName.length()-1).toString() + "更改为" + "无");
             }
         }
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(content.toString()), interview.getCandidateId());
        return new Content(candidate.getCandidateName(), content.toString(),BehaviorEnum.UPDATE);
    };
}
