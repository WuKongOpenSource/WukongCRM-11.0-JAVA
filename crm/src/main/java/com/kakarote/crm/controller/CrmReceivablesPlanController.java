package com.kakarote.crm.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmReceivablesPlanBO;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.ICrmReceivablesPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 回款计划表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@RestController
@RequestMapping("/crmReceivablesPlan")
@Api(tags = "回款计划模块接口")
public class CrmReceivablesPlanController {

    @Autowired
    private ICrmReceivablesPlanService crmReceivablesPlanService;

    @PostMapping("/add")
    @ApiOperation("保存数据")
    public Result add(@RequestBody CrmBusinessSaveBO crmModel) {
        crmReceivablesPlanService.saveAndUpdate(crmModel);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改数据")
    public Result update(@RequestBody CrmBusinessSaveBO crmModel) {
        crmReceivablesPlanService.saveAndUpdate(crmModel);
        return R.ok();
    }

    @PostMapping("/queryByContractAndCustomer")
    @ApiOperation("查询未被使用的回款计划")
    public Result<List<CrmReceivablesPlan>> queryByContractAndCustomer(@RequestBody CrmReceivablesPlanBO crmReceivablesPlanBO){
        List<CrmReceivablesPlan> crmReceivablesPlans = crmReceivablesPlanService.queryByContractAndCustomer(crmReceivablesPlanBO);
        return R.ok(crmReceivablesPlans);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据ID删除数据")
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmReceivablesPlanService.deleteByIds(ids);
        return R.ok();
    }

    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List<CrmModelFiledVO>> queryField() {
        List<CrmModelFiledVO> crmModelFiledList = crmReceivablesPlanService.queryField(null);
        return R.ok(crmModelFiledList);
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List<CrmModelFiledVO>> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> crmModelFiledList = crmReceivablesPlanService.queryField(id);
        return R.ok(crmModelFiledList);
    }
}

