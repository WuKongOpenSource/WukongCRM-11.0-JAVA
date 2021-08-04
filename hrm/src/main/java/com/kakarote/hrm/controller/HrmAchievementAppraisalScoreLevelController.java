package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.PO.HrmAchievementAppraisalScoreLevel;
import com.kakarote.hrm.service.IHrmAchievementAppraisalScoreLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 考评规则等级 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmAchievementAppraisalScoreLevel")
@Api(tags = "绩效考核-考评规则等级")
public class HrmAchievementAppraisalScoreLevelController {

    @Autowired
    private IHrmAchievementAppraisalScoreLevelService scoreLevelService;

    @PostMapping("/queryScoreLevelList/{employeeAppraisalId}")
    @ApiOperation("查询分数配置列表")
    public Result<List<HrmAchievementAppraisalScoreLevel>> queryScoreLevelList(@PathVariable Integer employeeAppraisalId){
        List<HrmAchievementAppraisalScoreLevel> levelList = scoreLevelService.queryScoreLevelList(employeeAppraisalId);
        return Result.ok(levelList);
    }

    @PostMapping("/queryScoreLevelListByAppraisalId/{appraisalId}")
    @ApiOperation("查询分数配置列表")
    public Result<List<HrmAchievementAppraisalScoreLevel>> queryScoreLevelListByAppraisalId(@PathVariable Integer appraisalId){
        List<HrmAchievementAppraisalScoreLevel> levelList = scoreLevelService.queryScoreLevelListByAppraisalId(appraisalId);
        return Result.ok(levelList);
    }



}

