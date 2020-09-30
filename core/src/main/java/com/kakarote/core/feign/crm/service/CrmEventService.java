package com.kakarote.core.feign.crm.service;

import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "crm",contextId = "crmEvent")
public interface CrmEventService {

    @PostMapping("/crmEvent/endContract")
    @ApiExplain("到期的合同日期")
    Result<List<String>> endContract(@RequestBody CrmEventBO crmEventBO);

    @PostMapping("/crmEvent/eventCustomer")
    @ApiExplain("需要联系的客户日期")
    Result<List<String>> eventCustomer(@RequestBody CrmEventBO crmEventBO);

    @PostMapping("/crmEvent/eventLeads")
    @ApiExplain("需要联系的线索日期")
    Result<List<String>> eventLeads(@RequestBody CrmEventBO crmEventBO);

    @PostMapping("/crmEvent/eventBusiness")
    @ApiExplain("需要联系的商机日期")
    Result<List<String>> eventBusiness(@RequestBody CrmEventBO crmEventBO);

    @PostMapping("/crmEvent/eventDealBusiness")
    @ApiExplain("预计成交的商机日期")
    Result<List<String>> eventDealBusiness(@RequestBody CrmEventBO crmEventBO);

    @PostMapping("/crmEvent/receiveContract")
    @ApiExplain("计划回款日期")
    Result<List<String>> receiveContract(@RequestBody CrmEventBO crmEventBO);

    @PostMapping("/crmEvent/eventCustomerPageList")
    @ApiExplain("日程客户列表")
    Result<BasePage<Map<String, Object>>> eventCustomerPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO);

    @PostMapping("/crmEvent/eventLeadsPageList")
    @ApiExplain("日程线索列表")
    Result<BasePage<Map<String, Object>>> eventLeadsPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO);

    @PostMapping("/crmEvent/eventBusinessPageList")
    @ApiExplain("日程商机列表")
    Result<BasePage<Map<String, Object>>> eventBusinessPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO);

    @PostMapping("/crmEvent/eventDealBusinessPageList")
    @ApiExplain("预计成交商机列表")
    Result<BasePage<Map<String, Object>>> eventDealBusinessPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO);

    @PostMapping("/crmEvent/eventContractPageList")
    @ApiExplain("日程合同列表")
    Result<BasePage<Map<String, Object>>> eventContractPageList(@RequestBody QueryEventCrmPageBO eventCrmPageBO);
}
