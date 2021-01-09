package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.entity.BO.AdminLanguagePackBO;
import com.kakarote.admin.entity.VO.AdminLanguagePackVO;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.service.IAdminLanguagePackService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 语言包表 前端控制器
 * </p>
 *
 * @author zmj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/adminLanguagePack")
@Api(tags = "语言包相关接口")
public class AdminLanguagePackController {

    @Autowired
    private IAdminLanguagePackService iAdminLanguagePackService;

    @ApiOperation("分页查询语言包列表")
    @PostMapping("/queryLanguagePackList")
    public Result<BasePage<AdminLanguagePackVO>> queryLanguagePackList(@RequestBody AdminLanguagePackBO adminLanguagePackBO) {
        return R.ok(iAdminLanguagePackService.queryLanguagePackList(adminLanguagePackBO, 0));
    }

    @ApiOperation("新增或编辑语言包")
    @PostMapping("/addOrUpdateLanguagePack")
    public Result<BasePage<AdminUserVO>> addOrUpdateLanguagePack(@RequestParam("file") MultipartFile file, @RequestParam("languagePackName") String languagePackName, @RequestParam(name="languagePackId", required=false) Integer languagePackId) {
        AdminLanguagePackBO adminLanguagePackBO = new AdminLanguagePackBO();
        adminLanguagePackBO.setLanguagePackName(languagePackName);
        adminLanguagePackBO.setLanguagePackId(languagePackId);
        return iAdminLanguagePackService.addOrUpdateLanguagePack(file, adminLanguagePackBO);
    }

    @PostMapping("/deleteLanguagePackById")
    @ApiOperation("删除语言包")
    public Result deleteLanguagePackById(@RequestParam("languagePackId") @ApiParam(name = "languagePackId", value = "languagePackId") Integer id) {
        iAdminLanguagePackService.deleteLanguagePackById(id);
        return R.ok();
    }

    @PostMapping("/exportLanguagePackById")
    @ApiOperation("导出语言包")
    public void exportLanguagePackById(@RequestParam("languagePackId") @ApiParam(name = "languagePackId", value = "languagePackId") Integer id, HttpServletResponse response) {

        iAdminLanguagePackService.exportLanguagePackById(id, response);
    }

    @PostMapping("/queryLanguagePackContextById")
    @ApiOperation("查询语言包字段信息")
    public Result queryLanguagePackContextById(@RequestParam("languagePackId") @ApiParam(name = "languagePackId", value = "languagePackId") Integer id) {
        return R.ok(iAdminLanguagePackService.queryLanguagePackContextById(id));
    }

    @PostMapping("/downloadExcel")
    @ApiOperation("导出模板")
    public void downloadExcel(HttpServletResponse response) {
        iAdminLanguagePackService.downloadExcel(response);
    }

    @ApiOperation("编辑语言包名称")
    @PostMapping("/updateLanguagePackNameById")
    public Result updateLanguagePackNameById(@RequestBody AdminLanguagePackBO adminLanguagePackBO) {
        iAdminLanguagePackService.updateLanguagePackNameById(adminLanguagePackBO);
        return Result.ok();
    }

    @ApiOperation("修改默认语言包配置")
    @PostMapping("/setDeflautLanguagePackSetting")
    public Result setDeflautLanguagePackSetting(@RequestParam("languagePackId") @ApiParam(name = "languagePackId", value = "languagePackId") Integer id) {
        iAdminLanguagePackService.setDeflautLanguagePackSetting(id, 0);
        return R.ok();
    }

    @ApiOperation(value = "查询系统默认语言")
    @PostMapping("/queryDeflautLanguagePackSetting")
    public JSONObject queryDeflautLanguagePackSetting() {
        return iAdminLanguagePackService.queryDeflautLanguagePackSetting(0);
    }
}

