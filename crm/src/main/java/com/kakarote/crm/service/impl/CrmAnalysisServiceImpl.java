package com.kakarote.crm.service.impl;

import cn.hutool.core.date.DateUtil;
import com.kakarote.core.feign.crm.entity.CustomerStats;
import com.kakarote.crm.mapper.CustomerStatsMapper;
import com.kakarote.crm.service.ICrmAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    private void initDataTable() {
        customerStatsMapper.createTableForCustomerStats(DEFAULT_TABLE_NAME_PREFIX + DateUtil.thisYear());
    }

    /**
     * 因为数据保
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCustomerStats() {
        Integer startCustomerId = customerStatsMapper.queryStartCustomerId();
        log.info("同步开始，同步起始数据ID：{}", startCustomerId);
        List<CustomerStats> customerStatsList = customerStatsMapper.selectCustomerStats(startCustomerId);
        if (customerStatsList.size() == 0) {
            log.info("没有可供同步的数据，同步结束");
            return true;
        }
        Map<String, List<CustomerStats>> customerStatsGroupMap = customerStatsList.stream()
                .collect(Collectors.groupingBy(x -> x.getCreateDate().substring(0, 4)));

        customerStatsGroupMap.forEach((key, value) -> {
            if (key != null) {
                log.info("同步数据开始，当前年份：{}", key);
                String tableName = DEFAULT_TABLE_NAME_PREFIX + key;
                customerStatsMapper.createTableForCustomerStats(tableName);
                this.batchInsertData(tableName, value);
                log.info("同步数据结束，同步数量：{}", value.size());
            }
        });

        Integer lastCustomerId = customerStatsMapper.queryLastCustomerId(startCustomerId);
        customerStatsMapper.saveStatsInfo(lastCustomerId, customerStatsList.size());
        return false;

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
     * @return java.util.Map<java.lang.String                                                               ,                                                                                                                               java.lang.Object>
     * @date 2020/9/17 13:46
     **/
    private Map<String, Object> supplementMapInfo(String tableName, List<CustomerStats> customerStatsList) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("tableName", tableName);
        map.put("customerStatsList", customerStatsList);
        return map;
    }

}
