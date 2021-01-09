package com.kakarote.bi.controller;


import com.kakarote.bi.entity.BO.AchievementBO;
import com.kakarote.bi.entity.PO.CrmAchievement;
import com.kakarote.bi.service.ICrmAchievementService;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLogHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 业绩目标 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-22
 */
@RestController
@RequestMapping("/biAchievement")
@Api(tags = "业绩目标控制器")
public class CrmAchievementController {

    @Autowired
    private ICrmAchievementService achievementService;

    @PostMapping("/queryAchievementList")
    @ApiOperation("查询业绩目标")
    public Result<List<CrmAchievement>> queryAchievementList(AchievementBO crmAchievement) {
        List<CrmAchievement> crmAchievements = achievementService.queryAchievementList(crmAchievement);
        return Result.ok(crmAchievements);
    }

    @PostMapping("/addAchievement")
    @ApiOperation("保存业绩目标")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "保存业绩目标",detail = "保存业绩目标")
    public Result addAchievement(@RequestBody CrmAchievement crmAchievement) {
        achievementService.addAchievement(crmAchievement);
        return Result.ok();
    }

    @PostMapping("/setAchievement")
    @ApiOperation("修改业绩目标")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "修改业绩目标",detail = "修改业绩目标")
    public Result setAchievement(@RequestBody List<CrmAchievement> crmAchievement) {
        achievementService.verifyCrmAchievementData(crmAchievement);
        achievementService.updateBatchById(crmAchievement);
        return Result.ok();
    }

    @PostMapping("/deleteAchievement")
    @ApiOperation("删除业绩目标")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.DELETE,object = "删除业绩目标",detail = "删除业绩目标")
    public Result deleteAchievement(@RequestParam("achievementId") Integer achievementId){
        CrmAchievement byId = achievementService.getById(achievementId);
        if(byId!=null){
            achievementService.removeById(achievementId);
        }
        return Result.ok();
    }
}

