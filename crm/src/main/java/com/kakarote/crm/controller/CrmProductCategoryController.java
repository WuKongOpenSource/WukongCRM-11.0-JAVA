package com.kakarote.crm.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.crm.common.log.CrmProductCategoryLog;
import com.kakarote.crm.entity.BO.CrmProductCategoryBO;
import com.kakarote.crm.entity.PO.CrmProductCategory;
import com.kakarote.crm.service.ICrmProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 产品分类表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/crmProductCategory")
@Api(tags = "产品类型模块接口")
@SysLog(logClass = CrmProductCategoryLog.class)
public class CrmProductCategoryController {

    @Autowired
    private ICrmProductCategoryService crmProductCategoryService;

    @PostMapping("/queryList")
    @ApiOperation("查询产品分类列表")
    public Result<List<CrmProductCategoryBO>> queryList(@ApiParam("type") String type) {
        List<CrmProductCategoryBO> list = crmProductCategoryService.queryList(type);
        return R.ok(list);
    }

    @PostMapping("/queryById/{id}")
    @ApiOperation("根据ID查询")
    public Result<CrmProductCategory> queryById(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer productId) {
        CrmProductCategory category = crmProductCategoryService.queryById(productId);
        return R.ok(category);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.SAVE,object = "#productCategory.name",detail = "'添加了产品类别:'+#productCategory.name")
    public Result save(@RequestBody CrmProductCategory productCategory) {
        crmProductCategoryService.saveAndUpdate(productCategory);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("保存")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "#productCategory.name",detail = "'修改了产品类别:'+#productCategory.name")
    public Result update(@RequestBody CrmProductCategory productCategory) {
        crmProductCategoryService.saveAndUpdate(productCategory);
        return R.ok();
    }

    @PostMapping("/deleteById/{id}")
    @ApiOperation("删除")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.DELETE)
    public Result deleteById(@PathVariable("id") Integer id) {
        crmProductCategoryService.deleteById(id);
        return R.ok();
    }
}

