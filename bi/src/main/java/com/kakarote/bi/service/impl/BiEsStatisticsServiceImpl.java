package com.kakarote.bi.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.service.BiEsStatisticsService;
import com.kakarote.core.utils.BiTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.valuecount.ParsedValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JiaS
 * @date 2020/12/1
 */
@Slf4j
@Service
public class BiEsStatisticsServiceImpl implements BiEsStatisticsService {

    private static final String DEFAULT_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    private static final String DEFAULT_ES_INDEX = "wk_single_customer";

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public List<JSONObject> getStatisticsCustomerInfo(BiTimeUtil.BiTimeEntity timeEntity, boolean isNeedDealNum) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("createTime");
        rangeQuery.gte(DateUtil.format(timeEntity.getBeginDate(), DEFAULT_FORMAT_STR));
        rangeQuery.lte(DateUtil.format(timeEntity.getEndDate(), DEFAULT_FORMAT_STR));
        queryBuilder.filter(rangeQuery);
        if (isNeedDealNum) {
            queryBuilder.filter(QueryBuilders.termQuery("dealStatus", "1"));
        }

        queryBuilder.filter(QueryBuilders.termsQuery("ownerUserId", timeEntity.getUserIds()));
        //根据时间分组统计总数
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        String dateFormat = timeEntity.getDateFormat();
        DateHistogramInterval dateHistogramInterval = this.getDateHistogramInterval(dateFormat);
        if (dateHistogramInterval == null){
            //默认按月
            dateFormat = "yyyyMM";
            dateHistogramInterval = DateHistogramInterval.MONTH;
        }
        DateHistogramAggregationBuilder fieldBuilder = AggregationBuilders.dateHistogram("groupCreateDate").field("createTime").dateHistogramInterval(dateHistogramInterval);
        ValueCountAggregationBuilder customerNumBuilder = AggregationBuilders.count("countCustomerNum").field("customerId");
        sourceBuilder.aggregation(fieldBuilder.subAggregation(customerNumBuilder));
        //分组查询拼接求各组数
        SearchRequest searchRequest = new SearchRequest(DEFAULT_ES_INDEX);
        searchRequest.types("_doc");
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = elasticsearchRestTemplate.getClient().search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            //es请求失败
            log.info("es请求错误：" + e.getMessage());
            return jsonObjectList;
        }
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> aggregationMap = aggregations.asMap();

        Histogram histogram = (Histogram) aggregationMap.get("groupCreateDate");
        for (Histogram.Bucket bucket : histogram.getBuckets()) {
            JSONObject jsonObject = new JSONObject();
            //设置有数据的时间
            String keyAsString = bucket.getKeyAsString();
            String type = DateUtil.parse(keyAsString, DEFAULT_FORMAT_STR).toString(dateFormat);
            ParsedValueCount caseNumCount = bucket.getAggregations().get("countCustomerNum");
            Integer num = Integer.valueOf(Long.toString(caseNumCount.getValue()));
            jsonObject.fluentPut("type", type).fluentPut("customerNum", num);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }


    /**
     * 处理分组日期
     * @date 2020/12/1 13:45
     * @param dateFormat
     * @return org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval
     **/
    private DateHistogramInterval getDateHistogramInterval(String dateFormat) {
        DateHistogramInterval dateHistogramInterval;
        switch (dateFormat) {
            case "yyyy":
                dateHistogramInterval = DateHistogramInterval.YEAR;
                break;
            case "yyyyMM":
                dateHistogramInterval = DateHistogramInterval.MONTH;
                break;
            case "yyyyMMdd":
                dateHistogramInterval = DateHistogramInterval.DAY;
                break;
            default:
                dateHistogramInterval = null;
                break;
        }
        return dateHistogramInterval;
    }


    @Override
    public List<JSONObject> mergeJsonObjectList(List<JSONObject> customerNumList, List<JSONObject> dealNumList) {
        if (CollUtil.isEmpty(customerNumList)) {
            return new ArrayList<>();
        }
        if (dealNumList == null) {
            dealNumList = new ArrayList<>();
        }
        for (JSONObject jsonObject : customerNumList) {
            String type = jsonObject.getString("type");
            Integer dealCustomerNum = 0;
            for (JSONObject dealNumObject : dealNumList) {
                if (type.equals(dealNumObject.getString("type"))) {
                    dealCustomerNum = dealNumObject.getInteger("customerNum");
                    break;
                }
            }
            jsonObject.put("dealCustomerNum", dealCustomerNum);
        }
        return customerNumList;
    }

}
