package com.kakarote.examine.controller;


import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.examine.entity.ExamineRecordReturnVO;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import com.kakarote.examine.entity.BO.ExamineBO;
import com.kakarote.examine.entity.PO.ExamineRecordLog;
import com.kakarote.examine.entity.VO.ExamineRecordLogVO;
import com.kakarote.examine.entity.VO.ExamineRecordVO;
import com.kakarote.examine.service.IExamineRecordLogService;
import com.kakarote.examine.service.IExamineRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 审核记录表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-19
 */
@RestController
@RequestMapping("/examineRecord")
public class ExamineRecordController {

    @Autowired
    private IExamineRecordService examineRecordService;

    @Autowired
    private IExamineRecordLogService examineRecordLogService;

    @PostMapping("/addExamineRecord")
    @ApiExplain("添加审批记录")
    public Result<ExamineRecordReturnVO> addExamineRecord(@RequestBody ExamineRecordSaveBO examineRecordSaveBO) {
        ExamineRecordReturnVO examineRecordVO = examineRecordService.addExamineRecord(examineRecordSaveBO);
        return Result.ok(examineRecordVO);
    }

    @PostMapping("/auditExamine")
    @ApiOperation("进行审批")
    public Result auditExamine(@RequestBody ExamineBO examineBO) {
        examineRecordService.auditExamine(examineBO);
        return Result.ok();
    }

    @PostMapping("/queryExamineRecordLog")
    @ApiOperation("获取审批历史记录")
    public Result<List<ExamineRecordLogVO>> queryExamineRecord(@RequestParam(value = "ownerUserId",required = false) String ownerUserId, @RequestParam("recordId") Integer recordId) {
        return Result.ok(examineRecordLogService.queryExamineRecordLog(recordId,ownerUserId));
    }


    @PostMapping("/queryExamineRecordInfo")
    @ApiOperation("获取指定的审批历史记录")
    public Result<ExamineRecordReturnVO> queryExamineRecordInfo(@RequestParam("recordId") Integer recordId) {
        return Result.ok(examineRecordService.queryExamineRecordInfo(recordId));
    }


    @PostMapping("/queryExamineLogById")
    @ApiExplain("获取指定审批历史记录")
    public Result<ExamineRecordLog> queryExamineLogById(@RequestParam("examineLogId") Integer examineLogId) {
        return Result.ok(examineRecordLogService.getById(examineLogId));
    }

    @PostMapping("/deleteExamineRecord")
    @ApiExplain("同步删除审批数据")
    public Result<Boolean> deleteExamineRecord(@RequestParam("recordId") Integer recordId) {
        return Result.ok(examineRecordService.deleteExamineRecord(recordId));
    }

    @PostMapping("/updateExamineRecord")
    @ApiExplain("同步修改审批状态")
    public Result<Boolean> updateExamineRecord(@RequestParam("recordId") Integer recordId,@RequestParam("examineStatus") Integer examineStatus) {
        return Result.ok(examineRecordService.updateExamineRecord(recordId,examineStatus));
    }


    @PostMapping("/deleteExamineRecordAndLog")
    @ApiExplain("初始化用删除审批历史数据")
    public Result<Boolean> deleteExamineRecordAndLog(@RequestParam("label") Integer label) {
        return Result.ok(examineRecordService.deleteExamineRecordAndLog(label));
    }

    @PostMapping("/queryExamineRecordList")
    @ApiOperation("获取审批详情")
    public Result<ExamineRecordVO> queryExamineRecordList(@RequestParam("recordId") Integer recordId,@RequestParam(value = "ownerUserId",required = false) Long ownerUserId) {
        return Result.ok(examineRecordService.queryExamineRecordList(recordId,ownerUserId));
    }


}

