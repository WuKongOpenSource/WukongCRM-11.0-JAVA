package com.kakarote.bi.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.common.MonthEnum;
import com.kakarote.bi.mapper.BiCustomerMapper;
import com.kakarote.bi.mapper.BiRankMapper;
import com.kakarote.bi.service.BiRankService;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.utils.BiTimeUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BiRankServiceImpl implements BiRankService {

    @Autowired
    private BiRankMapper biRankMapper;

    @Autowired
    private BiCustomerMapper biCustomerMapper;

    /**
     * 城市分布分析
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> addressAnalyse(BiParams biParams) {
        Integer menuId = 124;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        String[] addressArr = BiTimeUtil.getAddress();
        Map<String, Object> map = record.toMap();
        map.put("address", addressArr);
        return biRankMapper.addressAnalyse(map);
    }

    /**
     * 城市分布分析
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> portrait(BiParams biParams) {
        Integer menuId = 124;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        Integer status = BiTimeUtil.analyzeType(biParams.getType());
        Map<String, Object> map = new HashMap<>();
        map.put("userIds", record.getUserIds());
        map.put("type", status);
        map.put("startTime", biParams.getStartTime());
        map.put("endTime", biParams.getEndTime());
        return biRankMapper.portrait(map);
    }

    /**
     * 城市级别分析
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> portraitLevel(BiParams biParams) {
        Integer menuId = 124;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        Integer status = BiTimeUtil.analyzeType(biParams.getType());
        Map<String, Object> map = new HashMap<>();
        map.put("userIds", record.getUserIds());
        map.put("type", status);
        map.put("startTime", biParams.getStartTime());
        map.put("endTime", biParams.getEndTime());
        return biRankMapper.portraitLevel(map);
    }

    /**
     * 城市来源分析
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> portraitSource(BiParams biParams) {
        Integer menuId = 124;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        Integer status = BiTimeUtil.analyzeType(biParams.getType());
        Map<String, Object> map = new HashMap<>();
        map.put("userIds", record.getUserIds());
        map.put("type", status);
        map.put("startTime", biParams.getStartTime());
        map.put("endTime", biParams.getEndTime());
        return biRankMapper.portraitSource(map);
    }

    /**
     * 产品分类销量分析
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> contractProductRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        Integer status = BiTimeUtil.analyzeType(biParams.getType());
        Map<String, Object> map = new HashMap<>();
        map.put("userIds", record.getUserIds());
        map.put("type", status);
        map.put("startTime", biParams.getStartTime());
        map.put("endTime", biParams.getEndTime());
        return biRankMapper.contractProductRanKing(map);
    }

    /**
     * 合同金额排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> contractRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.contractRanKing(record.toMap());
    }

    /**
     * 回款金额排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> receivablesRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.receivablesRanKing(record.toMap());
    }

    /**
     * 签约合同排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> contractCountRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.contractCountRanKing(record.toMap());
    }

    private List<Map<String,String>> getMonthMap(Date beginDate, Date endDate){
        List<Map<String,String>> monthMap = new ArrayList<>();
        Integer beginYear = DateUtil.year(beginDate);
        Integer endYear = DateUtil.year(endDate);
        Integer beginMonth = DateUtil.month(beginDate);
        Integer endMonth = DateUtil.month(endDate);
        for (int i = 0; i <= endYear - beginYear; i++) {
            int year = beginYear + i;
            StringBuilder stringBuilder = new StringBuilder("(");
            Map<String,String> map = new HashMap<>();
            int startNum = i == 0 ? beginMonth : 0;
            for (int j = startNum; j < 12; j++) {
                stringBuilder.append("ifnull(").append(MonthEnum.valueOf(j+1)).append(",0) +");
                if (endYear.equals(year) && endMonth.equals(j)){
                    break;
                }
            }
            String month = stringBuilder.toString();
            map.put("year", Integer.toString(year));
            map.put("month",month.substring(0,month.length() - 1) + ")");
            monthMap.add(map);
        }
        return monthMap;
    }

    /**
     * 产品销量排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> productCountRanKing(BiParams biParams) {
        Integer menuId = 124;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.productCountRanKing(record.toMap());
    }

    /**
     * 新增客户数排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> customerCountRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.customerCountRanKing(record.toMap());
    }

    /**
     * 新增联系人排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> contactsCountRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.contactsCountRanKing(record.toMap());
    }

    @Override
    public JSONObject ranking(BiParams biParams) {
        //合同、回款金额排行筛选当天，按当月算，跨月或跨年就按照这几个月或年的目标之和计算，需特殊处理
        if (biParams.getLabel() == 1 || biParams.getLabel() == 2) {
            if (StrUtil.isNotEmpty(biParams.getType()) && Arrays.asList("today", "yesterday", "week", "lastWeek").contains(biParams.getType())) {
                String startTime = DateUtil.format(DateUtil.beginOfMonth(DateUtil.date()), "yyyy-MM-dd");
                String endTime = DateUtil.format(DateUtil.endOfMonth(DateUtil.date()), "yyyy-MM-dd");
                biParams.setType(null);
                biParams.setStartTime(startTime);
                biParams.setEndTime(endTime);
            }
        }
        if (StrUtil.isNotEmpty(biParams.getStartTime()) && StrUtil.isNotEmpty(biParams.getEndTime())) {
            String startTime = biParams.getStartTime();
            String endTime = biParams.getEndTime();
            if (biParams.getLabel() == 1 || biParams.getLabel() == 2) {
                Date start = DateUtil.parseDate(biParams.getStartTime());
                Date end = DateUtil.parseDate(biParams.getEndTime());
                Integer startMonth = Integer.valueOf(DateUtil.format(start, "yyyyMM"));
                Integer endMonth = Integer.valueOf(DateUtil.format(end, "yyyyMM"));
                if (startMonth.equals(endMonth)) {
                    startTime = DateUtil.format(DateUtil.beginOfMonth(start), "yyyy-MM-dd");
                    endTime = DateUtil.format(DateUtil.endOfMonth(end), "yyyy-MM-dd");
                } else {
                    startTime = DateUtil.format(DateUtil.beginOfMonth(DateUtil.parseDate(startTime)), "yyyy-MM-dd");
                    endTime = DateUtil.format(DateUtil.endOfMonth(DateUtil.parseDate(endTime)), "yyyy-MM-dd");
                }
            }
            biParams.setStartTime(startTime);
            biParams.setEndTime(endTime);
        }
//        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeTime(biParams);
//        Integer menuId = 126;
//        BiTimeUtil.dataFilter(menuId, record.getUserIds());
        Map<String, Object> self = new HashMap<>();
        List<JSONObject> recordList;
        switch (biParams.getLabel()) {
            case 1:
                recordList = contractRanKing1(biParams);
                break;
            case 2:
                recordList = receivablesRanKing1(biParams);
                break;
            case 3:
                recordList = contractCountRanKing(biParams);
                break;
            case 4:
                recordList = customerCountRanKing(biParams);
                break;
            case 5:
                recordList = contactsCountRanKing(biParams);
                break;
            case 8:
                recordList = recordCountRanKing(biParams);
                break;
            default:
                recordList = new ArrayList<>();
                break;
        }
        for (int i = 0; i < recordList.size(); i++) {
            JSONObject user = recordList.get(i);
            if (UserUtil.getUserId().equals(user.getLong("userId"))) {
                self = user.fluentPut("sort", i + 1).getInnerMap();
            }
        }
        return new JSONObject().fluentPut("self", self).fluentPut("ranking", recordList);
    }

    private List<JSONObject> receivablesRanKing1(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        List<Map<String, String>> monthMap = getMonthMap(record.getBeginDate(), record.getEndDate());
        return biRankMapper.receivablesRanKing1(record.toMap(),monthMap);
    }

    private List<JSONObject> contractRanKing1(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        List<Map<String, String>> monthMap = getMonthMap(record.getBeginDate(), record.getEndDate());
        return biRankMapper.contractRanKing1(record.toMap(),monthMap);
    }

    /**
     * 跟进客户数排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> customerGenjinCountRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.customerGenjinCountRanKing(record.toMap());
    }

    /**
     * 跟进次数排行榜
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> recordCountRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.recordCountRanKing(record.toMap());
    }

    /**
     * 出差次数排行
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> travelCountRanKing(BiParams biParams) {
        Integer menuId = 126;
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            return new ArrayList<>();
        }
        return biRankMapper.travelCountRanKing(record.toMap());
    }
}
