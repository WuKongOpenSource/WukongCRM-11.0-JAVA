package com.kakarote.crm.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.CrmSceneEnum;
import com.kakarote.crm.entity.BO.CrmCustomerPoolBO;
import com.kakarote.crm.entity.BO.CrmFieldVerifyBO;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.PO.CrmScene;
import com.kakarote.crm.entity.PO.CrmTeamMembers;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.kakarote.core.servlet.ApplicationContextHolder.getBean;

/**
 * @author zhangzhiwei
 * pageElasticsearch
 */
public interface CrmPageService {

    Logger log = LoggerFactory.getLogger(CrmPageService.class);

    /**
     * 查询列表页
     *
     * @param crmSearchBO 业务查询对象
     * @return data
     */
    default BasePage<Map<String, Object>> queryList(CrmSearchBO crmSearchBO, boolean isExcel) {
        SearchRequest searchRequest = new SearchRequest(getIndex());
        searchRequest.types(getDocType());
        searchRequest.source(createSourceBuilder(crmSearchBO));
        try {
            SearchResponse searchResponse = getRestTemplate().getClient().search(searchRequest, RequestOptions.DEFAULT);
            List<Map<String, Object>> mapList = new ArrayList<>();
            List<CrmModelFiledVO> voList = queryDefaultField();
            SearchHit[] hits = searchResponse.getHits().getHits();
            if (crmSearchBO.getPage() >= 100) {
                if (hits.length > 0) {
                    //处理searchAfter
                    SearchHit searchHit = hits[hits.length - 1];
                    Redis redis = BaseUtil.getRedis();
                    String searchAfterKey = "es:search:" + UserUtil.getUserId().toString();
                    if (crmSearchBO.getPage() == 100) {
                        redis.del(searchAfterKey);
                    }
                    int page = redis.getLength(searchAfterKey).intValue();
                    if (crmSearchBO.getPage() - 100 >= page) {
                        redis.rpush(searchAfterKey, searchHit.getSortValues());
                    }
                    //缓存一个小时
                    redis.expire(searchAfterKey, 3600);
                }
            }
            if (isExcel) {
                while (hits.length !=0 && hits.length % 10000 == 0) {
                    hits = getAllData(searchRequest, hits);
                }
            }
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                sourceAsMap.put(getLabel().getTable() + "Id", Integer.valueOf(hit.getId()));
                mapList.add(parseMap(sourceAsMap, voList));
            }
            BasePage<Map<String, Object>> basePage = new BasePage<>();
            basePage.setList(mapList);
            basePage.setTotal(searchResponse.getHits().getTotalHits());
            basePage.setCurrent(crmSearchBO.getPage());
            return basePage;
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    default SearchHit[] getAllData(SearchRequest searchRequest, SearchHit[] hits) {
        try {
            //处理searchAfter
            SearchHit searchHit = hits[hits.length - 1];
            searchRequest.source().searchAfter(searchHit.getSortValues());
            SearchResponse afterResult = getRestTemplate().getClient().search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] afterHits = afterResult.getHits().getHits();
            hits = ArrayUtils.addAll(hits, afterHits);
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
        return hits;
    }

    default Map<String, Object> parseMap(Map<String, Object> objectMap, List<CrmModelFiledVO> fieldList) {
        fieldList.forEach(field -> {
            if (!objectMap.containsKey(field.getFieldName())) {
                objectMap.put(field.getFieldName(), "");
            }
            if (field.getFieldType() == 0 && field.getType().equals(FieldEnum.USER.getType())) {
                if (ObjectUtil.isNotEmpty(objectMap.get(field.getFieldName()))) {
                    List<Long> ids = Convert.toList(Long.class, objectMap.get(field.getFieldName()));
                    objectMap.put(field.getFieldName(), ids.stream().map(UserCacheUtil::getUserName).collect(Collectors.joining(Const.SEPARATOR)));
                } else {
                    objectMap.put(field.getFieldName(), "");
                }
            }
            if (field.getFieldType() == 0 && field.getType().equals(FieldEnum.STRUCTURE.getType())) {
                if (ObjectUtil.isNotEmpty(objectMap.get(field.getFieldName()))) {
                    List<Integer> ids = Convert.toList(Integer.class, objectMap.get(field.getFieldName()));
                    objectMap.put(field.getFieldName(), ids.stream().map(UserCacheUtil::getDeptName).collect(Collectors.joining(",")));
                } else {
                    objectMap.put(field.getFieldName(), "");
                }
            }
            if (field.getFieldType() == 0 && Arrays.asList(3, 8, 9, 11).contains(field.getType())) {
                Object value = objectMap.get(field.getFieldName());
                if (ObjectUtil.isNotEmpty(value)) {
                    objectMap.put(field.getFieldName(), CollUtil.join(Convert.toList(String.class, value), ","));
                } else {
                    objectMap.put(field.getFieldName(), "");
                }
            }
            if (getBean(FieldService.class).equalsByType(field.getType())) {
                Object value = objectMap.get(field.getFieldName());
                if (ObjectUtil.isNotEmpty(value)) {
                    // TODO: 2021/1/29  临时逻辑
                    try {
                        objectMap.put(field.getFieldName(), JSON.parse((String) value));
                    } catch (JSONException e) {
                        objectMap.put(field.getFieldName(), value.toString());
                    }
                } else {
                    objectMap.put(field.getFieldName(), "");
                }
            }
        });
        return objectMap;
    }

    /**
     * 获取Elasticsearch对象
     *
     * @return restTemplate
     */
    default ElasticsearchRestTemplate getRestTemplate() {
        return getBean(ElasticsearchRestTemplate.class);
    }

    /**
     * 默认的type对象，不准备使用，固定值
     *
     * @return doc
     */
    default String getDocType() {
        return "_doc";
    }


    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    public String[] appendSearch();

    /**
     * 查询的字段，以及排序
     *
     * @param crmSearchBO data
     * @return data
     */
    default SearchSourceBuilder createSourceBuilder(CrmSearchBO crmSearchBO) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //排序以及查询字段
        sort(crmSearchBO, sourceBuilder);
        sourceBuilder.query(createQueryBuilder(crmSearchBO));
        return sourceBuilder;
    }

