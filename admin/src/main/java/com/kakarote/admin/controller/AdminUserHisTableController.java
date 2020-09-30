package com.kakarote.admin.controller;

import com.kakarote.admin.service.IAdminUserHisTableService;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.entity.CallUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ian
 * @date 2020/8/28
 */
@RestController
@Api(tags = "授权坐席相关接口")
@RequestMapping("/adminUserHisTable")
public class AdminUserHisTableController {

    @Autowired
    private IAdminUserHisTableService adminUserHisTableService;

    @PostMapping("/authorize")
    @ApiOperation("员工坐席授权")
    public Result<Boolean> authorize(@RequestBody CallUser callUser){
        return Result.ok(adminUserHisTableService.authorize(callUser.getUserIds(),callUser.getState()));
    }

    @PostMapping("/checkAuth")
    @ApiOperation("判断用户是否为坐席")
    public Result<Boolean> checkAuth() {
        return Result.ok(adminUserHisTableService.checkAuth());
    }

}
