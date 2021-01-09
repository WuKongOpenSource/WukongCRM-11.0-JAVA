package com.kakarote.crm.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.LoginFromCookie;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.crm.common.log.CrmPrintTemplateLog;
import com.kakarote.crm.entity.BO.CrmPrintTemplateBO;
import com.kakarote.crm.entity.PO.CrmPrintRecord;
import com.kakarote.crm.entity.PO.CrmPrintTemplate;
import com.kakarote.crm.entity.VO.CrmPrintFieldVO;
import com.kakarote.crm.service.ICrmPrintTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 打印模板表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/crmPrint")
@Api(tags = "打印模板")
@Slf4j
@SysLog(logClass = CrmPrintTemplateLog.class)
public class CrmPrintTemplateController {

    @Autowired
    private ICrmPrintTemplateService printTemplateService;

    @ApiOperation("查询打印模板列表")
    @PostMapping("/queryPrintTemplateList")
    public Result<BasePage<CrmPrintTemplate>> queryPrintTemplateList(@RequestBody CrmPrintTemplateBO printTemplateBO) {
        BasePage<CrmPrintTemplate> adminPrintTemplateBasePage = printTemplateService.queryPrintTemplateList(printTemplateBO);
        return Result.ok(adminPrintTemplateBasePage);
    }

    @ApiOperation("跟进ID查询打印模板")
    @PostMapping("/queryPrintTemplateById")
    public Result<CrmPrintTemplate> queryPrintTemplateById(@RequestParam("templateId") Integer templateId) {
        CrmPrintTemplate byId = printTemplateService.getById(templateId);
        return Result.ok(byId);
    }

    @ApiOperation("新增模板")
    @PostMapping("/addPrintTemplate")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.SAVE,object = "#adminPrintTemplate.templateName",detail = "'新增了打印模板:'+#adminPrintTemplate.templateName")
    public Result addPrintTemplate(@RequestBody CrmPrintTemplate adminPrintTemplate) {
        adminPrintTemplate.setUpdateTime(new Date());
        printTemplateService.save(adminPrintTemplate);
        return Result.ok();
    }

    @ApiOperation("修改模板")
    @PostMapping("/updatePrintTemplate")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "#adminPrintTemplate.templateName",detail = "'修改了打印模板:'+#adminPrintTemplate.templateName")
    public Result updatePrintTemplate(@RequestBody CrmPrintTemplate adminPrintTemplate) {
        printTemplateService.updateById(adminPrintTemplate);
        return Result.ok();
    }

    @ApiOperation("删除模板")
    @PostMapping("/deletePrintTemplate")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.DELETE)
    public Result deletePrintTemplate(@RequestParam("templateId") Integer templateId) {
        printTemplateService.deletePrintTemplate(templateId);
        return Result.ok();
    }

    @ApiOperation("查询字段")
    @PostMapping("/queryFields")
    public Result<CrmPrintFieldVO> queryFields(@RequestParam("type") Integer type) {
        CrmPrintFieldVO crmPrintFieldVO = printTemplateService.queryFields(type);
        return Result.ok(crmPrintFieldVO);
    }

    @ApiOperation("打印")
    @PostMapping("/print")
    public Result<String> print(@RequestParam("templateId") Integer templateId, @RequestParam("id") Integer id) {
        String print = printTemplateService.print(templateId, id);
        return Result.ok(print);
    }

    @ApiOperation("预览")
    @PostMapping("/preview")
    public Result<String> preview(@RequestParam("content") String content, @RequestParam("type") String type) {
        String s = printTemplateService.preview(content, type);
        return Result.ok(s);
    }

    @ApiOperation("下载文件")
    @RequestMapping("/down")
    @LoginFromCookie
    public void down(@RequestParam("type") Integer type, @RequestParam("key") String key, HttpServletResponse response) {
        String object = BaseUtil.getRedis().get(CrmCacheKey.CRM_PRINT_TEMPLATE_CACHE_KEY + key);
        if (StrUtil.isNotEmpty(object)) {
            JSONObject parse = JSON.parseObject(object);
            String path;
            if (type == 2) {
                path = parse.getString("word");
            } else {
                path = parse.getString("pdf");
            }
            if (FileUtil.exist(path)) {
                ServletUtil.write(response, FileUtil.file(path));
                return;
            }
        }
        ServletUtil.write(response, Result.ok().toJSONString(), "text/plain");
    }

    @ApiOperation("iframe")
    @RequestMapping("/preview.pdf")
    @LoginFromCookie
    public void preview(String key, HttpServletResponse response) {
        String object = BaseUtil.getRedis().get(CrmCacheKey.CRM_PRINT_TEMPLATE_CACHE_KEY + key);
        if (StrUtil.isNotEmpty(object)) {
            JSONObject parse = JSON.parseObject(object);
            String path = parse.getString("pdf");
            if (FileUtil.exist(path)) {
                File file = FileUtil.file(path);
                BufferedInputStream in = null;
                ServletOutputStream out = null;
                try {
                    in = FileUtil.getInputStream(file);
                    response.setContentType("application/pdf");
                    IoUtil.copy(in,response.getOutputStream());
                } catch (Exception ex) {
                    log.error("导出错误",ex);
                } finally {
                    IoUtil.close(in);
                    IoUtil.close(out);
                }
                return;
            }
        }
        ServletUtil.write(response, Result.ok().toJSONString(), "text/plain");
    }

    @ApiOperation("复制模板")
    @PostMapping("/copyTemplate")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.COPY)
    public Result copyTemplate(@RequestParam("templateId") Integer templateId) {
        printTemplateService.copyTemplate(templateId);
        return Result.ok();
    }

    @ApiOperation("保存打印记录")
    @PostMapping("/savePrintRecord")
    public Result savePrintRecord(@RequestBody CrmPrintRecord crmPrintRecord) {
        printTemplateService.savePrintRecord(crmPrintRecord);
        return Result.ok();
    }

    @ApiOperation("查询打印记录")
    @PostMapping("/queryPrintRecord")
    public Result<List<CrmPrintRecord>> queryPrintRecord(@RequestParam("crmType") Integer crmType, @RequestParam("typeId") Integer typeId) {
        List<CrmPrintRecord> crmPrintRecords = printTemplateService.queryPrintRecord(crmType, typeId);
        return Result.ok(crmPrintRecords);
    }

    @ApiOperation("查询打印记录")
    @PostMapping("/queryPrintRecordById")
    public Result<CrmPrintRecord> queryPrintRecordById(@RequestParam("recordId") Integer recordId) {
        CrmPrintRecord crmPrintRecord = printTemplateService.queryPrintRecordById(recordId);
        return Result.ok(crmPrintRecord);
    }

}

