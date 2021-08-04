package com.kakarote.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.kakarote.admin.entity.BO.QuerySysLogBO;
import com.kakarote.admin.entity.PO.LoginLog;
import com.kakarote.admin.entity.PO.SysLog;
import com.kakarote.admin.service.ISysLogService;
import com.kakarote.core.common.ModelType;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.ExcelParseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
        List<Map<String, Object>> objectList = page.getList().stream().map(obj -> {
            Map<String, Object> map = BeanUtil.beanToMap(obj);
            map.remove("id");
            map.remove("className");
            map.remove("methodName");
            map.remove("args");
            map.remove("userId");
            map.put("model", ModelType.valueOfName((String) map.get("model")));
            return map;
        }).collect(Collectors.toList());
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "用户"));
        dataList.add(ExcelParseUtil.toEntity("createTime", "时间"));
        dataList.add(ExcelParseUtil.toEntity("ipAddress", "ip地址"));
        dataList.add(ExcelParseUtil.toEntity("model", "模块"));
        dataList.add(ExcelParseUtil.toEntity("subModel", "子模块"));
        dataList.add(ExcelParseUtil.toEntity("behavior", "行为"));
        dataList.add(ExcelParseUtil.toEntity("object", "对象"));
        dataList.add(ExcelParseUtil.toEntity("detail", "操作详情"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "系统日志";
            }
        }, dataList);
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
        List<Map<String, Object>> objectList = page.getList().stream().map(obj -> {
            Map<String, Object> map = BeanUtil.beanToMap(obj);
            map.remove("id");
            map.remove("userId");
            map.remove("failResult");
            map.put("authResult", obj.getAuthResult()==1?"成功":"失败");
            return map;
        }).collect(Collectors.toList());

        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "用户"));
        dataList.add(ExcelParseUtil.toEntity("loginTime", "登陆时间"));
        dataList.add(ExcelParseUtil.toEntity("ipAddress", "ip地址"));
        dataList.add(ExcelParseUtil.toEntity("loginAddress", "登陆地点"));
        dataList.add(ExcelParseUtil.toEntity("deviceType", "设备类型"));
        dataList.add(ExcelParseUtil.toEntity("core", "终端内核"));
        dataList.add(ExcelParseUtil.toEntity("platform", "平台"));
        dataList.add(ExcelParseUtil.toEntity("authResult", "认证结果"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "登录日志";
            }
        }, dataList);
    }


}

