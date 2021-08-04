package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.QueryRecordListBO;
import com.kakarote.hrm.entity.VO.QueryRecordListVO;
import com.kakarote.hrm.service.IHrmActionRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * hrm员工操作记录表 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmActionRecord")
@Api(tags = "人力资源-操作记录接口")
public class HrmActionRecordController {

    @Autowired
    private IHrmActionRecordService actionRecordService;


    @PostMapping("/queryRecordList")
    @ApiOperation("查询操作记录列表")
    public Result<List<QueryRecordListVO>> queryRecordList(@RequestBody QueryRecordListBO queryRecordListBO) {
        List<QueryRecordListVO> list = actionRecordService.queryRecordList(queryRecordListBO);
        return Result.ok(list);
    }

}

