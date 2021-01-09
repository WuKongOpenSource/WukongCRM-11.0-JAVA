package com.kakarote.examine.controller;

import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.oa.entity.ExamineVO;
import com.kakarote.examine.entity.BO.ExaminePageBO;
import com.kakarote.examine.entity.VO.ExamineRecordInfoVO;
import com.kakarote.examine.service.IExamineService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/24
 */
@RestController
@RequestMapping("/examineWaiting")
public class MyExamineController {
    @Autowired
    private IExamineService examineService;

    @PostMapping("/queryOaExamineList")
    @ApiOperation("查询OA审批流列表")
    public Result<BasePage<ExamineVO>> queryOaExamineList(@RequestBody ExaminePageBO examinePageBo) {
        BasePage<ExamineVO> voBasePage = examineService.queryOaExamineList(examinePageBo);
        return Result.ok(voBasePage);
    }


    @PostMapping("/queryCrmExamineList")
    @ApiOperation("查询CRM审批流列表")
    public Result<BasePage<ExamineRecordInfoVO>> queryCrmExamineList(@RequestBody ExaminePageBO examinePageBo) {
        BasePage<ExamineRecordInfoVO> voBasePage = examineService.queryCrmExamineList(examinePageBo);
        return Result.ok(voBasePage);
    }


    @PostMapping("/queryOaExamineIdList")
    @ApiExplain("查询OA审批流关联业务主键列表")
    public Result<List<Integer>> queryOaExamineIdList(@RequestParam("status")Integer status, @RequestParam("categoryId")Integer categoryId) {
        return Result.ok(examineService.queryOaExamineIdList(status,categoryId));
    }

    @PostMapping("/queryCrmExamineIdList")
    @ApiExplain("查询CRM审批流联业务主键列表")
    public Result<List<Integer>> queryCrmExamineIdList(@RequestParam("label")Integer label,@RequestParam("status")Integer status) {
        return Result.ok(examineService.queryCrmExamineIdList(label,status));
    }

}
