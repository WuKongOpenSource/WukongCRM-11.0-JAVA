package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.constant.ConfigType;
import com.kakarote.hrm.entity.PO.HrmConfig;
import com.kakarote.hrm.service.IHrmConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 淘汰原因表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-02
 */
@RestController
@RequestMapping("/hrmRecruitEliminate")
@Api(tags = "查询淘汰原因列表(表单使用)")
public class HrmRecruitEliminateController {

    @Autowired
    private IHrmConfigService configService;

    @PostMapping("/queryRecruitEliminateList")
    @ApiOperation("查询淘汰原因列表")
    public Result<List<String>> queryRecruitEliminateList() {
        List<String> list = configService.lambdaQuery().eq(HrmConfig::getType, ConfigType.ELIMINATION_REASONS.getValue()).list()
                .stream().map(HrmConfig::getValue).collect(Collectors.toList());
        return Result.ok(list);
    }

}