    /**
     * 拼接查询条件
     *
     * @param crmSearchBO
     * @return
     */
    default BoolQueryBuilder createQueryBuilder(CrmSearchBO crmSearchBO) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StrUtil.isNotEmpty(crmSearchBO.getSearch())) {
            BoolQueryBuilder searchBoolQuery = QueryBuilders.boolQuery();
            for (String search : appendSearch()) {
                searchBoolQuery.should(QueryBuilders.wildcardQuery(search, "*" + crmSearchBO.getSearch().trim() + "*"));
            }
            queryBuilder.filter(searchBoolQuery);
        }

        if (crmSearchBO.getSceneId() != null) {
            sceneQuery(crmSearchBO, queryBuilder);
        } else {
            if (getLabel().equals(CrmEnum.LEADS)) {
                queryBuilder.filter(QueryBuilders.termQuery("isTransform", 0));
            }
        }
        //开始搜索相关
        crmSearchBO.getSearchList().forEach(search -> {
            Dict searchTransferMap = getSearchTransferMap();
            if (searchTransferMap.containsKey(search.getName())) {
                search.setName(searchTransferMap.getStr(search.getName()));
            }
            if ("business_type".equals(search.getFormType())) {
                List<String> values = search.getValues();
                if (values.size() > 1) {
                    Integer status = Integer.valueOf(values.get(1));
                    if (status < 0) {
                        queryBuilder.filter(QueryBuilders.termQuery("isEnd", Math.abs(status)));
                    } else {
                        queryBuilder.filter(QueryBuilders.termQuery(search.getName(), status));
                        queryBuilder.filter(QueryBuilders.termQuery("isEnd", 0));
                    }
                }
                queryBuilder.filter(QueryBuilders.termQuery("typeId", values.get(0)));
                return;
            }
            search(search, queryBuilder);
        });
        if (crmSearchBO.getPoolId() != null) {
            queryBuilder.filter(QueryBuilders.termQuery("poolId", crmSearchBO.getPoolId()));
        } else {
            queryBuilder.filter(QueryBuilders.existsQuery("ownerUserId"));
            setCrmDataAuth(queryBuilder);
        }
        if (queryBuilder.should().size() > 0) {
            queryBuilder.minimumShouldMatch(1);
        }
        return queryBuilder;
    }


    /**
     * 场景查询操作
     *
     * @param crmSearchBO  场景BO
     * @param queryBuilder 查询条件
     */
    @SuppressWarnings("unchecked")
    default void sceneQuery(CrmSearchBO crmSearchBO, BoolQueryBuilder queryBuilder) {
        Long userId = UserUtil.getUserId();
        CrmScene crmScene = getBean(ICrmSceneService.class).getById(crmSearchBO.getSceneId());
        if (crmScene != null) {
            if (StrUtil.isNotEmpty(crmScene.getBydata())) {
                if (CrmSceneEnum.CHILD.getName().equals(crmScene.getBydata())) {
                    List<Long> longList = getBean(AdminService.class).queryChildUserId(userId).getData();
                    queryBuilder.filter(QueryBuilders.termsQuery("ownerUserId", longList));
                } else if (CrmSceneEnum.SELF.getName().equals(crmScene.getBydata())) {
                    queryBuilder.filter(QueryBuilders.termQuery("ownerUserId", userId));
                } else if (CrmSceneEnum.STAR.getName().equals(crmScene.getBydata())) {
                    BaseService baseService;
                    switch (getLabel()) {
                        case LEADS: {
                            baseService = getBean(ICrmLeadsUserStarService.class);
                            break;
                        }
                        case CUSTOMER: {
                            baseService = getBean(ICrmCustomerUserStarService.class);
                            break;
                        }
                        case CONTACTS: {
                            baseService = getBean(ICrmContactsUserStarService.class);
                            break;
                        }
                        case BUSINESS: {
                            baseService = getBean(ICrmBusinessUserStarService.class);
                            break;
                        }
                        default:
                            return;
                    }
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.select(getLabel().getTable() + "_id");
                    queryWrapper.eq("user_id", userId);
                    List<Map<String, Object>> listMaps = baseService.listMaps(queryWrapper);
                    if (listMaps.size() > 0) {
                        queryBuilder.filter(QueryBuilders.idsQuery().addIds(listMaps.stream().map(map -> map.get(getLabel().getTable() + "Id").toString()).toArray(String[]::new)));
                    }
                }
                if (getLabel().equals(CrmEnum.LEADS)) {
                    if (CrmSceneEnum.TRANSFORM.getName().equals(crmScene.getBydata())) {
                        if (getLabel().equals(CrmEnum.LEADS)) {
                            queryBuilder.filter(QueryBuilders.termQuery("isTransform", 1));
                        }
                    } else {
                        queryBuilder.filter(QueryBuilders.termQuery("isTransform", 0));
                    }
                }
            } else {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    crmSearchBO.getSearchList().addAll(mapper.readValue(crmScene.getData(), new TypeReference<List<CrmSearchBO.Search>>() {
                    }));
                    if (getLabel().equals(CrmEnum.LEADS)) {
                        boolean isIdSearch = crmSearchBO.getSearchList().stream().anyMatch(search -> search.getSearchEnum().equals(CrmSearchBO.FieldSearchEnum.ID));
                        if (!isIdSearch) {
                            queryBuilder.filter(QueryBuilders.termQuery("isTransform", 0));
                        }
                    }
                } catch (Exception e) {
                    log.error("json序列化错误{}", crmScene.getData());
                    getBean(ICrmSceneService.class).removeById(crmScene.getSceneId());
                }
            }
        }
    }

    default void search(CrmSearchBO.Search search, BoolQueryBuilder queryBuilder) {
        if(search.getSearchEnum() == CrmSearchBO.FieldSearchEnum.SCRIPT) {
            queryBuilder.filter(QueryBuilders.scriptQuery(search.getScript()));
            return;
        }
        if(search.getSearchEnum() == CrmSearchBO.FieldSearchEnum.ID) {
            queryBuilder.filter(QueryBuilders.idsQuery().addIds(search.getValues().toArray(new String[0])));
            return;
        }
        String formType = search.getFormType();
        FieldEnum fieldEnum = FieldEnum.parse(formType);
        switch (fieldEnum) {
            case TEXTAREA:
                search.setName(search.getName() + ".keyword");
            case TEXT:
            case MOBILE:
            case EMAIL:
            case SELECT:
            case WEBSITE: {
                ElasticUtil.textSearch(search, queryBuilder);
                break;
            }
            case BOOLEAN_VALUE: {
                boolean value = TypeUtils.castToBoolean(search.getValues().get(0));
                value = (search.getSearchEnum() == CrmSearchBO.FieldSearchEnum.IS) == value;
                if (value) {
                    queryBuilder.filter(QueryBuilders.termQuery(search.getName(),"1"));
                } else {
                    BoolQueryBuilder builder = QueryBuilders.boolQuery();
                    builder.should(QueryBuilders.termQuery(search.getName(), "0"));
                    builder.should(QueryBuilders.termQuery(search.getName(), ""));
                    builder.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(search.getName())));
                    queryBuilder.filter(builder);
                }
                break;
            }
            case CHECKBOX:{
                search.setName(search.getName());
                ElasticUtil.checkboxSearch(search, queryBuilder);
                break;
            }
            case NUMBER:
            case FLOATNUMBER:
            case PERCENT:
                ElasticUtil.numberSearch(search, queryBuilder);
                break;
            case DATE_INTERVAL:
                break;
            case DATE:
            case DATETIME:
                ElasticUtil.dateSearch(search, queryBuilder,fieldEnum);
                break;
            case AREA_POSITION:
                PrefixQueryBuilder prefixQuery = QueryBuilders.prefixQuery(search.getName(), JSONToken.name(JSONToken.LBRACKET) + CollUtil.join(search.getValues(), Const.SEPARATOR));
                queryBuilder.filter(prefixQuery);
                break;
            case CURRENT_POSITION:
                if(search.getSearchEnum() == CrmSearchBO.FieldSearchEnum.IS) {
                    search.setValues(Collections.singletonList("\"" + search.getValues().get(0) + "\""));
                    search.setSearchEnum(CrmSearchBO.FieldSearchEnum.CONTAINS);
                }
                if(search.getSearchEnum() == CrmSearchBO.FieldSearchEnum.IS_NOT) {
                    search.setValues(Collections.singletonList("\"" + search.getValues().get(0) + "\""));
                    search.setSearchEnum(CrmSearchBO.FieldSearchEnum.NOT_CONTAINS);
                }
                ElasticUtil.textSearch(search, queryBuilder);
                break;
            case USER:
            case SINGLE_USER:
            case STRUCTURE:
                ElasticUtil.userSearch(search, queryBuilder);
            default:
                ElasticUtil.textSearch(search, queryBuilder);
                break;
        }
    }

    /**
     * 拼接客户管理数据权限
     */
    default void setCrmDataAuth(BoolQueryBuilder boolQueryBuilder) {
        UserInfo user = UserUtil.getUser();
        Long userId = user.getUserId();
        CrmEnum crmEnum = getLabel();
        if (UserUtil.isAdmin() || crmEnum.equals(CrmEnum.CUSTOMER_POOL)) {
            return;
        }
        BoolQueryBuilder authBoolQuery = QueryBuilders.boolQuery();
        List<Long> dataAuthUserIds = AuthUtil.queryAuthUserList(getLabel(), CrmAuthEnum.LIST);
        if (CollUtil.isNotEmpty(dataAuthUserIds)) {
            if (crmEnum.equals(CrmEnum.MARKETING)) {
                for (Long id : dataAuthUserIds) {
                    authBoolQuery.should(QueryBuilders.termQuery("ownerUserId", id))
                            .should(QueryBuilders.termQuery("relationUserId", id));
                }
            } else {
                authBoolQuery.should(QueryBuilders.termsQuery("ownerUserId", dataAuthUserIds));
                if (Arrays.asList(CrmEnum.CUSTOMER,CrmEnum.CONTACTS,CrmEnum.BUSINESS,CrmEnum.RECEIVABLES,CrmEnum.CONTRACT).contains(crmEnum)) {
                    authBoolQuery.should(QueryBuilders.termQuery("teamMemberIds",userId));
                }
            }
        }
        boolQueryBuilder.must(authBoolQuery);
    }

    /**
     * @param crmSearchBO   data
     * @param sourceBuilder data
     */
    default void sort(CrmSearchBO crmSearchBO, SearchSourceBuilder sourceBuilder) {
        //todo 暂时未考虑手机端的高级查询分页
        String searchAfterKey = "es:search:" + UserUtil.getUserId().toString();
        List<CrmFieldSortVO> crmFieldSortList = getBean(ICrmFieldService.class).queryListHead(getLabel().getType());
        crmFieldSortList.add(new CrmFieldSortVO().setFieldName("receiveTime").setName("接收到客户时间").setType(FieldEnum.DATETIME.getType()));
        crmFieldSortList.add(new CrmFieldSortVO().setFieldName("preOwnerUserName").setName("前负责人").setType(FieldEnum.TEXT.getType()));
        crmFieldSortList.add(new CrmFieldSortVO().setFieldName("createTime").setName("创建时间").setType(FieldEnum.DATETIME.getType()));
        crmFieldSortList.add(new CrmFieldSortVO().setFieldName("lastTime").setName("最后联系时间").setType(FieldEnum.DATETIME.getType()));
        crmFieldSortList.add(new CrmFieldSortVO().setFieldName("poolTime").setName("进入公海时间").setType(FieldEnum.DATETIME.getType()));
        if (crmSearchBO.getPage() <= 100) {
            if (crmSearchBO.getPageType().equals(1)) {
                // 设置起止和结束
                sourceBuilder.from((crmSearchBO.getPage() - 1) * crmSearchBO.getLimit());
            }
        }
        //设置查询条数
        sourceBuilder.size(crmSearchBO.getLimit());
        AtomicReference<Integer> fieldType = new AtomicReference<>(0);
        List<String> fieldList = new ArrayList<>();
        crmFieldSortList.forEach(crmField -> {
            if (crmField.getFieldName().equals(crmSearchBO.getSortField())) {
                fieldType.set(crmField.getType());
            }
            fieldList.add(crmField.getFieldName());
        });
        if (StrUtil.isEmpty(crmSearchBO.getSortField()) || crmSearchBO.getOrder() == null || fieldType.get().equals(0)) {
            crmSearchBO.setOrder(1).setSortField("updateTime");
        } else {
            FieldEnum fieldEnum = FieldEnum.parse(fieldType.get());
            switch (fieldEnum) {
                case TEXT:
                case TEXTAREA:
                case SELECT:
                case MOBILE:
                case FILE:
                case CHECKBOX:
                case USER:
                case STRUCTURE:
                case EMAIL:
                    crmSearchBO.setSortField(crmSearchBO.getSortField() + ".sort");
                    break;
                case DATE:
                case NUMBER:
                case FLOATNUMBER:
                case DATETIME:
                    break;
                default:
                    break;
            }
        }
        if (crmSearchBO.getPage() > 100) {
            Redis redis = BaseUtil.getRedis();
            Long length = redis.getLength(searchAfterKey);
            if ((crmSearchBO.getPage() - 100) > length.intValue()) {
                //分页数据错误,直接重置
                sourceBuilder.from(0);
                crmSearchBO.setPage(1);
            } else {
                Object[] keyIndex = redis.getKeyIndex(searchAfterKey, crmSearchBO.getPage() - 101);
                sourceBuilder.searchAfter(keyIndex);
            }
        }

        // 排序
        sourceBuilder.sort(SortBuilders.fieldSort(crmSearchBO.getSortField()).order(Objects.equals(2, crmSearchBO.getOrder()) ? SortOrder.ASC : SortOrder.DESC));
        sourceBuilder.sort(SortBuilders.fieldSort("_id").order(SortOrder.DESC));
        List<String> fieldNameList = new ArrayList<>();
        //只查询所需字段
        for (String fieldName : fieldList) {
            fieldNameList.add(fieldName);
            if (fieldName.endsWith("Name")) {
                String name = fieldName.substring(0, fieldName.indexOf("Name"));
                fieldNameList.add(name + "Id");
            }
            if (fieldName.endsWith("Num")) {
                String name = fieldName.substring(0, fieldName.indexOf("Num"));
                fieldNameList.add(name + "Id");
            }
        }
        if (getLabel().equals(CrmEnum.CONTRACT)) {
            fieldNameList.add("receivedMoney");
        } else if (getLabel().equals(CrmEnum.BUSINESS)) {
            fieldNameList.add("isEnd");
        }
        sourceBuilder.fetchSource(fieldNameList.toArray(new String[0]), null);
    }

    /**
     * 获取关联表字段转换
     *
     * @return
     */
    default Dict getSearchTransferMap() {
        return Dict.create();
    }

    /**
     * 保存Elasticsearch数据
     *
     * @param model obj
     * @param id    主键ID
     */
    default void savePage(CrmModelSaveBO model, Object id, boolean isExcel) {
        List<CrmModelFiledVO> crmModelFiledList = queryDefaultField();
        Map<String, Object> map = new HashMap<>(model.getEntity());
        model.getField().forEach(field -> {
            map.put(field.getFieldName(), field.getValue());
        });
        crmModelFiledList.forEach(modelField -> {
            if (map.get(modelField.getFieldName()) == null) {
                map.remove(modelField.getFieldName());
                return;
            }
            if (modelField.getFieldType() == 0 && Arrays.asList(3, 9, 10, 12).contains(modelField.getType())) {
                Object value = map.remove(modelField.getFieldName());
                if (value != null) {
                    map.put(modelField.getFieldName(), StrUtil.splitTrim(value.toString(), ","));
                } else {
                    map.put(modelField.getFieldName(), new ArrayList<>());
                }
            }
            if (FieldEnum.DATE.getType().equals(modelField.getType())) {
                Object value = map.remove(modelField.getFieldName());
                if (ObjectUtil.isNotEmpty(value)) {
                    if (value instanceof Date) {
                        map.put(modelField.getFieldName(), DateUtil.formatDate((Date) value));
                    } else if (value instanceof String) {
                        map.put(modelField.getFieldName(), value.toString());
                    }
                }
            }

            if (FieldEnum.DATETIME.getType().equals(modelField.getType())) {
                Object value = map.remove(modelField.getFieldName());
                if (ObjectUtil.isNotEmpty(value)) {
                    if (value instanceof Date) {
                        map.put(modelField.getFieldName(), DateUtil.formatDateTime((Date) value));
                    } else if (value instanceof String) {
                        map.put(modelField.getFieldName(), value.toString());
                    }
                }

            }
            if (FieldEnum.FILE.getType().equals(modelField.getType())) {
                Object value = map.remove(modelField.getFieldName());
                if (!ObjectUtil.isEmpty(value)) {
                    List<FileEntity> data = getBean(AdminFileService.class).queryFileList((String) value).getData();
                    map.put(modelField.getFieldName(), data.stream().map(FileEntity::getName).collect(Collectors.toList()));
                }
            }
            if (getBean(FieldService.class).equalsByType(modelField.getType())) {
                Object value = map.remove(modelField.getFieldName());
                if (!ObjectUtil.isEmpty(value)) {
                    if (value instanceof String) {
                        map.put(modelField.getFieldName(), value.toString());
                    } else {
                        map.put(modelField.getFieldName(), JSON.toJSONString(value));
                    }
                }
            }
            if (FieldEnum.DATE_INTERVAL.getType().equals(modelField.getType())) {
                Object value = map.remove(modelField.getFieldName());
                if (!ObjectUtil.isEmpty(value)) {
                    if (value instanceof String) {
                        map.put(modelField.getFieldName(), StrUtil.splitTrim(value.toString(), ","));
                    } else if (value instanceof Collection) {
                        map.put(modelField.getFieldName(), (Collection) value);
                    }
                }
            }

        });
        setOtherField(map);
        UpdateRequest request = new UpdateRequest(getIndex(), getDocType(),id.toString());
        request.doc(map);
        request.docAsUpsert(true);
        try {
            getRestTemplate().getClient().update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
        if (!isExcel) {
            getRestTemplate().refresh(getIndex());
        }
    }

    /**
     * 设置其他冗余字段
     *
     * @param map
     */
    void setOtherField(Map<String, Object> map);

    /**
     * 根据ID列表删除数据
     *
     * @param ids ids
     */
    default void deletePage(List<Integer> ids) {
        DeleteQuery query = new DeleteQuery();
        query.setQuery(QueryBuilders.idsQuery().addIds(ids.stream().map(Object::toString).toArray(String[]::new)));
        query.setIndex(getIndex());
        query.setType(getDocType());
        getRestTemplate().delete(query);
    }

    /**
     * 修改某个字段的值
     *
     * @param fieldName 字段
     * @param value     值
     * @param ids       ids
     */
    default void updateField(String fieldName, Object value, List<Integer> ids) {
        BulkRequest bulkRequest = new BulkRequest();
        Map<String, Object> map = new HashMap<>();
        if ("ownerUserId".equals(fieldName)) {
            map.put("ownerUserName", UserCacheUtil.getUserName((Long) value));
        }
        map.put(fieldName, value);
        ids.forEach(id -> {
            UpdateRequest request = new UpdateRequest(getIndex(), getDocType(), id.toString());
            request.doc(map);
            bulkRequest.add(request);
        });
        try {
            getRestTemplate().getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            getRestTemplate().refresh(getIndex());
        } catch (IOException e) {
            log.error("es修改失败", e);
        }
    }

    /**
     * 修改某个字段的值
     *
     * @param id id
     */
    default void updateField(JSONObject jsonObject, Integer id) {
        Map<String, Object> map = new HashMap<>();
        String fieldName = jsonObject.getString("fieldName");
        if (jsonObject.get("value") != null) {
            if (FieldEnum.DATE.getType().equals(jsonObject.getInteger("type"))) {
                Object value = jsonObject.get("value");
                map.put(fieldName, value);
            } else if (FieldEnum.DATETIME.getType().equals(jsonObject.getInteger("type"))) {
                Object value = jsonObject.get("value");
                map.put(fieldName, value);
            } else if (FieldEnum.FILE.getType().equals(jsonObject.getInteger("type"))) {
                Object value = jsonObject.get("value");
                List<FileEntity> data = getBean(AdminFileService.class).queryFileList((String) value).getData();
                map.put(fieldName, data.stream().map(FileEntity::getName).collect(Collectors.joining(",")));
            } else if (getBean(FieldService.class).equalsByType(jsonObject.getInteger("type"))) {
                Object value = jsonObject.get("value");
                if (!ObjectUtil.isEmpty(value)) {
                    map.put(fieldName, JSON.toJSONString(value));
                }
            } else if (jsonObject.getInteger("fieldType") == 0 && Arrays.asList(3, 8, 9, 10, 11, 12).contains(jsonObject.getInteger("type"))) {
                Object value = jsonObject.get("value");
                if (value != null) {
                    map.put(fieldName, StrUtil.splitTrim(value.toString(), ","));
                } else {
                    map.put(fieldName, new ArrayList<>());
                }
            } else {
                String value = jsonObject.getString("value");
                map.put(fieldName, value);
            }

        } else {
            map.put(fieldName, null);
        }

        map.put("updateTime", DateUtil.formatDateTime(new Date()));
        try {
            UpdateRequest request = new UpdateRequest(getIndex(), getDocType(), id.toString());
            request.doc(map);
            getRestTemplate().getClient().update(request, RequestOptions.DEFAULT);
            getRestTemplate().refresh(getIndex());
        } catch (IOException e) {
            log.error("es修改失败", e);
        }
    }

    /**
     * 批量更新es字段
     *
     * @param map
     * @param ids
     */
    default void updateField(Map<String, Object> map, List<Integer> ids) {
        BulkRequest bulkRequest = new BulkRequest();
        ids.forEach(id -> {
            UpdateRequest request = new UpdateRequest(getIndex(), getDocType(), id.toString());
            request.doc(map);
            bulkRequest.add(request);
        });
        try {
            getRestTemplate().getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            getRestTemplate().refresh(getIndex());
        } catch (IOException e) {
            log.error("es修改失败", e);
        }
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    public CrmEnum getLabel();


    /**
     * 获取索引名称
     *
     * @return index
     */
    default public String getIndex() {
        return getLabel().getIndex();
    }

    /**
     * 查询所有字段
     *
     * @return data
     */
    List<CrmModelFiledVO> queryDefaultField();

    /**
     * 客户放入公海
     *
     * @param poolBO bo
     */
    @SuppressWarnings("unchecked")
    default public void putInPool(CrmCustomerPoolBO poolBO) {
        SearchRequest searchRequest = new SearchRequest(getIndex());
        searchRequest.types(getDocType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(poolBO.getIds().size());
        searchRequest.source(sourceBuilder.fetchSource(new String[]{"poolId", "ownerUserId"}, null).query(QueryBuilders.idsQuery().addIds(poolBO.getIds().stream().map(Object::toString).toArray(String[]::new))));
        BulkRequest bulkRequest = new BulkRequest();
        try {
            SearchResponse searchResponse = getRestTemplate().getClient().search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : searchResponse.getHits()) {
                UpdateRequest request = new UpdateRequest(getIndex(), getDocType(), hit.getId());
                Map<String, Object> map = new HashMap<>();
                if (hit.getSourceAsMap().containsKey("poolId")) {
                    Object obj = hit.getSourceAsMap().get("poolId");
                    if (obj instanceof Collection) {
                        Set<Integer> set = new HashSet<>((List<Integer>) obj);
                        set.add(poolBO.getPoolId());
                        map.put("poolId", set);
                    } else if (obj instanceof Integer) {
                        Set<Integer> set = new HashSet<>();
                        set.add((Integer) obj);
                        set.add(poolBO.getPoolId());
                        map.put("poolId", set);
                    } else {
                        map.put("poolId", Collections.singletonList(poolBO.getPoolId()));
                    }
                } else {
                    map.put("poolId", Collections.singletonList(poolBO.getPoolId()));
                }
                Object ownerUserId = hit.getSourceAsMap().get("ownerUserId");
                if (ownerUserId != null) {
                    map.put("preOwnerUserName", UserCacheUtil.getUserName(TypeUtils.castToLong(ownerUserId)));
                    map.put("preOwnerUserId", TypeUtils.castToLong(ownerUserId));
                }
                map.put("ownerUserId", null);
                map.put("poolTime", DateUtil.formatDateTime(new Date()));
                request.doc(map);
                bulkRequest.add(request);
            }
            //处理联系人相关逻辑
            SearchRequest contactsRequest = new SearchRequest(CrmEnum.CONTACTS.getIndex());
            contactsRequest.types(getDocType());
            contactsRequest.source(SearchSourceBuilder.searchSource().fetchSource(new String[]{"contactsId"}, null).query(QueryBuilders.termsQuery("customerId", poolBO.getIds())));
            SearchResponse contactsResponse = getRestTemplate().getClient().search(contactsRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : contactsResponse.getHits()) {
                UpdateRequest request = new UpdateRequest(CrmEnum.CONTACTS.getIndex(), getDocType(), hit.getId());
                Map<String, Object> map = new HashMap<>();
                map.put("ownerUserId", null);
                map.put("ownerUserName", null);
                request.doc(map);
                bulkRequest.add(request);
            }
            //统一提交处理
            if (bulkRequest.requests() == null || bulkRequest.requests().size() == 0) {
                return;
            }
            getRestTemplate().getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("查询错误", e);
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
        getRestTemplate().refresh(getIndex());
    }

    /**
     * 领取客户
     */
    default public void receiveCustomer(CrmCustomerPoolBO poolBO, Integer isReceive, List<Integer> contactsIds) {
        BulkRequest bulkRequest = new BulkRequest();
        try {
            for (Integer id : poolBO.getIds()) {
                UpdateRequest request = new UpdateRequest(getIndex(), getDocType(), id.toString());
                Map<String, Object> map = new HashMap<>();
                String date = DateUtil.formatDateTime(new Date());
                map.put("ownerUserId", poolBO.getUserId());
                map.put("ownerUserName", UserCacheUtil.getUserName(poolBO.getUserId()));
                map.put("followup", 0);
                map.put("receiveTime", date);
                map.put("updateTime", date);
                map.put("isReceive", isReceive);
                map.put("poolId", new ArrayList<>());
                request.doc(map);
                bulkRequest.add(request);
            }
            for (Integer contactsId : contactsIds) {
                UpdateRequest contactsRequest = new UpdateRequest(CrmEnum.CONTACTS.getIndex(), getDocType(), contactsId.toString());
                Map<String, Object> contactsMap = new HashMap<>();
                String date = DateUtil.formatDateTime(new Date());
                contactsMap.put("ownerUserId", poolBO.getUserId());
                contactsMap.put("ownerUserName", UserCacheUtil.getUserName(poolBO.getUserId()));
                contactsMap.put("updateTime", date);
                contactsRequest.doc(contactsMap);
                bulkRequest.add(contactsRequest);
            }
            if (bulkRequest.requests() == null || bulkRequest.requests().size() == 0) {
                return;
            }
            getRestTemplate().getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("修改错误", e);
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
        getRestTemplate().refresh(getIndex());
    }


    /**
     * 查询详情信息
     *
     * @param crmModel model
     */
    default public List<CrmModelFiledVO> appendInformation(CrmModel crmModel) {
        AdminService adminService = getBean(AdminService.class);
        List<CrmModelFiledVO> filedVOS = new ArrayList<>();
        if (!getLabel().equals(CrmEnum.RETURN_VISIT)) {
            CrmModelFiledVO filedVO = new CrmModelFiledVO("owner_user_name", FieldEnum.USER);
            filedVO.setName("负责人");
            List<SimpleUser> data = adminService.queryUserByIds(Collections.singleton(crmModel.getOwnerUserId())).getData();
            filedVO.setValue(data);
            filedVO.setFieldType(1);
            filedVOS.add(filedVO);
        }
        if (getLabel().equals(CrmEnum.CUSTOMER) || getLabel().equals(CrmEnum.LEADS)) {
            filedVOS.add(new CrmModelFiledVO("last_content", FieldEnum.TEXTAREA, "最后跟进记录", 1).setValue(crmModel.get("lastContent")));
        }
        Object value = adminService.queryUserByIds(Collections.singletonList((Long) crmModel.get("createUserId"))).getData();
        filedVOS.add(new CrmModelFiledVO("create_user_name", FieldEnum.USER, "创建人", 1).setValue(value));
        filedVOS.add(new CrmModelFiledVO("create_time", FieldEnum.DATETIME, "创建时间", 1).setValue(crmModel.get("createTime")));
        filedVOS.add(new CrmModelFiledVO("update_time", FieldEnum.DATETIME, "更新时间", 1).setValue(crmModel.get("updateTime")));
        if (!getLabel().equals(CrmEnum.PRODUCT) && !getLabel().equals(CrmEnum.RECEIVABLES) && !getLabel().equals(CrmEnum.RETURN_VISIT) && !getLabel().equals(CrmEnum.INVOICE)) {
            filedVOS.add(new CrmModelFiledVO("last_time", FieldEnum.DATETIME, "最后跟进时间", 1).setValue(crmModel.get("lastTime")));
        }
        if (Arrays.asList(CrmEnum.CUSTOMER,CrmEnum.CONTACTS,CrmEnum.BUSINESS,CrmEnum.RECEIVABLES,CrmEnum.CONTRACT).contains(getLabel())) {
            List<CrmTeamMembers> teamMembers = ApplicationContextHolder.getBean(ICrmTeamMembersService.class)
                    .lambdaQuery().select(CrmTeamMembers::getUserId)
                    .eq(CrmTeamMembers::getType, getLabel().getType())
                    .eq(CrmTeamMembers::getTypeId, crmModel.get(getLabel().getTable()+"Id"))
                    .list();
            filedVOS.add(new CrmModelFiledVO("teamMemberIds", FieldEnum.TEXT, "相关团队", 1).setValue(teamMembers.stream().map(teamMember->UserCacheUtil.getUserName(teamMember.getUserId())).collect(Collectors.joining(Const.SEPARATOR))));
        }
        for (CrmModelFiledVO filedVO : filedVOS) {
            filedVO.setSysInformation(1);
        }
        return filedVOS;
    }


    /**
     * 补充审批字段信息
     *
     * @param label
     * @param typeId
     * @param recordId
     * @param examineRecordSaveBO
     * @return void
     * @date 2020/12/18 13:44
     **/
    default void supplementFieldInfo(Integer label, Integer typeId, Integer recordId, ExamineRecordSaveBO examineRecordSaveBO) {
        examineRecordSaveBO.setLabel(label);
        examineRecordSaveBO.setTypeId(typeId);
        examineRecordSaveBO.setRecordId(recordId);
        if (examineRecordSaveBO.getDataMap() != null) {
            examineRecordSaveBO.getDataMap().put("createUserId", UserUtil.getUserId());
        } else {
            Map<String, Object> entityMap = new HashMap<>(1);
            entityMap.put("createUserId", UserUtil.getUserId());
            examineRecordSaveBO.setDataMap(entityMap);
        }
    }

    /**
     * 去除不支持导入的字段
     *
     * @param crmModelFiledList
     * @return
     * @date 2021/1/30 15:12
     **/
    default void removeFieldByType(List<CrmModelFiledVO> crmModelFiledList) {
        List<FieldEnum> fieldEnums = Arrays.asList(FieldEnum.FILE, FieldEnum.CHECKBOX, FieldEnum.USER, FieldEnum.STRUCTURE,
                FieldEnum.AREA, FieldEnum.AREA_POSITION, FieldEnum.CURRENT_POSITION, FieldEnum.DATE_INTERVAL, FieldEnum.BOOLEAN_VALUE,
                FieldEnum.HANDWRITING_SIGN, FieldEnum.DESC_TEXT, FieldEnum.DETAIL_TABLE, FieldEnum.CALCULATION_FUNCTION);
        crmModelFiledList.removeIf(model -> fieldEnums.contains(FieldEnum.parse(model.getType())));
    }

    /**
     * 验证唯一字段是否重复
     *
     * @param fieldId
     * @param value
     * @param batchId
     * @return boolean
     * @date 2021/2/18 14:31
     **/
    default void uniqueFieldIsAbnormal(String name, Integer fieldId, String value, String batchId) {
        if (fieldId == null) {
            return;
        }
        CrmField field = getBean(ICrmFieldService.class).getById(fieldId);
        if (field == null || Objects.equals(field.getIsUnique(), 0)) {
            return;
        }
        CrmFieldVerifyBO crmFieldVerifyBO = new CrmFieldVerifyBO();
        crmFieldVerifyBO.setFieldId(fieldId);
        crmFieldVerifyBO.setValue(value);
        crmFieldVerifyBO.setBatchId(batchId);
        CrmFieldVerifyBO fieldVerifyBO = getBean(ICrmFieldService.class).verify(crmFieldVerifyBO);
        if (Objects.equals(fieldVerifyBO.getStatus(), 0)) {
            throw new CrmException(CrmCodeEnum.CRM_FIELD_ALREADY_EXISTS, name);
        }
    }
}
