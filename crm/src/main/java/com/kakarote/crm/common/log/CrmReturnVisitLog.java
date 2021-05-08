package com.kakarote.crm.common.log;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmReturnVisit;
import com.kakarote.crm.entity.PO.CrmReturnVisitData;
import com.kakarote.crm.service.ICrmReturnVisitDataService;
import com.kakarote.crm.service.ICrmReturnVisitService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrmReturnVisitLog {
    private SysLogUtil sysLogUtil = ApplicationContextHolder.getBean(SysLogUtil.class);

    private ICrmReturnVisitService crmReturnVisitService = ApplicationContextHolder.getBean(ICrmReturnVisitService.class);

    private AdminFileService adminFileService = ApplicationContextHolder.getBean(AdminFileService.class);

    private AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);

    private ICrmReturnVisitDataService crmReturnVisitDataService = ApplicationContextHolder.getBean(ICrmReturnVisitDataService.class);

    public Content update(@RequestBody CrmBusinessSaveBO crmModel) {
        CrmReturnVisit crmReturnVisit = BeanUtil.copyProperties(crmModel.getEntity(), CrmReturnVisit.class);
        String batchId = crmReturnVisit.getBatchId();
        sysLogUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_return_visit_data"));
        return sysLogUtil.updateRecord(BeanUtil.beanToMap(crmReturnVisitService.getById(crmReturnVisit.getVisitId())), BeanUtil.beanToMap(crmReturnVisit), CrmEnum.RETURN_VISIT, crmReturnVisit.getVisitNumber());
    }

    public List<Content> deleteByIds(List<Integer> ids) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : ids) {
            CrmReturnVisit crmReturnVisit = crmReturnVisitService.getById(id);
            contentList.add(sysLogUtil.addDeleteActionRecord(CrmEnum.RETURN_VISIT,crmReturnVisit.getVisitNumber()));
        }
        return contentList;
    }

    public List<Content> updateInformation(CrmUpdateInformationBO updateInformationBO) {
        List<Content> contentList = new ArrayList<>();
        String batchId = updateInformationBO.getBatchId();
        updateInformationBO.getList().forEach(record -> {
            CrmReturnVisit oldReturnVisit = crmReturnVisitService.getById(updateInformationBO.getId());
            Map<String, Object> oldReturnVisitMap = BeanUtil.beanToMap(oldReturnVisit);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmRetuenVisitMap = new HashMap<>(oldReturnVisitMap);
                crmRetuenVisitMap.put(record.getString("fieldName"), record.get("value"));
                CrmReturnVisit crmReturnVisit = BeanUtil.mapToBean(crmRetuenVisitMap, CrmReturnVisit.class, true);
                contentList.add(sysLogUtil.updateRecord(oldReturnVisitMap, crmRetuenVisitMap, CrmEnum.RETURN_VISIT, crmReturnVisit.getVisitNumber()));
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                String formType = record.getString("formType");
                if(formType == null){
                    return;
                }
                String oldFieldValue = crmReturnVisitDataService.lambdaQuery().select(CrmReturnVisitData::getValue).eq(CrmReturnVisitData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmReturnVisitData::getBatchId, batchId).one().getValue();
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
                contentList.add(new Content(oldReturnVisit.getVisitNumber(),detail));
            }
        });
        return contentList;
    }
}
