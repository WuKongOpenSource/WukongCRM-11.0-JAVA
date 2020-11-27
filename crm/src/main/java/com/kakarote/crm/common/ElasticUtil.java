package com.kakarote.crm.common;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.BO.EsUpdateFieldBO;
import com.kakarote.crm.entity.BO.EsUpdatePropertiesBO;
import com.kakarote.crm.entity.PO.CrmFieldConfig;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * @author elastic的一些通用操作
 */
@Slf4j
public class ElasticUtil {
    public static void init(List<CrmModelFiledVO> crmFieldList, RestHighLevelClient client, CrmEnum crmEnum) {
        String index = crmEnum.getIndex();
        GetIndexRequest indexRequest = new GetIndexRequest(index);
        try {
            boolean b = client.indices().exists(indexRequest, RequestOptions.DEFAULT);
            if (b) {
                log.info("索引存在:" + index);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> properties = new HashMap<>(crmFieldList.size());
        crmFieldList.forEach(crmField -> {
            properties.put(crmField.getFieldName(), parseType(crmField.getType()));
        });
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        CreateIndexRequest request = new CreateIndexRequest(index);
        try {
            //设置mapping参数
            request.mapping(mapping);
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            boolean falg = createIndexResponse.isAcknowledged();
            if (falg) {
                log.info("创建索引库:" + index + "成功！");
            }
        } catch (IOException e) {
            log.error("创建索引错误", e);
        }
    }


    public static void addField(RestHighLevelClient client, CrmFieldConfig crmField, Integer fieldType) {
        CrmEnum crmEnum = CrmEnum.parse(crmField.getLabel());
        try {
            JSONObject object = new JSONObject();
            JSONObject child = new JSONObject();
            child.put(StrUtil.toCamelCase(crmField.getFieldName()), parseType(fieldType));
            object.put("properties", child);
            PutMappingRequest request = new PutMappingRequest(crmEnum.getIndex());
            request.source(object);
            client.indices().putMapping(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("新增字段错误", e);
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    public static void removeField(RestHighLevelClient client, String fieldName, Integer label) {
        UpdateByQueryRequest request = new UpdateByQueryRequest(CrmEnum.parse(label).getIndex());
        request.setScript(new Script(ScriptType.INLINE, "painless", "ctx._source." + fieldName + " = null", new HashMap<>()));
        request.setBatchSize(1000);
        request.setRefresh(true);
        try {
            BulkByScrollResponse bulkByScrollResponse = client.updateByQuery(request, RequestOptions.DEFAULT);
            log.info(JSON.toJSONString(bulkByScrollResponse));
        } catch (IOException e) {
            log.error("删除字段错误", e);
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    public static void initData(RestHighLevelClient client, List<Map<String, Object>> mapList, CrmEnum crmEnum,String index) {
        BulkRequest bulkRequest = new BulkRequest();
        mapList.forEach(map -> {
            IndexRequest request = new IndexRequest(index, "_doc");
            request.id(map.get((crmEnum == CrmEnum.RETURN_VISIT ? "visit" : crmEnum.getTable()) + "Id").toString());
            try {
                request.source(map);
            } catch (Exception e) {
                System.out.println(map.toString());
            }

            bulkRequest.add(request);
        });
        if (bulkRequest.requests() == null || bulkRequest.requests().size() == 0) {
            return;
        }
        try {
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("bulkHasFailures:{}",bulk.hasFailures());
            boolean hasFailures = bulk.hasFailures();
            if (bulk.hasFailures()){
                log.info(JSON.toJSONString(bulk.buildFailureMessage()));
                int count= 3;
                while (count > 0 && hasFailures){
                    count--;
                    BulkResponse bulk1 = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                    hasFailures = bulk1.hasFailures();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, Object> parseType(Integer type) {
        FieldEnum fieldEnum = FieldEnum.parse(type);
        Map<String, Object> map = new HashMap<>();
        String name;
        switch (fieldEnum) {
            case TEXT:
                name = "keyword";
                break;
            case TEXTAREA:
                name = "text";
                map.put("fields", new JSONObject().fluentPut("sort", new JSONObject().fluentPut("type", "icu_collation_keyword").fluentPut("language", "zh").fluentPut("country", "CN"))
                        .fluentPut("keyword", new JSONObject().fluentPut("type", "keyword")));
                break;
            case SELECT:
                name = "keyword";
                break;
            case DATE:
                name = "date";
                map.put("format", "yyyy-MM-dd");
                break;
            case NUMBER:
                name = "scaled_float";
                map.put("scaling_factor", 1);
                break;
            case FLOATNUMBER:
                name = "scaled_float";
                map.put("scaling_factor", 100);
                break;
            case MOBILE:
                name = "keyword";
                break;
            case FILE:
                name = "keyword";
                break;
            case CHECKBOX:
                name = "keyword";
                break;
            case USER:
                name = "keyword";
                break;
            case STRUCTURE:
                name = "keyword";
                break;
            case DATETIME:
                name = "date";
                map.put("format", "yyyy-MM-dd HH:mm:ss");
                break;
            case EMAIL:
                name = "keyword";
                break;
            default:
                name = "keyword";
                break;
        }
        map.put("type", name);
        if ("keyword".equals(name)) {
            map.put("fields", new JSONObject().fluentPut("sort", new JSONObject().fluentPut("type", "icu_collation_keyword").fluentPut("language", "zh").fluentPut("country", "CN")));
        }
        return map;
    }

    public static void updateField(RestHighLevelClient client, EsUpdateFieldBO esUpdateFieldBO, List<String> indexs) {
        UpdateByQueryRequest request = new UpdateByQueryRequest(indexs.toArray(new String[0]));
        request.setQuery(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery(esUpdateFieldBO.getConditionField(), esUpdateFieldBO.getConditionValue())));
        String script = "ctx._source.%s='%s'";
        request.setScript(new Script(ScriptType.INLINE, "painless", String.format(script, esUpdateFieldBO.getUpdateField(), esUpdateFieldBO.getUpdateValue()), Collections.emptyMap()));
        request.setBatchSize(1000);
        //解决 version_conflict_engine_exception 异常
        request.setRefresh(true);
        try {
            client.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 冗余字段map对应
     */
    private static Map<String, List<EsUpdatePropertiesBO>> updateMap = new HashMap<>();

    static {
        List<EsUpdatePropertiesBO> userPropertiesList = new ArrayList<>();
        userPropertiesList.add(new EsUpdatePropertiesBO("ownerUserId", "ownerUserName", Lists.newArrayList(CrmEnum.LEADS.getIndex()
                , CrmEnum.CUSTOMER.getIndex(), CrmEnum.CONTACTS.getIndex(), CrmEnum.CONTRACT.getIndex(), CrmEnum.BUSINESS.getIndex()
                , CrmEnum.RECEIVABLES.getIndex(), CrmEnum.RETURN_VISIT.getIndex(), CrmEnum.PRODUCT.getIndex())));
        userPropertiesList.add(new EsUpdatePropertiesBO("createUserId", "createUserName", Lists.newArrayList(CrmEnum.LEADS.getIndex()
                , CrmEnum.CUSTOMER.getIndex(), CrmEnum.CONTACTS.getIndex(), CrmEnum.CONTRACT.getIndex(), CrmEnum.BUSINESS.getIndex()
                , CrmEnum.RECEIVABLES.getIndex(), CrmEnum.RETURN_VISIT.getIndex(), CrmEnum.PRODUCT.getIndex())));
        userPropertiesList.add(new EsUpdatePropertiesBO("companyUserId", "companyUserName", Lists.newArrayList(CrmEnum.CONTRACT.getIndex())));
        updateMap.put("user", userPropertiesList);
        List<EsUpdatePropertiesBO> customerPropertiesList = new ArrayList<>();
        customerPropertiesList.add(new EsUpdatePropertiesBO("customerId", "customerName", Lists.newArrayList(CrmEnum.CONTACTS.getIndex(),
                CrmEnum.BUSINESS.getIndex(), CrmEnum.CONTRACT.getIndex(), CrmEnum.RECEIVABLES.getIndex(), CrmEnum.RETURN_VISIT.getIndex())));
        updateMap.put("customer", customerPropertiesList);
        List<EsUpdatePropertiesBO> contactsPropertiesList = new ArrayList<>();
        contactsPropertiesList.add(new EsUpdatePropertiesBO("contactsId", "contactsName", Lists.newArrayList(CrmEnum.CONTRACT.getIndex(),
                CrmEnum.RETURN_VISIT.getIndex())));
        updateMap.put("contacts", contactsPropertiesList);
        List<EsUpdatePropertiesBO> businessPropertiesList = new ArrayList<>();
        businessPropertiesList.add(new EsUpdatePropertiesBO("businessId", "businessName", Lists.newArrayList(CrmEnum.CONTRACT.getIndex())));
        updateMap.put("business", businessPropertiesList);
        List<EsUpdatePropertiesBO> contractPropertiesList = new ArrayList<>();
        contractPropertiesList.add(new EsUpdatePropertiesBO("contractId", "contractNum", Lists.newArrayList(CrmEnum.RECEIVABLES.getIndex(), CrmEnum.RETURN_VISIT.getIndex())));
        updateMap.put("contract", contractPropertiesList);
        List<EsUpdatePropertiesBO> productPropertiesList = new ArrayList<>();
        productPropertiesList.add(new EsUpdatePropertiesBO("categoryId", "categoryName", Lists.newArrayList(CrmEnum.RECEIVABLES.getIndex(), CrmEnum.RETURN_VISIT.getIndex())));
        updateMap.put("product", productPropertiesList);
    }

    /**
     * 根据类型跟新es冗余数据
     *
     * @param client
     * @param type
     * @param id
     * @param name
     */
    public static void batchUpdateEsData(RestHighLevelClient client, String type, String id, String name) {
        List<EsUpdatePropertiesBO> propertiesList = updateMap.get(type);
        for (EsUpdatePropertiesBO properties : propertiesList) {
            updateField(client, new EsUpdateFieldBO(properties.getIdField(), id, properties.getNameField(), name), properties.getIndexs());
        }
    }

    public static void updateField(ElasticsearchRestTemplate template, String fieldName, Object value, List<Integer> ids, String index) {
        BulkRequest bulkRequest = new BulkRequest();
        ids.forEach(id -> {
            Map<String, Object> map = new HashMap<>();
            map.put(fieldName, value);
            UpdateRequest request = new UpdateRequest(index, "_doc", id.toString());
            request.doc(map);
            bulkRequest.add(request);
        });
        try {
            template.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            template.refresh(index);
        } catch (IOException e) {
            log.error("es修改失败", e);
        }
    }

    /**
     * 修改多个字段的值
     *
     * @param id id
     */
    public static void updateField(ElasticsearchRestTemplate template, Map<String, Object> map, Integer id, String index) {
        try {
            UpdateRequest request = new UpdateRequest(index, "_doc", id.toString());
            request.doc(map);
            template.getClient().update(request, RequestOptions.DEFAULT);
            template.refresh(index);
        } catch (IOException e) {
            log.error("es修改失败", e);
        }
    }
}
