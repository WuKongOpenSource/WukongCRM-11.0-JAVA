package com.kakarote.crm.controller;


import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.CrmUploadExcelService;
import com.kakarote.crm.service.ICrmProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/crmProduct")
@Api(tags = "产品模块接口")
public class CrmProductController {

    @Autowired
    private ICrmProductService crmProductService;

    @PostMapping("/queryById/{productId}")
    @ApiOperation("根据ID查询")
    public Result<CrmModel> queryById(@PathVariable("productId") @ApiParam(name = "id", value = "id") Integer productId) {
        CrmModel model = crmProductService.queryById(productId);
        return R.ok(model);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据ID删除数据")
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmProductService.deleteByIds(ids);
        return R.ok();
    }

    @PostMapping("/add")
    @ApiOperation("保存数据")
    public Result add(@RequestBody CrmModelSaveBO crmModel) {
        crmProductService.addOrUpdate(crmModel,false);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改数据")
    public Result update(@RequestBody CrmModelSaveBO crmModel) {
        crmProductService.addOrUpdate(crmModel,false);
        return R.ok();
    }

    @PostMapping("/changeOwnerUser")
    @ApiOperation("修改产品负责人")
    public Result changeOwnerUser(@RequestBody CrmChangeOwnerUserBO crmChangeOwnerUserBO){
        crmProductService.changeOwnerUser(crmChangeOwnerUserBO.getIds(),crmChangeOwnerUserBO.getOwnerUserId());
        return R.ok();
    }

    @PostMapping("/downloadExcel")
    @ApiOperation("下载导入模板")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        crmProductService.downloadExcel(response);
    }

    @PostMapping("/allExportExcel")
    @ApiOperation("全部导出")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) {
        search.setPageType(0);
        crmProductService.exportExcel(response, search);
    }

    @PostMapping("/batchExportExcel")
    @ApiOperation("选中导出")
    public void batchExportExcel(@RequestBody @ApiParam(name = "ids", value = "id列表") List<Integer> ids, HttpServletResponse response) {
        CrmSearchBO search = new CrmSearchBO();
        search.setPageType(0);
        search.setLabel(CrmEnum.PRODUCT.getType());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
        entity.setValues(ids.stream().map(Object::toString).collect(Collectors.toList()));
        search.getSearchList().add(entity);
        search.setPageType(0);
        crmProductService.exportExcel(response, search);
    }

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        BasePage<Map<String, Object>> mapBasePage = crmProductService.queryPageList(search);
        return R.ok(mapBasePage);
    }

    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List<CrmModelFiledVO>> queryField() {
        List<CrmModelFiledVO> crmModelFiledList = crmProductService.queryField(null);
        return R.ok(crmModelFiledList);
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List<CrmModelFiledVO>> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> crmModelFiledList = crmProductService.queryField(id);
        return R.ok(crmModelFiledList);
    }

    @PostMapping("/updateStatus")
    @ApiOperation("查询修改数据所需信息")
    public Result updateStatus(@RequestBody CrmProductStatusBO productStatusBO) {
        crmProductService.updateStatus(productStatusBO);
        return R.ok();
    }

    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> information = crmProductService.information(id);
        return R.ok(information);
    }


    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmProductService.queryFileList(id);
        return R.ok(fileEntities);
    }

    @PostMapping("/num")
    @ApiOperation("详情页数量展示")
    public Result<CrmInfoNumVO> num(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        CrmInfoNumVO infoNumVO = crmProductService.num(id);
        return R.ok(infoNumVO);
    }

    @PostMapping("/querySimpleEntity")
    @ApiExplain("查询简单的产品对象")
    public Result<List<SimpleCrmEntity>> querySimpleEntity() {
        List<SimpleCrmEntity> crmEntities = crmProductService.querySimpleEntity();
        return R.ok(crmEntities);
    }

    @PostMapping("/uploadExcel")
    @ApiOperation("导入产品")
    public Result<Long> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam("ownerUserId") Long ownerUserId, @RequestParam("repeatHandling") Integer repeatHandling) {
        UploadExcelBO uploadExcelBO = new UploadExcelBO();
        uploadExcelBO.setOwnerUserId(ownerUserId);
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        uploadExcelBO.setCrmEnum(CrmEnum.PRODUCT);
        uploadExcelBO.setPoolId(null);
        uploadExcelBO.setRepeatHandling(repeatHandling);
        Long messageId = ApplicationContextHolder.getBean(CrmUploadExcelService.class).uploadExcel(file, uploadExcelBO);
        return R.ok(messageId);
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmProductService.updateInformation(updateInformationBO);
        return R.ok();
    }

    @PostMapping("/querySaleProductPageList")
    @ApiOperation("获取上架产品列表页")
    public Result<BasePage<Map<String, Object>>> querySaleProductPageList(@RequestBody CrmQuerySaleProductPageBO querySaleProductPageBO){
        CrmSearchBO search = new CrmSearchBO();
        search.setPageType(0);
        search.setLabel(CrmEnum.PRODUCT.getType());
        search.setSearch(querySaleProductPageBO.getSearch());
        search.setPage(querySaleProductPageBO.getPage());
        search.setLimit(querySaleProductPageBO.getLimit());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setName("status");
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.IS);
        entity.setValues(Collections.singletonList("1"));
        search.getSearchList().add(entity);
        return queryPageList(search);
    }
}

