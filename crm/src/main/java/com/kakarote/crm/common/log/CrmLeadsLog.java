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
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmLeads;
import com.kakarote.crm.entity.PO.CrmLeadsData;
import com.kakarote.crm.service.ICrmLeadsDataService;
import com.kakarote.crm.service.ICrmLeadsService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrmLeadsLog {

    private SysLogUtil sysLogUtil = ApplicationContextHolder.getBean(SysLogUtil.class);

    private ICrmLeadsService crmLeadsService = ApplicationContextHolder.getBean(ICrmLeadsService.class);

    private AdminFileService adminFileService = ApplicationContextHolder.getBean(AdminFileService.class);

    private AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);

    private ICrmLeadsDataService crmLeadsDataService = ApplicationContextHolder.getBean(ICrmLeadsDataService.class);

    public Content update(@RequestBody CrmModelSaveBO crmModel) {
        CrmLeads crmLeads = BeanUtil.copyProperties(crmModel.getEntity(), CrmLeads.class);
        String batchId = StrUtil.isNotEmpty(crmLeads.getBatchId()) ? crmLeads.getBatchId() : IdUtil.simpleUUID();
        sysLogUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_leads_data"));
        return sysLogUtil.updateRecord(BeanUtil.beanToMap(crmLeadsService.getById(crmLeads.getLeadsId())), BeanUtil.beanToMap(crmLeads), CrmEnum.LEADS, crmLeads.getLeadsName());
    }

    public List<Content> changeOwnerUser(List<Integer> leadsIds, Long newOwnerUserId) {
        List<Content> contentList = new ArrayList<>();
        for (Integer leadsId : leadsIds) {
            contentList.add(sysLogUtil.addConversionRecord(CrmEnum.LEADS, newOwnerUserId, crmLeadsService.getById(leadsId).getLeadsName()));
        }
        return contentList;
    }


    public List<Content> transfer(List<Integer> leadsIds) {
        List<Content> contentList = new ArrayList<>();
        for (Integer leadsId : leadsIds) {
            CrmLeads leads = crmLeadsService.getById(leadsId);
            String leadsName = leads.getLeadsName();
            contentList.add(new Content(leadsName, "将线索\"" + leadsName + "\"转化为客户", BehaviorEnum.TRANSFER));
        }
        return contentList;
    }


    public List<Content> updateInformation(CrmUpdateInformationBO updateInformationBO) {
        List<Content> contentList = new ArrayList<>();
        String batchId = updateInformationBO.getBatchId();
        updateInformationBO.getList().forEach(record -> {
            CrmLeads oldLeads = crmLeadsService.getById(updateInformationBO.getId());
            Map<String, Object> oldLeadsMap = BeanUtil.beanToMap(oldLeads);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmLeadsMap = new HashMap<>(oldLeadsMap);
                crmLeadsMap.put(record.getString("fieldName"), record.get("value"));
                CrmLeads crmLeads = BeanUtil.mapToBean(crmLeadsMap, CrmLeads.class, true);
                contentList.add(sysLogUtil.updateRecord(oldLeadsMap, crmLeadsMap, CrmEnum.LEADS, crmLeads.getLeadsName()));
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                String formType = record.getString("formType");
                if(formType == null){
                    return;
                }
                String oldFieldValue = crmLeadsDataService.lambdaQuery().select(CrmLeadsData::getValue).eq(CrmLeadsData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmLeadsData::getBatchId, batchId).one().getValue();
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
                contentList.add(new Content(oldLeads.getLeadsName(), detail, BehaviorEnum.UPDATE));
            }
        });
        return contentList;
    }


    public List<Content> deleteByIds(List<Integer> ids) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : ids) {
            CrmLeads crmLeads = crmLeadsService.lambdaQuery().select(CrmLeads::getLeadsName).eq(CrmLeads::getLeadsId, id).one();
            if (crmLeads != null) {
                String name = crmLeads.getLeadsName();
                contentList.add(sysLogUtil.addDeleteActionRecord(CrmEnum.LEADS, name));
            }
        }
        return contentList;
    }
}
