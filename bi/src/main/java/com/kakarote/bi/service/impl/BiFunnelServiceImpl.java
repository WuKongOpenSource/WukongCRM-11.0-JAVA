package com.kakarote.bi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.common.BiPatch;
import com.kakarote.bi.mapper.BiFunnelMapper;
import com.kakarote.bi.service.BiFunnelService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.utils.BiTimeUtil;
import com.kakarote.core.utils.UserCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BiFunnelServiceImpl implements BiFunnelService {

    @Autowired
    private BiFunnelMapper biFunnelMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CrmService crmService;

    /**
     * 销售漏斗
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> sellFunnel(BiParams biParams) {
        Integer menuId = 103;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<JSONObject> list = new ArrayList<>();
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return list;
        }
        Map<String, Object> map = record.toMap();
        map.put("typeId", biParams.getTypeId());
        list = biFunnelMapper.sellFunnel(map);
        return list;
    }

    /**
     * 新增商机分析图
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> addBusinessAnalyze(BiParams biParams) {
        Integer menuId = 103;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getCycleNum();
        List<Long> userIds = record.getUserIds();
        Integer beginTime = record.getBeginTime();
        List<JSONObject> list = new ArrayList<>();
        if (userIds.size() == 0) {
            return list;
        }
        List<Integer> timeList = new ArrayList<>();
        for (int i = 1; i <= cycleNum; i++) {
            timeList.add(beginTime);
            beginTime = BiTimeUtil.estimateTime(beginTime);
        }
        Map<String, Object> map = record.toMap();
        map.put("timeList", timeList);
        List<JSONObject> jsonList = biFunnelMapper.addBusinessAnalyze(map);
        BiPatch.supplementJsonList(jsonList,"type",timeList,"businessMoney","businessNum");
        return jsonList;
    }

    /**
     * 新增商机分析表
     *
     * @param biParams params
     * @return data
     */
    @Override
    public BasePage<JSONObject> sellFunnelList(BiParams biParams) {
        Integer menuId = 103;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        BasePage<JSONObject> parse = new BasePage<>(biParams.getPage(), biParams.getLimit());
        BasePage<JSONObject> page = biFunnelMapper.sellFunnelList(parse, record.getUserIds(), record.getSqlDateFormat(), biParams.getType());
        page.getList().forEach(object -> {
            if (object.getLong("createUserId") != null) {
                object.put("createUserName", UserCacheUtil.getUserName(object.getLong("createUserId")));
            }
            if (object.getLong("ownerUserId") != null) {
                object.put("ownerUserName", UserCacheUtil.getUserName(object.getLong("ownerUserId")));
            }
            if (object.getInteger("customerId") != null) {
                List<SimpleCrmEntity> crmEntities = crmService.queryCustomerInfo(Collections.singleton(object.getInteger("customerId"))).getData();
                object.put("customerName", crmEntities.size() > 0 ? crmEntities.get(0).getName() : "");
            }
        });
        return page;
    }

    /**
     * 商机转化率分析
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> win(BiParams biParams) {
        Integer menuId = 103;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getCycleNum();
        List<Long> userIds = record.getUserIds();
        Integer beginTime = record.getBeginTime();
        List<JSONObject> list = new ArrayList<>();
        if (userIds.size() == 0) {
            return list;
        }
        List<Integer> timeList = new ArrayList<>();
        for (int i = 1; i <= cycleNum; i++) {
            timeList.add(beginTime);
            beginTime = BiTimeUtil.estimateTime(beginTime);
        }
        Map<String, Object> map = record.toMap();
        map.put("timeList", timeList);
        List<JSONObject> jsonList = biFunnelMapper.win(map);
        BiPatch.supplementJsonList(jsonList,"type",timeList,"businessNum","businessEnd","proportion");
        return jsonList;
    }
}
