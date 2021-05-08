package com.kakarote.crm.common.log;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
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
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmProductStatusBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmProduct;
import com.kakarote.crm.entity.PO.CrmProductData;
import com.kakarote.crm.service.ICrmProductDataService;
import com.kakarote.crm.service.ICrmProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrmProductLog {
    private SysLogUtil sysLogUtil = ApplicationContextHolder.getBean(SysLogUtil.class);

    private ICrmProductService crmProductService = ApplicationContextHolder.getBean(ICrmProductService.class);

    private AdminFileService adminFileService = ApplicationContextHolder.getBean(AdminFileService.class);

    private AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);

    private ICrmProductDataService crmProductDataService = ApplicationContextHolder.getBean(ICrmProductDataService.class);

    public List<Content> deleteByIds(List<Integer> ids) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : ids) {
            CrmProduct product = crmProductService.getById(id);
            contentList.add(sysLogUtil.addDeleteActionRecord(CrmEnum.PRODUCT,product.getName()));
        }
        return contentList;
    }

    public Content update(CrmModelSaveBO crmModel) {
        CrmProduct crmProduct = BeanUtil.copyProperties(crmModel.getEntity(), CrmProduct.class);
        String batchId = crmProduct.getBatchId();
        sysLogUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_product_data"));
        return sysLogUtil.updateRecord(BeanUtil.beanToMap(crmProductService.getById(crmProduct.getProductId())), BeanUtil.beanToMap(crmProduct), CrmEnum.PRODUCT, crmProduct.getName());
    }

    public List<Content> changeOwnerUser(CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : crmChangeOwnerUserBO.getIds()) {
            CrmProduct product = crmProductService.getById(id);
            contentList.add(sysLogUtil.addConversionRecord(CrmEnum.PRODUCT, crmChangeOwnerUserBO.getOwnerUserId(), product.getName()));
        }
        return contentList;
    }

    public List<Content> updateStatus(CrmProductStatusBO productStatusBO) {
        List<Content> contentList = new ArrayList<>();
        String detail = "将产品";
        if (productStatusBO.getStatus() == 0){
            detail += "下架";
        }else {
            detail += "上架";
        }
        for (Integer id : productStatusBO.getIds()) {
            CrmProduct product = crmProductService.getById(id);
            contentList.add(new Content(product.getName(),detail, BehaviorEnum.UPDATE));
        }
        return contentList;
    }

    public List<Content> updateInformation(CrmUpdateInformationBO updateInformationBO) {
        List<Content> contentList = new ArrayList<>();
        String batchId = updateInformationBO.getBatchId();
        Integer productId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmProduct oldProduct = crmProductService.getById(updateInformationBO.getId());
            Map<String, Object> oldProductMap = BeanUtil.beanToMap(oldProduct);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmProductMap = new HashMap<>(oldProductMap);
                crmProductMap.put(record.getString("fieldName"), record.get("value"));
                CrmProduct crmProduct = BeanUtil.mapToBean(crmProductMap, CrmProduct.class, true);
                contentList.add(sysLogUtil.updateRecord(oldProductMap, crmProductMap, CrmEnum.PRODUCT, crmProduct.getName()));
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                String oldFieldValue = crmProductDataService.lambdaQuery().select(CrmProductData::getValue).eq(CrmProductData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmProductData::getBatchId, batchId).one().getValue();
                String formType = record.getString("formType");
                if(formType == null){
                    return;
                }
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
                contentList.add(new Content(oldProduct.getName(),detail));
            }
        });
        return contentList;
    }
}
