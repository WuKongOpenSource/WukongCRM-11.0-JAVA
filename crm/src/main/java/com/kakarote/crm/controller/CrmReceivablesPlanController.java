package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmReceivablesPlanBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.ICrmReceivablesPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        crmReceivablesPlanService.addOrUpdate(crmModel);
        return R.ok();
    }

    @PostMapping("/batchSave")
    @ApiOperation("批量保存数据")
    public Result batchSave(@RequestBody List<CrmReceivablesPlanBO> receivablesPlans) {
        crmReceivablesPlanService.batchSave(receivablesPlans);
        return R.ok();
    }

    @PostMapping("/queryById/{receivablesPlanId}")
    @ApiOperation("根据ID查询")
    public Result<CrmModel> queryById(@PathVariable("receivablesPlanId") @ApiParam(name = "id", value = "id") Integer receivablesPlanId) {
        CrmModel model = crmReceivablesPlanService.queryById(receivablesPlanId);
        return R.ok(model);
    }

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        BasePage<Map<String, Object>> mapBasePage = crmReceivablesPlanService.queryPageList(search);
        return R.ok(mapBasePage);
    }

    @PostMapping("/update")
    @ApiOperation("修改数据")
    public Result update(@RequestBody CrmBusinessSaveBO crmModel) {
        crmReceivablesPlanService.addOrUpdate(crmModel);
        return R.ok();
    }

    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> information = crmReceivablesPlanService.information(id);
        return R.ok(information);
    }

    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmReceivablesPlanService.queryFileList(id);
        return R.ok(fileEntities);
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
    public Result<List> queryField(@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)){
            return R.ok(crmReceivablesPlanService.queryField(null));
        }
        return R.ok(crmReceivablesPlanService.queryFormPositionField(null));
    }

    @PostMapping("/batchExportExcel")
    @ApiOperation("选中导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "回款计划导出",detail = "选中导出")
    public void batchExportExcel(@RequestBody @ApiParam(name = "ids", value = "id列表") List<Integer> ids, HttpServletResponse response) {
        CrmSearchBO search = new CrmSearchBO();
        search.setPageType(0);
        search.setLabel(CrmEnum.RECEIVABLES.getType());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
        entity.setValues(ids.stream().map(Object::toString).collect(Collectors.toList()));
        search.getSearchList().add(entity);
        search.setPageType(0);
        crmReceivablesPlanService.exportExcel(response, search);
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmReceivablesPlanService.updateInformation(updateInformationBO);
        return R.ok();
    }

    @PostMapping("/allExportExcel")
    @ApiOperation("全部导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "回款导出",detail = "全部导出")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) {
        search.setPageType(0);
        crmReceivablesPlanService.exportExcel(response, search);
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id,@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)){
            return R.ok(crmReceivablesPlanService.queryField(id));
        }
        return R.ok(crmReceivablesPlanService.queryFormPositionField(id));
    }
}

