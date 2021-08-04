package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.HrmSalarySlipOption;
import com.kakarote.hrm.entity.VO.QuerySendDetailListVO;
import com.kakarote.hrm.entity.VO.QuerySendRecordListVO;
import com.kakarote.hrm.entity.VO.SlipEmployeeVO;
import com.kakarote.hrm.service.IHrmSalarySlipRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 发工资条记录 前端控制器
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@RestController
@RequestMapping("/hrmSalarySlipRecord")
@Api(tags = "工资条-工资条记录接口")
@SysLog(subModel = SubModelType.HRM_SALARY_SLIP)
public class HrmSalarySlipRecordController {


    @Autowired
    private IHrmSalarySlipRecordService slipRecordService;

    @PostMapping("/querySlipEmployeePageList")
    @ApiOperation("查询工资条选择发送员工列表")
    public Result<BasePage<SlipEmployeeVO>> querySlipEmployeePageList(@RequestBody QuerySlipEmployeePageListBO slipEmployeePageListBO){
        BasePage<SlipEmployeeVO> page = slipRecordService.querySlipEmployeePageList(slipEmployeePageListBO);
        return Result.ok(page);
    }

    @PostMapping("/sendSalarySlip")
    @ApiOperation("发工资条")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "发送工资条",detail = "发送工资条")
    public Result sendSalarySlip(@RequestBody SendSalarySlipBO sendSalarySlipBO){
        slipRecordService.sendSalarySlip(sendSalarySlipBO);
        return Result.ok();
    }

    @PostMapping("/querySendRecordList")
    @ApiOperation("查询发放工资条记录列表")
    public Result<BasePage<QuerySendRecordListVO>> querySendRecordList(@RequestBody QuerySendRecordListBO querySendRecordListBO){
        BasePage<QuerySendRecordListVO> page = slipRecordService.querySendRecordList(querySendRecordListBO);
        return Result.ok(page);
    }

    @PostMapping("/deleteSendRecord/{id}")
    @ApiOperation("删除发放记录")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "删除发放记录",detail = "删除发放记录")
    public Result deleteSendRecord(@PathVariable String id){
        slipRecordService.deleteSendRecord(id);
        return Result.ok();
    }

    @PostMapping("/querySendDetailList")
    @ApiOperation("查询发放工资条记录详情列表")
    public Result<BasePage<QuerySendDetailListVO>> querySendDetailList(@RequestBody QuerySendDetailListBO querySendRecordListBO){
        BasePage<QuerySendDetailListVO> page = slipRecordService.querySendDetailList(querySendRecordListBO);
        return Result.ok(page);
    }

    @PostMapping("/querySlipDetail/{id}")
    @ApiOperation("查询工资条明细")
    public Result<List<HrmSalarySlipOption>> querySlipDetail(@PathVariable Integer id){
        List<HrmSalarySlipOption> list = slipRecordService.querySlipDetail(id);
        return Result.ok(list);
    }

    @PostMapping("/setSlipRemarks")
    @ApiOperation("添加修改工资条备注")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "工资条备注",detail = "'添加修改工资条备注:'+#setSlipRemarksBO.remarks")
    public Result setSlipRemarks(@RequestBody SetSlipRemarksBO setSlipRemarksBO){
        slipRecordService.setSlipRemarks(setSlipRemarksBO);
        return Result.ok();
    }

}

