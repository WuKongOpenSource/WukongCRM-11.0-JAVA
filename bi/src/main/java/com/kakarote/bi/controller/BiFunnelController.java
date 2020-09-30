package com.kakarote.bi.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.service.BiFunnelService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/biFunnel")
@Api(tags = "销售漏斗分析")
@Slf4j
public class BiFunnelController {

    @Autowired
    private BiFunnelService biFunnelService;


    @ApiOperation("销售漏斗")
    @PostMapping("/sellFunnel")
    public Result<List<JSONObject>> sellFunnel(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biFunnelService.sellFunnel(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("新增商机分析图")
    @PostMapping("/addBusinessAnalyze")
    public Result<List<JSONObject>> addBusinessAnalyze(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biFunnelService.addBusinessAnalyze(biParams);
        return Result.ok(objectList);
    }

    @ApiOperation("新增商机分析表")
    @PostMapping("/sellFunnelList")
    public Result<BasePage<JSONObject>> sellFunnelList(@RequestBody BiParams biParams) {
        BasePage<JSONObject> basePage = biFunnelService.sellFunnelList(biParams);
        return Result.ok(basePage);
    }

    @ApiOperation("商机转化率分析")
    @PostMapping("/win")
    public Result<List<JSONObject>> win(@RequestBody BiParams biParams) {
        List<JSONObject> objectList = biFunnelService.win(biParams);
        return Result.ok(objectList);
    }
}
