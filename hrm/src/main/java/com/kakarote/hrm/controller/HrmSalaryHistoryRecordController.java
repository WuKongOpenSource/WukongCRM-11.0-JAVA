package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.entity.BO.QueryHistorySalaryDetailBO;
import com.kakarote.hrm.entity.BO.QueryHistorySalaryListBO;
import com.kakarote.hrm.entity.VO.QueryHistorySalaryDetailVO;
import com.kakarote.hrm.entity.VO.QueryHistorySalaryListVO;
import com.kakarote.hrm.service.IHrmSalaryMonthRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/hrmSalaryHistoryRecord")
@Api(tags = "薪资管理-历史薪资")
public class HrmSalaryHistoryRecordController {

    @Autowired
    private IHrmSalaryMonthRecordService salaryMonthRecordService;

    @PostMapping("/queryHistorySalaryList")
    @ApiOperation("查询历史薪资列表")
    public Result<BasePage<QueryHistorySalaryListVO>> queryHistorySalaryList(@RequestBody QueryHistorySalaryListBO queryHistorySalaryListBO){
        BasePage<QueryHistorySalaryListVO> page = salaryMonthRecordService.queryHistorySalaryList(queryHistorySalaryListBO);
        return Result.ok(page);
    }

    @PostMapping("/queryHistorySalaryDetail")
    @ApiOperation("查询历史薪资详情")
    public Result<QueryHistorySalaryDetailVO> queryHistorySalaryDetail(@RequestBody QueryHistorySalaryDetailBO queryHistorySalaryDetailBO){
        QueryHistorySalaryDetailVO historySalaryDetail = salaryMonthRecordService.queryHistorySalaryDetail(queryHistorySalaryDetailBO);
        return Result.ok(historySalaryDetail);
    }

}

