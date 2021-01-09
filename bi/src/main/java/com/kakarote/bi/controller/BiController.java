package com.kakarote.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.entity.VO.ProductStatisticsVO;
import com.kakarote.bi.service.BiService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.utils.UserUtil;
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

@RestController
@RequestMapping("/bi")
@Api(tags = "商业智能接口")
@Slf4j
public class BiController {

    @Autowired
    private BiService biService;


    @PostMapping("/productStatistics")
    @ApiOperation("产品销售情况统计")
    public Result<BasePage<ProductStatisticsVO>> productStatistics(@RequestBody BiParams biParams) {
        BasePage<ProductStatisticsVO> objectList = biService.queryProductSell(biParams);
        return Result.ok(objectList);
    }

    @PostMapping("/productStatisticsExport")
    @ApiOperation("产品销售情况统计导出")
    public void productStatisticsExport(@RequestBody BiParams biParams) {
        List<Map<String, Object>> list = biService.productSellExport(biParams);
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("categoryName", "产品分类");
            writer.addHeaderAlias("productName", "产品名称");
            writer.addHeaderAlias("contractNum", "合同数");
            writer.addHeaderAlias("num", "数量合计");
            writer.addHeaderAlias("total", "订单产品小计");
            writer.merge(4, "产品销售情况统计");
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            HttpServletResponse response = UserUtil.getUser().getResponse();
            for (int i = 0; i < 5; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=productStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出产品销售情况统计错误：", e);
        }
    }

    @PostMapping("/taskCompleteStatistics")
    @ApiOperation("获取商业智能业绩目标完成情况")
    public Result<List<JSONObject>> taskCompleteStatistics(@RequestParam("year") String year, @RequestParam("type") Integer type, Integer deptId, Long userId, Integer isUser) {
        List<JSONObject> objectList = biService.taskCompleteStatistics(year, type, deptId, userId, isUser);
        return Result.ok(objectList);
    }

    @PostMapping("/taskCompleteStatisticsExport")
    @ApiOperation("获取商业智能业绩目标完成情况导出")
    public void taskCompleteStatisticsExport(@RequestParam("year") String year, @RequestParam("type") Integer type, Integer deptId, Long userId, Integer isUser) {
        List<Map<String, Object>> list = biService.taskCompleteStatisticsExport(year, type, deptId, userId, isUser);
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("name", "名称");
            writer.addHeaderAlias("month", "月份");
            writer.addHeaderAlias("achievement", "目标");
            writer.addHeaderAlias("money", "完成");
            writer.addHeaderAlias("rate", "完成率");
            writer.merge(4, "业绩目标完成情况统计");
            HttpServletResponse response = UserUtil.getUser().getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 5; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=taskCompleteStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出业绩目标完成情况统计错误：", e);
        }
        // 关闭writer，释放内存
    }
}
