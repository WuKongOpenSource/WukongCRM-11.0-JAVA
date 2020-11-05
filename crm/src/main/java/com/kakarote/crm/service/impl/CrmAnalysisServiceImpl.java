package com.kakarote.crm.service.impl;

import com.kakarote.core.feign.crm.entity.CustomerStats;
import com.kakarote.core.redis.Redis;
import com.kakarote.crm.mapper.CustomerStatsMapper;
import com.kakarote.crm.service.ICrmAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2020/9/16
 */
@Slf4j
@Service
public class CrmAnalysisServiceImpl implements ICrmAnalysisService {

    private static final String DEFAULT_TABLE_NAME_PREFIX = "wk_crm_customer_stats_";
    private static final String STATS_MAX_NUM_NAME = "customerStatsMaxNum";
    private static final String ALREADY_EXIST_YEAR_NAME = "customerStatsAlreadyExistYear";
    private static final Long DEFAULT_EACH_NUM = 100000L;
    private static final Integer BATCH_COUNT = 500;

    @Autowired
    private CustomerStatsMapper customerStatsMapper;

    @Autowired
    private Redis redis;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCustomerStats() {
        Long maxCustomerId = customerStatsMapper.maxCustomerId();
        if (maxCustomerId == null) {
            //无数据
            log.info("没有数据可以同步");
            return true;
        }
        Long maxNum = redis.get(STATS_MAX_NUM_NAME);
        List<String> yearList = redis.get(ALREADY_EXIST_YEAR_NAME);
        if (maxNum == null || yearList == null) {
            //首次
            Long abortNum;
            List<CustomerStats> customerStatsList;
            if (DEFAULT_EACH_NUM >= maxCustomerId) {
                customerStatsList = customerStatsMapper.selectCustomerStats(0L, maxCustomerId);
                abortNum = maxCustomerId;
            } else {
                customerStatsList = customerStatsMapper.selectCustomerStats(0L, DEFAULT_EACH_NUM);
                abortNum = DEFAULT_EACH_NUM;
            }
            Map<String, List<CustomerStats>> customerStatsGroupMap = customerStatsList.stream()
                    .collect(Collectors.groupingBy(x -> x.getCreateDate().substring(0, 4)));
            List<String> years = new ArrayList<>();
            customerStatsGroupMap.forEach((key, value) -> {
                if (key != null) {
                    String tableName = DEFAULT_TABLE_NAME_PREFIX + key;
                    customerStatsMapper.createTableForCustomerStats(tableName);
                    years.add(key);
                    this.batchInsertData(tableName, value);
                }
            });
            log.info("本次同步截止到【0-{}】，已完成",abortNum);
            redis.set(STATS_MAX_NUM_NAME, abortNum);
            redis.set(ALREADY_EXIST_YEAR_NAME, years);
            return true;
        }
        //每次
        Long newMaxNum = maxNum + DEFAULT_EACH_NUM;
        List<CustomerStats> customerStatsList;
        Long abortNum;
        if (newMaxNum >= maxCustomerId) {
            customerStatsList = customerStatsMapper.selectCustomerStats(maxNum + 1, maxCustomerId);
            abortNum = maxCustomerId;
        } else {
            customerStatsList = customerStatsMapper.selectCustomerStats(maxNum + 1, newMaxNum);
            abortNum = newMaxNum;
        }
        if (customerStatsList.isEmpty()){
            //无最新数据
            log.info("本次同步截止到【0-{}】，同步已完成100%", abortNum);
            return true;
        }
        Map<String, List<CustomerStats>> customerStatsGroupMap = customerStatsList.stream()
                .collect(Collectors.groupingBy(x -> x.getCreateDate().substring(0, 4)));
        customerStatsGroupMap.forEach((key, value) -> {
            if (key != null) {
                String tableName = DEFAULT_TABLE_NAME_PREFIX + key;
                if (yearList.contains(key)) {
                    this.batchInsertData(tableName, value);
                } else {
                    customerStatsMapper.createTableForCustomerStats(tableName);
                    yearList.add(key);
                    this.batchInsertData(tableName, value);
                }
            }
        });
        log.info("本次同步截止到【0-{}】，已完成", abortNum);
        redis.set(STATS_MAX_NUM_NAME, abortNum);
        redis.set(ALREADY_EXIST_YEAR_NAME, yearList);
        return true;
    }


    /**
     * 分批插入数据
     *
     * @param tableName
     * @param customerStatsList
     * @return boolean
     * @date 2020/9/18 10:13
     **/
    private boolean batchInsertData(String tableName, List<CustomerStats> customerStatsList) {
        if (customerStatsList.isEmpty()) {
            log.warn("this collect is empty !");
            return false;
        }
        int statsSize = customerStatsList.size();
        if (statsSize <= BATCH_COUNT) {
            Map<String, Object> map = supplementMapInfo(tableName, customerStatsList);
            customerStatsMapper.saveBatchByCustomerStats(map);
        } else {
            int lastSize = 0;
            for (int i = 1; i <= statsSize; i++) {
                if (i % BATCH_COUNT == 0 || i == statsSize) {
                    List<CustomerStats> customerStats = customerStatsList.subList(lastSize, i);
                    Map<String, Object> map = supplementMapInfo(tableName, customerStats);
                    customerStatsMapper.saveBatchByCustomerStats(map);
                    lastSize = i;
                }
            }
        }
        return true;
    }


    /**
     * 组装map
     *
     * @param tableName
     * @param customerStatsList
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @date 2020/9/17 13:46
     **/
    private Map<String, Object> supplementMapInfo(String tableName, List<CustomerStats> customerStatsList) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("tableName", tableName);
        map.put("customerStatsList", customerStatsList);
        return map;
    }

}
