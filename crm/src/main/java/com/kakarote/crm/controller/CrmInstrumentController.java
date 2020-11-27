package com.kakarote.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.crm.entity.BO.CrmSearchParamsBO;
import com.kakarote.crm.entity.PO.CrmActivity;
import com.kakarote.crm.service.CrmInstrumentService;
import com.kakarote.crm.service.ICrmInstrumentSortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * CRM仪表盘 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
@RestController
@RequestMapping("/crmInstrument/")
@Api(tags = "仪表盘相关接口")
public class CrmInstrumentController {

    @Autowired
    private ICrmInstrumentSortService sortService;

    @Autowired
    private CrmInstrumentService instrumentService;



    @PostMapping("/queryModelSort")
    @ApiOperation("查询模块排序")
    public Result<JSONObject> queryModelSort() {
        JSONObject object = sortService.queryModelSort();
        return Result.ok(object);
    }

    @PostMapping("/setModelSort")
    @ApiOperation("修改模块排序")
    public Result setModelSort(@RequestBody JSONObject jsonObject) {
        sortService.setModelSort(jsonObject);
        return R.ok();
    }

    @PostMapping("/queryBulletin")
    @ApiOperation("查询销售简报")
    public Result<JSONObject> queryBulletin(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.queryBulletin(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/queryBulletinInfo")
    @ApiOperation("查询销售简报详情")
    public Result<BasePage<Map<String, Object>>> queryBulletinInfo(@RequestBody BiParams biParams) {
        BasePage<Map<String, Object>> basePage = instrumentService.queryBulletinInfo(biParams);
        return R.ok(basePage);
    }

    @PostMapping("/forgottenCustomerCount")
    @ApiOperation("遗忘客户统计")
    public Result<JSONObject> forgottenCustomerCount(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.forgottenCustomerCount(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/sellFunnel")
    @ApiOperation("销售漏斗")
    public Result<JSONObject> sellFunnel(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.sellFunnel(biParams);
        return R.ok(jsonObject);
    }


    @PostMapping("/sellFunnelBusinessList")
    @ApiOperation("销售漏斗商机状态列表")
    public Result<BasePage<Map<String, Object>>> sellFunnelBusinessList(@RequestBody CrmSearchParamsBO crmSearchParamsBO) {
        BasePage<Map<String, Object>> mapBasePage = instrumentService.sellFunnelBusinessList(crmSearchParamsBO);
        return R.ok(mapBasePage);
    }

    @PostMapping("/salesTrend")
    @ApiOperation("销售趋势")
    public Result<JSONObject> salesTrend(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.salesTrend(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/queryDataInfo")
    @ApiOperation("数据汇总")
    public Result<JSONObject> queryDataInfo(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.queryDataInfo(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/queryPerformance")
    @ApiOperation("业绩指标")
    public Result<JSONObject> queryPerformance(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.queryPerformance(biParams);
        return R.ok(jsonObject);
    }


    @PostMapping("/queryRecordList")
    @ApiOperation("查询跟进记录统计列表")
    public Result<BasePage<CrmActivity>> queryRecordList(@RequestBody BiParams biParams){
        BasePage<CrmActivity> page = instrumentService.queryRecordList(biParams);
        return Result.ok(page);
    }


    @PostMapping("/queryRecordCount")
    @ApiOperation("查询销售简报的跟进记录统计")
    public Result<List<JSONObject>> queryRecordCount(@RequestBody BiParams biParams) {
        List<JSONObject> data = instrumentService.queryRecordCount(biParams);
        return Result.ok(data);
    }

    /**
     * 遗忘客户列表
     */
    @PostMapping("/forgottenCustomerPageList")
    @ApiOperation("遗忘客户列表")
    public Result<BasePage<Map<String,Object>>> forgottenCustomerPageList(@RequestBody BiParams biParams){
        BasePage<Map<String,Object>> page = instrumentService.forgottenCustomerPageList(biParams);
        return Result.ok(page);
    }


    @PostMapping("/unContactCustomerPageList")
    @ApiOperation("未联系客户列表")
    public Result<BasePage<Map<String,Object>>> unContactCustomerPageList(@RequestBody BiParams biParams){
        BasePage<Map<String,Object>> page = instrumentService.unContactCustomerPageList(biParams);
        return Result.ok(page);
    }

}
