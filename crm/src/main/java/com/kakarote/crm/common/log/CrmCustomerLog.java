package com.kakarote.crm.common.log;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmCustomerData;
import com.kakarote.crm.service.ICrmCustomerDataService;
import com.kakarote.crm.service.ICrmCustomerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CrmCustomerLog {

    private SysLogUtil sysLogUtil = ApplicationContextHolder.getBean(SysLogUtil.class);

    private ICrmCustomerService crmCustomerService = ApplicationContextHolder.getBean(ICrmCustomerService.class);

    private AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);

    private AdminFileService adminFileService = ApplicationContextHolder.getBean(AdminFileService.class);

    private ICrmCustomerDataService crmCustomerDataService = ApplicationContextHolder.getBean(ICrmCustomerDataService.class);

    public Content update(CrmBusinessSaveBO crmModel) {
        CrmCustomer crmCustomer = BeanUtil.copyProperties(crmModel.getEntity(), CrmCustomer.class);
        String batchId = StrUtil.isNotEmpty(crmCustomer.getBatchId()) ? crmCustomer.getBatchId() : IdUtil.simpleUUID();
        sysLogUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_customer_data"));
        return sysLogUtil.updateRecord(BeanUtil.beanToMap(crmCustomerService.getById(crmCustomer.getCustomerId())), BeanUtil.beanToMap(crmCustomer), CrmEnum.CUSTOMER, crmCustomer.getCustomerName());
    }

    public List<Content> deleteByIds(List<Integer> ids) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : ids) {
            String name = crmCustomerService.getCustomerName(id);
            if (name != null) {
                contentList.add(sysLogUtil.addDeleteActionRecord(CrmEnum.CUSTOMER, name));
            }
        }
        return contentList;
    }

    public List<Content> lock(Integer status, String id) {
        return sysLogUtil.addIsLockRecord(StrUtil.splitTrim(id, Const.SEPARATOR), status);
    }

    public List<Content> setDealStatus(Integer dealStatus,String id) {
        String detail = "修改客户:%s成交状态:";
        if (dealStatus == 0) {
            detail += "未成交。";
        } else {
            detail += "已成交。";
        }
        String finalDetail = detail;
        return StrUtil.splitTrim(id, Const.SEPARATOR).stream().map(customerId -> {
            String customerName = crmCustomerService.getCustomerName(Integer.valueOf(customerId));
            return new Content(customerName, String.format(finalDetail, customerName));
        }).collect(Collectors.toList());
    }

    public List<Content> changeOwnerUser(CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        return crmChangeOwnerUserBO.getIds().stream().map(id -> {
            String customerName = crmCustomerService.getCustomerName(id);
            return sysLogUtil.addConversionRecord(CrmEnum.CUSTOMER, crmChangeOwnerUserBO.getOwnerUserId(), customerName);
        }).collect(Collectors.toList());
    }

    public List<Content> addMembers(CrmMemberSaveBO crmMemberSaveBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : crmMemberSaveBO.getIds()) {
            String name = crmCustomerService.getCustomerName(id);
            for (Long memberId : crmMemberSaveBO.getMemberIds()) {
                contentList.add(sysLogUtil.addMemberActionRecord(CrmEnum.CUSTOMER, id, memberId, name));
            }
        }
        return contentList;
    }

    public List<Content> updateMembers(CrmMemberSaveBO crmMemberSaveBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : crmMemberSaveBO.getIds()) {
            String name = crmCustomerService.getCustomerName(id);
            for (Long memberId : crmMemberSaveBO.getMemberIds()) {
                contentList.add(sysLogUtil.addMemberActionRecord(CrmEnum.CUSTOMER, id, memberId, name));
            }
        }
        return contentList;
    }

    public List<Content> deleteMembers(CrmMemberSaveBO crmMemberSaveBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : crmMemberSaveBO.getIds()) {
            CrmCustomer oldCustomer = crmCustomerService.getById(id);
            for (Long memberId : crmMemberSaveBO.getMemberIds()) {
                if (!memberId.equals(UserUtil.getUserId())) {
                    contentList.add(sysLogUtil.addDeleteMemberActionRecord(CrmEnum.CUSTOMER, memberId, false, oldCustomer.getCustomerName()));
                } else {
                    contentList.add(sysLogUtil.addDeleteMemberActionRecord(CrmEnum.CUSTOMER, memberId, true, oldCustomer.getCustomerName()));
                }
            }
        }
        return contentList;
    }

    public Content exitTeam(Integer customerId) {
        CrmCustomer oldCustomer = crmCustomerService.getById(customerId);
        return sysLogUtil.addDeleteMemberActionRecord(CrmEnum.CUSTOMER, UserUtil.getUserId(), true, oldCustomer.getCustomerName());
    }

    public List<Content> updateCustomerByIds(CrmCustomerPoolBO poolBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : poolBO.getIds()) {
            String customerName = crmCustomerService.getCustomerName(id);
            contentList.add(new Content(customerName,"将客户：" + customerName + "放入公海"));
        }
        return contentList;
    }

    public List<Content> distributeByIds(CrmCustomerPoolBO poolBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : poolBO.getIds()) {
            String customerName = crmCustomerService.getCustomerName(id);
            String userName = UserCacheUtil.getUserName(poolBO.getUserId());
            contentList.add(new Content(customerName,"将客户：" + customerName + "分配给：" + userName));
        }
        return contentList;
    }

    public List<Content> receiveByIds(CrmCustomerPoolBO poolBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : poolBO.getIds()) {
            String customerName = crmCustomerService.getCustomerName(id);
            contentList.add(new Content(customerName,"领取了客户：" + customerName));
        }
        return contentList;
    }

    public List<Content> updateInformation(CrmUpdateInformationBO updateInformationBO) {
        List<Content> contentList = new ArrayList<>();
        String batchId = updateInformationBO.getBatchId();
        Integer customerId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmCustomer oldCustomer = crmCustomerService.getById(customerId);
            Map<String, Object> oldCustomerMap = BeanUtil.beanToMap(oldCustomer);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmCustomerMap = new HashMap<>(oldCustomerMap);
                crmCustomerMap.put(record.getString("fieldName"), record.get("value"));
                CrmCustomer crmCustomer = BeanUtil.mapToBean(crmCustomerMap, CrmCustomer.class, true);
                contentList.add(sysLogUtil.updateRecord(oldCustomerMap, crmCustomerMap, CrmEnum.CUSTOMER, crmCustomer.getCustomerName()));
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                String formType = record.getString("formType");
                if(formType == null){
                    return;
                }
                String oldFieldValue = crmCustomerDataService.lambdaQuery().select(CrmCustomerData::getValue).eq(CrmCustomerData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmCustomerData::getBatchId, batchId).last("limit 1").one().getValue();
                String newValue = record.getString("value");
                if (formType.equals(FieldEnum.USER.getFormType()) || formType.equals(FieldEnum.SINGLE_USER.getFormType())) {
                    oldFieldValue = adminService.queryUserByIds(TagUtil.toLongSet(oldFieldValue)).getData().stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
                    newValue = adminService.queryUserByIds(TagUtil.toLongSet(record.getString("value"))).getData().stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
                } else if (formType.equals(FieldEnum.STRUCTURE.getFormType())) {
                    oldFieldValue = adminService.queryDeptByIds(TagUtil.toSet(oldFieldValue)).getData().stream().map(SimpleDept::getName).collect(Collectors.joining(","));
                    newValue = adminService.queryDeptByIds(TagUtil.toSet(record.getString("value"))).getData().stream().map(SimpleDept::getName).collect(Collectors.joining(","));
                } else if (formType.equals(FieldEnum.FILE.getFormType())) {
                    oldFieldValue = adminFileService.queryFileList(oldFieldValue).getData().stream().map(FileEntity::getName).collect(Collectors.joining(","));
                    newValue = adminFileService.queryFileList(record.getString("value")).getData().stream().map(FileEntity::getName).collect(Collectors.joining(","));
                }
                String oldValue = StrUtil.isEmpty(oldFieldValue) ? "空" : oldFieldValue;
                String detail = "将" + record.getString("name") + " 由" + oldValue + "修改为" + newValue + "。";
                contentList.add(new Content(oldCustomer.getCustomerName(), detail, BehaviorEnum.UPDATE));
            }
        });
        return contentList;
    }
}
