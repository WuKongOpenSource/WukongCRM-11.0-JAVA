package com.kakarote.crm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.crm.entity.PO.CrmMarketingForm;
import com.kakarote.crm.service.ICrmMarketingFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/2
 */
@RestController
@RequestMapping("/crmMarketingForm")
@Api(tags = "活动表单接口")
public class CrmMarketingFormController {

    @Autowired
    private ICrmMarketingFormService crmMarketingFormService;


    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "添加或修改表单")
    public Result<CrmMarketingForm> saveOrUpdateCrmMarketingForm(@Validated @RequestBody CrmMarketingForm crmMarketingForm) {
        return Result.ok(crmMarketingFormService.saveOrUpdateCrmMarketingForm(crmMarketingForm));
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询表单列表")
    public Result<BasePage<CrmMarketingForm>> queryCrmMarketingFormList(@Validated @RequestBody PageEntity pageEntity) {
        return Result.ok(crmMarketingFormService.queryCrmMarketingFormList(pageEntity));
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询表单列表")
    public Result<List<CrmMarketingForm>> queryAllCrmMarketingFormList() {
        return Result.ok(crmMarketingFormService.queryAllCrmMarketingFormList());
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除表单")
    public Result deleteCrmMarketingForm(@RequestParam("id") Integer id) {
        crmMarketingFormService.deleteCrmMarketingForm(id);
        return Result.ok();
    }

    @PostMapping("/updateStatus")
    @ApiOperation(value = "修改状态")
    public Result deleteCrmMarketingForm(@Validated @RequestBody CrmMarketingForm crmMarketingForm) {
        crmMarketingFormService.updateStatus(crmMarketingForm);
        return Result.ok();
    }

}
