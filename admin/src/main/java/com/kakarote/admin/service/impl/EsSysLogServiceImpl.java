package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.admin.entity.BO.QuerySysLogBO;
import com.kakarote.admin.entity.PO.AdminUser;
import com.kakarote.admin.entity.PO.LoginLog;
import com.kakarote.admin.entity.PO.SysLog;
import com.kakarote.admin.service.IAdminUserService;
import com.kakarote.admin.service.ISysLogService;
import com.kakarote.core.entity.BasePage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author hmb
 * @since 2020-11-25
 */
@Service
@ConditionalOnClass(RestHighLevelClient.class)
@Slf4j
public class EsSysLogServiceImpl implements ISysLogService {


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private IAdminUserService adminUserService;

    private static final Integer SYS_LOG_TYPE = 1;

    private static final Integer LOGIN_LOG_TYPE = 2;

    private static String getIndexAliases(Integer logType){
        String indexAliases;
        if (logType.equals(SYS_LOG_TYPE)){
            indexAliases = "wk-sys-log";
        }else {
            indexAliases = "wk-login-log";
        }
        return indexAliases;
    }

    private static final String SYS_LOG_INDEX_SOURCE =
            "{\n" +
                    "  \"mappings\": {\n" +
                    "    \"properties\": {\n" +
                    "      \"className\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"methodName\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"args\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"model\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"subModel\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"object\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"behavior\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"detail\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"ipAddress\": {\n" +
                    "        \"type\": \"ip\"\n" +
                    "      },\n" +
                    "      \"userId\": {\n" +
                    "        \"type\": \"long\"\n" +
                    "      },\n" +
                    "      \"realname\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"createTime\": {\n" +
                    "        \"type\": \"date\",\n" +
                    "        \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"aliases\": {\n" +
                    "    \"wk-sys-log\": {}\n" +
                    "  }\n" +
                    "}";
    private static final String LOGIN_LOG_INDEX_SOURCE =
            "{\n" +
                    "  \"mappings\": {\n" +
                    "    \"properties\": {\n" +
                    "      \"userId\": {\n" +
                    "        \"type\": \"long\"\n" +
                    "      },\n" +
                    "      \"realname\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"loginTime\": {\n" +
                    "        \"type\": \"date\",\n" +
                    "        \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"\n" +
                    "      },\n" +
                    "      \"ipAddress\": {\n" +
                    "        \"type\": \"ip\"\n" +
                    "      },\n" +
                    "      \"loginAddress\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"deviceType\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"core\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"platform\": {\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      },\n" +
                    "      \"authResult\": {\n" +
                    "        \"type\": \"integer\"\n" +
                    "      },\n" +
                    "      \"failResult\":{\n" +
                    "        \"type\": \"keyword\"\n" +
                    "      }" +
                    "    }\n" +
                    "  },\n" +
                    "  \"aliases\": {\n" +
                    "    \"wk-login-log\": {}\n" +
                    "  }\n" +
                    "}";

    @Resource
    private ThreadPoolTaskExecutor adminThreadPoolExecutor;

    @Override
    public void saveSysLog(SysLog sysLog) {
        String index = getSysLogIndex();
        adminThreadPoolExecutor.execute(() -> {
            IndexRequest indexRequest = new IndexRequest(index, "_doc");
            Map<String, Object> source = BeanUtil.beanToMap(sysLog);
            source.put("createTime", DateUtil.formatDateTime(sysLog.getCreateTime()));
            source.remove("id");
            indexRequest.source(source);
            try {
                IndexResponse index1 = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                System.out.println(index1.status());
            } catch (IOException e) {
                log.error("保存系统日志异常,msg:{}", e.getMessage());
            }
        });
    }

    @Override
    public void saveLoginLog(LoginLog loginLog) {
        String index = getLoginLogIndex();
        adminThreadPoolExecutor.execute(() -> {
            if (loginLog.getAuthResult() == 1){
                adminUserService.lambdaUpdate()
                        .set(AdminUser::getLastLoginTime,loginLog.getLoginTime())
                        .set(AdminUser::getLastLoginIp,loginLog.getIpAddress())
                        .eq(AdminUser::getUserId,loginLog.getUserId())
                        .update();
            }
            IndexRequest indexRequest = new IndexRequest(index, "_doc");
            Map<String, Object> source = BeanUtil.beanToMap(loginLog);
            source.put("loginTime", DateUtil.formatDateTime(loginLog.getLoginTime()));
            source.remove("id");
            indexRequest.source(source);
            try {
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("保存登录日志异常,msg:{}", e.getMessage());
            }
        });
    }

