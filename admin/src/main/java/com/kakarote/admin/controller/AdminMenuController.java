package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.service.IAdminMenuService;
import com.kakarote.core.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminMenu")
@Api(tags = "菜单模块")
public class AdminMenuController {

    @Autowired
    private IAdminMenuService adminMenuService;

    @RequestMapping("/getMenuListByType/{type}")
    @ApiOperation("根据类型查询菜单")
    public Result<JSONObject> getMenuListByType(@PathVariable("type") Integer type) {
        AdminRoleTypeEnum typeEnum = AdminRoleTypeEnum.parse(type);
        JSONObject byType = adminMenuService.getMenuListByType(typeEnum);
        return Result.ok(byType);
    }

    @RequestMapping("/getWorkMenuList")
    @ApiOperation("查询项目管理菜单")
    public Result<JSONObject> getWorkMenuList() {
        JSONObject byType = adminMenuService.getMenuListByType(AdminRoleTypeEnum.WORK);
        return Result.ok(byType);
    }

    @RequestMapping("/queryMenuId")
    public Result<Integer> queryMenuId(@RequestParam("realm1") String realm1,@RequestParam("realm2") String realm2,
                                       @RequestParam("realm3") String realm3){
        Integer menuId = adminMenuService.queryMenuId(realm1,realm2,realm3);
        return Result.ok(menuId);

    }
}

