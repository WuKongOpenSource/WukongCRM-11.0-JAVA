package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.SetInterviewResultBO;
import com.kakarote.hrm.entity.BO.SetRecruitInterviewBO;
import com.kakarote.hrm.service.IHrmRecruitInterviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 面试表 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmRecruitInterview")
@Api(tags = "招聘管理-面试管理")
public class HrmRecruitInterviewController {

    @Autowired
    private IHrmRecruitInterviewService recruitInterviewService;

    /**
     * 新建或编辑面试(安排面试)
     */
    @PostMapping("/addInterview")
    @ApiOperation("安排面试")
    public Result addInterview(@RequestBody SetRecruitInterviewBO setRecruitInterviewBO){
        recruitInterviewService.setInterview(setRecruitInterviewBO);
        return Result.ok();
    }


    @PostMapping("/setInterviewResult")
    @ApiOperation("填写面试结果")
    public Result setInterviewResult(@RequestBody SetInterviewResultBO setInterviewResultBO){
        recruitInterviewService.setInterviewResult(setInterviewResultBO);
        return Result.ok();
    }


    @PostMapping("/addBatchInterview")
    @ApiOperation("批量安排面试")
    public Result addBatchInterview(@RequestBody SetRecruitInterviewBO setRecruitInterviewBO){
        recruitInterviewService.addBatchInterview(setRecruitInterviewBO);
        return Result.ok();
    }
}

