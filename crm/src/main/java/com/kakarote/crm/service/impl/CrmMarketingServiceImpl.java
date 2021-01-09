package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.BO.CrmCensusBO;
import com.kakarote.crm.entity.BO.CrmMarketingPageBO;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmSyncDataBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmFieldMapper;
import com.kakarote.crm.mapper.CrmMarketingMapper;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 营销表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-08-12
 */
@Service
public class CrmMarketingServiceImpl extends BaseServiceImpl<CrmMarketingMapper, CrmMarketing> implements ICrmMarketingService {


    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private ICrmMarketingInfoService crmMarketingInfoService;

    @Autowired
    private ICrmMarketingFieldService crmMarketingFieldService;

    @Autowired
    private ICrmMarketingFormService crmMarketingFormService;


    @Override
    public void addOrUpdate(CrmMarketing crmMarketing) {
        Long userId = UserUtil.getUserId();
        if (crmMarketing.getMarketingId() == null){
            Set<Long> relationIds = TagUtil.toLongSet(crmMarketing.getRelationUserId());
            crmMarketing.setRelationUserId(TagUtil.fromLongSet(relationIds));
            crmMarketing.setCreateUserId(userId);
            crmMarketing.setCreateTime(new Date());
            crmMarketing.setUpdateTime(new Date());
            save(crmMarketing);
        }else {
            crmMarketing.setUpdateTime(new Date());
            updateById(crmMarketing);
        }
    }

    @Override
    public BasePage<CrmMarketing> queryPageList(CrmMarketingPageBO crmMarketingPageBO, Integer status) {
        List<Long> userIds = adminService.queryChildUserId(UserUtil.getUserId()).getData();
        userIds.add(UserUtil.getUserId());
        BasePage<CrmMarketing> page = getBaseMapper().queryPageList(crmMarketingPageBO.parse(),userIds,
                crmMarketingPageBO.getCrmType(),crmMarketingPageBO.getSearch(),UserUtil.isAdmin(),crmMarketingPageBO.getTimeType(),
                status);
        for (CrmMarketing crmMarketing : page.getList()) {
            crmMarketing.setCreateUserName(UserCacheUtil.getUserName(crmMarketing.getCreateUserId()));
            Integer crmType = crmMarketing.getCrmType();
            if (Arrays.asList(FIXED_CRM_TYPE).contains(crmType)){
                if (crmType == 1) {
                    crmMarketing.setCrmTypeName("线索");
                } else {
                    crmMarketing.setCrmTypeName("客户");
                }
            }else {
                CrmMarketingForm marketingForm = crmMarketingFormService.getById(crmType);
                if (marketingForm != null) {
                    crmMarketing.setCrmTypeName(marketingForm.getTitle());
                }
            }
            if (crmMarketing.getMainFileIds() != null) {
                List<FileEntity> recordList = adminFileService.queryByIds(TagUtil.toLongSet(crmMarketing.getMainFileIds())).getData();
                crmMarketing.setMainFileList(recordList);
                crmMarketing.setMainFile(recordList.size()>0?recordList.get(0):null);
            }
        }
        return page;
    }

