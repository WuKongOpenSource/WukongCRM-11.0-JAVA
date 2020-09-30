package com.kakarote.crm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmSceneConfigBO;
import com.kakarote.crm.entity.PO.CrmScene;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.ICrmSceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 场景 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-06
 */
@RestController
@RequestMapping("/crmScene")
@Api(tags = "场景模块")
public class CrmSceneController {

    @Autowired
    private ICrmSceneService crmSceneService;

    @PostMapping("/queryScene")
    @ApiOperation("查询场景列表")
    public Result<List<CrmScene>> queryScene(@ApiParam(name = "type", value = "类型") @RequestParam("type") Integer type) {
        List<CrmScene> sceneList = crmSceneService.queryScene(CrmEnum.parse(type));
        return Result.ok(sceneList);
    }

    @PostMapping("/queryField")
    @ApiOperation("查询场景搜索字段")
    public Result<List<CrmModelFiledVO>> queryField(@RequestParam("label") Integer label) {
        List<CrmModelFiledVO> filedVOS = crmSceneService.queryField(label);
        return Result.ok(filedVOS);
    }

    @PostMapping("/addScene")
    @ApiOperation("新增场景")
    public Result addScene(@RequestBody @Valid CrmScene adminScene) {
        crmSceneService.addScene(adminScene);
        return Result.ok();
    }

    @PostMapping("/updateScene")
    @ApiOperation("修改场景")
    public Result updateScene(@RequestBody @Valid CrmScene adminScene) {
        crmSceneService.updateScene(adminScene);
        return Result.ok();
    }

    @PostMapping("/setDefaultScene")
    @ApiOperation("设置默认场景")
    public Result setDefaultScene(@RequestParam("sceneId") Integer sceneId) {
        crmSceneService.setDefaultScene(sceneId);
        return Result.ok();
    }

    @PostMapping("/deleteScene")
    @ApiOperation("删除场景")
    public Result deleteScene(@RequestParam("sceneId") Integer sceneId) {
        crmSceneService.deleteScene(sceneId);
        return Result.ok();
    }


    @PostMapping("/querySceneConfig")
    @ApiOperation("查询场景设置")
    public Result<JSONObject> querySceneConfig(@RequestParam("type") Integer type) {
        JSONObject jsonObject = crmSceneService.querySceneConfig(type);
        return Result.ok(jsonObject);
    }


    @PostMapping("/sceneConfig")
    @ApiOperation("设置场景")
    public Result sceneConfig(@RequestBody CrmSceneConfigBO sceneConfig) {
        crmSceneService.sceneConfig(sceneConfig);
        return Result.ok();
    }
}

