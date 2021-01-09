package com.kakarote.crm.controller;

import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.crm.service.ICrmBusinessService;
import com.kakarote.crm.service.ICrmContractService;
import com.kakarote.crm.service.ICrmCustomerService;
import com.kakarote.crm.service.ICrmLeadsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crmEvent")
@Api(value = "oa日程模块远程调用接口")
public class CrmEventController {


    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ICrmLeadsService leadsService;

    @Autowired
    private ICrmBusinessService crmBusinessService;

    @PostMapping("/endContract")
    @ApiExplain("到期的合同日期")
    public Result<List<String>> endContract(@RequestBody CrmEventBO crmEventBO){
        List<String> list = crmContractService.endContract(crmEventBO);
        return Result.ok(list);
    }

    @PostMapping("/eventCustomer")
    @ApiExplain("需要联系的客户日期")
    public Result<List<String>> eventCustomer(@RequestBody CrmEventBO crmEventBO){
        List<String> list = crmCustomerService.eventCustomer(crmEventBO);
        return Result.ok(list);
    }

    @PostMapping("/eventLeads")
    @ApiExplain("需要联系的线索日期")
    public Result<List<String>> eventLeads(@RequestBody CrmEventBO crmEventBO){
        List<String> list = leadsService.eventLeads(crmEventBO);
        return Result.ok(list);
    }

    @PostMapping("/eventBusiness")
    @ApiExplain("需要联系的商机日期")
    public Result<List<String>> eventBusiness(@RequestBody CrmEventBO crmEventBO){
        List<String> list = crmBusinessService.eventBusiness(crmEventBO);
        return Result.ok(list);
    }

    @PostMapping("/eventDealBusiness")
    @ApiExplain("预计成交的商机日期")
    public Result<List<String>> eventDealBusiness(@RequestBody CrmEventBO crmEventBO){
        List<String> list = crmBusinessService.eventDealBusiness(crmEventBO);
        return Result.ok(list);
    }

    @PostMapping("/receiveContract")
    @ApiExplain("计划回款日期")
    public Result<List<String>> receiveContract(@RequestBody CrmEventBO crmEventBO){
        List<String> list = crmContractService.receiveContract(crmEventBO);
        return Result.ok(list);
    }

    @PostMapping("/eventCustomerPageList")
    @ApiExplain("日程客户列表")
    public Result<BasePage<Map<String, Object>>> eventCustomerPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String, Object>> page = crmCustomerService.eventCustomerPageList(eventCrmPageBO);
        return Result.ok(page);
    }

    @PostMapping("/eventLeadsPageList")
    @ApiExplain("日程线索列表")
    public Result<BasePage<Map<String, Object>>> eventLeadsPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String, Object>> page = leadsService.eventLeadsPageList(eventCrmPageBO);
        return Result.ok(page);
    }

    @PostMapping("/eventBusinessPageList")
    @ApiExplain("日程商机列表")
    public Result<BasePage<Map<String, Object>>> eventBusinessPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String, Object>> page = crmBusinessService.eventBusinessPageList(eventCrmPageBO);
        return Result.ok(page);
    }

    @PostMapping("/eventDealBusinessPageList")
    @ApiExplain("预计成交商机列表")
    public Result<BasePage<Map<String, Object>>> eventDealBusinessPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String, Object>> page = crmBusinessService.eventDealBusinessPageList(eventCrmPageBO);
        return Result.ok(page);
    }

    /**
     * type = 1 当日到期合同
     * type = 2 当日回款合同
     */
    @PostMapping("/eventContractPageList")
    @ApiExplain("日程合同列表")
    public Result<BasePage<Map<String, Object>>> eventContractPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO){
        BasePage<Map<String, Object>> page = crmContractService.eventContractPageList(eventCrmPageBO);
        return Result.ok(page);
    }




}
