package com.kakarote.bi.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.service.BiEmployeeService;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.utils.ExcelParseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/biEmployee")
@Api(tags = "员工业绩分析模块")
@Slf4j
public class BiEmployeeController {

    @Autowired
    private BiEmployeeService biEmployeeService;


    @ApiOperation("合同数量分析")
    @PostMapping("/contractNumStats")
    public Result<List<JSONObject>> contractNumStats(@RequestBody BiParams biParams) {
        biParams.setType("contractNum");
        List<JSONObject> objectList = biEmployeeService.contractNumStats(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("合同数量分析导出")
    @PostMapping("/contractNumStatsExport")
    public void contractNumStatsExport(@RequestBody BiParams biParams) {
        biParams.setType("contractNum");
        List<JSONObject> recordList = biEmployeeService.contractNumStats(biParams);
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        Map<String, Object> thisMonth = new HashMap<>();
        thisMonth.put("type", "当月合同数量(个)");
        Map<String, Object> lastMonthGrowth = new HashMap<>();
        lastMonthGrowth.put("type", "环比增长（%）");
        Map<String, Object> lastYearGrowth = new HashMap<>();
        lastYearGrowth.put("type", "同比增长（%）");
        recordList.forEach(record -> {
            monthList.add(record.getString("month"));
            thisMonth.put(record.getString("month"), record.getString("thisMonth"));
            lastMonthGrowth.put(record.getString("month"), record.getString("lastMonthGrowth"));
            lastYearGrowth.put(record.getString("month"), record.getString("lastYearGrowth"));
        });
        list.add(thisMonth);
        list.add(lastMonthGrowth);
        list.add(lastYearGrowth);

        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("type", "日期"));
        monthList.forEach(month -> dataList.add(ExcelParseUtil.toEntity(month, month)));
        ExcelParseUtil.exportExcel(recordList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "合同数量分析";
            }
        }, dataList);
    }

    @ApiOperation("合同金额分析")
    @PostMapping("/contractMoneyStats")
    public Result<List<JSONObject>> contractMoneyStats(@RequestBody BiParams biParams) {
        biParams.setType("contractMoney");
        List<JSONObject> objectList = biEmployeeService.contractNumStats(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("合同金额分析导出")
    @PostMapping("/contractMoneyStatsExport")
    public void contractMoneyStatsExport(@RequestBody BiParams biParams) {
        biParams.setType("contractMoney");
        List<JSONObject> recordList = biEmployeeService.contractNumStats(biParams);
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        Map<String, Object> thisMonth = new HashMap<>();
        thisMonth.put("type", "当月合同金额(元)");
        Map<String, Object> lastMonthGrowth = new HashMap<>();
        lastMonthGrowth.put("type", "环比增长（%）");
        Map<String, Object> lastYearGrowth = new HashMap<>();
        lastYearGrowth.put("type", "同比增长（%）");
        recordList.forEach(record -> {
            monthList.add(record.getString("month"));
            thisMonth.put(record.getString("month"), record.getString("thisMonth"));
            lastMonthGrowth.put(record.getString("month"), record.getString("lastMonthGrowth"));
            lastYearGrowth.put(record.getString("month"), record.getString("lastYearGrowth"));
        });
        list.add(thisMonth);
        list.add(lastMonthGrowth);
        list.add(lastYearGrowth);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("type", "日期"));
        monthList.forEach(month -> dataList.add(ExcelParseUtil.toEntity(month, month)));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "合同金额分析";
            }
        }, dataList);
    }

    @ApiOperation("回款金额分析")
    @PostMapping("/receivablesMoneyStats")
    public Result<List<JSONObject>> receivablesMoneyStats(@RequestBody BiParams biParams) {
        biParams.setType("receivables");
        List<JSONObject> objectList = biEmployeeService.contractNumStats(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("回款金额分析导出")
    @PostMapping("/receivablesMoneyStatsExport")
    public void receivablesMoneyStatsExport(@RequestBody BiParams biParams) {
        biParams.setType("receivables");
        List<JSONObject> recordList = biEmployeeService.contractNumStats(biParams);
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        Map<String, Object> thisMonth = new HashMap<>();
        thisMonth.put("type", "当月回款金额(元)");
        Map<String, Object> lastMonthGrowth = new HashMap<>();
        lastMonthGrowth.put("type", "环比增长（%）");
        Map<String, Object> lastYearGrowth = new HashMap<>();
        lastYearGrowth.put("type", "同比增长（%）");
        recordList.forEach(record -> {
            monthList.add(record.getString("month"));
            thisMonth.put(record.getString("month"), record.getString("thisMonth"));
            lastMonthGrowth.put(record.getString("month"), record.getString("lastMonthGrowth"));
            lastYearGrowth.put(record.getString("month"), record.getString("lastYearGrowth"));
        });
        list.add(thisMonth);
        list.add(lastMonthGrowth);
        list.add(lastYearGrowth);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("type", "日期"));
        monthList.forEach(month -> dataList.add(ExcelParseUtil.toEntity(month, month)));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "回款金额分析";
            }
        }, dataList);
    }


    @ApiOperation("合同汇总表")
    @PostMapping("/totalContract")
    public Result<JSONObject> totalContract(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biEmployeeService.totalContract(biParams);
        return Result.ok(jsonObject);
    }

    @ApiOperation("合同汇总表导出")
    @PostMapping("/totalContractExport")
    public void totalContractExport(@RequestBody BiParams biParams) {
        JSONObject record = biEmployeeService.totalContract(biParams);
        List<JSONObject> recordList = record.getJSONArray("list").toJavaList(JSONObject.class);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("type", "日期"));
        dataList.add(ExcelParseUtil.toEntity("contractNum", "签约合同数（个）"));
        dataList.add(ExcelParseUtil.toEntity("contractMoney", "签约合同金额（元）"));
        dataList.add(ExcelParseUtil.toEntity("receivablesMoney", "回款金额（元）"));
        ExcelParseUtil.exportExcel(recordList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "合同汇总";
            }
        }, dataList);
    }

    @ApiOperation("发票统计")
    @PostMapping("/invoiceStats")
    public Result<JSONObject> invoiceStats(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biEmployeeService.invoiceStats(biParams);
        return Result.ok(jsonObject);
    }

    @ApiOperation("发票统计导出")
    @PostMapping("/invoiceStatsExport")
    public void invoiceStatsExport(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biEmployeeService.invoiceStats(biParams);
        List<JSONObject> recordList =jsonObject.getJSONArray("list").toJavaList(JSONObject.class);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("type", "日期"));
        dataList.add(ExcelParseUtil.toEntity("receivablesMoney", "回款金额（元）"));
        dataList.add(ExcelParseUtil.toEntity("invoiceMoney", "开票金额（元）"));
        dataList.add(ExcelParseUtil.toEntity("receivablesNoInvoice", "已回款未开票（元）"));
        dataList.add(ExcelParseUtil.toEntity("invoiceNoReceivables", "已开票未回款（元）"));
        ExcelParseUtil.exportExcel(recordList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "发票统计";
            }
        }, dataList);
    }
}
