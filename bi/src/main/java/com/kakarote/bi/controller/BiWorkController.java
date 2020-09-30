package com.kakarote.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.entity.VO.BiPageVO;
import com.kakarote.bi.entity.VO.BiParamVO;
import com.kakarote.bi.service.BiWorkService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.crm.entity.BiParams;
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
import java.io.IOException;
import java.util.List;

import static com.kakarote.core.utils.BaseUtil.getResponse;

@RestController
@RequestMapping("/biWork")
@Api(tags = "办公分析接口")
@Slf4j
public class BiWorkController {

    @Autowired
    private BiWorkService biWorkService;


    @ApiOperation("查询日志统计信息")
    @PostMapping("/logStatistics")
    public Result<List<JSONObject>> logStatistics(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biWorkService.logStatistics(biParams);
        return R.ok(objectList);
    }


    @ApiOperation("查询日志统计信息导出")
    @PostMapping("/logStatisticsExport")
    public void logStatisticsExport(@RequestBody BiParams biParams) throws IOException {
        List<JSONObject> recordList = biWorkService.logStatistics(biParams);
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("count", "填写数");
            writer.addHeaderAlias("unCommentCount", "未评论数");
            writer.addHeaderAlias("commentCount", "已评论数");
            writer.merge(3, "日志分析");
            HttpServletResponse response = getResponse();
            writer.write(recordList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 4; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=logStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        }
    }

    @ApiOperation("查询审批统计信息")
    @PostMapping("/examineStatistics")
    public Result<JSONObject> examineStatistics(@RequestBody BiParams biParams) {
        JSONObject object = biWorkService.examineStatistics(biParams);
        return R.ok(object);
    }


    @ApiOperation("查询审批统计信息导出")
    @PostMapping("/examineStatisticsExport")
    public void examineStatisticsExport(@RequestBody BiParams biParams) throws IOException {
        JSONObject object = biWorkService.examineStatistics(biParams);
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<JSONObject> categoryList = object.getJSONArray("categoryList").toJavaList(JSONObject.class);
            writer.addHeaderAlias("realname", "员工");
            for (JSONObject record : categoryList) {
                writer.addHeaderAlias("count_" + record.getInteger("categoryId"), record.getString("title"));
            }
            writer.merge(categoryList.size(), "审批分析");
            HttpServletResponse response = getResponse();
            List<JSONObject> userList = object.getJSONArray("userList").toJavaList(JSONObject.class);
            userList.forEach(user->{
                user.remove("img");
                user.remove("userId");
                user.remove("username");
            });
            writer.write(userList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < categoryList.size(); i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=examineStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        }
    }


    /**
     * 查询审批详情
     * @author zhangzhiwei
     */
    @ApiOperation("查询审批详情")
    @PostMapping("/examineInfo")
    public Result<BiPageVO<JSONObject>> examineInfo(@RequestBody BiParamVO biParamVO){
        return R.ok(biWorkService.examineInfo(biParamVO));
    }
}
