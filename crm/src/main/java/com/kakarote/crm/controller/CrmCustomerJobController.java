package com.kakarote.crm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmCustomerPoolBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.mapper.CrmCustomerPoolMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户定时放入公海接口
 *
 * @author hmb
 */
@RestController
@RequestMapping("/crmCustomerJob")
@Slf4j
public class CrmCustomerJobController {

    @Autowired
    private ICrmCustomerPoolService crmCustomerPoolService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ICrmCustomerPoolRuleService crmCustomerPoolRuleService;

    @Autowired
    private CrmCustomerPoolMapper crmCustomerPoolMapper;

    @Autowired
    private ICrmCustomerPoolRelationService crmCustomerPoolRelationService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ICrmOwnerRecordService crmOwnerRecordService;

    @Resource
    private CrmPageService customerPoolService;

    @Autowired
    private Redis redis;



    @ApiExplain("放入公海")
    @PostMapping("/putInInternational")
    @Transactional(rollbackFor = Exception.class)
    public Result putInInternational(){
        try {
            UserInfo userInfo = redis.get(CrmCacheKey.CRM_CUSTOMER_JOB_CACHE_KEY);
            List<CrmCustomerPool> poolList = crmCustomerPoolService.lambdaQuery()
                    .eq(CrmCustomerPool::getStatus, 1).eq(CrmCustomerPool::getPutInRule, 1).list();
            poolList.forEach(pool -> {
                List<Long> userIdsList = new ArrayList<>();
                if (StrUtil.isNotEmpty(pool.getMemberDeptId())) {
                    userIdsList = adminService.queryUserByDeptIds(TagUtil.toSet(pool.getMemberDeptId())).getData();
                }
                if (StrUtil.isNotEmpty(pool.getMemberUserId())) {
                    userIdsList.addAll(TagUtil.toLongSet(pool.getMemberUserId()));
                }
                Set<Integer> customerIdSet = new HashSet<>();
                List<CrmCustomerPoolRule> ruleList = crmCustomerPoolRuleService.lambdaQuery().eq(CrmCustomerPoolRule::getPoolId,pool.getPoolId()).list();
                for (CrmCustomerPoolRule rule : ruleList) {
                    Map<String, Object> record = BeanUtil.beanToMap(rule);
                    if (CollectionUtil.isNotEmpty(userIdsList)) {
                        record.put("ids", userIdsList);
                    }
                    if (rule.getType().equals(1)) {
                        Set<Integer> ids = crmCustomerPoolMapper.putInPoolByRecord(record);
                        customerIdSet.addAll(ids);
                    } else if (rule.getType().equals(2)) {
                        Set<Integer> ids = crmCustomerPoolMapper.putInPoolByBusiness(record);
                        customerIdSet.addAll(ids);
                    } else if (rule.getType().equals(3)) {
                        Set<Integer> ids = crmCustomerPoolMapper.putInPoolByDealStatus(record);
                        customerIdSet.addAll(ids);
                    }
                }
                List<Integer> customerIdList = crmCustomerPoolRelationService.lambdaQuery().select(CrmCustomerPoolRelation::getCustomerId)
                        .eq(CrmCustomerPoolRelation::getPoolId,pool.getPoolId()).list()
                        .stream().map(CrmCustomerPoolRelation::getCustomerId).collect(Collectors.toList());
                customerIdSet.removeAll(customerIdList);
                customerIdSet.forEach(customerId -> {
                    CrmCustomer crmCustomer = crmCustomerService.getById(customerId);
                    if (ObjectUtil.isNotEmpty(crmCustomer.getOwnerUserId())) {
                        CrmOwnerRecord crmOwnerRecord = new CrmOwnerRecord();
                        crmOwnerRecord.setTypeId(customerId);
                        crmOwnerRecord.setType(CrmEnum.CUSTOMER_POOL.getType());
                        crmOwnerRecord.setPreOwnerUserId(crmCustomer.getOwnerUserId());
                        crmOwnerRecordService.save(crmOwnerRecord);
                        crmCustomerService.lambdaUpdate()
                                .set(CrmCustomer::getPreOwnerUserId,crmOwnerRecord.getPreOwnerUserId())
                                .set(CrmCustomer::getOwnerUserId,null)
                                .set(CrmCustomer::getPoolTime,new Date())
                                .set(CrmCustomer::getIsReceive, null)
                                .set(CrmCustomer::getRoUserId, ",")
                                .set(CrmCustomer::getRwUserId, ",")
                                .eq(CrmCustomer::getCustomerId,customerId).update();
                    }
                    CrmCustomerPoolRelation customerPoolRelation = new CrmCustomerPoolRelation();
                    customerPoolRelation.setCustomerId(customerId);
                    customerPoolRelation.setPoolId(pool.getPoolId());
                    crmCustomerPoolRelationService.save(customerPoolRelation);
                });
                if (CollUtil.isNotEmpty(customerIdSet)){
                    ApplicationContextHolder.getBean(ICrmContactsService.class).lambdaUpdate()
                            .set(CrmContacts::getOwnerUserId,null)
                            .in(CrmContacts::getCustomerId,customerIdSet).update();
                    CrmCustomerPoolBO crmCustomerPoolBO = new CrmCustomerPoolBO();
                    crmCustomerPoolBO.setIds(Lists.newArrayList(customerIdSet));
                    crmCustomerPoolBO.setPoolId(pool.getPoolId());
                    customerPoolService.putInPool(crmCustomerPoolBO);
                }
            });
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(500,e.getMessage());
        }
    }


}