    @Override
    public JSONObject queryById(Integer marketingId,String device) {
        AES aes = SecureUtil.aes(BYTES);
        CrmMarketing marketing = getById(marketingId);
        Integer subCount = crmMarketingInfoService.lambdaQuery().eq(CrmMarketingInfo::getMarketingId, marketingId).count();
        JSONObject crmMarketing = BeanUtil.copyProperties(marketing, JSONObject.class);
        Integer crmType = marketing.getCrmType();
        if (Arrays.asList(FIXED_CRM_TYPE).contains(crmType)){
            if (crmType == 1) {
                crmMarketing.put("crmTypeName","线索");
            } else {
                crmMarketing.put("crmTypeName","客户");
            }
        }else {
            CrmMarketingForm marketingForm = crmMarketingFormService.getById(crmType);
            if (marketingForm != null) {
                crmMarketing.put("crmTypeName",marketingForm.getTitle());
            }
        }
        crmMarketing.put("subCount",subCount);
        crmMarketing.put("enMarketingId",aes.encryptHex(marketing.getMarketingId().toString()));
        crmMarketing.put("currentUserId",aes.encryptHex(UserUtil.getUserId().toString()));
        crmMarketing.put("relationUserInfo", adminService.queryUserByIds(TagUtil.toLongSet(marketing.getRelationUserId())).getData());
        crmMarketing.put("createUserInfo", adminService.queryUserById(marketing.getCreateUserId()).getData());
        if (StrUtil.isNotEmpty(marketing.getDetailFileIds())) {
            List<FileEntity> recordList = adminFileService.queryByIds(TagUtil.toLongSet(marketing.getDetailFileIds())).getData();
            crmMarketing.put("detailFileList", recordList);
        }else {
            crmMarketing.put("detailFileList", new ArrayList<>());
        }
        if (marketing.getMainFileIds() != null) {
            List<FileEntity> recordList = adminFileService.queryByIds(TagUtil.toLongSet(marketing.getMainFileIds())).getData();
            crmMarketing.put("mainFileList",  recordList);
        }else {
            crmMarketing.put("mainFileList",  new ArrayList<>());
        }
        Integer second = marketing.getSecond();
        Integer count = crmMarketingInfoService.lambdaQuery().eq(CrmMarketingInfo::getMarketingId,marketingId).eq(CrmMarketingInfo::getDevice,device).count();
        if (second == 1 && count > 0 ){
            crmMarketing.put("isAdd",0);
        }else {
            crmMarketing.put("isAdd",1);
        }
        if (marketing.getStatus() == 0 || System.currentTimeMillis() > marketing.getEndTime().getTime()){
            crmMarketing.put("isEnd",1);
        }else {
            crmMarketing.put("isEnd",0);
        }
        return crmMarketing;
    }

    @Override
    public void deleteByIds(List<Integer> marketingIds) {
        for (Integer marketingId : marketingIds) {
            Integer count = crmMarketingInfoService.lambdaQuery().eq(CrmMarketingInfo::getMarketingId,marketingId).eq(CrmMarketingInfo::getStatus,0).count();
            if (count > 0){
                throw new CrmException(CrmCodeEnum.CRM_MARKETING_UNSYNCHRONIZED_DATA);
            }
            removeById(marketingId);
            crmMarketingInfoService.lambdaUpdate().eq(CrmMarketingInfo::getMarketingId,marketingId).remove();
        }
    }

    @Autowired
    private ICrmFieldService crmFieldService;

