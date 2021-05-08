package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.*;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.log.CrmLeadsLog;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmLeads;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.CrmUploadExcelService;
import com.kakarote.crm.service.ICrmLeadsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 线索表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
@RestController
@RequestMapping("/crmLeads")
@Api(tags = "线索模块接口")
@SysLog(subModel = SubModelType.CRM_LEADS,logClass = CrmLeadsLog.class)
public class CrmLeadsController {

    @Autowired
    private ICrmLeadsService crmLeadsService;

    @Autowired
    private CrmUploadExcelService uploadExcelService;

    @PostMapping("/queryById/{leadsId}")
    @ApiOperation("根据ID查询")
    public Result<CrmModel> queryById(@PathVariable("leadsId") @ApiParam(name = "id", value = "id") Integer leadsId) {
        Integer number = crmLeadsService.lambdaQuery().eq(CrmLeads::getLeadsId,leadsId).count();
        if (number == 0){
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED,"线索");
        }
        CrmModel model = crmLeadsService.queryById(leadsId);
        return R.ok(model);
    }

    @PostMapping("/querySimpleEntity")
    @ApiExplain("查询简单的客户对象")
    public Result<List<SimpleCrmEntity>> querySimpleEntity(@RequestBody List<Integer> ids) {
        List<SimpleCrmEntity> crmEntities = crmLeadsService.querySimpleEntity(ids);
        return R.ok(crmEntities);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据ID删除数据")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmLeadsService.deleteByIds(ids);
        return R.ok();
    }

    @PostMapping("/transfer")
    @ApiOperation("线索转客户功能")
    @SysLogHandler(behavior = BehaviorEnum.TRANSFER)
    public Result transfer(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmLeadsService.transfer(ids);
        return R.ok();
    }

    @PostMapping("/downloadExcel")
    @ApiOperation("下载导入模板")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        crmLeadsService.downloadExcel(response);
    }

    @PostMapping("/allExportExcel")
    @ApiOperation("全部导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "线索导出",detail = "全部导出")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) {
        search.setPageType(0);
        crmLeadsService.exportExcel(response, search);
    }

    @PostMapping("/batchExportExcel")
    @ApiOperation("选中导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "线索导出",detail = "选中导出")
    public void batchExportExcel(@RequestBody @ApiParam(name = "ids", value = "id列表") List<Integer> ids, HttpServletResponse response) {
        CrmSearchBO search = new CrmSearchBO();
        search.setPageType(0);
        search.setLabel(CrmEnum.LEADS.getType());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
        entity.setValues(ids.stream().map(Object::toString).collect(Collectors.toList()));
        search.getSearchList().add(entity);
        search.setPageType(0);
        crmLeadsService.exportExcel(response, search);
    }

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        BasePage<Map<String, Object>> mapBasePage = crmLeadsService.queryPageList(search);
        return R.ok(mapBasePage);
    }

    @PostMapping("/changeOwnerUser")
    @ApiOperation("修改线索负责人")
    @SysLogHandler(behavior = BehaviorEnum.CHANGE_OWNER)
    public Result changeOwnerUser(@RequestBody CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        crmLeadsService.changeOwnerUser(crmChangeOwnerUserBO.getIds(), crmChangeOwnerUserBO.getOwnerUserId());
        return R.ok();
    }

    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List> queryLeadsField(@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)){
            return R.ok(crmLeadsService.queryField(null));
        }
        return R.ok(crmLeadsService.queryFormPositionField(null));
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id,
                                   @RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmLeadsService.queryField(id));
        }
        return R.ok(crmLeadsService.queryFormPositionField(id));
    }

    @PostMapping("/add")
    @ApiOperation("保存数据")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#crmModel.entity[leadsName]",detail = "'新增了线索:' + #crmModel.entity[leadsName]")
    public Result add(@RequestBody CrmModelSaveBO crmModel) {
        crmLeadsService.addOrUpdate(crmModel,false);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改数据")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result update(@RequestBody CrmModelSaveBO crmModel) {
        crmLeadsService.addOrUpdate(crmModel,false);
        return R.ok();
    }

    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmLeadsService.queryFileList(id);
        return R.ok(fileEntities);
    }

    @PostMapping("/num")
    @ApiOperation("详情页数量展示")
    public Result<CrmInfoNumVO> num(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        CrmInfoNumVO num = crmLeadsService.num(id);
        return R.ok(num);
    }

    @PostMapping("/star/{id}")
    @ApiOperation("线索标星")
    public Result star(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        crmLeadsService.star(id);
        return R.ok();
    }

    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> information = crmLeadsService.information(id);
        return R.ok(information);
    }

    @PostMapping("/uploadExcel")
    @ApiOperation("导入线索")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_IMPORT,object = "导入线索",detail = "导入线索")
    public Result<Long> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam("repeatHandling") Integer repeatHandling) {
        UploadExcelBO uploadExcelBO = new UploadExcelBO();
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        uploadExcelBO.setCrmEnum(CrmEnum.LEADS);
        uploadExcelBO.setPoolId(null);
        uploadExcelBO.setRepeatHandling(repeatHandling);
        Long messageId = uploadExcelService.uploadExcel(file, uploadExcelBO);
        return R.ok(messageId);
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmLeadsService.updateInformation(updateInformationBO);
        return R.ok();
    }
}

