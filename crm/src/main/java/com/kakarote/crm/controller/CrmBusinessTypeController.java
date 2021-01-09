package com.kakarote.crm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.crm.common.log.CrmBusinessTypeLog;
import com.kakarote.crm.entity.PO.CrmBusinessType;
import com.kakarote.crm.service.ICrmBusinessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商机状态组类别 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/crmBusinessType")
@Api(tags = "商机状态组控制器")
@SysLog(logClass = CrmBusinessTypeLog.class)
public class CrmBusinessTypeController {

    @Autowired
    private ICrmBusinessTypeService crmBusinessTypeService;

    @PostMapping("/queryBusinessTypeList")
    @ApiOperation("查询列表")
    public Result<BasePage<CrmBusinessType>> queryBusinessTypeList(@RequestBody PageEntity pageEntity) {
        BasePage<CrmBusinessType> crmBusinessTypeBasePage = crmBusinessTypeService.queryBusinessTypeList(pageEntity);
        return Result.ok(crmBusinessTypeBasePage);
    }

    @PostMapping("/getBusinessType")
    @ApiOperation("根据ID查询")
    public Result<CrmBusinessType> getBusinessType(@RequestParam("id") Integer id) {
        CrmBusinessType businessType = crmBusinessTypeService.getBusinessType(id);
        return Result.ok(businessType);
    }

    @PostMapping("/setBusinessType")
    @ApiOperation("设置商机组")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "#jsonObject[crmBusinessType].name",detail = "'设置商机组:'+#jsonObject[crmBusinessType].name")
    public Result setBusinessType(@RequestBody JSONObject jsonObject) {
        crmBusinessTypeService.addBusinessType(jsonObject);
        return Result.ok();
    }


    @PostMapping("/deleteById")
    @ApiOperation("删除商机状态组")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.DELETE)
    public Result deleteById(@RequestParam("id") Integer typeId) {
        crmBusinessTypeService.deleteById(typeId);
        return Result.ok();
    }

    @PostMapping("/updateStatus")
    @ApiOperation("商机组启停用")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE)
    public Result updateStatus(@RequestParam("typeId") Integer typeId, @RequestParam("status") Integer status) {
        crmBusinessTypeService.updateStatus(typeId, status);
        return Result.ok();
    }
}

