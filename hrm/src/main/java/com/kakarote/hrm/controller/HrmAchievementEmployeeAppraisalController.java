package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.constant.achievement.EmployeeAppraisalStatus;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.VO.*;
import com.kakarote.hrm.service.IHrmAchievementEmployeeAppraisalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工绩效考核 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmAchievementEmployeeAppraisal")
@Api(tags = "绩效考核-员工端")
public class HrmAchievementEmployeeAppraisalController {

    @Autowired
    private IHrmAchievementEmployeeAppraisalService employeeAppraisalService;

    @PostMapping("/queryAppraisalNum")
    @ApiOperation("查询员工绩效数量")
    public Result<Map<Integer,Integer>> queryAppraisalNum(){
        Map<Integer,Integer> map = employeeAppraisalService.queryAppraisalNum();
        return Result.ok(map);
    }

    @PostMapping("/queryMyAppraisal")
    @ApiOperation("查询我的绩效")
    public Result<BasePage<QueryMyAppraisalVO>> queryMyAppraisal(@RequestBody BasePageBO basePageBO) {
        BasePage<QueryMyAppraisalVO> list = employeeAppraisalService.queryMyAppraisal(basePageBO);
        return Result.ok(list);
    }

    @PostMapping("/queryTargetConfirmList")
    @ApiOperation("查询目标确认列表")
    public Result<BasePage<TargetConfirmListVO>> queryTargetConfirmList(@RequestBody BasePageBO basePageBO){
       BasePage<TargetConfirmListVO> page =  employeeAppraisalService.queryTargetConfirmList(basePageBO);
       return Result.ok(page);
    }


    @PostMapping("/queryEvaluatoList")
    @ApiOperation("查询结果评定列表")
    public Result<BasePage<EvaluatoListVO>> queryEvaluatoList(@RequestBody EvaluatoListBO evaluatoListBO){
        BasePage<EvaluatoListVO> page = employeeAppraisalService.queryEvaluatoList(evaluatoListBO);
        return Result.ok(page);
    }

    @PostMapping("/queryResultConfirmList")
    @ApiOperation("查询结果确认列表")
    public Result<BasePage<ResultConfirmListVO>> queryResultConfirmList(@RequestBody BasePageBO basePageBO){
        BasePage<ResultConfirmListVO> page = employeeAppraisalService.queryResultConfirmList(basePageBO);
        return Result.ok(page);
    }


    @PostMapping("/queryEmployeeAppraisalDetail/{employeeAppraisalId}")
    @ApiOperation("查询考核详情")
    public Result<EmployeeAppraisalDetailVO> queryEmployeeAppraisalDetail(@PathVariable Integer employeeAppraisalId){
        EmployeeAppraisalDetailVO employeeAppraisalDetailVO = employeeAppraisalService.queryEmployeeAppraisalDetail(employeeAppraisalId);
        return Result.ok(employeeAppraisalDetailVO);
    }

    @PostMapping("/writeAppraisal")
    @ApiOperation("填写绩效")
    public Result writeAppraisal(@RequestBody WriteAppraisalBO writeAppraisalBO){
        employeeAppraisalService.writeAppraisal(writeAppraisalBO);
        return Result.ok();
    }


    @PostMapping("/targetConfirm")
    @ApiOperation("目标确认")
    public Result targetConfirm(@RequestBody TargetConfirmBO targetConfirmBO){
        employeeAppraisalService.targetConfirm(targetConfirmBO);
        return Result.ok();
    }


    @PostMapping("/resultEvaluato")
    @ApiOperation("结果评定")
    public Result resultEvaluato(@RequestBody ResultEvaluatoBO resultEvaluatoBO){
        employeeAppraisalService.resultEvaluato(resultEvaluatoBO);
        return Result.ok();
    }


    @PostMapping("/queryResultConfirmByAppraisalId/{appraisalId}")
    @ApiOperation("绩效结果确认")
    public Result<ResultConfirmByAppraisalIdVO> queryResultConfirmByAppraisalId(@PathVariable String appraisalId){
        ResultConfirmByAppraisalIdVO resultConfirmByAppraisalIdVO = employeeAppraisalService.queryResultConfirmByAppraisalId(appraisalId);
        return Result.ok(resultConfirmByAppraisalIdVO);
    }


    @PostMapping("/updateScoreLevel")
    @ApiOperation("修改考评分数")
    public Result updateScoreLevel(@RequestBody UpdateScoreLevelBO updateScoreLevelBO){
        employeeAppraisalService.updateScoreLevel(updateScoreLevelBO);
        return Result.ok();
    }

    @PostMapping("/resultConfirm/{appraisalId}")
    @ApiOperation("结果确认")
    public Result resultConfirm(@PathVariable String appraisalId){
        employeeAppraisalService.resultConfirm(appraisalId);
        return Result.ok();
    }

    @PostMapping("/updateSchedule")
    @ApiOperation("修改目标进度")
    public Result updateSchedule(@RequestBody UpdateScheduleBO updateScheduleBO){
        employeeAppraisalService.updateSchedule(updateScheduleBO);
        return Result.ok();
    }
    @PostMapping("/queryTargetConfirmScreen")
    @ApiOperation("查询目标确认列表的绩效筛选条件")
    public Result<List<AchievementAppraisalVO>> queryTargetConfirmScreen(){
        List<AchievementAppraisalVO> list = employeeAppraisalService.queryTargetConfirmScreen(EmployeeHolder.getEmployeeId(), EmployeeAppraisalStatus.PENDING_CONFIRMATION.getValue()) ;
        return Result.ok(list);
    }
    @PostMapping("/queryEvaluatoScreen")
    @ApiOperation("查询结果评定列表的绩效筛选条件")
    public Result<List<AchievementAppraisalVO>> queryEvaluatoScreen(Integer status){
        List<AchievementAppraisalVO> list = employeeAppraisalService.queryEvaluatoScreen(EmployeeHolder.getEmployeeId(),status) ;
        return Result.ok(list);
    }
    @PostMapping("/queryLevelIdByScore")
    @ApiOperation("查询等级id通过评分")
    public Result queryLevelIdByScore(@RequestBody QueryLevelIdByScoreBO queryLevelIdByScoreBO){
        Integer levelId   =  employeeAppraisalService.queryLevelIdByScore(queryLevelIdByScoreBO);
        return Result.ok(levelId );
    }
}

