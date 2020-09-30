package com.kakarote.work.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.work.service.IWorkCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 项目收藏表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/workCollect")
@Api(tags = "项目收藏")
public class WorkCollectController {
    @Autowired
    private IWorkCollectService workCollectService;

    @PostMapping("/collect/{workId}")
    @ApiOperation("收藏按钮")
    public Result collect(@PathVariable @NotNull Integer workId){
        workCollectService.collect(workId);
        return R.ok();
    }
}

