package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.log.CrmInvoiceLog;
import com.kakarote.crm.entity.BO.CrmChangeOwnerUserBO;
import com.kakarote.crm.entity.BO.CrmContractSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmInvoice;
import com.kakarote.crm.entity.PO.CrmInvoiceInfo;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.ICrmInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List> queryInvoiceField(@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)){
            return R.ok(crmInvoiceService.queryField(null));
        }
        return R.ok(crmInvoiceService.queryFormPositionField(null));
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id,
                                   @RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmInvoiceService.queryField(id));
        }
        return R.ok(crmInvoiceService.queryFormPositionField(id));
    }

    @PostMapping("/add")
    @ApiOperation("新增发票保存数据")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#crmModel.entity[invoiceName]",detail = "'新增了发票:' + #crmModel.entity[invoiceName]")
    public Result add(@RequestBody CrmContractSaveBO crmModel) {
        crmInvoiceService.addOrUpdate(crmModel,false);
        return R.ok();
    }

    @PostMapping("/querySimpleEntity")
    @ApiExplain("查询简单的客户对象")
    public Result<List<SimpleCrmEntity>> querySimpleEntity(@RequestBody List<Integer> ids) {
        List<SimpleCrmEntity> crmEntities = crmInvoiceService.querySimpleEntity(ids);
        return R.ok(crmEntities);
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInvoice(@RequestBody CrmContractSaveBO  crmModel) {
        crmInvoiceService.addOrUpdate(crmModel,false);
        return R.ok();
    }
    @PostMapping("/queryById/{invoiceId}")
    @ApiOperation("查询详情")
    public Result<CrmInvoice> queryById(@PathVariable("invoiceId") Integer invoiceId) {
        return R.ok(crmInvoiceService.queryById(invoiceId));
    }

    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> information = crmInvoiceService.information(id);
        return R.ok(information);
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

    @PostMapping("/allExportExcel")
    @ApiOperation("全部导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT, object = "发票导出", detail = "全部导出")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) {
        search.setPageType(0);
        crmInvoiceService.exportExcel(response, search);
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmInvoiceService.updateInformation(updateInformationBO);
        return R.ok();
    }

}

