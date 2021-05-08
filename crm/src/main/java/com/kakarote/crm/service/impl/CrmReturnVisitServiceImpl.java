package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmContract;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.PO.CrmReturnVisit;
import com.kakarote.crm.entity.PO.CrmReturnVisitData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmReturnVisitMapper;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 访问服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@Service
public class CrmReturnVisitServiceImpl extends BaseServiceImpl<CrmReturnVisitMapper, CrmReturnVisit> implements ICrmReturnVisitService, CrmPageService {


    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmCustomerUserStarService crmCustomerUserStarService;

    @Autowired
    private ICrmReturnVisitDataService crmReturnVisitDataService;

    @Autowired
    private ICrmNumberSettingService crmNumberSettingService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private AdminFileService adminFileService;


    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmContactsService crmContactsService;

    @Autowired
    private FieldService fieldService;

    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        BasePage<Map<String, Object>> basePage = queryList(search,false);
        for (Map<String, Object> map : basePage.getList()) {
            map.put("visitId",map.remove("return_visitId"));
        }
        return basePage;
    }

    @Override
    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<CrmReturnVisit> list = lambdaQuery().select(CrmReturnVisit::getVisitId,CrmReturnVisit::getVisitNumber).in(CrmReturnVisit::getVisitId, ids).list();
        return list.stream().map(crmReturnVisit -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmReturnVisit.getVisitId());
            simpleCrmEntity.setName(crmReturnVisit.getVisitNumber());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(CrmBusinessSaveBO crmModel) {
        CrmReturnVisit crmReturnVisit = BeanUtil.copyProperties(crmModel.getEntity(), CrmReturnVisit.class);
        String batchId = StrUtil.isNotEmpty(crmReturnVisit.getBatchId()) ? crmReturnVisit.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_return_visit_data"));
        crmReturnVisitDataService.saveData(crmModel.getField(), batchId);
        if (ObjectUtil.isNotEmpty(crmReturnVisit.getVisitId())) {
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(getById(crmReturnVisit.getVisitId())), BeanUtil.beanToMap(crmReturnVisit), CrmEnum.RETURN_VISIT, crmReturnVisit.getVisitNumber(), crmReturnVisit.getVisitId());
            crmReturnVisit.setUpdateTime(DateUtil.date());
            updateById(crmReturnVisit);
            crmReturnVisit = getById(crmReturnVisit.getVisitId());
        } else {
            List<AdminConfig> configList = adminService.queryConfigByName("numberSetting").getData();
            AdminConfig adminConfig = configList.stream().filter(config -> Objects.equals(getLabel().getType().toString(), config.getValue())).collect(Collectors.toList()).get(0);
            if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmReturnVisit.getVisitNumber())) {
                String result = crmNumberSettingService.generateNumber(adminConfig, crmReturnVisit.getVisitTime());
                crmReturnVisit.setVisitNumber(result);
            }
            Integer contract = lambdaQuery().eq(CrmReturnVisit::getVisitNumber, crmReturnVisit.getVisitNumber()).count();
            if (contract != 0) {
                throw new CrmException(CrmCodeEnum.CRM_CRMRETURNVISIT_NUM_ERROR);
            }
            crmReturnVisit.setBatchId(batchId);
            crmReturnVisit.setUpdateTime(new Date());
            if(crmReturnVisit.getOwnerUserId() == null){
                crmReturnVisit.setOwnerUserId(UserUtil.getUserId());
            }
            save(crmReturnVisit);
            actionRecordUtil.addRecord(crmReturnVisit.getVisitId(), CrmEnum.RETURN_VISIT, crmReturnVisit.getVisitNumber());
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmReturnVisit));
        savePage(crmModel, crmReturnVisit.getVisitId(), false);
    }


    @Override
    public void setOtherField(Map<String, Object> map) {
        String customerName = crmCustomerService.getCustomerName((Integer) map.get("customerId"));
        map.put("customerName", customerName);
        if (map.containsKey("contactsId") && ObjectUtil.isNotEmpty(map.get("contactsId"))) {
            String contactsName = crmContactsService.getContactsName((Integer) map.get("contactsId"));
            map.put("contactsName", contactsName);
        } else {
            map.put("contactsName", "");
        }
        CrmContract contract = crmContractService.getById((Serializable) map.get("contractId"));
        map.put("contractNum", contract != null ? contract.getNum() : "");
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName", ownerUserName);
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName", createUserName);
    }

    @Override
    public Dict getSearchTransferMap() {
        return Dict.create()
                .set("customerId", "customerName").set("contractId", "contractNum").set("contactsId", "contactsName");
    }

    @Override
    public CrmModel queryById(Integer id) {
        CrmModel crmModel;
        if (id != null) {
            crmModel = getBaseMapper().queryById(id);
            crmModel.setLabel(CrmEnum.RETURN_VISIT.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            crmReturnVisitDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
        } else {
            crmModel = new CrmModel(CrmEnum.RETURN_VISIT.getType());
        }
        return crmModel;
    }

    @Override
    public List<CrmModelFiledVO> queryField(Integer id) {
        return queryField(id,false);
    }

    private List<CrmModelFiledVO> queryField(Integer id,boolean appendInformation) {
        CrmModel crmModel = this.supplementCrmModel(id);
        List<CrmModelFiledVO> crmModelFiledVOS = crmFieldService.queryField(crmModel);
        if (id == null) {
            crmModelFiledVOS.forEach(field -> {
                if ("ownerUserId".equals(field.getFieldName())) {
                    SimpleUser user = new SimpleUser();
                    user.setUserId(UserUtil.getUserId());
                    user.setRealname(UserUtil.getUser().getRealname());
                    field.setDefaultValue(Collections.singleton(user));
                }
            });
        }
        if(appendInformation){
            List<CrmModelFiledVO> modelFiledVOS = appendInformation(crmModel);
            crmModelFiledVOS.addAll(modelFiledVOS);
        }
        return crmModelFiledVOS;
    }

    @Override
    public List<List<CrmModelFiledVO>> queryFormPositionField(Integer id) {
        CrmModel crmModel = this.supplementCrmModel(id);
        List<List<CrmModelFiledVO>> crmModelFiledVOS = crmFieldService.queryFormPositionFieldVO(crmModel);
        if (id == null) {
            for (List<CrmModelFiledVO> filedVOList : crmModelFiledVOS) {
                filedVOList.forEach(field ->{
                    if ("ownerUserId".equals(field.getFieldName())) {
                        SimpleUser user = new SimpleUser();
                        user.setUserId(UserUtil.getUserId());
                        user.setRealname(UserUtil.getUser().getRealname());
                        field.setDefaultValue(Collections.singleton(user));
                    }
                });
            }
        }
        return crmModelFiledVOS;
    }

    private CrmModel supplementCrmModel(Integer id){
        CrmModel crmModel = queryById(id);
        if (id != null) {
            List<JSONObject> customerList = new ArrayList<>();
            if (crmModel.get("customerId") != null) {
                JSONObject customer = new JSONObject();
                customerList.add(customer.fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName")));
            }
            crmModel.put("customerId", customerList);
            List<JSONObject> contractList = new ArrayList<>();
            JSONObject contract = new JSONObject();
            if (crmModel.get("contractId") != null && !"0".equals(crmModel.get("contractId").toString())) {
                contractList.add(contract.fluentPut("contractId", crmModel.get("contractId")).fluentPut("num", crmModel.get("contractNum")));
                crmModel.put("contractId", contractList);
            }
            List<JSONObject> contactsList = new ArrayList<>();
            JSONObject contacts = new JSONObject();
            if (crmModel.get("contactsId") != null && !"0".equals(crmModel.get("contactsId").toString())) {
                contactsList.add(contacts.fluentPut("contactsId", crmModel.get("contactsId")).fluentPut("name", crmModel.get("contactsName")));
                crmModel.put("contactsId", contactsList);
            }
        }
        return crmModel;
    }

    @Override
    public List<CrmModelFiledVO> information(Integer visitId) {
        return queryField(visitId,true);
    }

    @Override
    public List<FileEntity> queryFileList(Integer id) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmReturnVisit crmReturnVisit = getById(id);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmReturnVisit.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmReturnVisitData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmReturnVisitData::getValue);
            wrapper.eq(CrmReturnVisitData::getBatchId, crmReturnVisit.getBatchId());
            wrapper.in(CrmReturnVisitData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(crmReturnVisitDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("回访详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        return fileEntityList;
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        LambdaQueryWrapper<CrmReturnVisit> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmReturnVisit::getVisitId, ids);
        List<String> batchList = listObjs(wrapper, Object::toString);
        //删除自定义字段
        crmReturnVisitDataService.deleteByBatchId(batchList);
        deletePage(ids);
    }

    @Override
    public void updateReturnVisitRemindConfig(Integer status, Integer value) {
        AdminConfig adminConfig = adminService.queryFirstConfigByName("returnVisitRemindConfig").getData();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
        }
        adminConfig.setStatus(status);
        adminConfig.setValue(value != null ? value.toString() : "7");
        adminConfig.setName("returnVisitRemindConfig");
        adminConfig.setDescription("客户回访提醒设置");
        adminService.updateAdminConfig(adminConfig);
    }

    @Override
    public AdminConfig queryReturnVisitRemindConfig() {
        AdminConfig adminConfig = adminService.queryFirstConfigByName("returnVisitRemindConfig").getData();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
            adminConfig.setStatus(0);
            adminConfig.setValue("7");
            adminConfig.setName("returnVisitRemindConfig");
            adminConfig.setDescription("客户回访提醒设置");
        }
        return adminConfig;
    }

    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"visit_number"};
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    @Override
    public CrmEnum getLabel() {
        return CrmEnum.RETURN_VISIT;
    }

    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        List<CrmModelFiledVO> filedList = crmFieldService.queryField(getLabel().getType());
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
        return filedList;
    }


    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer visitId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmReturnVisit oldReturnVisit = getById(updateInformationBO.getId());
            uniqueFieldIsAbnormal(record.getString("name"),record.getInteger("fieldId"),record.getString("value"),batchId);
            Map<String, Object> oldReturnVisitMap = BeanUtil.beanToMap(oldReturnVisit);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmRetuenVisitMap = new HashMap<>(oldReturnVisitMap);
                crmRetuenVisitMap.put(record.getString("fieldName"), record.get("value"));
                CrmReturnVisit crmReturnVisit = BeanUtil.mapToBean(crmRetuenVisitMap, CrmReturnVisit.class, true);
                actionRecordUtil.updateRecord(oldReturnVisitMap, crmRetuenVisitMap, CrmEnum.RETURN_VISIT, crmReturnVisit.getVisitNumber(), crmReturnVisit.getVisitId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("visit_id", updateInformationBO.getId()).update();
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                CrmReturnVisitData returnVisitData = crmReturnVisitDataService.lambdaQuery().select(CrmReturnVisitData::getValue,CrmReturnVisitData::getId).eq(CrmReturnVisitData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmReturnVisitData::getBatchId, batchId).one();
                String value = returnVisitData != null ? returnVisitData.getValue() : null;
                actionRecordUtil.publicContentRecord(CrmEnum.RETURN_VISIT, BehaviorEnum.UPDATE, visitId, oldReturnVisit.getVisitNumber(), record,value);
                String newValue = fieldService.convertObjectValueToString(record.getInteger("type"),record.get("value"),record.getString("value"));
                CrmReturnVisitData crmReturnVisitData = new CrmReturnVisitData();
                crmReturnVisitData.setId(returnVisitData != null ? returnVisitData.getId() : null);
                crmReturnVisitData.setFieldId(record.getInteger("fieldId"));
                crmReturnVisitData.setName(record.getString("fieldName"));
                crmReturnVisitData.setValue(newValue);
                crmReturnVisitData.setCreateTime(new Date());
                crmReturnVisitData.setBatchId(batchId);
                crmReturnVisitDataService.saveOrUpdate(crmReturnVisitData);

            }
            updateField(record, visitId);
        });
        this.lambdaUpdate().set(CrmReturnVisit::getUpdateTime,new Date()).eq(CrmReturnVisit::getVisitId,visitId).update();
    }
}
