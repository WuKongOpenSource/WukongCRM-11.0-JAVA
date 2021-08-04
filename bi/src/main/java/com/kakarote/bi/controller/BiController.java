package com.kakarote.bi.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.entity.VO.ProductStatisticsVO;
import com.kakarote.bi.service.BiService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.utils.ExcelParseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        List<Map<String, Object>> objectList = biService.productSellExport(biParams);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("categoryName", "产品分类"));
        dataList.add(ExcelParseUtil.toEntity("productName", "产品名称"));
        dataList.add(ExcelParseUtil.toEntity("contractNum", "合同数"));
        dataList.add(ExcelParseUtil.toEntity("num", "数量合计"));
        dataList.add(ExcelParseUtil.toEntity("total", "订单产品小计"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "产品销售情况";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("name", "名称"));
        dataList.add(ExcelParseUtil.toEntity("month", "月份"));
        dataList.add(ExcelParseUtil.toEntity("achievement", "目标"));
        dataList.add(ExcelParseUtil.toEntity("money", "完成"));
        dataList.add(ExcelParseUtil.toEntity("rate", "完成率"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "业绩目标完成情况";
            }
        }, dataList);
    }
}
