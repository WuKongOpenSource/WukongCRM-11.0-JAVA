package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmActivityEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmCustomerPoolVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmCustomerPoolMapper;
import com.kakarote.crm.service.*;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 公海表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Service(value = "customerPoolService")
public class CrmCustomerPoolServiceImpl extends BaseServiceImpl<CrmCustomerPoolMapper, CrmCustomerPool> implements ICrmCustomerPoolService, CrmPageService {

    @Autowired
    private ICrmCustomerPoolRelationService customerPoolRelationService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ICrmCustomerPoolRuleService customerPoolRuleService;

    @Autowired
    private ICrmCustomerPoolFieldSortService crmCustomerPoolFieldSortService;

    @Autowired
    private ICrmCustomerPoolFieldSettingService customerPoolFieldSettingService;

    @Autowired
    private ICrmCustomerDataService crmCustomerDataService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmBackLogDealService backLogDealService;

    @Autowired
    private FieldService fieldService;

    /**
     * 查询公海规则列表
     *
     * @param pageEntity entity
     */
    @Override
    public BasePage<CrmCustomerPoolVO> queryPoolSettingList(PageEntity pageEntity) {
        BasePage<CrmCustomerPool> basePage = query().select("pool_id", "pool_name", "status", "admin_user_id", "member_user_id", "member_dept_id").page(pageEntity.parse());
        BasePage<CrmCustomerPoolVO> voBasePage = new BasePage<>(basePage.getCurrent(), basePage.getSize(), basePage.getTotal(), basePage.isSearchCount());
        basePage.getList().forEach(pool -> {
            Integer count = customerPoolRelationService.lambdaQuery().eq(CrmCustomerPoolRelation::getPoolId, pool.getPoolId()).count();
            CrmCustomerPoolVO customerPoolVO = new CrmCustomerPoolVO();
            customerPoolVO.setPoolId(pool.getPoolId());
            customerPoolVO.setCustomerNum(count);
            customerPoolVO.setPoolName(pool.getPoolName());
            customerPoolVO.setStatus(pool.getStatus());
            customerPoolVO.setAdminUser(adminService.queryUserByIds(StrUtil.splitTrim(pool.getAdminUserId(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList())).getData());
            customerPoolVO.setMemberUser(adminService.queryUserByIds(StrUtil.splitTrim(pool.getMemberUserId(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList())).getData());
            customerPoolVO.setMemberDept(adminService.queryDeptByIds(StrUtil.splitTrim(pool.getMemberDeptId(), Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList())).getData());
            voBasePage.getList().add(customerPoolVO);
        });
        return voBasePage;
    }

    /**
     * 后台公海选择列表
     */
    @Override
    public List<CrmCustomerPool> queryPoolNameList() {
        return lambdaQuery().select(CrmCustomerPool::getPoolId, CrmCustomerPool::getPoolName).list();
    }

    /**
     * 修改公海状态
     *
     * @param poolId 公海ID
     * @param status 状态
     */
    @Override
    public void changeStatus(Integer poolId, Integer status) {
        Integer count = customerPoolRelationService.lambdaQuery().eq(CrmCustomerPoolRelation::getPoolId, poolId).count();
        if (count > 0 && status == 0) {
            throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_EXIST_USER_ERROR);
        }
        Integer poolNum = lambdaQuery().eq(CrmCustomerPool::getStatus, 1).count();
        CrmCustomerPool pool = getById(poolId);
        if (pool.getStatus().equals(1) && poolNum.equals(1) && status.equals(0)) {
            throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_LAST_ERROR);
        }
        pool.setStatus(status);
        updateById(pool);
    }

