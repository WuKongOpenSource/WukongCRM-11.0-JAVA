package com.kakarote.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.admin.entity.BO.QuerySysLogBO;
import com.kakarote.admin.entity.PO.LoginLog;
import com.kakarote.admin.entity.PO.SysLog;
import com.kakarote.admin.service.ISysLogService;
import com.kakarote.core.common.ModelType;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/adminSysLog")
@Api(tags = "系统日志")
@Slf4j
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @PostMapping("/saveSysLog")
    public Result saveSysLog(@RequestBody SysLog sysLog){
        sysLogService.saveSysLog(sysLog);
        return Result.ok();
    }



    @PostMapping("/querySysLogPageList")
    @ApiOperation("查询系统日志列表")
    public Result<BasePage<SysLog>> querySysLogPageList(@RequestBody QuerySysLogBO querySysLogBO){
        BasePage<SysLog> page = sysLogService.querySysLogPageList(querySysLogBO);
        return Result.ok(page);
    }

    @PostMapping("/exportSysLog")
    @ApiOperation("导出系统日志")
    public void exportSysLog(@RequestBody QuerySysLogBO querySysLogBO, HttpServletResponse response){
        querySysLogBO.setPageType(0);
        BasePage<SysLog> page = sysLogService.querySysLogPageList(querySysLogBO);
        List<Map<String, Object>> dataList = page.getList().stream().map(obj -> {
            Map<String, Object> map = BeanUtil.beanToMap(obj);
            map.remove("id");
            map.remove("className");
            map.remove("methodName");
            map.remove("args");
            map.remove("userId");
            map.put("model", ModelType.valueOfName((String) map.get("model")));
            return map;
        }).collect(Collectors.toList());
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("realname", "用户");
            writer.addHeaderAlias("createTime", "时间");
            writer.addHeaderAlias("ipAddress", "ip地址");
            writer.addHeaderAlias("model", "模块");
            writer.addHeaderAlias("subModel", "子模块");
            writer.addHeaderAlias("behavior", "行为");
            writer.addHeaderAlias("object", "对象");
            writer.addHeaderAlias("detail", "操作详情");
            writer.merge(7, "系统日志");
            writer.setOnlyAlias(true);
            writer.write(dataList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 7; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=sysLog.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出日志错误：", e);
        }
    }

    @PostMapping("/saveLoginLog")
    public Result saveLoginLog(@RequestBody LoginLog loginLog){
        sysLogService.saveLoginLog(loginLog);
        return Result.ok();
    }

    @PostMapping("/queryLoginLogPageList")
    @ApiOperation("查询登录日志列表")
    public Result<BasePage<LoginLog>> queryLoginLogPageList(@RequestBody QuerySysLogBO querySysLogBO){
        BasePage<LoginLog> page = sysLogService.queryLoginLogPageList(querySysLogBO);
        return Result.ok(page);
    }


    @PostMapping("/exportLoginLog")
    @ApiOperation("导出登陆日志")
    public void exportLoginLog(@RequestBody QuerySysLogBO querySysLogBO, HttpServletResponse response){
        querySysLogBO.setPageType(0);
        BasePage<LoginLog> page = sysLogService.queryLoginLogPageList(querySysLogBO);
        List<Map<String, Object>> dataList = page.getList().stream().map(obj -> {
            Map<String, Object> map = BeanUtil.beanToMap(obj);
            map.remove("id");
            map.remove("userId");
            map.remove("failResult");
            map.put("authResult", obj.getAuthResult()==1?"成功":"失败");
            return map;
        }).collect(Collectors.toList());
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("realName", "用户");
            writer.addHeaderAlias("loginTime", "登陆时间");
            writer.addHeaderAlias("ipAddress", "ip地址");
            writer.addHeaderAlias("loginAddress", "登陆地点");
            writer.addHeaderAlias("deviceType", "设备类型");
            writer.addHeaderAlias("core", "终端内核");
            writer.addHeaderAlias("platform", "平台");
            writer.addHeaderAlias("authResult", "认证接过");
            writer.merge(7, "登陆日志");
            writer.setOnlyAlias(true);
            writer.write(dataList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 7; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=loginLog.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出日志错误：", e);
        }
    }


}

