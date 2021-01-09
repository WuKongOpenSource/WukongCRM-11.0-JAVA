package com.kakarote.crm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.crm.entity.CrmSaveExamineRecordBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmInfo;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.examine.entity.ExamineMessageBO;
import com.kakarote.crm.entity.BO.CrmAuditExamineBO;
import com.kakarote.crm.entity.BO.CrmExamineData;
import com.kakarote.crm.service.ICrmExamineLogService;
import com.kakarote.crm.service.ICrmExamineRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审核记录表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@RestController
@RequestMapping("/crmExamineRecord")
@Api(tags = "审批记录")
public class CrmExamineRecordController {

    @Autowired
    private ICrmExamineRecordService examineRecordService;

    @Autowired
    private ICrmExamineLogService examineLogService;


    @PostMapping("/saveExamineRecord")
    @ApiModelProperty("/保存审批")
    public Result<CrmExamineData> saveExamineRecord(@RequestBody CrmSaveExamineRecordBO examineRecordBO){
        CrmExamineData crmExamineData = examineRecordService.saveExamineRecord(examineRecordBO.getType(), examineRecordBO.getUserId(), examineRecordBO.getOwnerUserId(), examineRecordBO.getRecordId(), examineRecordBO.getStatus());
        return Result.ok(crmExamineData);
    }

    @PostMapping("/queryByRecordId")
    @ApiModelProperty("/查询审批记录")
    public Result<List<JSONObject>> queryByRecordId( @ApiParam("记录ID") @RequestParam("recordId") Integer recordId){
        return Result.ok(examineLogService.queryByRecordId(recordId));
    }

    @PostMapping("/queryExamineRecordList")
    @ApiOperation("查询审批记录列表")
    public Result<JSONObject> queryExamineRecordList(
            @ApiParam("记录ID") @RequestParam("recordId") Integer recordId,
            @ApiParam("负责人ID") @RequestParam("ownerUserId") Long ownerUserId) {
        JSONObject object = examineRecordService.queryExamineRecordList(recordId, ownerUserId);
        return Result.ok(object);
    }

    @PostMapping("/queryExamineLogList")
    @ApiOperation("查询审批流程列表")
    public Result<List<JSONObject>> queryExamineLogList(@ApiParam("负责人ID") @RequestParam(value = "ownerUserId",required = false) String ownerUserId,@ApiParam("记录ID") @RequestParam("recordId") Integer recordId) {
        List<JSONObject> object = examineRecordService.queryExamineLogList(recordId,ownerUserId);
        return Result.ok(object);
    }

    @PostMapping("/auditExamine")
    @ApiOperation("进行审批")
    public Result auditExamine(@RequestBody CrmAuditExamineBO auditExamine) {
        examineRecordService.auditExamine(auditExamine.getRecordId(), auditExamine.getStatus(), auditExamine.getRemarks(), auditExamine.getId(), auditExamine.getNextUserId());
        return Result.ok();
    }


    @PostMapping("/queryConditionData")
    @ApiExplain("获取条件数据")
    public Result<Map<String, Object>> getDataMapForNewExamine(@RequestBody ExamineConditionDataBO examineConditionDataBO) {
        return Result.ok(examineRecordService.getDataMapForNewExamine(examineConditionDataBO));
    }


    @PostMapping("/updateCheckStatusByNewExamine")
    @ApiExplain("修改状态")
    public Result<Boolean> updateCheckStatusByNewExamine(@RequestBody ExamineConditionDataBO examineConditionDataBO){
        return Result.ok(examineRecordService.updateCheckStatusByNewExamine(examineConditionDataBO));
    }

    @PostMapping("/addMessageForNewExamine")
    @ApiExplain("发送消息")
    public Result addMessageForNewExamine(@RequestBody ExamineMessageBO examineMessageBO){
        examineRecordService.addMessageForNewExamine(examineMessageBO);
        return Result.ok();
    }


    @PostMapping("/getCrmSimpleInfo")
    @ApiExplain("发送消息")
    public Result<SimpleCrmInfo> getCrmSimpleInfo(@RequestBody ExamineConditionDataBO examineConditionDataBO){
        return Result.ok(examineRecordService.getCrmSimpleInfo(examineConditionDataBO));
    }
}

