package com.kakarote.crm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.crm.entity.BO.MarketingFieldBO;
import com.kakarote.crm.entity.PO.CrmMarketingField;
import com.kakarote.crm.service.ICrmMarketingFieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/2
 */
@RestController
@RequestMapping("/crmMarketingField")
@Api(tags = "活动表单字段接口")
public class CrmMarketingFieldController {

    @Autowired
    private ICrmMarketingFieldService crmMarketingFieldService;

    /**
     * @author wyq
     * 查询新增或编辑字段
     */
    @ApiOperation("查询新增或编辑字段")
    @PostMapping("/queryField/{formId}")
    public Result<List<CrmMarketingField>> queryField(@PathVariable Integer formId){
        List<CrmMarketingField> list = crmMarketingFieldService.queryField(formId);
        return Result.ok(list);
    }

    @ApiOperation("保存自定义字段")
    @PostMapping("/saveField")
    public Result saveField(@RequestBody MarketingFieldBO marketingFieldBO){
        crmMarketingFieldService.saveField(marketingFieldBO);
        return Result.ok();
    }
}
