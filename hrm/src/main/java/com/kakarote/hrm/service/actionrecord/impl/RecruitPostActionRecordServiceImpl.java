package com.kakarote.hrm.service.actionrecord.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.hrm.constant.*;
import com.kakarote.hrm.entity.BO.UpdateRecruitPostStatusBO;
import com.kakarote.hrm.entity.PO.HrmRecruitPostType;
import com.kakarote.hrm.service.IHrmRecruitPostTypeService;
import com.kakarote.hrm.service.actionrecord.AbstractHrmActionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: HrmRecruitPostActionRecordService.java
 * @Description: 招聘职位操作记录
 * @author: hmb
 * @date: 2020-04-29 16:14
 */
@Service("recruitPostActionRecordService")
@SysLog(subModel = SubModelType.HRM_RECRUITMENT)
public class RecruitPostActionRecordServiceImpl extends AbstractHrmActionRecordService {

    private static HrmActionTypeEnum actionTypeEnum = HrmActionTypeEnum.RECRUIT_POST;
    private static LabelGroupEnum labelGroupEnum = LabelGroupEnum.RECRUIT_POST;

    @Autowired
    private IHrmRecruitPostTypeService postTypeService;

    /**
     * 属性kv
     */
    private static Dict properties = Dict.create().set("postName", "职位名称").set("deptId", "部门").set("employmentForms", "聘用形式").set("employmentStatus","工作性质").set("city", "工作城市")
            .set("recruitNum", "招聘人数").set("reason", "招聘原因").set("workTime", "工作经验").set("educationRequire", "学历要求").set("minSalary", "开始薪资")
            .set("maxSalary", "结束薪资").set("latestEntryTime", "最迟到岗时间").set("ownerEmployeeId", "负责人").set("description", "职位描述")
            .set("emergencyLevel", "紧急程度").set("postTypeId", "职位类型").set("minAge","最小年龄").set("maxAge","最大年龄");


    /**
     * 新建/删除操作记录
     */
    @SysLogHandler(isReturn = true)
    public Content addOrDeleteRecord(HrmActionBehaviorEnum behaviorEnum, Integer postId,String object){
        String content = behaviorEnum.getName()+"了" + labelGroupEnum.getDesc();
        actionRecordService.saveRecord(actionTypeEnum,behaviorEnum, Collections.singletonList(content),postId);
        if (HrmActionBehaviorEnum.ADD.equals(behaviorEnum)){
            return new Content(object,content, BehaviorEnum.SAVE);
        }else {
            return new Content(object,content,BehaviorEnum.DELETE);
        }
    }

    /**
     * 修改职位状态操作记录
     */
    public void updateStatusRecord(UpdateRecruitPostStatusBO updateRecruitPostStatusBO){
        String content = "";
        if (updateRecruitPostStatusBO.getStatus() == IsEnum.YES.getValue()){
            content += "启用";
        }else {
            content += "停止";
        }
        content += "了职位";
        if(StrUtil.isNotEmpty(updateRecruitPostStatusBO.getReason())){
            content += ",原因:"+updateRecruitPostStatusBO.getReason();
        }
        actionRecordService.saveRecord(actionTypeEnum,HrmActionBehaviorEnum.UPDATE,Collections.singletonList(content),updateRecruitPostStatusBO.getPostId());
    }

    /**
     * 职位实体类修改
     */
    public void entityUpdateRecord(Map<String,Object> oldRecord, Map<String,Object> newRecord, Integer postId) {
        List<String> contentList = entityCommonUpdateRecord(labelGroupEnum,properties, oldRecord, newRecord);
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, contentList, postId);
    }

    @Override
    protected String compare(LabelGroupEnum labelGroupEnum, Dict properties, String newFieldKey, String oldValue, String newValue) {
        String content = super.compare(labelGroupEnum, properties, newFieldKey, oldValue, newValue);
        if ("deptId".equals(newFieldKey)) {
            content = hrmDeptCompare(properties.getStr(newFieldKey),oldValue,newValue);
        } else if ("workTime".equals(newFieldKey)) {
            if (!"空".equals(oldValue)){
                oldValue = RecruitEnum.RecruitPostWorkTime.parseName(Integer.parseInt(oldValue));
            }
            if (!"空".equals(newValue)){
                newValue = RecruitEnum.RecruitPostWorkTime.parseName(Integer.parseInt(newValue));
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("educationRequire".equals(newFieldKey)) {
            if (!"空".equals(oldValue)){
                oldValue = PostEducationEnum.parseName(Integer.parseInt(oldValue));
            }
            if (!"空".equals(newValue)){
                newValue = PostEducationEnum.parseName(Integer.parseInt(newValue));
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("ownerEmployeeId".equals(newFieldKey)) {
            content = employeeCompare(properties.getStr(newFieldKey),oldValue,newValue);
        }else if ("emergencyLevel".equals(newFieldKey)){
            if (!"空".equals(oldValue)){
                oldValue = RecruitEnum.RecruitPostEmergencyLevel.parseName(Integer.parseInt(oldValue));
            }
            if (!"空".equals(newValue)){
                newValue = RecruitEnum.RecruitPostEmergencyLevel.parseName(Integer.parseInt(newValue));
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("employmentForms".equals(newFieldKey)){
            if (!"空".equals(oldValue)){
                oldValue = EmploymentFormsEnum.parseName(Integer.parseInt(oldValue));
            }
            if (!"空".equals(newValue)){
                newValue = EmploymentFormsEnum.parseName(Integer.parseInt(newValue));
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("employmentStatus".equals(newFieldKey)){
            if (!"空".equals(oldValue)){
                oldValue = EmployeeStatusEnum.parseName(Integer.parseInt(oldValue));
            }
            if (!"空".equals(newValue)){
                newValue = EmployeeStatusEnum.parseName(Integer.parseInt(newValue));
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("postTypeId".equals(newFieldKey)){
            HrmRecruitPostType oldPoseType = postTypeService.getById(oldValue);
            HrmRecruitPostType newPoseType = postTypeService.getById(newValue);
            if (oldPoseType != null){
                oldValue = oldPoseType.getName();
            }
            if (!"空".equals(newValue)){
                newValue = newPoseType.getName();
            }
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("minAge".equals(newFieldKey) || "maxAge".equals(newFieldKey)) {
            oldValue = "-1".equals(oldValue)?"不限":oldValue;
            newValue = "-1".equals(newValue)?"不限":newValue;
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }else if ("minSalary".equals(newFieldKey) || "maxSalary".equals(newFieldKey)) {
            oldValue = "-1.00".equals(oldValue)?"面议":oldValue;
            newValue = "-1.00".equals(newValue)?"面议":newValue;
            content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
        }
        return  content;
    }

}
