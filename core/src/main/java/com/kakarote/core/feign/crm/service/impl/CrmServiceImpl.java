package com.kakarote.core.feign.crm.service.impl;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmSearchBO;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.crm.service.CrmService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class CrmServiceImpl implements CrmService {
    /**
     * 查询客户信息
     *
     * @param ids ids
     * @return entity
     */
    @Override
    public Result<List<SimpleCrmEntity>> queryCustomerInfo(Collection ids) {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 查询客户信息(like)
     *
     * @param name 名字
     * @return entity
     */
    @Override
    public Result<List<SimpleCrmEntity>> queryByNameCustomerInfo(String name) {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 查询客户信息(eq)
     *
     * @param name 名字
     * @return entity
     */
    @Override
    public Result<List<SimpleCrmEntity>> queryNameCustomerInfo(String name) {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 查询联系人信息
     *
     * @param ids ids
     * @return entity
     */
    @Override
    public Result<List<SimpleCrmEntity>> queryContactsInfo(Collection ids) {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 查询商机信息
     *
     * @param ids ids
     * @return entity
     */
    @Override
    public Result<List<SimpleCrmEntity>> queryBusinessInfo(Collection ids) {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 查询合同信息
     *
     * @param ids ids
     * @return entity
     */
    @Override
    public Result<List<SimpleCrmEntity>> queryContractInfo(Collection ids) {
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result<List<SimpleCrmEntity>> queryProductInfo() {
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result addActivity(Integer type, Integer activityType, Integer activityTypeId) {
        return Result.ok();
    }

    @Override
    public Result batchUpdateEsData(String id, String name) {
        return Result.ok();
    }

    @Override
    public Result putInInternational() {
        return Result.ok();
    }

    @Override
    public Result<List> queryPoolNameListByAuth() {
        return Result.ok(new ArrayList());
    }

    @Override
    public Result<List<ExamineField>> queryExamineField(Integer label) {
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result<BasePage<Map<String, Object>>> queryCustomerPageList(CrmSearchBO search) {
        return Result.ok(new BasePage<>());
    }
}
