package com.kakarote.crm.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.R;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

/**
 * @author hmb
 * crm权限拦截切面
 */
@Aspect
@Component
@Slf4j
@Order(10)
public class CrmAuthAspect {


    @Around("execution(* com.kakarote.crm.controller..*(..)) && execution(@(org.springframework.web.bind.annotation.*Mapping) * *(..)) && !execution(@(com.kakarote.core.common.ParamAspect) * *(..))")
    public Object before(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = attributes.getRequest();
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        Long userId = UserUtil.getUserId();
        boolean flag = false;
        CrmEnum crmEnum = CrmEnum.parse(split[1].substring(3).toUpperCase());
        if (crmEnum  != null && !userId.equals(UserUtil.getSuperUser())) {
            if ("add".equals(split[2]) || "update".equals(split[2])) {
                BufferedReader streamReader = request.getReader();
                StringBuilder rawData = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    rawData.append(inputStr);
                }
                JSONObject jsonObject = JSON.parseObject(rawData.toString());
                if (crmEnum.equals(CrmEnum.CUSTOMER)) {
                    CrmCustomer entity = jsonObject.getObject("entity", CrmCustomer.class);
                    if (entity.getCustomerId() != null) {
                        flag = AuthUtil.isCrmAuth(crmEnum, entity.getCustomerId());
                    }
                } else if (crmEnum.equals(CrmEnum.LEADS)) {
                    CrmLeads entity = jsonObject.getObject("entity", CrmLeads.class);
                    if (entity.getLeadsId() != null) {
                        flag = AuthUtil.isCrmAuth(crmEnum, entity.getLeadsId());
                    }
                } else if (crmEnum.equals(CrmEnum.CONTRACT)) {
                    CrmContract entity = jsonObject.getObject("entity", CrmContract.class);
                    if (entity.getContractId() != null) {
                        flag = AuthUtil.isCrmAuth(crmEnum, entity.getContractId());
                    }
                } else if (crmEnum.equals(CrmEnum.CONTACTS)) {
                    CrmContacts entity = jsonObject.getObject("entity", CrmContacts.class);
                    if (entity.getContactsId() != null) {
                        flag = AuthUtil.isCrmAuth(crmEnum, entity.getContactsId());
                    }
                } else if (crmEnum.equals(CrmEnum.BUSINESS)) {
                    CrmBusiness entity = jsonObject.getObject("entity", CrmBusiness.class);
                    if (entity.getBusinessId() != null) {
                        flag = AuthUtil.isCrmAuth(crmEnum, entity.getBusinessId());
                    }
                } else if (crmEnum.equals(CrmEnum.RECEIVABLES)) {
                    CrmReceivables entity = jsonObject.getObject("entity", CrmReceivables.class);
                    if (entity.getReceivablesId() != null) {
                        flag = AuthUtil.isCrmAuth(crmEnum, entity.getReceivablesId());
                    }
                }
            } else if ("deleteByIds".equals(split[2])) {
                if (!Arrays.asList("crmCustomerPool", "crmMarketing").contains(split[1])) {
                    BufferedReader streamReader = request.getReader();
                    StringBuilder rawData = new StringBuilder();
                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null) {
                        rawData.append(inputStr);
                    }
                    List<Integer> idsArr = JSON.parseArray(rawData.toString(), Integer.class);
                    for (Integer id : idsArr) {
                        if (id != null) {
                            flag = AuthUtil.isCrmAuth(crmEnum, id);
                        }
                    }
                }
            } else if ("queryById".equals(split[2])) {
                //客户公海单独处理
                if (!Arrays.asList("crmCustomer", "crmMarketing").contains(split[1])) {
                    flag = AuthUtil.isCrmAuth(crmEnum, Integer.valueOf(split[3]));
                }
            }
            if (flag) {
                if ("queryById".equals(split[2])) {
                    CrmModel crmModel = new CrmModel();
                    crmModel.put("dataAuth",0);
                    return R.ok(crmModel);
                } else {
                    throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
                }
            }
        }
        return point.proceed();
    }
}