    private String getSysLogIndex() {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String index = getIndexAliases(SYS_LOG_TYPE) + "-" + month;
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        try {
            boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            if (!exists) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
                createIndexRequest.source(SYS_LOG_INDEX_SOURCE, XContentType.JSON);
                restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            log.error("创建系统日志索引异常,msg:{}", e.getMessage());
        }
        return index;
    }

    private String getLoginLogIndex() {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String index = getIndexAliases(LOGIN_LOG_TYPE) + "-" + month;
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        try {
            boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            if (!exists) {
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
                createIndexRequest.source(LOGIN_LOG_INDEX_SOURCE, XContentType.JSON);
                    restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            log.error("创建登录日志索引异常,msg:{}", e.getMessage());
        }
        return index;
    }

    @Override
    public BasePage<SysLog> querySysLogPageList(QuerySysLogBO querySysLogBO) {
        getSysLogIndex();
        BasePage<SysLog> page = new BasePage<>();
        List<SysLog> logList = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = createSearchSourceBuilder(querySysLogBO);
        searchSourceBuilder.sort("createTime", SortOrder.DESC);
        searchSourceBuilder.fetchSource(null, new String[]{"args","className","methodName"});
        try {
            SearchRequest searchRequest = new SearchRequest(getIndexAliases(SYS_LOG_TYPE));
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                logList.add(BeanUtil.mapToBean(sourceAsMap, SysLog.class, true));
            }
            page.setTotal(hits.getTotalHits());
            page.setCurrent(querySysLogBO.getPage());
            page.setList(logList);
        } catch (IOException e) {
            log.error("查询系统日志异常,msg:{}", e.getMessage());
        }
        return page;
    }

    @Override
    public BasePage<LoginLog> queryLoginLogPageList(QuerySysLogBO querySysLogBO) {
        getLoginLogIndex();
        BasePage<LoginLog> page = new BasePage<>();
        List<LoginLog> logList = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = createSearchSourceBuilder(querySysLogBO);
        searchSourceBuilder.sort("loginTime", SortOrder.DESC);
        try {
            SearchRequest searchRequest = new SearchRequest(getIndexAliases(LOGIN_LOG_TYPE));
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                logList.add(BeanUtil.mapToBean(sourceAsMap, LoginLog.class, true));
            }
            page.setTotal(hits.getTotalHits());
            page.setCurrent(querySysLogBO.getPage());
            page.setList(logList);
        } catch (IOException e) {
            log.error("查询系统日志异常,msg:{}", e.getMessage());
        }
        return page;
    }

    private SearchSourceBuilder createSearchSourceBuilder(QuerySysLogBO querySysLogBO) {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        //query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (querySysLogBO.getType() != null){
            if (querySysLogBO.getType() == 1){
                boolQueryBuilder.mustNot(QueryBuilders.termQuery("model","admin"));
            }else if (querySysLogBO.getType() == 2){
                boolQueryBuilder.must(QueryBuilders.termQuery("model","admin"));
            }
        }
        if (querySysLogBO.getStartTime() != null && querySysLogBO.getEndTime() != null) {
            RangeQueryBuilder createTimeQuery = QueryBuilders.rangeQuery("createTime")
                    .gte(querySysLogBO.getStartTime())
                    .lte(querySysLogBO.getEndTime());
            boolQueryBuilder.filter(createTimeQuery);
        }
        if (CollUtil.isNotEmpty(querySysLogBO.getUserIds())) {
            TermsQueryBuilder userIdQuery = QueryBuilders.termsQuery("userId", querySysLogBO.getUserIds());
            boolQueryBuilder.filter(userIdQuery);
        }
        if (StrUtil.isNotEmpty(querySysLogBO.getModel())) {
            TermQueryBuilder modelQuery = QueryBuilders.termQuery("model", querySysLogBO.getModel());
            boolQueryBuilder.filter(modelQuery);
        }
        if (CollUtil.isNotEmpty(querySysLogBO.getSubModelLabels())) {
            TermsQueryBuilder modelQuery = QueryBuilders.termsQuery("subModelLabel", querySysLogBO.getSubModelLabels());
            boolQueryBuilder.filter(modelQuery);
        }
        //分页
        if (querySysLogBO.getPage() <= 100) {
            if (querySysLogBO.getPageType().equals(1)) {
                // 设置起止和结束
                searchSourceBuilder.from((querySysLogBO.getPage() - 1) * querySysLogBO.getLimit());
            }
        }
        //设置查询条数
        searchSourceBuilder.size(querySysLogBO.getLimit());
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }
}
