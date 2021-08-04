package com.kakarote.bi.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.entity.VO.BiPageVO;
import com.kakarote.bi.entity.VO.BiParamVO;
import com.kakarote.bi.service.BiWorkService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "员工"));
        dataList.add(ExcelParseUtil.toEntity("count", "填写数"));
        dataList.add(ExcelParseUtil.toEntity("unCommentCount", "未评论数"));
        dataList.add(ExcelParseUtil.toEntity("commentCount", "已评论数"));
        ExcelParseUtil.exportExcel(recordList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "日志统计";
            }
        }, dataList);
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
        List<JSONObject> userList = object.getJSONArray("userList").toJavaList(JSONObject.class);
        List<JSONObject> categoryList = object.getJSONArray("categoryList").toJavaList(JSONObject.class);
        userList.forEach(user->{
            user.remove("img");
            user.remove("userId");
            user.remove("username");
        });
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("realname", "员工"));
        for (JSONObject record : categoryList) {
            dataList.add(ExcelParseUtil.toEntity("count_" + record.getInteger("categoryId"), record.getString("title")));
        }
        ExcelParseUtil.exportExcel(userList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "审批分析";
            }
        }, dataList);
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
