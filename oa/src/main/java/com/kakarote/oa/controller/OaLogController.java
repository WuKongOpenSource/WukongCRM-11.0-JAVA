package com.kakarote.oa.controller;


import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.oa.common.log.OaLogLog;
import com.kakarote.oa.entity.BO.LogBO;
import com.kakarote.oa.entity.PO.OaLogRule;
import com.kakarote.oa.service.IOaCommonService;
import com.kakarote.oa.service.IOaLogRuleService;
import com.kakarote.oa.service.IOaLogService;
import com.kakarote.oa.service.IOaLogUserFavourService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作日志表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/oaLog")
@Api(tags = "日志模块")
@Slf4j
@SysLog(subModel = SubModelType.OA_LOG,logClass = OaLogLog.class)
public class OaLogController {

    @Autowired
    private IOaLogService oaLogService;

    @Autowired
    private IOaLogRuleService oaLogRuleService;

    @Autowired
    private IOaCommonService oaCommonService;

    @Autowired
    private IOaLogUserFavourService oaLogUserFavourService;

    @PostMapping("/initOaData")
    @ApiExplain("初始化日志数据")
    public Result<Boolean> initOaData() {
        return R.ok(oaCommonService.initOaData());
    }

    @PostMapping("/initCalendarData")
    @ApiExplain("初始化日历数据")
    public Result<Boolean> initCalendarData() {
        return R.ok(oaCommonService.initCalendarData());
    }

    @PostMapping("/initOaExamineData")
    @ApiExplain("初始化OA审批数据")
    public Result<Boolean> initOaExamineData() {
        return R.ok(oaCommonService.initOaExamineData());
    }

    @PostMapping("/queryList")
    @ApiOperation("日志列表")
    public Result<BasePage<JSONObject>> queryList(@RequestBody LogBO bo) {
        BasePage<JSONObject> basePage = oaLogService.queryList(bo);
        return R.ok(basePage);
    }

    @PostMapping("/favourOrCancel")
    @ApiOperation("用户点赞或取消")
    public Result<JSONObject> favourOrCancel(@RequestParam("isFavour") boolean isFavour,@RequestParam("logId") Integer logId) {
        return R.ok(oaLogUserFavourService.userFavourOrCancel(isFavour,logId));
    }

    /**
     * 获取日志欢迎语
     */
    @PostMapping("/getLogWelcomeSpeech")
    @ApiOperation("获取日志欢迎语")
    public Result<String> getLogWelcomeSpeech() {
        String value = oaLogService.getLogWelcomeSpeech();
        return R.ok(value);
    }

    @PostMapping("/queryLogBulletin")
    @ApiOperation("查询日志统计信息")
    public Result<JSONObject> queryLogBulletin() {
        JSONObject object = oaLogService.queryLogBulletin();
        return R.ok(object);
    }

    @PostMapping("/queryCompleteStats")
    @ApiOperation("查询日志完成情况统计")
    public Result<JSONObject> queryCompleteStats(@RequestParam("type") Integer type) {
        JSONObject object = oaLogService.queryCompleteStats(type);
        return R.ok(object);
    }

    @PostMapping("/queryCompleteOaLogList")
    @ApiOperation("查询日志完成情况统计列表")
    public Result<BasePage<JSONObject>> queryCompleteOaLogList(@RequestBody LogBO bo) {
        BasePage<JSONObject> basePage = oaLogService.queryCompleteOaLogList(bo);
        return R.ok(basePage);
    }

    @PostMapping("/queryIncompleteOaLogList")
    @ApiOperation("查询日志未完成情况统计列表")
    public Result<BasePage<SimpleUser>> queryIncompleteOaLogList(@RequestBody LogBO bo) {
        BasePage<SimpleUser> basePage = oaLogService.queryIncompleteOaLogList(bo);
        return R.ok(basePage);
    }

    @PostMapping("/addOrUpdate")
    @ApiOperation("修改或新增")
    @SysLogHandler
    public Result addOrUpdate(@RequestBody JSONObject jsonObject) {
        oaLogService.saveAndUpdate(jsonObject);
        return R.ok();
    }

    @PostMapping("/deleteById")
    @ApiOperation("修改")
    @SysLogHandler(behavior = BehaviorEnum.DELETE,object = "",detail = "删除了日志")
    public Result deleteById(@RequestParam("logId") Integer logId) {
        oaLogService.deleteById(logId);
        return R.ok();
    }

    @PostMapping("/queryLogBulletinByType")
    @ApiOperation("查询日志完成情况统计列表2")
    public Result<BasePage<JSONObject>> queryLogBulletinByType(@RequestBody LogBO bo) {
        BasePage<JSONObject> basePage = oaLogService.queryLogBulletinByType(bo);
        return R.ok(basePage);
    }

    @PostMapping("/queryLogRecordCount")
    @ApiOperation("查询日志记录统计")
    public Result<List<JSONObject>> queryLogRecordCount(Integer logId, Integer today) {
        List<JSONObject> objectList = oaLogService.queryLogRecordCount(logId, today);
        return R.ok(objectList);
    }

    @PostMapping("/queryById")
    @ApiOperation("根据日志id获取日志")
    public Result<JSONObject> queryById(@RequestParam("logId") Integer logId){
        JSONObject oaLog=oaLogService.queryById(logId);
        return Result.ok(oaLog);
    }

    @PostMapping("/export")
    @ApiOperation("导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "导出日志",detail = "导出日志")
    public void export(@RequestBody LogBO logBO,HttpServletResponse response){
        List<Map<String, Object>> list = oaLogService.export(logBO);
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("category", "日志类型");
            writer.addHeaderAlias("createTime", "创建日期");
            writer.addHeaderAlias("createUserName", "创建人");
            writer.addHeaderAlias("sendName", "发送给");
            writer.addHeaderAlias("content", "今日工作内容");
            writer.addHeaderAlias("tomorrow", "明日工作内容");
            writer.addHeaderAlias("question", "遇到问题");
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.addHeaderAlias("comment", "回复");
            writer.merge(8, "日志信息");
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 9; i++) {
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
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=log.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出日志错误：", e);
        }
    }



    @PostMapping("/queryOaLogRuleList")
    @ApiOperation("查询日志规则")
    public Result<List<OaLogRule>> queryOaLogRuleList(){
        List<OaLogRule> logRules = oaLogRuleService.queryOaLogRuleList();
        return Result.ok(logRules);
    }

    /**
     * 设置日志提交设置
     */
    @PostMapping("/setOaLogRule")
    @ApiOperation("设置日志提交设置")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_OTHER_SETTINGS,behavior = BehaviorEnum.UPDATE,object = "设置日志提交设置",detail = "设置日志提交设置")
    public Result setOaLogRule(@RequestBody List<OaLogRule> ruleList) {
        oaLogRuleService.setOaLogRule(ruleList);
        return R.ok();
    }
}

