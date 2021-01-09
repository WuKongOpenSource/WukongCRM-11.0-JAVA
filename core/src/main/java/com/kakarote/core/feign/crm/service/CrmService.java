package com.kakarote.core.feign.crm.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmSearchBO;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.crm.service.impl.CrmServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = "crm", contextId = "", fallback = CrmServiceImpl.class)
public interface CrmService {

    /**
     * 查询客户信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmCustomer/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryCustomerInfo(@RequestBody Collection ids);


    /**
     * 查询客户信息(like)
     *
     * @param name 名字
     * @return entity
     */
    @PostMapping("/crmCustomer/queryByNameCustomerInfo")
    public Result<List<SimpleCrmEntity>> queryByNameCustomerInfo(@RequestParam("name") String name);


    /**
     * 查询客户信息(eq)
     *
     * @param name 名字
     * @return entity
     */
    @PostMapping("/crmCustomer/queryNameCustomerInfo")
    public Result<List<SimpleCrmEntity>> queryNameCustomerInfo(@RequestParam("name") String name);

    /**
     * 查询联系人信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmContacts/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryContactsInfo(@RequestBody Collection ids);

    /**
     * 查询商机信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmBusiness/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryBusinessInfo(@RequestBody Collection ids);

    /**
     * 查询合同信息
     *
     * @param ids ids
     * @return entity
     */
    @PostMapping("/crmContract/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryContractInfo(@RequestBody Collection ids);

    @PostMapping("/crmProduct/querySimpleEntity")
    public Result<List<SimpleCrmEntity>> queryProductInfo();

    /**
     * 添加活动记录
     *
     * @param type
     * @param activityType   活动类型
     * @param activityTypeId 类型ID
     * @return
     */
    @PostMapping("/crmActivity/addActivity")
    Result addActivity(@RequestParam("type") Integer type, @RequestParam("activityType") Integer activityType, @RequestParam("activityTypeId") Integer activityTypeId);

    @PostMapping(value = "/crmField/batchUpdateEsData")
    Result batchUpdateEsData(@RequestParam("id") String id, @RequestParam("name") String name);

    @PostMapping("/crmCustomerJob/putInInternational")
    Result putInInternational();

    @PostMapping("/crmCustomerPool/queryPoolNameListByAuth")
    Result<List> queryPoolNameListByAuth() ;

    @PostMapping("/crmField/queryExamineField")
    public Result<List<ExamineField>> queryExamineField(@RequestParam("label") Integer label);

    @PostMapping("/crmCustomer/queryPageList")
    @ApiOperation("查询列表页数据")
    Result<BasePage<Map<String, Object>>> queryCustomerPageList(@RequestBody CrmSearchBO search);

}
