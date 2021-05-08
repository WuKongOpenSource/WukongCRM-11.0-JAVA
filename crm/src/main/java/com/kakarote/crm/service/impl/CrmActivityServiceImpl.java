package com.kakarote.crm.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.constant.*;
import com.kakarote.crm.entity.BO.CrmActivityBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmActivityVO;
import com.kakarote.crm.mapper.CrmActivityMapper;
import com.kakarote.crm.service.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * crm活动表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
@Service
public class CrmActivityServiceImpl extends BaseServiceImpl<CrmActivityMapper, CrmActivity> implements ICrmActivityService {

    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private AdminFileService adminFileService;

    /**
     * 删除活动记录
     * 本质是置为删除
     *
     * @param crmActivityEnum 记录类型
     * @param ids             ids
     */
    @Override
    public void deleteActivityRecord(CrmActivityEnum crmActivityEnum, List<Integer> ids) {
        LambdaUpdateWrapper<CrmActivity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CrmActivity::getActivityType, crmActivityEnum.getType());
        wrapper.in(CrmActivity::getActivityTypeId, ids);
        wrapper.set(CrmActivity::getStatus, 0);
        update(wrapper);
    }

    /**
     * 新增活动记录
     *
     * @param type           type
     * @param activityEnum   类型
     * @param activityTypeId 类型ID
     * @param businessChange 商机变动
     */
    @Override
    public void addActivity(Integer type, CrmActivityEnum activityEnum, Integer activityTypeId, String businessChange) {
        String content = "";
        if (type == 2) {
            content = "创建了" + activityEnum.getRemarks() + ": ";
        } else if (type == 3) {
            content = businessChange;
        }
        CrmActivity crmActivity = new CrmActivity();
        crmActivity.setType(type);
        crmActivity.setActivityType(activityEnum.getType());
        crmActivity.setActivityTypeId(activityTypeId);
        crmActivity.setContent(content);
        crmActivity.setCreateUserId(UserUtil.getUserId());
        crmActivity.setCreateTime(new Date());
        save(crmActivity);
    }

    /**
     * 新增活动记录
     *
     * @param type           type
     * @param activityEnum   类型
     * @param activityTypeId 类型ID
     */
    @Override
    public void addActivity(Integer type, CrmActivityEnum activityEnum, Integer activityTypeId) {
        addActivity(type, activityEnum, activityTypeId, "");
    }

    @Autowired
    private AdminService adminService;

    @Override
    public CrmActivityVO getCrmActivityPageList(CrmActivityBO crmActivity) {
        CrmActivityVO activityVO = new CrmActivityVO();
        Integer activityType = crmActivity.getActivityType();
        Integer activityTypeId = crmActivity.getActivityTypeId();
        int page = crmActivity.getPage() - 1;
        Map<String, Object> kv = new HashMap<>();
        kv.put("activityType", activityType);
        kv.put("activityTypeId", activityTypeId);
        kv.put("crmType", crmActivity.getCrmType());
        kv.put("intervalDay", crmActivity.getIntervalDay());
        kv.put("search", crmActivity.getSearch());
        kv.put("startDate", crmActivity.getStartDate());
        kv.put("endDate", crmActivity.getEndDate());
        kv.put("page", page);
        if (!UserUtil.isAdmin() && !AuthUtil.isReadFollowRecord(crmActivity.getCrmType())) {
            kv.put("nofollowRecord", true);
        }
        List<String> times = getBaseMapper().getActivityCountByTime(kv);
        String time = null;
        if (times.size() == 0) {
            activityVO.setList(new ArrayList<>());
        } else {
            time = times.get(0);
            kv.remove("page");
            kv.put("time", time);
            List<CrmActivity> recordList = getBaseMapper().getCrmActivityPageList(kv);
            recordList.forEach(record -> {
                if (record.getType() == 1 || record.getType() == 4) {
                    buildActivityRelation(record);
                }
                SimpleUser user = adminService.queryUserById(record.getCreateUserId()).getData();
                record.setUserImg(user.getImg());
                record.setRealname(user.getRealname());
            });
            activityVO.setList(recordList);
        }
        activityVO.setTime(time);
        activityVO.setLastPage(times.size() < 2);
        return activityVO;
    }

    /**
     * 添加活动记录
     *
     * @param crmActivity crmActivity
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCrmActivityRecord(CrmActivity crmActivity) {
        if (crmActivity.getNextTime() != null && crmActivity.getNextTime().getTime() < System.currentTimeMillis()) {
            throw new CrmException(CrmCodeEnum.CRM_NEXT_TIME_ERROR);
        }
        UserInfo user = UserUtil.getUser();
        BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + user.getUserId().toString());
        String batchId = StrUtil.isEmpty(crmActivity.getBatchId()) ? IdUtil.simpleUUID() : crmActivity.getBatchId();
        crmActivity.setType(1);
        crmActivity.setCreateUserId(user.getUserId());
        crmActivity.setBatchId(batchId);
        crmActivity.setCreateTime(new Date());
        save(crmActivity);
        updateNextTime(crmActivity);
        if (crmActivity.getType() == 1) {
            crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.TODAY_CUSTOMER, crmActivity.getActivityTypeId());
            crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.FOLLOW_LEADS, crmActivity.getActivityTypeId());
            crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.FOLLOW_CUSTOMER, crmActivity.getActivityTypeId());
            crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.TO_ENTER_CUSTOMER_POOL, crmActivity.getActivityTypeId());
        }
        actionRecordUtil.addFollowupActionRecord(crmActivity.getActivityType(), crmActivity.getActivityTypeId(), "");
    }

    /**
     * 查询文件batchId
     *
     * @param id           id
     * @param activityType 类型
     * @return data
     */
    @Override
    public List<String> queryFileBatchId(Integer id, Integer activityType) {
        LambdaQueryWrapper<CrmActivity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmActivity::getBatchId);
        wrapper.eq(CrmActivity::getActivityType, activityType);
        wrapper.eq(CrmActivity::getActivityTypeId, id);
        return listObjs(wrapper, Object::toString);
    }

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public void updateNextTime(CrmActivity crmActivity) {
        updateNextTime(crmActivity,false);
    }
    /**
     * 更新下次联系时间
     *
     * @param crmActivity activity
     */
    private void updateNextTime(CrmActivity crmActivity,Boolean isDel) {
        Date laseTime = new Date();
        Date nextTime = crmActivity.getNextTime();
        Integer followup = 1;
        String lastContent = crmActivity.getContent();
        if (isDel){
            laseTime = crmActivity.getCreateTime();
            if (StrUtil.isEmpty(crmActivity.getContent())){
                followup = 0;
            }
        }
        Integer activityType = crmActivity.getActivityType();
        Integer activityTypeId = crmActivity.getActivityTypeId();
        if (activityType.equals(CrmEnum.LEADS.getType())) {
            CrmLeads crmLeads = ApplicationContextHolder.getBean(ICrmLeadsService.class).getById(activityTypeId);
            if (isDel && StrUtil.isEmpty(crmActivity.getContent())){
                laseTime = crmLeads.getCreateTime();
            }
            ApplicationContextHolder.getBean(ICrmLeadsService.class).lambdaUpdate()
                    .set(CrmLeads::getLastTime,laseTime).set(CrmLeads::getFollowup,followup)
                    .set(CrmLeads::getLastContent,lastContent).set(CrmLeads::getNextTime,nextTime)
                    .set(CrmLeads::getUpdateTime,new Date())
                    .eq(CrmLeads::getLeadsId,activityTypeId).update();
        } else if (activityType.equals(CrmEnum.CUSTOMER.getType())) {
            CrmCustomer customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).getById(activityTypeId);
            if (isDel && StrUtil.isEmpty(crmActivity.getContent())){
                laseTime = customer.getCreateTime();
            }
            ApplicationContextHolder.getBean(ICrmCustomerService.class).lambdaUpdate()
                    .set(CrmCustomer::getLastTime,laseTime).set(CrmCustomer::getFollowup,followup)
                    .set(CrmCustomer::getLastContent,lastContent).set(CrmCustomer::getNextTime,nextTime)
                    .set(CrmCustomer::getUpdateTime,new Date())
                    .eq(CrmCustomer::getCustomerId,activityTypeId).update();
        } else if (activityType.equals(CrmEnum.BUSINESS.getType())) {
            CrmBusiness business = ApplicationContextHolder.getBean(ICrmBusinessService.class).getById(activityTypeId);
            if (isDel && StrUtil.isEmpty(crmActivity.getContent())){
                laseTime = business.getCreateTime();
            }
            ApplicationContextHolder.getBean(ICrmBusinessService.class).lambdaUpdate()
                    .set(CrmBusiness::getLastTime,laseTime).set(CrmBusiness::getFollowup,followup)
                    .set(CrmBusiness::getNextTime,nextTime)
                    .set(CrmBusiness::getUpdateTime,new Date())
                    .eq(CrmBusiness::getBusinessId,activityTypeId).update();
        } else if (activityType.equals(CrmEnum.CONTACTS.getType())) {
            CrmContacts contacts = ApplicationContextHolder.getBean(ICrmContactsService.class).getById(activityTypeId);
            if (isDel && StrUtil.isEmpty(crmActivity.getContent())){
                laseTime = contacts.getCreateTime();
            }
            ApplicationContextHolder.getBean(ICrmContactsService.class).lambdaUpdate()
                    .set(CrmContacts::getLastTime,laseTime)
                    .set(CrmContacts::getNextTime,nextTime)
                    .set(CrmContacts::getUpdateTime,new Date())
                    .eq(CrmContacts::getContactsId,activityTypeId).update();
        } else if (activityType.equals(CrmEnum.CONTRACT.getType())) {
            CrmContract contract = ApplicationContextHolder.getBean(ICrmContractService.class).getById(activityTypeId);
            if (isDel && StrUtil.isEmpty(crmActivity.getContent())){
                laseTime = contract.getCreateTime();
            }
            ApplicationContextHolder.getBean(ICrmContractService.class).lambdaUpdate()
                    .set(CrmContract::getLastTime,laseTime)
                    .set(CrmContract::getUpdateTime,new Date())
                    .eq(CrmContract::getContractId,activityTypeId).update();
        }
        String index = CrmEnum.parse(activityType).getIndex();
        UpdateRequest updateRequest = new UpdateRequest(index, "_doc", activityTypeId.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("lastTime", DateUtil.formatDateTime(laseTime));
        map.put("followup", followup);
        map.put("lastContent", crmActivity.getContent());
        map.put("updateTime", DateUtil.formatDateTime(new Date()));
        if (nextTime != null) {
            map.put("nextTime", DateUtil.formatDateTime(nextTime));
        }else {
            map.put("nextTime", null);
        }
        updateRequest.doc(map);
        try {
            elasticsearchRestTemplate.getClient().update(updateRequest, RequestOptions.DEFAULT);
            elasticsearchRestTemplate.refresh(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<CrmContacts> crmContactsList = new ArrayList<>();
        List<CrmBusiness> businessList = new ArrayList<>();
        List<CrmActivityRelation> activityRelationList = new ArrayList<>();
        if (crmActivity.getContactsIds() != null) {
            for (Integer contactId : TagUtil.toSet(crmActivity.getContactsIds())) {
                CrmContacts crmContacts = new CrmContacts();
                crmContacts.setContactsId(contactId);
                crmContacts.setNextTime(nextTime);
                CrmActivityRelation activityRelation = new CrmActivityRelation();
                activityRelation.setActivityId(crmActivity.getActivityId());
                activityRelation.setType(CrmEnum.CONTACTS.getType());
                activityRelation.setTypeId(contactId);
                activityRelationList.add(activityRelation);
                crmContactsList.add(crmContacts);
                UpdateRequest updateContactsRequest = new UpdateRequest(CrmEnum.CONTACTS.getIndex(), "_doc", contactId.toString());
                Map<String, Object> contactsMap = new HashMap<>();
                contactsMap.put("nextTime", DateUtil.formatDateTime(nextTime));
                updateContactsRequest.doc(contactsMap);
                try {
                    elasticsearchRestTemplate.getClient().update(updateContactsRequest, RequestOptions.DEFAULT);
                    elasticsearchRestTemplate.refresh(index);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (crmContactsList.size() > 0) {
                ApplicationContextHolder.getBean(ICrmContactsService.class).updateBatchById(crmContactsList, Const.BATCH_SAVE_SIZE);
            }
        }
        if (crmActivity.getBusinessIds() != null) {
            for (Integer businessId : TagUtil.toSet(crmActivity.getBusinessIds())) {
                CrmBusiness crmBusiness = new CrmBusiness();
                crmBusiness.setBusinessId(businessId);
                crmBusiness.setNextTime(nextTime);
                CrmActivityRelation activityRelation = new CrmActivityRelation();
                activityRelation.setActivityId(crmActivity.getActivityId());
                activityRelation.setType(CrmEnum.BUSINESS.getType());
                activityRelation.setTypeId(businessId);
                activityRelationList.add(activityRelation);
                businessList.add(crmBusiness);
                UpdateRequest updateBusinessRequest = new UpdateRequest(CrmEnum.BUSINESS.getIndex(), "_doc", businessId.toString());
                Map<String, Object> businessMap = new HashMap<>();
                businessMap.put("nextTime", DateUtil.formatDateTime(nextTime));
                updateBusinessRequest.doc(businessMap);
                try {
                    elasticsearchRestTemplate.getClient().update(updateBusinessRequest, RequestOptions.DEFAULT);
                    elasticsearchRestTemplate.refresh(index);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (businessList.size() > 0) {
                ApplicationContextHolder.getBean(ICrmBusinessService.class).updateBatchById(businessList, Const.BATCH_SAVE_SIZE);
            }
        }
        ApplicationContextHolder.getBean(ICrmActivityRelationService.class).saveBatch(activityRelationList, Const.BATCH_SAVE_SIZE);
    }

    @Override
    public void buildActivityRelation(CrmActivity record) {
        if (record.getStatus() == 1 && record.getBatchId() != null) {
            List<FileEntity> data = ApplicationContextHolder.getBean(AdminFileService.class).queryFileList(record.getBatchId()).getData();
            Map<String, List<FileEntity>> collect = data.stream().collect(Collectors.groupingBy(FileEntity::getFileType));
            if (collect.containsKey("file")) {
                record.setFile(collect.get("file"));
            } else {
                record.setFile(new ArrayList<>());
            }
            if (collect.containsKey("img")) {
                record.setImg(collect.get("img"));
            } else {
                record.setImg(new ArrayList<>());
            }
        }
        if (record.getActivityType().equals(CrmEnum.CUSTOMER.getType())) {
            String businessIds = record.getBusinessIds();
            List<SimpleCrmEntity> businessList = new ArrayList<>();
            if (businessIds != null) {
                ICrmBusinessService crmBusinessService = ApplicationContextHolder.getBean(ICrmBusinessService.class);
                List<String> businessIdsArr = StrUtil.splitTrim(businessIds, Const.SEPARATOR);
                businessList.addAll(crmBusinessService.querySimpleEntity(businessIdsArr.stream().map(Integer::valueOf).collect(Collectors.toList())));
            }
            String contactsIds = record.getContactsIds();
            List<SimpleCrmEntity> contactsList = new ArrayList<>();
            if (contactsIds != null) {
                ICrmContactsService crmContactsService = ApplicationContextHolder.getBean(ICrmContactsService.class);
                List<String> contactsIdsArr = StrUtil.splitTrim(contactsIds, Const.SEPARATOR);
                contactsList.addAll(crmContactsService.querySimpleEntity(contactsIdsArr.stream().map(Integer::valueOf).collect(Collectors.toList())));
            }
            record.setBusinessList(businessList);
            record.setContactsList(contactsList);
        }
    }


    @Override
    public void deleteCrmActivityRecord(Integer activityId) {
        CrmActivity crmActivity = getById(activityId);
        if(crmActivity == null) {
            return;
        }
        List<Long> longs = AuthUtil.queryAuthUserList(null, CrmAuthEnum.DELETE);
        if (!longs.contains(crmActivity.getCreateUserId())){
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        if (crmActivity.getType() != 1) {
            throw new CrmException(CrmCodeEnum.CRM_CAN_ONLY_DELETE_FOLLOW_UP_RECORDS);
        }
        adminFileService.delete(Collections.singletonList(crmActivity.getBatchId()));
        //查询是否存在上一次跟进记录，存在时更新对应数据的最后跟进时间和内容。
        Optional<CrmActivity> lastRecordOpt = lambdaQuery().eq(CrmActivity::getType, 1).eq(CrmActivity::getActivityType, crmActivity.getActivityType())
                .eq(CrmActivity::getActivityTypeId, crmActivity.getActivityTypeId())
                .ne(CrmActivity::getActivityId, activityId).orderByDesc(CrmActivity::getCreateTime).last("limit 1").oneOpt();
        if (lastRecordOpt.isPresent()) {
            CrmActivity lastRecord = lastRecordOpt.get();
            updateNextTime(lastRecord,true);
        }else {
            crmActivity.setContent("");
            crmActivity.setNextTime(null);
            updateNextTime(crmActivity,true);
        }
        removeById(activityId);
    }

    @Override
    public CrmActivity updateActivityRecord(CrmActivity crmActivity) {
        CrmActivity activity = getById(crmActivity.getActivityId());
        if(activity == null){
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED,"跟进记录");
        }
        List<Long> longs = AuthUtil.queryAuthUserList(null, CrmAuthEnum.EDIT);
        if (!longs.contains(activity.getCreateUserId())){
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        updateById(crmActivity);
        CrmActivity record = getBaseMapper().queryActivityById(crmActivity.getActivityId());
        SimpleUser user = adminService.queryUserById(record.getCreateUserId()).getData();
        record.setUserImg(user.getImg());
        record.setRealname(user.getRealname());
        buildActivityRelation(record);
        updateNextTime(record);
        return record;
    }


    /**
     * 外勤签到
     */
    @Override
    public void outworkSign(CrmActivity crmActivity) {
        UserInfo user = UserUtil.getUser();
        String batchId = StrUtil.isEmpty(crmActivity.getBatchId()) ? IdUtil.simpleUUID() : crmActivity.getBatchId();
        crmActivity.setType(4);
        crmActivity.setCreateUserId(user.getUserId());
        crmActivity.setCreateTime(new Date());
        crmActivity.setBatchId(batchId);
        save(crmActivity);
    }

    /**
     * app外勤统计
     */
    @Override
    public BasePage<JSONObject> queryOutworkStats(PageEntity entity, String startTime, String endTime) {
        Integer menuId = 215;
        List<Long> userIdByAuth = AuthUtil.getUserIdByAuth(menuId);
        return getBaseMapper().queryOutworkStats(entity.parse(), startTime, endTime, userIdByAuth);
    }

    /**
     * app外勤详情
     */
    @Override
    public BasePage<CrmActivity> queryOutworkList(PageEntity entity, String startTime, String endTime, Long userId) {
        Integer menuId = 215;
        List<Long> authUserIdList = AuthUtil.getUserIdByAuth(menuId);
        if (!authUserIdList.contains(userId)) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<CrmActivity> basePage = getBaseMapper().queryOutworkList(entity.parse(), startTime, endTime, userId);
        basePage.getList().forEach(this::buildActivityRelation);
        return basePage;
    }

    /**
     * app 查询签到照片上传设置
     */
    @Override
    public Integer queryPictureSetting() {
        AdminConfig adminConfig = adminService.queryFirstConfigByName("pictureSetting").getData();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
            adminConfig.setStatus(1);
            adminConfig.setName("pictureSetting");
            adminService.updateAdminConfig(adminConfig);
        }
        return adminConfig.getStatus();
    }

    /**
     * app 查询签到照片上传设置
     */
    @Override
    public void setPictureSetting(Integer status) {
        AdminConfig adminConfig = adminService.queryFirstConfigByName("pictureSetting").getData();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
        }
        adminConfig.setStatus(status);
        adminConfig.setName("pictureSetting");
        adminService.updateAdminConfig(adminConfig);
    }

    /**
     * 删除外勤签到
     */
    @Override
    public void deleteOutworkSign(Integer activityId) {
        CrmActivity crmActivity = getById(activityId);
        if (crmActivity == null) {
            return;
        }
        Integer menuId = 216;
        List<Long> authUserIdList = AuthUtil.getUserIdByAuth(menuId);
        if (!authUserIdList.contains(crmActivity.getCreateUserId())) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        removeById(activityId);
    }
}
