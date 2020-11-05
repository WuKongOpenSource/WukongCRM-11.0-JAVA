package com.kakarote.oa.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.oa.entity.BO.QueryEventListBO;
import com.kakarote.oa.entity.PO.OaEvent;
import com.kakarote.oa.mapper.OaEventMapper;
import com.kakarote.oa.mapper.OaEventNoticeMapper;
import com.kakarote.oa.service.IOaEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/oaEventJob")
public class OaEventNoticeJobController {

    @Autowired
    private OaEventNoticeMapper oaEventNoticeMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private IOaEventService oaEventService;

    @Autowired
    private OaEventMapper eventMapper;

    @Autowired
    private Redis redis;

    /**
     * 定时器日程提醒
     */
    @PostMapping("/eventNoticeCron")
    public Result eventNoticeCron(){
        DateTime nowDate = DateUtil.date();
        long startTime = DateUtil.beginOfDay(nowDate).getTime();
        long endTime = DateUtil.endOfDay(nowDate).getTime();
        QueryEventListBO queryEventListBO = new QueryEventListBO();
        queryEventListBO.setStartTime(startTime);
        queryEventListBO.setEndTime(endTime);
        List<OaEvent> oaEventList = eventMapper.queryList(queryEventListBO);
        oaEventService.eventNotice(oaEventList);
        return Result.ok();
    }
}
