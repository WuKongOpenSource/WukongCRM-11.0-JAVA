package com.kakarote.bi.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.service.BiRankService;
import com.kakarote.core.common.R;
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
import java.util.List;
import java.util.Map;

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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "签订人"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("money", "合同金额（元）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }

            @Override
            public String getExcelName() {
                return "合同金额排行榜";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "签订人"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("money", "回款金额（元）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "回款金额排行榜";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "签订人"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("count", "签约合同数（个）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "签约合同排行";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "签订人"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("count", "产品销量"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "产品销量排行";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "创建人"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("count", "新增客户数（个）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "新增客户数排行";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "创建人"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("count", "新增联系人数（个）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "新增联系人排行";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "员工"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("count", "跟进客户数（个）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "跟进客户数排行";
            }
        }, dataList);
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

        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "员工"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("count", "跟进客户数（个）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "跟进次数排行";
            }
        }, dataList);
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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("order", "公司总排名"));
        dataList.add(ExcelParseUtil.toEntity("realname", "员工"));
        dataList.add(ExcelParseUtil.toEntity("structureName", "部门"));
        dataList.add(ExcelParseUtil.toEntity("count", "出差次数（次）"));
        ExcelParseUtil.exportExcel(objectList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "出差次数排行";
            }
        }, dataList);
    }

}
