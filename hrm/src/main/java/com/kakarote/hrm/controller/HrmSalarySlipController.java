package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.entity.BO.QuerySalarySlipListBO;
import com.kakarote.hrm.entity.VO.QuerySalarySlipListVO;
import com.kakarote.hrm.service.IHrmSalarySlipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 工资条 前端控制器
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@RestController
@RequestMapping("/hrmSalarySlip")
@Api(tags = "工资条-工资条接口")
public class HrmSalarySlipController {

    @Autowired
    private IHrmSalarySlipService salarySlipService;

    @PostMapping("/querySalarySlipList")
    @ApiOperation("查询工资条列表")
    public Result<BasePage<QuerySalarySlipListVO>> querySalarySlipList(@RequestBody QuerySalarySlipListBO querySalarySlipListBO){
        BasePage<QuerySalarySlipListVO> page = salarySlipService.querySalarySlipList(querySalarySlipListBO);
        return Result.ok(page);
    }

}

