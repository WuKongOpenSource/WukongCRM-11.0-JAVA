package com.kakarote.crm.common.log;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmChangeOwnerUserBO;
import com.kakarote.crm.entity.BO.CrmContactsSaveBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.PO.CrmContactsData;
import com.kakarote.crm.service.ICrmContactsDataService;
import com.kakarote.crm.service.ICrmContactsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrmContactsLog {


    private ICrmContactsService crmContactsService = ApplicationContextHolder.getBean(ICrmContactsService.class);

    private SysLogUtil sysLogUtil = ApplicationContextHolder.getBean(SysLogUtil.class);

    private AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);

    private AdminFileService adminFileService = ApplicationContextHolder.getBean(AdminFileService.class);

    private ICrmContactsDataService crmContactsDataService = ApplicationContextHolder.getBean(ICrmContactsDataService.class);

    public List<Content> deleteByIds(List<Integer> ids) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : ids) {
            String name = crmContactsService.getContactsName(id);
            if (name != null) {
                contentList.add(sysLogUtil.addDeleteActionRecord(CrmEnum.CONTACTS, name));
            }
        }
        return contentList;
    }

    public List<Content> changeOwnerUser(CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        return crmChangeOwnerUserBO.getIds().stream().map(id -> {
            String contactsName = crmContactsService.getContactsName(id);
            return sysLogUtil.addConversionRecord(CrmEnum.CONTACTS, crmChangeOwnerUserBO.getOwnerUserId(), contactsName);
        }).collect(Collectors.toList());
    }

    public Content update(CrmContactsSaveBO crmModel) {
        CrmContacts crmContacts = BeanUtil.copyProperties(crmModel.getEntity(), CrmContacts.class);
        String batchId = StrUtil.isNotEmpty(crmContacts.getBatchId()) ? crmContacts.getBatchId() : IdUtil.simpleUUID();
        sysLogUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_contacts_data"));
        return sysLogUtil.updateRecord(BeanUtil.beanToMap(crmContactsService.getById(crmContacts.getContactsId())), BeanUtil.beanToMap(crmContacts), CrmEnum.CONTACTS, crmContacts.getName());
    }


    public List<Content> updateInformation(CrmUpdateInformationBO updateInformationBO) {
        List<Content> contentList = new ArrayList<>();
        String batchId = updateInformationBO.getBatchId();
        updateInformationBO.getList().forEach(record -> {
            CrmContacts oldContacts = crmContactsService.getById(updateInformationBO.getId());
            Map<String, Object> oldContactsMap = BeanUtil.beanToMap(oldContacts);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmContactsMap = new HashMap<>(oldContactsMap);
                crmContactsMap.put(record.getString("fieldName"), record.get("value"));
                CrmContacts crmContacts = BeanUtil.mapToBean(crmContactsMap, CrmContacts.class, true);
                contentList.add(sysLogUtil.updateRecord(oldContactsMap, crmContactsMap, CrmEnum.CONTACTS, crmContacts.getName()));
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                String formType = record.getString("formType");
                if(formType == null){
                    return;
                }
                String oldFieldValue = crmContactsDataService.lambdaQuery().select(CrmContactsData::getValue).eq(CrmContactsData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmContactsData::getBatchId, batchId).one().getValue();
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
                contentList.add(new Content(oldContacts.getName(), detail, BehaviorEnum.UPDATE));
            }
        });
        return contentList;
    }
}
