package com.kakarote.crm.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.log.CrmInvoiceLog;
import com.kakarote.crm.entity.BO.CrmChangeOwnerUserBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.PO.CrmInvoice;
import com.kakarote.crm.entity.PO.CrmInvoiceInfo;
import com.kakarote.crm.service.ICrmInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  发票
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/crmInvoice")
@Api(tags = "发票模块")
@SysLog(subModel = SubModelType.CRM_INVOICE,logClass = CrmInvoiceLog.class)
public class CrmInvoiceController {

    @Autowired
    private ICrmInvoiceService crmInvoiceService;

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        BasePage<Map<String, Object>> mapBasePage = crmInvoiceService.queryPageList(search);
        return R.ok(mapBasePage);
    }

    @PostMapping("/save")
    @ApiOperation("添加")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#crmInvoice.invoiceApplyNumber",detail = "'新建了发票:'+#crmInvoice.invoiceApplyNumber")
    public Result saveInvoice(@RequestBody CrmInvoice crmInvoice) {
        crmInvoiceService.saveInvoice(crmInvoice,crmInvoice.getCheckUserId());
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInvoice(@RequestBody CrmInvoice crmInvoice) {
        crmInvoiceService.updateInvoice(crmInvoice,crmInvoice.getCheckUserId());
        return R.ok();
    }
    @PostMapping("/queryById/{invoiceId}")
    @ApiOperation("查询详情")
    public Result<CrmInvoice> queryById(@PathVariable("invoiceId") Integer invoiceId) {
        return R.ok(crmInvoiceService.queryById(invoiceId));
    }

    @PostMapping("/updateInvoiceStatus")
    @ApiOperation("修改开票状态")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInvoiceStatus(@RequestBody CrmInvoice crmInvoice) {
        crmInvoiceService.updateInvoiceStatus(crmInvoice);
        return R.ok();
    }

    @PostMapping("/resetInvoiceStatus")
    @ApiOperation("重置开票状态")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result resetInvoiceStatus(@RequestBody CrmInvoice crmInvoice) {
        crmInvoiceService.updateInvoiceStatus(crmInvoice);
        return R.ok();
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("删除发票数据")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmInvoiceService.deleteByIds(ids);
        return R.ok();
    }


    @PostMapping("/changeOwnerUser")
    @ApiOperation("变更负责人")
    @SysLogHandler(behavior = BehaviorEnum.CHANGE_OWNER)
    public Result changeOwnerUser(@RequestBody CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        crmInvoiceService.changeOwnerUser(crmChangeOwnerUserBO.getIds(), crmChangeOwnerUserBO.getOwnerUserId());
        return R.ok();
    }

    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmInvoiceService.queryFileList(id);
        return R.ok(fileEntities);
    }

    @PostMapping("/saveInvoiceInfo")
    @ApiOperation("新建发票信息")
    public Result saveInvoiceInfo(@RequestBody CrmInvoiceInfo crmInvoiceInfo) {
        crmInvoiceService.saveInvoiceInfo(crmInvoiceInfo);
        return R.ok();
    }

    @PostMapping("/updateInvoiceInfo")
    @ApiOperation("编辑发票信息")
    public Result updateInvoiceInfo(@RequestBody CrmInvoiceInfo crmInvoiceInfo) {
        crmInvoiceService.updateInvoiceInfo(crmInvoiceInfo);
        return R.ok();
    }

    @PostMapping("/deleteInvoiceInfo")
    @ApiOperation("删除发票信息")
    public Result deleteInvoiceInfo(@RequestParam("infoId") Integer infoId) {
        crmInvoiceService.deleteInvoiceInfo(infoId);
        return R.ok();
    }

}

