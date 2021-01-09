package com.kakarote.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.oa.entity.BO.QueryEventCrmBO;
import com.kakarote.oa.entity.BO.QueryEventTaskBO;
import com.kakarote.oa.entity.BO.UpdateTypeUserBO;
import com.kakarote.oa.entity.PO.OaCalendarType;
import com.kakarote.oa.entity.VO.EventTaskVO;
import com.kakarote.oa.service.IOaCalendarTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日历类型 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/oaCalendar")
@Api(tags = "日历")
public class OaCalendarTypeController {

    @Autowired
    private IOaCalendarTypeService calendarTypeService;

    @PostMapping("/addOrUpdate")
    @ApiOperation("添加/更新日历类型")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_OTHER_SETTINGS,behavior = BehaviorEnum.UPDATE,object = "日历类型设置",detail = "日历类型设置")
    public Result addOrUpdate(@RequestBody OaCalendarType oaCalendarType) {
        calendarTypeService.addOrUpdateType(oaCalendarType);
        return Result.ok();
    }


    @PostMapping("/delete/{typeId}")
    @ApiOperation("删除日历类型")
    public Result delete(@PathVariable Integer typeId) {
        calendarTypeService.deleteType(typeId);
        return Result.ok();
    }

    @PostMapping("/queryTypeList")
    @ApiOperation("根据查询后台日历类型列表")
    public Result<List<OaCalendarType>> queryTypeList() {
        List<OaCalendarType> calendarTypeList = calendarTypeService.queryTypeList();
        return Result.ok(calendarTypeList);
    }

    /**
     * 根据查询个人日历类型列表
     */
    @PostMapping("/queryTypeListByUser")
    @ApiOperation("根据查询后台日历类型列表")
    public Result<List<OaCalendarType>> queryTypeListByUser(Long userId) {
        if (userId == null) {
            userId = UserUtil.getUserId();
        }
        List<OaCalendarType> calendarTypeList = calendarTypeService.queryTypeListByUser(userId);
        return Result.ok(calendarTypeList);
    }

    @PostMapping("/updateTypeUser")
    @ApiOperation("修改用户展示的日历类型")
    public Result updateTypeUser(@RequestBody UpdateTypeUserBO updateTypeUserBO) {
        calendarTypeService.updateTypeUser(updateTypeUserBO);
        return Result.ok();
    }


    @PostMapping("/eventTask")
    @ApiOperation("日程任务")
    public Result<List<EventTaskVO>> eventTask(@RequestBody QueryEventTaskBO eventTaskBO) {
        List<EventTaskVO> eventTaskVOList = calendarTypeService.eventTask(eventTaskBO);
        return Result.ok(eventTaskVOList);
    }

    /**
     * crm统计--前端展示日历使用
     */
    @PostMapping("/eventCrm")
    @ApiOperation("日程任务")
    public Result<JSONObject> eventCrm(@RequestBody QueryEventCrmBO queryEventCrmBO) {
        JSONObject jsonObject = calendarTypeService.eventCrm(queryEventCrmBO);
        return Result.ok(jsonObject);
    }


    @PostMapping("/eventCustomer")
    @ApiOperation("日程客户列表")
    public Result<BasePage<Map<String,Object>>> eventCustomer(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String,Object>> page = calendarTypeService.eventCustomer(eventCrmPageBO);
        return Result.ok(page);
    }

    @PostMapping("/eventLeads")
    @ApiOperation("日程线索列表")
    public Result<BasePage<Map<String,Object>>> eventLeads(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String,Object>> page = calendarTypeService.eventLeads(eventCrmPageBO);
        return Result.ok(page);
    }

    @PostMapping("/eventBusiness")
    @ApiOperation("日程商机列表")
    public Result<BasePage<Map<String,Object>>> eventBusiness(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String,Object>> page = calendarTypeService.eventBusiness(eventCrmPageBO);
        return Result.ok(page);
    }

    @PostMapping("/eventDealBusiness")
    @ApiOperation("预计成交商机列表")
    public Result<BasePage<Map<String,Object>>> eventDealBusiness(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String,Object>> page = calendarTypeService.eventDealBusiness(eventCrmPageBO);
        return Result.ok(page);
    }

    @PostMapping("/eventContract")
    @ApiOperation("日程客户列表")
    public Result<BasePage<Map<String,Object>>> eventContract(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String,Object>> page = calendarTypeService.eventContract(eventCrmPageBO);
        return Result.ok(page);
    }
}