    /**
     * @param prePoolId  原公海ID
     * @param postPoolId 转移的公海ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(Integer prePoolId, Integer postPoolId) {
        CrmCustomerPool pool = getById(postPoolId);
        if (pool == null) {
            return;
        }
        if (pool.getStatus() == 0) {
            throw new CrmException(CrmCodeEnum.CRM_POOL_TRANSFER_ERROR);
        }
        //原公海客户Ids
        List<Integer> oldLists = customerPoolRelationService.lambdaQuery()
                .eq(CrmCustomerPoolRelation::getPoolId, prePoolId)
                .list()
                .stream().map(CrmCustomerPoolRelation::getCustomerId)
                .collect(Collectors.toList());

        //要放入的客户ids
        List<Integer> newLists = customerPoolRelationService.lambdaQuery()
                .eq(CrmCustomerPoolRelation::getPoolId, postPoolId)
                .list()
                .stream().map(CrmCustomerPoolRelation::getCustomerId)
                .collect(Collectors.toList());
        if (oldLists.size() == 0) {
            return;
        }
        List<String> ids = new ArrayList<>();
        for (Integer id : oldLists) {
            ids.add(id.toString());
            if(ids.size() >= 1000){
                transferPoolByEs(ids,prePoolId,postPoolId);
                ids = new ArrayList<>();
            }
        }
        /*
          转移最后剩余的数据以及刷新es索引
         */
        transferPoolByEs(ids,prePoolId,postPoolId);
        getRestTemplate().refresh(CrmEnum.CUSTOMER.getIndex());