    @Override
    public List<CrmModelFiledVO> queryField(Integer marketingId) {
        CrmMarketing marketing = getById(marketingId);
        Integer crmType = marketing.getCrmType();
        if(Arrays.asList(FIXED_CRM_TYPE).contains(crmType)) {
            List<CrmField> list = crmFieldService.lambdaQuery().in(CrmField::getFieldId, TagUtil.toSet(marketing.getFieldDataId())).list();
            return list.stream().map(field->{
                CrmModelFiledVO crmModelFiled = BeanUtil.copyProperties(field, CrmModelFiledVO.class);
                FieldEnum typeEnum = FieldEnum.parse(crmModelFiled.getType());
                crmFieldService.recordToFormType(crmModelFiled,typeEnum);
                return crmModelFiled;
            }).collect(Collectors.toList());
        }else {
            List<CrmMarketingField> crmMarketingFields = crmMarketingFieldService.lambdaQuery().in(CrmMarketingField::getFieldId, TagUtil.toSet(marketing.getFieldDataId())).list();
            return crmMarketingFields.stream().map(field->{
                CrmModelFiledVO crmModelFiled = BeanUtil.copyProperties(field, CrmModelFiledVO.class);
                FieldEnum typeEnum = FieldEnum.parse(crmModelFiled.getType());
                crmFieldService.recordToFormType(crmModelFiled,typeEnum);
                return crmModelFiled;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public void updateStatus(String marketingIds, Integer status) {
        List<CrmMarketing> crmMarketingList = new ArrayList<>();
        TagUtil.toSet(marketingIds).forEach(id->{
            CrmMarketing marketing = new CrmMarketing();
            marketing.setMarketingId(id);
            marketing.setStatus(status);
            crmMarketingList.add(marketing);
        });
        updateBatchById(crmMarketingList,100);
    }

    @Override
    public void updateShareNum(Integer marketingId, Integer num) {
        CrmMarketing crmMarketing = getById(marketingId);
        if (num == null){
            lambdaUpdate().set(CrmMarketing::getShareNum,crmMarketing.getShareNum()+1).eq(CrmMarketing::getMarketingId,marketingId).update();
        }else {
            lambdaUpdate().set(CrmMarketing::getShareNum,crmMarketing.getShareNum()+num).eq(CrmMarketing::getMarketingId,marketingId).update();
        }
    }

    @Override
    public BasePage<JSONObject> census(CrmCensusBO crmCensusBO) {
        Integer marketingId = crmCensusBO.getMarketingId();
        Long userId = UserUtil.getUserId();
        Integer status = crmCensusBO.getStatus();
        List<Long> userIds = adminService.queryChildUserId(userId).getData();
        userIds.add(userId);
        BasePage<JSONObject> page = getBaseMapper().census(crmCensusBO.parse(),marketingId,userIds,status);
        List<JSONObject> recordList = new ArrayList<>();
        page.getList().forEach(record -> {
            String fieldInfo = record.getString("fieldInfo");
            record.remove("fieldInfo");
            JSONObject jsonObject = JSON.parseObject(fieldInfo);
            JSONObject entity = jsonObject.getJSONObject("entity");
            jsonObject.getJSONArray("field").forEach(field->{
                JSONObject adminFieldv = (JSONObject)field;
                entity.put(adminFieldv.getString("fieldName"),adminFieldv.getString("value"));
            });
            entity.put("status",record.getInteger("status"));
            entity.put("rId",record.getInteger("rId"));
            entity.put("ownerUserName",UserCacheUtil.getUserName(record.getLong("ownerUserId")));
            record.put("entity",entity);
            record.remove("status","rId");
            recordList.add(new JSONObject().fluentPutAll(entity.getInnerMap()));
        });
        page.setList(recordList);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject queryAddField(String marketingId) {
        AES aes = SecureUtil.aes(BYTES);
        Integer marketingIdInt = Integer.valueOf(aes.decryptStr(marketingId));
        CrmMarketing crmMarketing = getById(marketingIdInt);
        if (crmMarketing.getStatus() == 0){
            throw new CrmException(CrmCodeEnum.CRM_MARKETING_STOP);
        }
        if (System.currentTimeMillis() > crmMarketing.getEndTime().getTime()){
            throw new CrmException(CrmCodeEnum.CRM_MARKETING_QR_CODE_EXPIRED);
        }
        lambdaUpdate().set(CrmMarketing::getBrowse,crmMarketing.getBrowse()+1).eq(CrmMarketing::getMarketingId,marketingIdInt).update();
        List<CrmModelFiledVO> recordList;
        if (Arrays.asList(FIXED_CRM_TYPE).contains(crmMarketing.getCrmType())) {
            List<CrmField> list = crmFieldService.lambdaQuery().in(CrmField::getFieldId, TagUtil.toSet(crmMarketing.getFieldDataId())).list();
            recordList = list.stream().map(field -> {
                CrmModelFiledVO crmModelFiled = BeanUtil.copyProperties(field, CrmModelFiledVO.class);
                FieldEnum typeEnum = FieldEnum.parse(crmModelFiled.getType());
                crmFieldService.recordToFormType(crmModelFiled, typeEnum);
                return crmModelFiled;
            }).collect(Collectors.toList());
        }else {
            List<CrmMarketingField> list = crmMarketingFieldService.lambdaQuery().in(CrmMarketingField::getFieldId, TagUtil.toSet(crmMarketing.getFieldDataId())).list();
            recordList = list.stream().map(field -> {
                CrmModelFiledVO crmModelFiled = BeanUtil.copyProperties(field, CrmModelFiledVO.class);
                FieldEnum typeEnum = FieldEnum.parse(crmModelFiled.getType());
                crmFieldService.recordToFormType(crmModelFiled, typeEnum);
                return crmModelFiled;
            }).collect(Collectors.toList());
        }
        JSONObject kv = new JSONObject().fluentPut("marketingName", crmMarketing.getMarketingName()).fluentPut("list", recordList);
        if (crmMarketing.getMainFileIds() != null) {
            List<FileEntity> fileEntities = adminFileService.queryByIds(TagUtil.toLongSet(crmMarketing.getMainFileIds())).getData();
            kv.put("mainFileList",  fileEntities);
        }else {
            kv.put("mainFileList",  new ArrayList<>());
        }
        return kv;
    }

    @Override
    public void saveMarketingInfo(JSONObject data) {
        AES aes = SecureUtil.aes(BYTES);
        Integer marketingId = Integer.valueOf(aes.decryptStr(data.getString("marketingId")));
        Long currentUserId = Long.valueOf(aes.decryptStr(data.getString("currentUserId")));
        UserUtil.setUser(ApplicationContextHolder.getBean(AdminService.class).queryLoginUserInfo(currentUserId).getData());
        CrmMarketing crmMarketing = getById(marketingId);
        CrmMarketingInfo marketingInfo = new CrmMarketingInfo();
        marketingInfo.setFieldInfo(data.getJSONObject("fieldInfo").toJSONString());
        marketingInfo.setDevice(data.getString("device"));
        marketingInfo.setMarketingId(marketingId);
        marketingInfo.setOwnerUserId(currentUserId);
        Integer second = crmMarketing.getSecond();
        Integer count = crmMarketingInfoService.lambdaQuery().eq(CrmMarketingInfo::getMarketingId,marketingInfo.getMarketingId())
                .eq(CrmMarketingInfo::getDevice,marketingInfo.getDevice()).count();
        if (second == 1 && count > 0 ){
            throw new CrmException(CrmCodeEnum.CRM_MARKETING_CAN_ONLY_BE_FILLED_ONCE);
        }
        lambdaUpdate().set(CrmMarketing::getSubmitNum,crmMarketing.getSubmitNum()+1).eq(CrmMarketing::getMarketingId,marketingId).update();
        marketingInfo.setCreateTime(new Date());
        crmMarketingInfoService.save(marketingInfo);
    }

    @Autowired
    private ICrmLeadsService crmLeadsService;

    @Autowired
    private ICrmCustomerService customerService;

    @Autowired
    private CrmFieldMapper crmFieldMapper;

    @Override
    public void syncData(CrmSyncDataBO syncDataBO) {
        List<Integer> ids;
        if (CollUtil.isEmpty(syncDataBO.getrIds())){
            ids = crmMarketingInfoService.lambdaQuery().select(CrmMarketingInfo::getRId).eq(CrmMarketingInfo::getMarketingId, syncDataBO.getMarketingId())
                    .eq(CrmMarketingInfo::getOwnerUserId, UserUtil.getUserId()).list()
                    .stream().map(CrmMarketingInfo::getRId).collect(Collectors.toList());
        }else {
            ids = syncDataBO.getrIds();
        }
        for (Integer id : ids) {
            CrmMarketingInfo marketingInfo = crmMarketingInfoService.getById(id);
            if (!marketingInfo.getOwnerUserId().equals(UserUtil.getUserId())){
                throw new CrmException(CrmCodeEnum.CRM_ONLY_SYNC_DATA_FOR_WHICH_YOU_ARE_RESPONSIBLE);
            }
            CrmMarketing crmMarketing = getById(marketingInfo.getMarketingId());
            Integer crmType = crmMarketing.getCrmType();
            Integer status = marketingInfo.getStatus();
            if (status == 1){
                throw new CrmException(CrmCodeEnum.CRM_MARKETING_DATA_SYNCED);
            }
            CrmModelSaveBO crmModelSaveBO = JSON.parseObject(marketingInfo.getFieldInfo(), CrmModelSaveBO.class);
            try {
                if (crmType.equals(CrmEnum.LEADS.getType())){
                    List<CrmModelFiledVO> filedList = crmLeadsService.queryField(null);
                    List<CrmModelFiledVO> uniqueList = filedList.stream().filter(field -> field.getIsUnique()!=null && field.getIsUnique().equals(1)).collect(Collectors.toList());
                    CrmEnum crmEnum = CrmEnum.LEADS;
                    Map<String,String> map = new HashMap<>();
                    for (CrmModelFiledVO crmModelFiledVO : crmModelSaveBO.getField()) {
                        map.put(crmModelFiledVO.getFieldName(),crmModelFiledVO.getValue().toString());
                    }
                    for (CrmModelFiledVO field : uniqueList) {
                        if (field.getFieldType() == 1){
                            String value = crmModelSaveBO.getEntity().get(field.getFieldName()).toString();
                            if (StrUtil.isEmpty(value)) {
                                continue;
                            }
                            Integer count = crmFieldMapper.verifyFixedField(crmEnum.getTable(), StrUtil.toUnderlineCase(field.getFieldName()), value, null, crmEnum.getType());
                            if (count > 0) {
                                crmMarketingInfoService.lambdaUpdate().set(CrmMarketingInfo::getStatus,2).eq(CrmMarketingInfo::getRId,marketingInfo.getRId()).update();
                                throw new CrmException(CrmCodeEnum.CRM_FIELD_ALREADY_EXISTS,field.getName());
                            }
                        }else {
                            if (StrUtil.isEmpty(map.get(field.getFieldName()))) {
                                continue;
                            }
                            Integer count = crmFieldMapper.verifyField(crmEnum.getTable(), field.getFieldId(), map.get(field.getFieldName()), null);
                            if (count > 0) {
                                crmMarketingInfoService.lambdaUpdate().set(CrmMarketingInfo::getStatus,2).eq(CrmMarketingInfo::getRId,marketingInfo.getRId()).update();
                                throw new CrmException(CrmCodeEnum.CRM_FIELD_ALREADY_EXISTS,field.getName());
                            }
                        }
                    }
                    crmLeadsService.addOrUpdate(crmModelSaveBO,false);
                    crmMarketingInfoService.lambdaUpdate().set(CrmMarketingInfo::getStatus,1).eq(CrmMarketingInfo::getRId,marketingInfo.getRId()).update();
                }else if (crmType.equals(CrmEnum.CUSTOMER.getType())){
                    List<CrmModelFiledVO> filedList = customerService.queryField(null);
                    List<CrmModelFiledVO> uniqueList = filedList.stream().filter(field -> field.getIsUnique()!=null && field.getIsUnique().equals(1)).collect(Collectors.toList());
                    CrmEnum crmEnum = CrmEnum.CUSTOMER;
                    Map<String,String> map = new HashMap<>();
                    for (CrmModelFiledVO crmModelFiledVO : crmModelSaveBO.getField()) {
                        map.put(crmModelFiledVO.getFieldName(),crmModelFiledVO.getValue().toString());
                    }
                    for (CrmModelFiledVO field : uniqueList) {
                        if (field.getFieldType() == 1){
                            String value = crmModelSaveBO.getEntity().get(field.getFieldName()).toString();
                            if (StrUtil.isEmpty(value)) {
                                continue;
                            }
                            Integer count = crmFieldMapper.verifyFixedField(crmEnum.getTable(), StrUtil.toUnderlineCase(field.getFieldName()), value, null, crmEnum.getType());
                            if (count > 0) {
                                crmMarketingInfoService.lambdaUpdate().set(CrmMarketingInfo::getStatus,2).eq(CrmMarketingInfo::getRId,marketingInfo.getRId()).update();
                                throw new CrmException(CrmCodeEnum.CRM_FIELD_ALREADY_EXISTS,field.getName());
                            }
                        }else {
                            if (StrUtil.isEmpty(map.get(field.getFieldName()))) {
                                continue;
                            }
                            Integer count = crmFieldMapper.verifyField(crmEnum.getTable(), field.getFieldId(), map.get(field.getFieldName()), null);
                            if (count > 0) {
                                crmMarketingInfoService.lambdaUpdate().set(CrmMarketingInfo::getStatus,2).eq(CrmMarketingInfo::getRId,marketingInfo.getRId()).update();
                                throw new CrmException(CrmCodeEnum.CRM_FIELD_ALREADY_EXISTS,field.getName());
                            }
                        }
                    }
                    customerService.addOrUpdate(crmModelSaveBO,false,null);
                    crmMarketingInfoService.lambdaUpdate().set(CrmMarketingInfo::getStatus,1).eq(CrmMarketingInfo::getRId,marketingInfo.getRId()).update();
                }
            }catch (CrmException e){
                crmMarketingInfoService.lambdaUpdate().set(CrmMarketingInfo::getStatus,2).eq(CrmMarketingInfo::getRId,marketingInfo.getRId()).update();
                throw new CrmException(CrmCodeEnum.CRM_SYNC_FAILED,e.getMsg());
            }
        }
    }
}
