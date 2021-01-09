package com.kakarote.crm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.log.CrmActivityLog;
import com.kakarote.crm.constant.CrmActivityEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmActivityBO;
import com.kakarote.crm.entity.PO.CrmActivity;
import com.kakarote.crm.entity.VO.CrmActivityVO;
import com.kakarote.crm.service.ICrmActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * crm活动表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
@RestController
@RequestMapping("/crmActivity")
@Api(tags = "活动模块")
@SysLog(logClass = CrmActivityLog.class)
public class CrmActivityController {

    @Autowired
    private ICrmActivityService crmActivityService;

    @PostMapping("/getCrmActivityPageList")
    @ApiOperation("查询跟进记录列表")
    public Result<CrmActivityVO> getCrmActivityPageList(@RequestBody CrmActivityBO crmActivityBO) {
        CrmActivityVO pageList = crmActivityService.getCrmActivityPageList(crmActivityBO);
        return Result.ok(pageList);
    }

    @PostMapping("/addCrmActivityRecord")
    @ApiOperation("添加跟进记录")
    @SysLogHandler(behavior = BehaviorEnum.FOLLOW_UP)
    public Result addCrmActivityRecord(@RequestBody @Valid CrmActivity crmActivity) {
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.parse(crmActivity.getActivityType()), crmActivity.getActivityTypeId());
        if (auth) {
            return R.noAuth();
        }
        crmActivityService.addCrmActivityRecord(crmActivity);
        return R.ok();
    }

    @PostMapping("/addActivity")
    @ApiExplain("添加活动记录(内部使用)")
    public Result addActivity(@RequestParam("type") Integer type, @RequestParam("activityType") Integer activityType, @RequestParam("activityTypeId") Integer activityTypeId) {
        crmActivityService.addActivity(type, CrmActivityEnum.parse(activityType), activityTypeId);
        return R.ok();
    }

    /**
     * 删除跟进记录
     *
     * @param activityId
     */
    @PostMapping("/deleteCrmActivityRecord/{activityId}")
    @ApiOperation("添加跟进记录")
    public Result deleteCrmActivityRecord(@PathVariable("activityId") Integer activityId) {
        crmActivityService.deleteCrmActivityRecord(activityId);
        return Result.ok();
    }

    /**
     * 修改跟进记录
     */
    @PostMapping("/updateActivityRecord")
    @ApiOperation("修改跟进记录")
    public Result<CrmActivity> updateActivityRecord(@RequestBody CrmActivity crmActivity) {
        boolean auth = AuthUtil.isRwAuth(crmActivity.getActivityTypeId(), CrmEnum.parse(crmActivity.getActivityType()));
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        CrmActivity data = crmActivityService.updateActivityRecord(crmActivity);
        return Result.ok(data);
    }

    @PostMapping("/outworkSign")
    @ApiOperation("外勤签到")
    public Result outworkSign(CrmActivity crmActivity) {
        crmActivityService.outworkSign(crmActivity);
        return R.ok();
    }


    @PostMapping("/queryOutworkStats")
    @ApiOperation("app外勤统计")
    public Result<BasePage<JSONObject>> queryOutworkStats(PageEntity pageEntity, String startTime, String endTime) {
        BasePage<JSONObject> basePage = crmActivityService.queryOutworkStats(pageEntity, startTime, endTime);
        return R.ok(basePage);
    }

    @PostMapping("/queryOutworkList")
    @ApiOperation("app外勤详情")
    public Result<BasePage<CrmActivity>> queryOutworkList(PageEntity pageEntity, String startTime, String endTime, Long userId) {
        BasePage<CrmActivity> basePage = crmActivityService.queryOutworkList(pageEntity, startTime, endTime, userId);
        return R.ok(basePage);
    }


    @PostMapping("/queryPictureSetting")
    @ApiOperation("app查询签到照片上传设置")
    public Result<Integer> queryPictureSetting() {
        Integer integer = crmActivityService.queryPictureSetting();
        return R.ok(integer);
    }


    @PostMapping("/setPictureSetting")
    @ApiOperation("app设置签到照片上传")
    public Result setPictureSetting(Integer status) {
        crmActivityService.setPictureSetting(status);
        return R.ok();
    }


    @PostMapping("/deleteOutworkSign")
    @ApiOperation("删除外勤签到")
    public Result deleteOutworkSign(@RequestParam("activityId") Integer activityId) {
        crmActivityService.deleteOutworkSign(activityId);
        return R.ok();
    }

}

