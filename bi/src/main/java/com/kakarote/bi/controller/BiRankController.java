package com.kakarote.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.service.BiRankService;
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
import java.util.List;

import static com.kakarote.core.utils.BaseUtil.getResponse;

@RestController
@RequestMapping("/biRanking")
@Api(tags = "商业智能排行榜接口")
@Slf4j
public class BiRankController {

    @Autowired
    private BiRankService biRankService;

    @ApiOperation("城市分布分析")
    @PostMapping("/addressAnalyse")
    public Result<List<JSONObject>> addressAnalyse(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.addressAnalyse(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户行业分析")
    @PostMapping("/portrait")
    public Result<List<JSONObject>> portrait(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.portrait(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户级别分析")
    @PostMapping("/portraitLevel")
    public Result<List<JSONObject>> portraitLevel(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.portraitLevel(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户来源分析")
    @PostMapping("/portraitSource")
    public Result<List<JSONObject>> portraitSource(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.portraitSource(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("产品分类销量分析")
    @PostMapping("/contractProductRanKing")
    public Result<List<JSONObject>> contractProductRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.contractProductRanKing(biParams);
        return Result.ok(objectList);
    }


    @ApiOperation("合同金额排行榜")
    @PostMapping("/contractRanKing")
    public Result<List<JSONObject>> contractRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.contractRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("合同金额排行榜导出")
    @PostMapping("/contractRanKingExport")
    public void contractRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.contractRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("money", "合同金额（元）");
            writer.merge(3, "合同金额排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=contractRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出合同金额排行错误：", e);
        }
    }

    @ApiOperation("回款金额排行榜")
    @PostMapping("/receivablesRanKing")
    public Result<List<JSONObject>> receivablesRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.receivablesRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("回款金额排行榜导出")
    @PostMapping("/receivablesRanKingExport")
    public void receivablesRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.receivablesRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("money", "回款金额（元）");
            writer.merge(3, "回款金额排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=receivablesRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出回款金额排行错误：", e);
        }
    }


    @ApiOperation("签约合同排行榜")
    @PostMapping("/contractCountRanKing")
    public Result<List<JSONObject>> contractCountRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.contractCountRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("签约合同排行榜导出")
    @PostMapping("/contractCountRanKingExport")
    public void contractCountRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.contractCountRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "签约合同数（个）");
            writer.merge(3, "签约合同排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=contractCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出签约合同排行错误：", e);
        }
    }


    @ApiOperation("产品销量排行榜")
    @PostMapping("/productCountRanKing")
    public Result<List<JSONObject>> productCountRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.productCountRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("产品销量排行榜导出")
    @PostMapping("/productCountRanKingExport")
    public void productCountRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.productCountRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "产品销量");
            writer.merge(3, "产品销量排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=productCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出产品销量排行错误：", e);
        }
    }

    @ApiOperation("新增客户数排行榜")
    @PostMapping("/customerCountRanKing")
    public Result<List<JSONObject>> customerCountRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.customerCountRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("新增客户数排行榜导出")
    @PostMapping("/customerCountRanKingExport")
    public void customerCountRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.customerCountRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "创建人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "新增客户数（个）");
            writer.merge(3, "新增客户数排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=customerCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出新增客户数排行错误：", e);
        }
    }

    @PostMapping("/ranking")
    @ApiOperation("排行榜")
    public Result<JSONObject> ranking(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biRankService.ranking(biParams);
        return R.ok(jsonObject);
    }

    @ApiOperation("新增联系人排行榜")
    @PostMapping("/contactsCountRanKing")
    public Result<List<JSONObject>> contactsCountRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.contactsCountRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("新增联系人排行榜导出")
    @PostMapping("/contactsCountRanKingExport")
    public void contactsCountRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.contactsCountRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "创建人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "新增联系人数（个）");
            writer.merge(3, "新增联系人排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=contactsCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出新增联系人排行错误：", e);
        }
    }


    @ApiOperation("跟进客户数排行榜")
    @PostMapping("/customerGenjinCountRanKing")
    public Result<List<JSONObject>> customerGenjinCountRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.customerGenjinCountRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("跟进客户数排行榜导出")
    @PostMapping("/customerGenjinCountRanKingExport")
    public void customerGenjinCountRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.customerGenjinCountRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "跟进客户数（个）");
            writer.merge(3, "跟进客户数排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=customerFollowupCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出跟进客户数排行错误：", e);
        }
    }


    @ApiOperation("跟进次数排行榜")
    @PostMapping("/recordCountRanKing")
    public Result<List<JSONObject>> recordCountRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.recordCountRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("跟进次数排行榜导出")
    @PostMapping("/recordCountRanKingExport")
    public void recordCountRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.recordCountRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "跟进次数（次）");
            writer.merge(3, "跟进次数排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=FollowupRecordCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出跟进次数排行错误：", e);
        }
    }


    @ApiOperation("出差次数排行")
    @PostMapping("/travelCountRanKing")
    public Result<List<JSONObject>> travelCountRanKing(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.travelCountRanKing(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("出差次数排行导出")
    @PostMapping("/travelCountRanKingExport")
    public void travelCountRanKingExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biRankService.travelCountRanKing(biParams);
        for (int i = 0; i < objectList.size(); i++) {
            objectList.get(i).put("order", i + 1);
        }
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "出差次数（次）");
            writer.merge(3, "出差次数排行");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(objectList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=travelCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出出差次数排行错误：", e);
        }
    }

}