        oldLists.removeAll(newLists);
        List<CrmCustomerPoolRelation> poolRelationList = new ArrayList<>(oldLists.size());
        oldLists.forEach(id -> {
            CrmCustomerPoolRelation poolRelation = new CrmCustomerPoolRelation();
            poolRelation.setCustomerId(id);
            poolRelation.setPoolId(postPoolId);
            poolRelationList.add(poolRelation);
        });
        customerPoolRelationService.removeByMap(new JSONObject().fluentPut("pool_id", prePoolId));
        customerPoolRelationService.saveBatch(poolRelationList);
        backLogDealService.removeByMap(new JSONObject().fluentPut("pool_id", prePoolId));

    }

    /**
     * 公海客户转移的es处理
     * @param ids 客户id
     * @param prePoolId 前公海ID
     * @param postPoolId 放入的公海ID
     */
    @SuppressWarnings("unchecked")
    private void transferPoolByEs(List<String> ids,Integer prePoolId, Integer postPoolId){
        if(ids.size() == 0){
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        SearchRequest searchRequest = new SearchRequest(getIndex());
        searchRequest.types(getDocType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        IdsQueryBuilder idsQuery = QueryBuilders.idsQuery();
        idsQuery.ids().addAll(ids);
        searchRequest.source(sourceBuilder.fetchSource(new String[]{"poolId"}, null).query(idsQuery));
        sourceBuilder.size(1000);
        try {
            SearchResponse searchResponse = getRestTemplate().getClient().search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : searchResponse.getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Object poolId = sourceAsMap.get("poolId");
                if (poolId instanceof Collection) {
                    UpdateRequest request = new UpdateRequest(getIndex(), getDocType(), hit.getId());
                    Set<Integer> set = new HashSet<>((Collection<Integer>) poolId);
                    set.remove(prePoolId);
                    set.add(postPoolId);
                    sourceAsMap.put("poolId", set);
                    request.doc(sourceAsMap);
                    bulkRequest.add(request);
                }
            }
            if(bulkRequest.requests().size() > 0){
                getRestTemplate().getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            }
        } catch (IOException e){
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 根据ID查询公海信息
     *
     * @param poolId 公海ID
     * @return data
     */
    @Override
    public CrmCustomerPoolVO queryPoolById(Integer poolId) {
        CrmCustomerPool pool = getById(poolId);
        if (pool == null) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        Integer count = customerPoolRelationService.lambdaQuery().eq(CrmCustomerPoolRelation::getPoolId, pool.getPoolId()).count();
        CrmCustomerPoolVO customerPoolVO = BeanUtil.copyProperties(pool, CrmCustomerPoolVO.class);
        customerPoolVO.setPoolId(pool.getPoolId());
        customerPoolVO.setCustomerNum(count);
        customerPoolVO.setPoolName(pool.getPoolName());
        customerPoolVO.setStatus(pool.getStatus());
        customerPoolVO.setAdminUser(adminService.queryUserByIds(StrUtil.splitTrim(pool.getAdminUserId(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList())).getData());
        customerPoolVO.setMemberUser(adminService.queryUserByIds(StrUtil.splitTrim(pool.getMemberUserId(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList())).getData());
        customerPoolVO.setMemberDept(adminService.queryDeptByIds(StrUtil.splitTrim(pool.getMemberDeptId(), Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList())).getData());
        List<CrmCustomerPoolRule> ruleList = customerPoolRuleService.lambdaQuery().eq(CrmCustomerPoolRule::getPoolId, poolId).list();
        //客户级别设置转为前端需要的数据结构
        Map<Integer, List<CrmCustomerPoolRule>> map = ruleList.stream().collect(Collectors.groupingBy(CrmCustomerPoolRule::getType));
        List<CrmCustomerPoolRule> newRuleList = new ArrayList<>();
        map.forEach((k, v) -> {
            CrmCustomerPoolRule record = new CrmCustomerPoolRule();
            List<JSONObject> levelSettingList = new ArrayList<>();
            v.forEach(r -> {
                record.setRuleId(r.getRuleId());
                record.setPoolId(r.getPoolId());
                record.setType(k);
                record.setDealHandle(r.getDealHandle());
                record.setBusinessHandle(r.getBusinessHandle());
                record.setCustomerLevelSetting(r.getCustomerLevelSetting());
                levelSettingList.add(new JSONObject().fluentPut("level", r.getLevel()).fluentPut("limitDay", r.getLimitDay()));
            });
            record.setLevelSetting(levelSettingList);
            newRuleList.add(record);
        });
        List<CrmCustomerPoolFieldSetting> list = customerPoolFieldSettingService.lambdaQuery().eq(CrmCustomerPoolFieldSetting::getPoolId, poolId).list();
        customerPoolVO.setRule(newRuleList);
        customerPoolVO.setField(list);
        return customerPoolVO;
    }

    /**
     * 查询公海默认字段
     */
    @Override
    public List<CrmModelFiledVO> queryPoolField() {
        List<CrmModelFiledVO> fieldList = crmFieldService.queryField(CrmEnum.CUSTOMER.getType());
        fieldList.removeIf(field -> FieldEnum.DESC_TEXT.getType().equals(field.getType()));
        List<Object> dealStatusList = new ArrayList<>();
        dealStatusList.add(new JSONObject().fluentPut("name", "未成交").fluentPut("value", 0));
        dealStatusList.add(new JSONObject().fluentPut("name", "已成交").fluentPut("value", 1));
        fieldList.add(new CrmModelFiledVO("deal_status", FieldEnum.SELECT, "成交状态", 1).setSetting(dealStatusList).setFormType("dealStatus"));
        fieldList.add(new CrmModelFiledVO().setFieldName("lastTime").setName("最后跟进时间").setType(13));
        fieldList.add(new CrmModelFiledVO().setFieldName("lastContent").setName("最后跟进记录").setType(2));
        fieldList.add(new CrmModelFiledVO().setFieldName("updateTime").setName("更新时间").setType(13));
        fieldList.add(new CrmModelFiledVO().setFieldName("createTime").setName("创建时间").setType(13));
        fieldList.add(new CrmModelFiledVO().setFieldName("createUserId").setName("创建人").setType(10));
        fieldList.add(new CrmModelFiledVO().setFieldName("detailAddress").setName("详细地址").setType(1));
        fieldList.add(new CrmModelFiledVO().setFieldName("address").setName("地区定位").setType(18));
        fieldList.add(new CrmModelFiledVO().setFieldName("preOwnerUserId").setName("前负责人").setType(10));
        fieldList.add(new CrmModelFiledVO().setFieldName("poolTime").setName("进入公海时间").setType(13));
        fieldList.forEach(record -> {
            record.setFieldName(StrUtil.toCamelCase(record.getFieldName()));
            crmFieldService.recordToFormType(record, FieldEnum.parse(record.getType()));
        });
        return fieldList;
    }

    /**
     * 删除客户数据
     *
     * @param ids ids
     */
    @Override
    public void deleteByIds(List<Integer> ids, Integer poolId) {
        CrmCustomerPool customerPool = getById(poolId);
        if (customerPool == null) {
            return;
        }
        boolean isPoolAdmin = StrUtil.splitTrim(customerPool.getAdminUserId(), Const.SEPARATOR).contains(UserUtil.getUserId().toString());
        if (!isPoolAdmin && !UserUtil.isAdmin()) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        crmCustomerService.lambdaUpdate().set(CrmCustomer::getUpdateTime, new Date()).set(CrmCustomer::getStatus, 3).in(CrmCustomer::getCustomerId, ids).update();
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmCustomer::getCustomerId, ids);
        List<String> batchList = crmCustomerService.listObjs(wrapper, Object::toString);
        adminFileService.delete(batchList);
        //删除跟进记录
        crmActivityService.deleteActivityRecord(CrmActivityEnum.CUSTOMER, ids);
        //删除字段操作记录
        crmActionRecordService.deleteActionRecord(CrmEnum.CUSTOMER, ids);
        //删除自定义字段
        crmCustomerDataService.deleteByBatchId(batchList);
        ICrmBackLogDealService dealService = ApplicationContextHolder.getBean(ICrmBackLogDealService.class);
        //删除提醒
        dealService.lambdaUpdate().eq(CrmBackLogDeal::getPoolId, poolId).in(CrmBackLogDeal::getTypeId, ids).remove();
        //删除公海关联
        customerPoolRelationService.lambdaUpdate().in(CrmCustomerPoolRelation::getCustomerId, ids).remove();
        //删除联系人
        ICrmContactsService contactsService = ApplicationContextHolder.getBean(ICrmContactsService.class);
        List<CrmContacts> list = contactsService.lambdaQuery().select(CrmContacts::getContactsId).in(CrmContacts::getCustomerId, ids).list();
        contactsService.deleteByIds(list.stream().map(CrmContacts::getContactsId).collect(Collectors.toList()));
        deletePage(ids);

    }

    /**
     * 获取客户级别选项
     *
     * @return data
     */
    @Override
    public List<String> queryCustomerLevel() {
        CrmField level = crmFieldService.lambdaQuery().eq(CrmField::getLabel, 2).eq(CrmField::getFieldName, "level").one();
        return StrUtil.splitTrim(level.getOptions(), Const.SEPARATOR);
    }

    /**
     * 设置公海规则
     *
     * @param jsonObject obj
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setCustomerPool(JSONObject jsonObject) {
        CrmCustomerPool customerPool = JSONObject.toJavaObject(jsonObject, CrmCustomerPool.class);
        if (customerPool.getPoolId() == null) {
            if ((StrUtil.isEmpty(customerPool.getMemberUserId()) && StrUtil.isEmpty(customerPool.getMemberDeptId())) || StrUtil.isEmpty(customerPool.getAdminUserId())) {
                throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_USER_IS_NULL_ERROR);
            }
            customerPool.setCreateUserId(UserUtil.getUserId());
            customerPool.setCreateTime(DateUtil.date());
            save(customerPool);
        } else {
            updateById(customerPool);
            JSONObject object = new JSONObject().fluentPut("pool_id", customerPool.getPoolId());
            backLogDealService.removeByMap(object);
            customerPoolRuleService.removeByMap(object);
        }
        JSONArray ruleList = jsonObject.getJSONArray("rule");
        ruleList.forEach(object -> {
            JSONObject rule = TypeUtils.castToJavaBean(object, JSONObject.class);
            JSONArray levelSettingList = rule.getJSONArray("level");
            levelSettingList.forEach(levelObject -> {
                CrmCustomerPoolRule crmCustomerPoolRule = TypeUtils.castToJavaBean(levelObject, CrmCustomerPoolRule.class);
                crmCustomerPoolRule.setPoolId(customerPool.getPoolId());
                crmCustomerPoolRule.setType(rule.getInteger("type"));
                crmCustomerPoolRule.setDealHandle(rule.getInteger("dealHandle"));
                crmCustomerPoolRule.setBusinessHandle(rule.getInteger("businessHandle"));
                crmCustomerPoolRule.setCustomerLevelSetting(rule.getInteger("customerLevelSetting"));
                customerPoolRuleService.save(crmCustomerPoolRule);
            });
        });
        List<CrmCustomerPoolFieldSetting> fieldList = jsonObject.getJSONArray("field").toJavaList(CrmCustomerPoolFieldSetting.class);
        List<CrmCustomerPoolFieldSetting> list = customerPoolFieldSettingService.lambdaQuery().eq(CrmCustomerPoolFieldSetting::getPoolId, customerPool.getPoolId()).list();
        List<String> collect = fieldList.stream().map(CrmCustomerPoolFieldSetting::getFieldName).collect(Collectors.toList());
        list.removeIf(obj->collect.contains(obj.getFieldName()));
        if (CollUtil.isNotEmpty(list)){
            customerPoolFieldSettingService.removeByIds(list.stream().map(CrmCustomerPoolFieldSetting::getSettingId).collect(Collectors.toList()));
        }
        for (CrmCustomerPoolFieldSetting field : fieldList) {
            field.setPoolId(customerPool.getPoolId());
            if (field.getSettingId() != null) {
                customerPoolFieldSettingService.updateById(field);
                ICrmCustomerPoolFieldSortService bean = ApplicationContextHolder.getBean(ICrmCustomerPoolFieldSortService.class);
                //同步更新列表页字段排序表
                String fieldName = field.getFieldName();
                if ("preOwnerUserId".equals(fieldName)){
                    fieldName = "preOwnerUserName";
                }else if ("createUserId".equals(fieldName)){
                    fieldName = "createUserName";
                }
                if (field.getIsHidden() == 1) {
                    bean.removeByMap(new JSONObject().fluentPut("field_name", fieldName).fluentPut("pool_id", customerPool.getPoolId()));
                } else {
                    bean.lambdaUpdate()
                            .set(CrmCustomerPoolFieldSort::getName, field.getName())
                            .eq(CrmCustomerPoolFieldSort::getPoolId, customerPool.getPoolId())
                            .eq(CrmCustomerPoolFieldSort::getFieldId, field.getFieldId())
                            .update();
                }
            } else {
                customerPoolFieldSettingService.save(field);
            }
        }
    }

    /**
     * 查询公海字段配置
     *
     * @param poolId 公海ID
     * @return data
     */
    @Override
    public JSONObject queryPoolFieldConfig(Integer poolId) {
        Long userId = UserUtil.getUserId();
        //查出自定义字段，查看顺序表是否存在该字段，没有则插入，设为隐藏
        List<CrmCustomerPoolFieldSetting> fieldList = customerPoolFieldSettingService.lambdaQuery().eq(CrmCustomerPoolFieldSetting::getPoolId, poolId).eq(CrmCustomerPoolFieldSetting::getIsHidden, 0).list();
        List<String> list = crmCustomerPoolFieldSortService.lambdaQuery()
                .select(CrmCustomerPoolFieldSort::getFieldName)
                .eq(CrmCustomerPoolFieldSort::getUserId, userId)
                .eq(CrmCustomerPoolFieldSort::getPoolId, poolId)
                .list().stream().map(CrmCustomerPoolFieldSort::getFieldName).collect(Collectors.toList());
        fieldList.removeIf(setting -> list.contains(StrUtil.toCamelCase(setting.getFieldName())));
        for (CrmCustomerPoolFieldSetting record : fieldList) {
            CrmCustomerPoolFieldSort newField = new CrmCustomerPoolFieldSort();
            newField.setFieldName(StrUtil.toCamelCase(record.getFieldName())).setType(record.getType()).setName(record.getName()).setPoolId(poolId).setIsHidden(1).setUserId(userId).setSort(1);
            crmCustomerPoolFieldSortService.save(newField);
        }
        List<CrmCustomerPoolFieldSort> noHideList = crmCustomerPoolFieldSortService.lambdaQuery()
                .select(CrmCustomerPoolFieldSort::getId, CrmCustomerPoolFieldSort::getName, CrmCustomerPoolFieldSort::getFieldName)
                .eq(CrmCustomerPoolFieldSort::getPoolId, poolId)
                .eq(CrmCustomerPoolFieldSort::getUserId, userId)
                .eq(CrmCustomerPoolFieldSort::getIsHidden, 0)
                .orderByAsc(CrmCustomerPoolFieldSort::getSort)
                .list();
        List<CrmCustomerPoolFieldSort> hideList = crmCustomerPoolFieldSortService.lambdaQuery()
                .select(CrmCustomerPoolFieldSort::getId, CrmCustomerPoolFieldSort::getName, CrmCustomerPoolFieldSort::getFieldName)
                .eq(CrmCustomerPoolFieldSort::getPoolId, poolId)
                .eq(CrmCustomerPoolFieldSort::getUserId, userId)
                .eq(CrmCustomerPoolFieldSort::getIsHidden, 1)
                .orderByAsc(CrmCustomerPoolFieldSort::getSort)
                .list();
        return new JSONObject().fluentPut("value", noHideList).fluentPut("hide_value", hideList);
    }

    /**
     * 公海展示配置
     *
     * @param object obj
     */
    @Override
    public void poolFieldConfig(JSONObject object) {
        JSONArray hideFields = object.getJSONArray("hideFields");
        JSONArray noHideFields = object.getJSONArray("noHideFields");
        List<Integer> hideIds =new ArrayList<>(hideFields.size());
        List<Integer> noHideIds =new ArrayList<>(noHideFields.size());
        for (int i = 0; i < hideFields.size(); i++) {
            hideIds.add(hideFields.getJSONObject(i).getInteger("id"));
        }
        for (int i = 0; i < noHideFields.size(); i++) {
            noHideIds.add(noHideFields.getJSONObject(i).getInteger("id"));
        }
        if (noHideIds.size() < 2) {
            throw new CrmException(CrmCodeEnum.CRM_POOL_FIELD_HIDE_ERROR);
        }
        List<CrmCustomerPoolFieldSort> crmCustomerPoolFieldSorts = new ArrayList<>();
        for (int i = 0; i < noHideIds.size(); i++) {
            CrmCustomerPoolFieldSort sorts = new CrmCustomerPoolFieldSort();
            sorts.setId(noHideIds.get(i));
            sorts.setSort(i);
            sorts.setIsHidden(0);
            crmCustomerPoolFieldSorts.add(sorts);
        }
        if (CollUtil.isNotEmpty(hideIds)) {
            hideIds.forEach(sort -> crmCustomerPoolFieldSorts.add(new CrmCustomerPoolFieldSort().setId(sort).setIsHidden(1)));
        }
        crmCustomerPoolFieldSortService.updateBatchById(crmCustomerPoolFieldSorts);
    }

    /**
     * 删除公海
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomerPool(Integer poolId) {
        Integer customerNum = customerPoolRelationService.lambdaQuery().eq(CrmCustomerPoolRelation::getPoolId, poolId).count();
        if (customerNum > 0) {
            throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_EXIST_USER_DELETE_ERROR);
        }
        Integer poolNum = lambdaQuery().eq(CrmCustomerPool::getStatus, 1).count();
        CrmCustomerPool pool = getById(poolId);
        if (pool.getStatus().equals(1) && poolNum.equals(1)) {
            throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_LAST_DELETE_ERROR);
        }
        removeById(poolId);
        JSONObject object = new JSONObject().fluentPut("pool_id", poolId);
        //删除待办
        backLogDealService.removeByMap(object);
        //删除公海规则
        customerPoolRuleService.removeByMap(object);
        //删除字段设置
        customerPoolFieldSettingService.removeByMap(object);
    }

    /**
     * 查询公海客户列表
     *
     * @param search
     */
    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        search.setLabel(CrmEnum.CUSTOMER_POOL.getType());
        BasePage<Map<String, Object>> basePage = queryList(search,false);
        basePage.getList().forEach(map -> {
            map.put("poolId", search.getPoolId());
        });
        return basePage;
    }

    /**
     * 查询前台公海列表
     */
    @Override
    public List<CrmCustomerPool> queryPoolNameListByAuth() {
        UserInfo user = UserUtil.getUser();
        List<CrmCustomerPool> list = lambdaQuery().select(CrmCustomerPool::getPoolId, CrmCustomerPool::getPoolName, CrmCustomerPool::getMemberUserId, CrmCustomerPool::getAdminUserId, CrmCustomerPool::getMemberDeptId)
                .eq(CrmCustomerPool::getStatus, 1)
                .orderByDesc(CrmCustomerPool::getCreateTime)
                .list();
        if (!UserUtil.isAdmin()) {
            list.removeIf(pool -> {
                boolean isAdmin = StrUtil.splitTrim(pool.getAdminUserId(), Const.SEPARATOR).contains(user.getUserId().toString());
                boolean isMember = StrUtil.splitTrim(pool.getMemberUserId(), Const.SEPARATOR).contains(user.getUserId().toString());
                boolean isDept = StrUtil.splitTrim(pool.getMemberDeptId(), Const.SEPARATOR).contains(user.getDeptId().toString());
                return !isAdmin && !isMember && !isDept;
            });
        }
        list.forEach(pool -> {
            pool.setMemberDeptId(null);
            pool.setMemberUserId(null);
            pool.setAdminUserId(null);
        });
        return list;
    }


    @Override
    public List<Integer> queryPoolIdByUserId() {
        return getBaseMapper().queryPoolIdByUserId(UserUtil.getUserId(), UserUtil.getUser().getDeptId());
    }

    /**
     * 查询前台公海字段
     */
    @Override
    public List<CrmCustomerPoolFieldSort> queryPoolListHead(Integer poolId) {
        Long userId = UserUtil.getUserId();
        LambdaQueryWrapper<CrmCustomerPoolFieldSort> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmCustomerPoolFieldSort::getId, CrmCustomerPoolFieldSort::getFieldId, CrmCustomerPoolFieldSort::getFieldName, CrmCustomerPoolFieldSort::getName, CrmCustomerPoolFieldSort::getType, CrmCustomerPoolFieldSort::getIsHidden);
        wrapper.eq(CrmCustomerPoolFieldSort::getPoolId, poolId).eq(CrmCustomerPoolFieldSort::getUserId, userId);
        wrapper.orderByAsc(CrmCustomerPoolFieldSort::getSort);
        List<CrmCustomerPoolFieldSort> list = crmCustomerPoolFieldSortService.list(wrapper);
        if (list.size() == 0) {
            List<CrmCustomerPoolFieldSetting> settings = customerPoolFieldSettingService.lambdaQuery().eq(CrmCustomerPoolFieldSetting::getPoolId, poolId).eq(CrmCustomerPoolFieldSetting::getIsHidden, 0).list();
            for (int i = 0; i < settings.size(); i++) {
                CrmCustomerPoolFieldSetting setting = settings.get(i);
                CrmCustomerPoolFieldSort sort = BeanUtil.copyProperties(setting, CrmCustomerPoolFieldSort.class);
                if ("preOwnerUserId".equals(setting.getFieldName())){
                    setting.setFieldName("preOwnerUserName");
                }else if ("createUserId".equals(setting.getFieldName())){
                    setting.setFieldName("createUserName");
                }
                sort.setFieldName(StrUtil.toCamelCase(setting.getFieldName()));
                sort.setUserId(userId).setSort(i).setIsHidden(0);
                sort.setFieldId(setting.getFieldId());
                list.add(sort);
            }
            crmCustomerPoolFieldSortService.saveBatch(list);
        }
        list.removeIf(fieldSort -> fieldService.equalsByType(fieldSort.getType(),FieldEnum.DESC_TEXT,FieldEnum.DETAIL_TABLE,FieldEnum.FILE));
        list.forEach(fieldSort -> {
            if ("website".equals(fieldSort.getFieldName())) {
                fieldSort.setFormType(FieldEnum.WEBSITE.getFormType());
            }else {
                fieldSort.setFormType(FieldEnum.parse(fieldSort.getType()).getFormType());
            }
        });
        ICrmCustomerPoolFieldStyleService styleService = ApplicationContextHolder.getBean(ICrmCustomerPoolFieldStyleService.class);
        list.removeIf(sort -> !sort.getIsHidden().equals(0));
        List<CrmCustomerPoolFieldStyle> fieldStyles = styleService.lambdaQuery().eq(CrmCustomerPoolFieldStyle::getPoolId, poolId).eq(CrmCustomerPoolFieldStyle::getUserId, userId).list();
        list.forEach(sort -> {
            for (CrmCustomerPoolFieldStyle fieldStyle : fieldStyles) {
                if (Objects.equals(sort.getFieldName(), fieldStyle.getFieldName())) {
                    sort.setWidth(fieldStyle.getStyle());
                    break;
                }
            }
            if (sort.getWidth() == null) {
                sort.setWidth(100);
            }
        });
        return list;
    }

    /**
     * 查询公海权限
     *
     * @param poolId 公海ID
     * @return auth
     */
    @Override
    public JSONObject queryAuthByPoolId(Integer poolId) {
        LambdaQueryWrapper<CrmCustomerPool> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmCustomerPool::getAdminUserId);
        wrapper.eq(CrmCustomerPool::getPoolId, poolId);
        String adminUserIds = getOne(wrapper).getAdminUserId();
        List<Long> adminUserIdList = Arrays.stream(adminUserIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        JSONObject record = new JSONObject().fluentPut("index", true).fluentPut("receive", true).fluentPut("delete", false).fluentPut("distribute", false).fluentPut("excelexport", false);
        if (adminUserIdList.contains(UserUtil.getUserId()) || UserUtil.isAdmin()) {
            record.fluentPut("delete", true).fluentPut("distribute", true).fluentPut("excelexport", true);
        }
        return record;
    }


    @Override
    public JSONObject getOnePoolAuthByPoolIds(List<Integer> poolIdList) {
        JSONObject record = new JSONObject();
        if (CollUtil.isEmpty(poolIdList)){
            return record;
        }
        if (!UserUtil.isAdmin()){
            List<Integer> poolIds = this.queryPoolIdByUserId();
            if (CollUtil.isEmpty(poolIds)){
                return record;
            }
            boolean b = poolIdList.stream().anyMatch(poolIds::contains);
            if (!b){
                return record;
            }
        }
        LambdaQueryWrapper<CrmCustomerPool> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmCustomerPool::getAdminUserId,CrmCustomerPool::getPoolId);
        wrapper.in(CrmCustomerPool::getPoolId, poolIdList);
        List<CrmCustomerPool> crmCustomerPoolList = list(wrapper);
        if (CollUtil.isNotEmpty(crmCustomerPoolList)){
            record.fluentPut("index", true).fluentPut("receive", true).fluentPut("delete", false)
                    .fluentPut("distribute", false).fluentPut("excelexport", false);
            for (CrmCustomerPool crmCustomerPool : crmCustomerPoolList) {
                String adminUserIds = crmCustomerPool.getAdminUserId();
                List<Long> adminUserIdList = Arrays.stream(adminUserIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
                if (adminUserIdList.contains(UserUtil.getUserId()) || UserUtil.isAdmin()) {
                    record.fluentPut("delete", true).fluentPut("distribute", true).fluentPut("excelexport", true);
                    record.fluentPut("poolId",crmCustomerPool.getPoolId());
                    return record;
                }
            }
            record.fluentPut("poolId",crmCustomerPoolList.get(0).getPoolId());
        }
        return record;
    }

    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"customerName", "telephone", "mobile"};
    }

    /**
     * 设置其他冗余字段
     *
     * @param map
     */
    @Override
    public void setOtherField(Map<String, Object> map) {

    }

    @Override
    public Dict getSearchTransferMap() {
        return Dict.create().set("createUserName", "createUserId").set("preOwnerUserName","preOwnerUserId");
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    @Override
    public CrmEnum getLabel() {
        return CrmEnum.CUSTOMER;
    }

    /**
     * 查询所有字段
     *
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        return ApplicationContextHolder.getBean(CrmCustomerServiceImpl.class).queryDefaultField();
    }
}
