package com.kakarote.bi.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.mapper.BiCustomerMapper;
import com.kakarote.bi.service.BiCustomerService;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.servlet.ApplicationContextHolder;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("biCustomer")
@Api(tags = "客户商业智能模块")
@Slf4j
public class BiCustomerController {

    @Autowired
    private BiCustomerService biCustomerService;

    @ApiOperation("客户总量分析")
    @PostMapping("/totalCustomerStats")
    public Result<List<JSONObject>> totalCustomerStats(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.totalCustomerStats(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户总量分析图")
    @PostMapping("/totalCustomerTable")
    public Result<JSONObject> totalCustomerTable(@RequestBody BiParams biParams) {
        JSONObject object = biCustomerService.totalCustomerTable(biParams);
        return Result.ok(object);
    }

    @ApiOperation("客户总量分析图导出")
    @PostMapping("/totalCustomerTableExport")
    public void totalCustomerTableExport(@RequestBody BiParams biParams) {
        JSONObject kv = biCustomerService.totalCustomerTable(biParams);
        List<JSONObject> recordList = kv.getJSONArray("list").toJavaList(JSONObject.class);
        List<Map<String, Object>> list = new ArrayList<>(recordList);
        JSONObject total = kv.getJSONObject("total");
        total.put("dealCustomerRate", "");
        list.add(total);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "员工姓名"));
        dataList.add(ExcelParseUtil.toEntity("customerNum", "新增客户数"));
        dataList.add(ExcelParseUtil.toEntity("dealCustomerNum", "成交客户数"));
        dataList.add(ExcelParseUtil.toEntity("dealCustomerRate", "客户成交率"));
        dataList.add(ExcelParseUtil.toEntity("contractMoney", "合同总金额"));
        dataList.add(ExcelParseUtil.toEntity("receivablesMoney", "回款金额"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "客户总量分析";
            }
        }, dataList);
    }

    @ApiOperation("客户跟进次数分析")
    @PostMapping("/customerRecordStats")
    public Result<List<JSONObject>> customerRecordStats(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.customerRecordStats(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户跟进次数分析表")
    @PostMapping("/customerRecordInfo")
    public Result<JSONObject> customerRecordInfo(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biCustomerService.customerRecordInfo(biParams);
        return Result.ok(jsonObject);
    }

    @ApiOperation("客户跟进次数分析表导出")
    @PostMapping("/customerRecordInfoExport")
    public void customerRecordInfoExport(@RequestBody BiParams biParams) {
        JSONObject kv = biCustomerService.customerRecordInfo(biParams);
        List<JSONObject> recordList = kv.getJSONArray("list").toJavaList(JSONObject.class);
        List<Map<String, Object>> list = new ArrayList<>(recordList);
        JSONObject total = kv.getJSONObject("total");
        list.add(total);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "员工姓名"));
        dataList.add(ExcelParseUtil.toEntity("recordCount", "跟进次数"));
        dataList.add(ExcelParseUtil.toEntity("customerCount", "跟进客户数"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "客户跟进次数分析";
            }
        }, dataList);
    }


    @ApiOperation("客户跟进方式分析")
    @PostMapping("/customerRecodCategoryStats")
    public Result<List<JSONObject>> customerRecodCategoryStats(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.customerRecodCategoryStats(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户跟进方式分析导出")
    @PostMapping("/customerRecodCategoryStatsExport")
    public void customerRecodCategoryStatsExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.customerRecodCategoryStats(biParams);
        List<Map<String, Object>> list = new ArrayList<>(objectList);
        JSONObject total = new JSONObject();
        Long totalRecordNum = objectList.stream().collect(Collectors.summarizingInt(r -> r.getInteger("recordNum"))).getSum();
        total.fluentPut("category", "总计").fluentPut("recordNum", totalRecordNum).fluentPut("proportion", "100%");
        list.add(total);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("category", "跟进方式"));
        dataList.add(ExcelParseUtil.toEntity("recordNum", "个数"));
        dataList.add(ExcelParseUtil.toEntity("proportion", "占比(%)"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "客户跟进方式分析";
            }
        }, dataList);
    }


    @ApiOperation("客户转化率分析图")
    @PostMapping("/customerConversionStats")
    public Result<List<JSONObject>> customerConversionStats(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.customerConversionStats(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户转化率分析表")
    @PostMapping("/customerConversionInfo")
    public Result<BasePage<JSONObject>> customerConversionInfo(@RequestBody BiParams basePageRequest) {
        BasePage<JSONObject> basePage = biCustomerService.customerConversionInfo(basePageRequest);
        return Result.ok(basePage);
    }


    @ApiOperation("公海客户分析图")
    @PostMapping("/poolStats")
    public Result<List<JSONObject>> poolStats(@RequestBody BiParams biParams) {
        List<JSONObject> basePage = biCustomerService.poolStats(biParams);
        return Result.ok(basePage);
    }

    @ApiOperation("公海客户分析表")
    @PostMapping("/poolTable")
    public Result<JSONObject> poolTable(@RequestBody BiParams biParams) {
        JSONObject basePage = biCustomerService.poolTable(biParams);
        return Result.ok(basePage);
    }

    @ApiOperation("公海客户分析表导出")
    @PostMapping("/poolTableExport")
    public void poolTableExport(@RequestBody BiParams biParams) {
        JSONObject kv = biCustomerService.poolTable(biParams);
        List<JSONObject> recordList = kv.getJSONArray("list").toJavaList(JSONObject.class);
        List<Map<String, Object>> list = new ArrayList<>(recordList);
        JSONObject total = kv.getJSONObject("total");
        list.add(total);

        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "姓名"));
        dataList.add(ExcelParseUtil.toEntity("deptName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("receiveNum", "公海池领取客户数"));
        dataList.add(ExcelParseUtil.toEntity("putInNum", "进入公海客户数"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "公海客户分析";
            }
        }, dataList);
    }


    @ApiOperation("员工客户成交周期图")
    @PostMapping("/employeeCycle")
    public Result<List<JSONObject>> employeeCycle(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.employeeCycle(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("员工客户成交周期表")
    @PostMapping("/employeeCycleInfo")
    public Result<JSONObject> employeeCycleInfo(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biCustomerService.employeeCycleInfo(biParams);
        return Result.ok(jsonObject);
    }

    @ApiOperation("员工客户成交周期表导出")
    @PostMapping("/employeeCycleInfoExport")
    public void employeeCycleInfoExport(@RequestBody BiParams biParams) {
        JSONObject kv = biCustomerService.employeeCycleInfo(biParams);
        List<JSONObject> recordList = kv.getJSONArray("list").toJavaList(JSONObject.class);
        List<Map<String, Object>> list = new ArrayList<>(recordList);
        JSONObject total = kv.getJSONObject("total");
        list.add(total);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "姓名"));
        dataList.add(ExcelParseUtil.toEntity("cycle", "成交周期（天）"));
        dataList.add(ExcelParseUtil.toEntity("customerNum", "成交客户数"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "员工客户成交周期分析";
            }
        }, dataList);
    }


    @ApiOperation("地区成交周期图")
    @PostMapping("/districtCycle")
    public Result<JSONObject> districtCycle(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biCustomerService.districtCycle(biParams);
        return Result.ok(jsonObject);
    }

    @ApiOperation("地区成交周期图导出")
    @PostMapping("/districtCycleExport")
    public void districtCycleExport(@RequestBody BiParams biParams) {
        JSONObject kv = biCustomerService.districtCycle(biParams);
        List<JSONObject> recordList = kv.getJSONArray("list").toJavaList(JSONObject.class);
        List<Map<String, Object>> list = new ArrayList<>(recordList);
        JSONObject total = kv.getJSONObject("total");
        list.add(total);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("type", "地区"));
        dataList.add(ExcelParseUtil.toEntity("cycle", "成交周期（天）"));
        dataList.add(ExcelParseUtil.toEntity("customerNum", "成交客户数"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "地区成交周期分析";
            }
        }, dataList);
    }

    @ApiOperation("产品成交周期")
    @PostMapping("/productCycle")
    public Result<JSONObject> productCycle(@RequestBody BiParams biParams) {
        JSONObject jsonObject = biCustomerService.productCycle(biParams);
        return Result.ok(jsonObject);
    }

    @ApiOperation("产品成交周期导出")
    @PostMapping("/productCycleExport")
    public void productCycleExport(@RequestBody BiParams biParams) {
        JSONObject kv = biCustomerService.productCycle(biParams);
        List<JSONObject> recordList = kv.getJSONArray("list").toJavaList(JSONObject.class);
        List<Map<String, Object>> list = new ArrayList<>(recordList);
        JSONObject total = kv.getJSONObject("total");
        list.add(total);

        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("productName", "产品名称"));
        dataList.add(ExcelParseUtil.toEntity("cycle", "成交周期（天）"));
        dataList.add(ExcelParseUtil.toEntity("customerNum", "成交客户数"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "产品成交周期分析";
            }
        }, dataList);
    }

    @ApiOperation("客户满意度分析")
    @PostMapping("/customerSatisfactionTable")
    public Result<List<JSONObject>> customerSatisfactionTable(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.customerSatisfactionTable(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("客户满意度分析导出")
    @PostMapping("/customerSatisfactionExport")
    public void customerSatisfactionExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.customerSatisfactionTable(biParams);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        JSONObject object = ApplicationContextHolder.getBean(BiCustomerMapper.class).querySatisfactionOptionList();
        List<String> optionList = StrUtil.splitTrim(object.getString("options"), Const.SEPARATOR);
        dataList.add(ExcelParseUtil.toEntity("realname", "员工姓名"));
        dataList.add(ExcelParseUtil.toEntity("visitContractNum", "回访合同总数"));
        optionList.forEach(option ->  dataList.add(ExcelParseUtil.toEntity(option, option)));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "客户满意度分析";
            }
        }, dataList);
    }

    @ApiOperation("产品满意度分析")
    @PostMapping("/productSatisfactionTable")
    public Result<List<JSONObject>> productSatisfactionTable(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.productSatisfactionTable(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("产品满意度分析导出")
    @PostMapping("/productSatisfactionExport")
    public void productSatisfactionExport(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biCustomerService.productSatisfactionTable(biParams);
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        JSONObject object = ApplicationContextHolder.getBean(BiCustomerMapper.class).querySatisfactionOptionList();
        List<String> optionList = StrUtil.splitTrim(object.getString("options"), Const.SEPARATOR);
        dataList.add(ExcelParseUtil.toEntity("productName", "产品名称"));
        dataList.add(ExcelParseUtil.toEntity("visitContractNum", "回访合同总数"));
        optionList.forEach(option ->  dataList.add(ExcelParseUtil.toEntity(option, option)));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "产品满意度分析";
            }
        }, dataList);
    }
}
