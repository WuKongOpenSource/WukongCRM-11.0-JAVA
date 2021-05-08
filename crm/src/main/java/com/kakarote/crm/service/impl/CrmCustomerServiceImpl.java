package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.*;
import com.kakarote.crm.constant.*;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.*;
import com.kakarote.crm.mapper.CrmCustomerMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Service(value = "customerService")
@Slf4j
public class CrmCustomerServiceImpl extends BaseServiceImpl<CrmCustomerMapper, CrmCustomer> implements ICrmCustomerService, CrmPageService {

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmCustomerDataService crmCustomerDataService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    @Autowired
    private ICrmCustomerSettingService crmCustomerSettingService;

    @Autowired
    private ICrmBusinessService crmBusinessService;

    @Autowired
    private ICrmContactsService crmContactsService;

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmCustomerUserStarService crmCustomerUserStarService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;


    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private ICrmCustomerPoolService crmCustomerPoolService;

    @Autowired
    private ICrmCustomerPoolRelationService customerPoolRelationService;

    @Autowired
    private FieldService fieldService;

    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryField(Integer id) {
        return queryField(id,false);
    }

    private List<CrmModelFiledVO> queryField(Integer id,boolean appendInformation) {
        CrmModel crmModel = queryById(id, null);
        List<CrmModelFiledVO> vos = crmFieldService.queryField(crmModel);
        JSONObject value = new JSONObject();
        value.put("location", crmModel.get("location"));
        value.put("address", crmModel.get("address"));
        value.put("detailAddress", crmModel.get("detailAddress"));
        value.put("lng", crmModel.get("lng"));
        value.put("lat", crmModel.get("lat"));
        vos.add(new CrmModelFiledVO("map_address", FieldEnum.MAP_ADDRESS, "地区定位", 1).setIsNull(0).setValue(value));
        if(appendInformation){
            List<CrmModelFiledVO> modelFiledVOS = appendInformation(crmModel);
            if (crmModel.get("preOwnerUserId") != null) {
                CrmModelFiledVO filedVO = new CrmModelFiledVO("preOwnerUserName", FieldEnum.SINGLE_USER, "前负责人", 1);
                List<SimpleUser> data = adminService.queryUserByIds(Collections.singleton((Long) crmModel.get("preOwnerUserId"))).getData();
                filedVO.setValue(data.get(0));
                modelFiledVOS.add(filedVO.setSysInformation(1));
            }
            modelFiledVOS.add(new CrmModelFiledVO("receive_time", FieldEnum.DATETIME, "负责人获取客户时间", 1).setValue(crmModel.get("receiveTime")).setSysInformation(1));
            vos.addAll(modelFiledVOS);
        }
        return vos;
    }

    @Override
    public List<List<CrmModelFiledVO>> queryFormPositionField(Integer id) {
        CrmModel crmModel = queryById(id, null);
        List<List<CrmModelFiledVO>> vos = crmFieldService.queryFormPositionFieldVO(crmModel);
        JSONObject value = new JSONObject();
        value.put("location", crmModel.get("location"));
        value.put("address", crmModel.get("address"));
        value.put("detailAddress", crmModel.get("detailAddress"));
        value.put("lng", crmModel.get("lng"));
        value.put("lat", crmModel.get("lat"));
        CrmModelFiledVO crmModelFiledVO = new CrmModelFiledVO("map_address", FieldEnum.MAP_ADDRESS, "地区定位", 1).setIsNull(0).setValue(value);
        crmModelFiledVO.setStylePercent(100);
        vos.add(ListUtil.toList(crmModelFiledVO));
        return vos;
    }

    /**
     * 导出时查询所有数据
     *
     * @param search 业务查询对象
     * @return data
     */
    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        BasePage<Map<String, Object>> basePage = queryList(search,false);
        Long userId = UserUtil.getUserId();
        List<Integer> starIds = crmCustomerUserStarService.starList(userId);
        basePage.getList().forEach(map -> {
            Integer customerId = (Integer) map.get("customerId");
            map.put("star", starIds.contains((customerId)) ? 1 : 0);
            Integer businessCount = crmBusinessService.lambdaQuery().eq(CrmBusiness::getCustomerId, customerId).eq(CrmBusiness::getStatus, 1).count();
            map.put("businessCount", businessCount);
            //查询联系人,新建合同关联需要
            Object contactsId = map.get("contactsId");
            if (ObjectUtil.isNotEmpty(contactsId)) {
                CrmContacts contacts = crmContactsService.lambdaQuery().select(CrmContacts::getName, CrmContacts::getMobile, CrmContacts::getAddress)
                        .eq(CrmContacts::getContactsId, contactsId).one();
                if (contacts != null) {
                    map.put("contactsName", contacts.getName());
                    map.put("contactsMobile", contacts.getMobile());
                    map.put("contactsAddress", contacts.getAddress());
                }
            }
        });
        setPoolDay(basePage.getList());
        return basePage;
    }


    /**
     * 为客户设置距进入公海时间
     *
     * @param list
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @date 2020/10/26 10:16
     **/
    private void setPoolDay(List<Map<String, Object>> list) {
        Long userId = UserUtil.getUserId();
        Date date = new Date();
        List<CrmCustomerPool> poolList = crmCustomerPoolService.lambdaQuery().eq(CrmCustomerPool::getStatus, 1).eq(CrmCustomerPool::getPutInRule, 1).eq(CrmCustomerPool::getRemindSetting, 1).list();
        poolList.forEach(pool -> {
            List<Long> userIdsList = new ArrayList<>();
            List<Integer> deptIds = StrUtil.splitTrim(pool.getMemberDeptId(), Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList());
            if (deptIds.size() > 0) {
                userIdsList.addAll(adminService.queryUserByDeptIds(deptIds).getData());
            }
            if (StrUtil.isNotEmpty(pool.getMemberUserId())) {
                userIdsList.addAll(Arrays.stream(pool.getMemberUserId().split(Const.SEPARATOR)).map(Long::parseLong).collect(Collectors.toList()));
            }
            List<CrmCustomerPoolRule> ruleList = ApplicationContextHolder.getBean(ICrmCustomerPoolRuleService.class).lambdaQuery().eq(CrmCustomerPoolRule::getPoolId, pool.getPoolId()).list();
            for (CrmCustomerPoolRule rule : ruleList) {
                Integer remindDay = pool.getRemindDay();
                //已成交客户是否进入公海 0不进入 1进入
                Integer dealHandle = rule.getDealHandle();
                //有商机客户是否进入公海 0不进入 1进入
                Integer businessHandle = rule.getBusinessHandle();
                Integer limitDay = rule.getLimitDay();
                //客户级别设置 1全部 2根据级别分别设置
                Integer levelSetting = rule.getCustomerLevelSetting();
                String level = rule.getLevel();
                userIdsList.add(userId);
                for (Map<String, Object> map : list) {
                    //成交状态 0 未成交 1 已成交
                    Integer dealStatus = (Integer) map.get("dealStatus");
                    //商机个数
                    Integer businessCount = (Integer) map.get("businessCount");
                    Long ownerUserId = TypeUtils.castToLong(map.get("ownerUserId"));
                    String customerLevel = TypeUtils.castToString(map.get("level"));
                    //判断负责人
                    if (!userIdsList.contains(ownerUserId)) {
                        continue;
                    }
                    //成交状态
                    if (Objects.equals(dealHandle, 0) && Objects.equals(dealStatus, 1)) {
                        continue;
                    }
                    //商机数量
                    if (Objects.equals(businessHandle, 0) && businessCount > 0) {
                        continue;
                    }
                    //客户级别
                    if (Objects.equals(levelSetting, 2) && !Objects.equals(level, customerLevel)) {
                        continue;
                    }
                    Date receiveTime = map.get("receiveTime") != null ? DateUtil.parse((String) map.get("receiveTime")) : null;
                    if (rule.getType().equals(1)) {
                        //跟进时间
                        Date lastTime = DateUtil.parse((String) map.get("lastTime"));
                        if (lastTime == null) {
                            lastTime = DateUtil.parse((String) map.get("createTime"));
                        }
                        if (receiveTime != null) {
                            lastTime = lastTime.getTime() > receiveTime.getTime() ? lastTime : receiveTime;
                        }
                        setPoolDayForCustomer(lastTime, date, limitDay, levelSetting, map);
                    }
                    if (rule.getType().equals(2)) {
                        setPoolDayForCustomer(receiveTime, date, limitDay, levelSetting, map);
                    }
                    if (rule.getType().equals(3)) {
                        setPoolDayForCustomer(receiveTime, date, limitDay, levelSetting, map);
                    }
                }
            }
        });
    }


    /**
     * 计算客户的距进入公海时间
     *
     * @param startTime
     * @param date
     * @param limitDay
     * @param levelSetting
     * @param map
     * @return void
     * @date 2020/10/26 10:02
     **/
    private static void setPoolDayForCustomer(Date startTime, Date date, Integer limitDay, Integer levelSetting, Map<String, Object> map) {
        if (startTime == null) {
            return;
        }
        long betweenDay = DateUtil.betweenDay(startTime, date, true);
        Integer poolDay = limitDay - (int) betweenDay;
        Integer customerPoolDay = (Integer) map.get("poolDay");
        if (customerPoolDay != null) {
            poolDay = poolDay < customerPoolDay ? poolDay : customerPoolDay;
        }
        poolDay = poolDay > 0 ? poolDay : 0;
        if (Objects.equals(levelSetting, 1)) {
            //所有客户
            map.put("poolDay", poolDay);
        } else if (Objects.equals(levelSetting, 2)) {
            //客户级别
            map.put("poolDay", poolDay);
        }
    }

    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    @Override
    public CrmModel queryById(Integer id, Integer poolId) {
        CrmModel crmModel;
        if (id != null) {
            crmModel = getBaseMapper().queryById(id, UserUtil.getUserId());
            crmModel.setLabel(CrmEnum.CUSTOMER.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            crmCustomerDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
            if (ObjectUtil.isNotEmpty(poolId)) {
                LambdaQueryWrapper<CrmCustomerPoolFieldSetting> wrapper = new LambdaQueryWrapper<>();
                wrapper.select(CrmCustomerPoolFieldSetting::getFieldName);
                wrapper.eq(CrmCustomerPoolFieldSetting::getPoolId, poolId).eq(CrmCustomerPoolFieldSetting::getIsHidden, 1);
                List<String> nameList = ApplicationContextHolder.getBean(ICrmCustomerPoolFieldSettingService.class).listObjs(wrapper, Object::toString);
                nameList.forEach(crmModel::remove);
                JSONObject poolAuthList = crmCustomerPoolService.queryAuthByPoolId(poolId);
                crmModel.put("poolAuthList", poolAuthList);
            }else{
                Long isPool = (Long) crmModel.get("isPool");
                if (Objects.equals(isPool,1L)){
                    String poolIdStr = baseMapper.queryPoolIdsByCustomer(id);
                    if (StrUtil.isNotEmpty(poolIdStr)){
                        List<String> poolIds = StrUtil.splitTrim(poolIdStr, Const.SEPARATOR);
                        List<Integer> poolIdList = poolIds.stream().map(Integer::valueOf).collect(Collectors.toList());
                        JSONObject poolAuthList = crmCustomerPoolService.getOnePoolAuthByPoolIds(poolIdList);
                        crmModel.put("poolAuthList", poolAuthList);
                    }
                }
            }
            Integer contactsId = (Integer) crmModel.get("contactsId");
            if (contactsId != null) {
                CrmContacts contacts = crmContactsService.getById(contactsId);
                crmModel.put("contactsName", contacts.getName());
                crmModel.put("contactsMobile", contacts.getMobile());
                crmModel.put("contactsAddress", contacts.getAddress());
            }
        } else {
            crmModel = new CrmModel(CrmEnum.CUSTOMER.getType());
        }
        return crmModel;
    }

    /**
     * 保存或新增信息
     *
     * @param crmModel model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addOrUpdate(CrmModelSaveBO crmModel, boolean isExcel, Integer poolId) {
        CrmCustomer crmCustomer = BeanUtil.copyProperties(crmModel.getEntity(), CrmCustomer.class);
        if (crmCustomer.getCustomerId() != null) {
            if (!UserUtil.isAdmin() && getBaseMapper().queryIsRoUser(crmCustomer.getCustomerId(), UserUtil.getUserId()) > 0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
        }
        String batchId = StrUtil.isNotEmpty(crmCustomer.getBatchId()) ? crmCustomer.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_customer_data"));
        crmCustomerDataService.saveData(crmModel.getField(), batchId);
        if (StrUtil.isEmpty(crmCustomer.getEmail())) {
            crmCustomer.setEmail(null);
        }
        if (crmCustomer.getCustomerId() != null) {
            crmBackLogDealService.deleteByType(crmCustomer.getOwnerUserId(), getLabel(), CrmBackLogEnum.TODAY_CUSTOMER, crmCustomer.getCustomerId());
            crmBackLogDealService.deleteByType(crmCustomer.getOwnerUserId(), getLabel(), CrmBackLogEnum.FOLLOW_CUSTOMER, crmCustomer.getCustomerId());
            crmCustomer.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(getById(crmCustomer.getCustomerId())), BeanUtil.beanToMap(crmCustomer), CrmEnum.CUSTOMER, crmCustomer.getCustomerName(), crmCustomer.getCustomerId());
            updateById(crmCustomer);
            crmCustomer = getById(crmCustomer.getCustomerId());
            ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(), "customer", crmCustomer.getCustomerId().toString(), crmCustomer.getCustomerName());
        } else {
            crmCustomer.setCreateTime(DateUtil.date());
            crmCustomer.setUpdateTime(DateUtil.date());
            crmCustomer.setReceiveTime(DateUtil.date());
            crmCustomer.setCreateUserId(UserUtil.getUserId());
            if (!isExcel && crmCustomer.getOwnerUserId() == null) {
                //导入会手动选择负责人,需要判断
                crmCustomer.setOwnerUserId(UserUtil.getUserId());
            }
            crmCustomer.setBatchId(batchId);
            crmCustomer.setLastTime(new Date());
            crmCustomer.setStatus(1);
            crmCustomer.setDealStatus(0);
            if (crmCustomer.getOwnerUserId() != null) {
                if (!crmCustomerSettingService.queryCustomerSettingNum(1, crmCustomer.getOwnerUserId())) {
                    throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_SETTING_USER_ERROR);
                }
            }
            save(crmCustomer);
            crmActivityService.addActivity(2, CrmActivityEnum.CUSTOMER, crmCustomer.getCustomerId(), "");
            actionRecordUtil.addRecord(crmCustomer.getCustomerId(), CrmEnum.CUSTOMER, crmCustomer.getCustomerName());
            if (isExcel && poolId != null) {
                CrmCustomerPoolRelation relation = new CrmCustomerPoolRelation();
                relation.setCustomerId(crmCustomer.getCustomerId());
                relation.setPoolId(poolId);
                customerPoolRelationService.save(relation);
            }
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmCustomer));
        if (isExcel && poolId != null) {
            List<CrmCustomerPoolRelation> poolRelations = customerPoolRelationService.lambdaQuery()
                    .select(CrmCustomerPoolRelation::getPoolId)
                    .eq(CrmCustomerPoolRelation::getCustomerId, crmCustomer.getCustomerId())
                    .list();
            crmModel.getEntity().put("poolId", poolRelations.stream().map(CrmCustomerPoolRelation::getPoolId).collect(Collectors.toList()));
        }
        savePage(crmModel, crmCustomer.getCustomerId(), isExcel);
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", crmCustomer.getCustomerId());
        map.put("customerName", crmCustomer.getCustomerName());
        return map;
    }

    @Override
    public void setOtherField(Map<String, Object> map) {
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName", ownerUserName);
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName", createUserName);
    }

    /**
     * 删除客户数据
     *
     * @param ids ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        Integer contactsNum = crmContactsService.lambdaQuery().in(CrmContacts::getCustomerId, ids).count();
        Integer businessNum = crmBusinessService.lambdaQuery().in(CrmBusiness::getCustomerId, ids).eq(CrmBusiness::getStatus, 1).count();
        if (contactsNum > 0 || businessNum > 0) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_JOIN_ERROR);
        }
        lambdaUpdate().set(CrmCustomer::getUpdateTime, new Date()).set(CrmCustomer::getStatus, 3).in(CrmCustomer::getCustomerId, ids).update();
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmCustomer::getCustomerId, ids);
        List<String> batchList = listObjs(wrapper, Object::toString);
        //删除文件
        adminFileService.delete(batchList);
        //删除跟进记录
        crmActivityService.deleteActivityRecord(CrmActivityEnum.CUSTOMER, ids);
        //删除字段操作记录
        crmActionRecordService.deleteActionRecord(CrmEnum.CUSTOMER, ids);
        //删除自定义字段
        crmCustomerDataService.deleteByBatchId(batchList);
        deletePage(ids);

    }


    @Override
    public JSONObject detectionDataCanBeDelete(List<Integer> ids) {
        Integer contactsNum = crmContactsService.lambdaQuery().in(CrmContacts::getCustomerId, ids).count();
        Integer businessNum = crmBusinessService.lambdaQuery().in(CrmBusiness::getCustomerId, ids).eq(CrmBusiness::getStatus, 1).count();
        JSONObject record = new JSONObject();
        record.fluentPut("contactsNum",contactsNum).fluentPut("businessNum",businessNum).fluentPut("isMore",ids.size() > 1);
        return record;
    }

    /**
     * 修改客户负责人
     *
     * @param changOwnerUserBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeOwnerUser(CrmChangeOwnerUserBO changOwnerUserBO) {
        if (!isMaxOwner(changOwnerUserBO.getOwnerUserId(), changOwnerUserBO.getIds())) {
            throw new CrmException(CrmCodeEnum.THE_NUMBER_OF_CUSTOMERS_HAS_REACHED_THE_LIMIT);
        }
        String ownerUserName = UserCacheUtil.getUserName(changOwnerUserBO.getOwnerUserId());
        List<Long> userList = new ArrayList<>();
        if (UserUtil.isAdmin()){
            userList = adminService.queryUserList(1).getData();
        }else {
            userList.add(UserUtil.getUserId());
            userList.addAll(adminService.queryChildUserId(UserUtil.getUserId()).getData());
        }
        List<Long> finalUserList = userList;
        BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + changOwnerUserBO.getOwnerUserId().toString());
        changOwnerUserBO.getIds().forEach(id -> {
            if (AuthUtil.isChangeOwnerUserAuth(id, CrmEnum.CUSTOMER,CrmAuthEnum.EDIT)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            CrmCustomer customer = getById(id);
            if (2 == changOwnerUserBO.getTransferType() && !changOwnerUserBO.getOwnerUserId().equals(customer.getOwnerUserId())) {
                ApplicationContextHolder.getBean(ICrmTeamMembersService.class).addSingleMember(getLabel(),customer.getCustomerId(),customer.getOwnerUserId(),changOwnerUserBO.getPower(),changOwnerUserBO.getExpiresTime(),customer.getCustomerName());
            }
            ApplicationContextHolder.getBean(ICrmTeamMembersService.class).deleteMember(getLabel(),new CrmMemberSaveBO(id,changOwnerUserBO.getOwnerUserId()));
            customer.setOwnerUserId(changOwnerUserBO.getOwnerUserId());
            customer.setFollowup(0);
            customer.setIsReceive(1);
            customer.setReceiveTime(DateUtil.date());
            BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + customer.getOwnerUserId().toString());
            updateById(customer);
            actionRecordUtil.addConversionRecord(id, CrmEnum.CUSTOMER, changOwnerUserBO.getOwnerUserId(), customer.getCustomerName());
            changOwnerUserBO.getChangeType().forEach(type -> {
                switch (type) {
                    case 1: {
                        List<Integer> ids = crmContactsService.lambdaQuery()
                                .select(CrmContacts::getContactsId)
                                .eq(CrmContacts::getCustomerId, id)
                                .in(CrmContacts::getOwnerUserId, finalUserList)
                                .list().stream().map(CrmContacts::getContactsId).collect(Collectors.toList());
                        changOwnerUserBO.setIds(ids);
                        crmContactsService.changeOwnerUser(changOwnerUserBO);
                        break;
                    }
                    case 2: {
                        List<Integer> ids = crmBusinessService.lambdaQuery()
                                .select(CrmBusiness::getBusinessId)
                                .eq(CrmBusiness::getCustomerId, id)
                                .in(CrmBusiness::getOwnerUserId,finalUserList)
                                .list().stream().map(CrmBusiness::getBusinessId).collect(Collectors.toList());
                        CrmChangeOwnerUserBO changOwnerUser = new CrmChangeOwnerUserBO();
                        changOwnerUser.setPower(changOwnerUserBO.getPower());
                        changOwnerUser.setTransferType(changOwnerUserBO.getTransferType());
                        changOwnerUser.setIds(ids);
                        changOwnerUser.setOwnerUserId(changOwnerUserBO.getOwnerUserId());
                        crmBusinessService.changeOwnerUser(changOwnerUser);
                        break;
                    }
                    case 3: {
                        List<Integer> ids = crmContractService.lambdaQuery()
                                .select(CrmContract::getContractId)
                                .eq(CrmContract::getCustomerId, id)
                                .in(CrmContract::getOwnerUserId,finalUserList)
                                .list().stream().map(CrmContract::getContractId).collect(Collectors.toList());
                        CrmChangeOwnerUserBO changOwnerUser = new CrmChangeOwnerUserBO();
                        changOwnerUser.setTransferType(changOwnerUserBO.getTransferType());
                        changOwnerUser.setPower(changOwnerUserBO.getPower());
                        changOwnerUser.setIds(ids);
                        changOwnerUser.setOwnerUserId(changOwnerUserBO.getOwnerUserId());
                        crmContractService.changeOwnerUser(changOwnerUser);
                        break;
                    }
                    default:
                        break;
                }
            });
            //修改es
            Map<String, Object> map = new HashMap<>();
            map.put("ownerUserId", changOwnerUserBO.getOwnerUserId());
            map.put("ownerUserName", ownerUserName);
            map.put("followup", 0);
            map.put("isReceive", 1);
            map.put("receiveTime", DateUtil.formatDateTime(new Date()));
            updateField(map, Collections.singletonList(id));
        });
    }

    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    @Override
    public void exportExcel(HttpServletResponse response, CrmSearchBO search) {
        List<Map<String, Object>> dataList = queryList(search,true).getList();
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<CrmFieldSortVO> headList = crmFieldService.queryListHead(getLabel().getType());
            headList.removeIf(head -> FieldEnum.HANDWRITING_SIGN.getFormType().equals(head.getFormType()));
            headList.forEach(head -> writer.addHeaderAlias(head.getFieldName(), head.getName()));
            writer.merge(headList.size() - 1, "客户信息");
            if (dataList.size() == 0) {
                Map<String, Object> record = new HashMap<>();
                headList.forEach(head -> record.put(head.getFieldName(), ""));
                dataList.add(record);
            }
            for (Map<String, Object> record : dataList) {
                headList.forEach(field ->{
                    if (fieldService.equalsByType(field.getType())) {
                        record.put(field.getFieldName(),ActionRecordUtil.parseValue(record.get(field.getFieldName()),field.getType(),false));
                    }
                });
                record.put("dealStatus", Objects.equals(1, record.get("dealStatus")) ? "已成交" : "未成交");
                record.put("status", Objects.equals(1, record.get("status")) ? "未锁定" : "已锁定");
            }
            writer.setOnlyAlias(true);
            writer.write(dataList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=customer.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出客户错误：", e);
        }
    }

    /**
     * 客户放入公海
     *
     * @param poolBO bo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomerByIds(CrmCustomerPoolBO poolBO) {
        if (poolBO.getIds().size() == 0) {
            return;
        }
        Long userId = UserUtil.getUserId();
        List<CrmOwnerRecord> ownerRecordList = new ArrayList<>();
        List<CrmCustomerPoolRelation> poolRelationList = new ArrayList<>();
        for (Integer id : poolBO.getIds()) {
            CrmCustomer crmCustomer = getById(id);
            if (crmCustomer.getOwnerUserId() == null) {
                continue;
            }
            /* 放入公海和修改负责人所需的权限相同 */
            if (AuthUtil.isChangeOwnerUserAuth(id,getLabel(),CrmAuthEnum.EDIT)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            CrmOwnerRecord crmOwnerRecord = new CrmOwnerRecord();
            crmOwnerRecord.setTypeId(id);
            crmOwnerRecord.setType(CrmEnum.CUSTOMER_POOL.getType());
            crmOwnerRecord.setPreOwnerUserId(crmCustomer.getOwnerUserId());
            crmOwnerRecord.setCreateTime(DateUtil.date());
            ownerRecordList.add(crmOwnerRecord);
            lambdaUpdate()
                    .set(CrmCustomer::getOwnerUserId, null)
                    .set(CrmCustomer::getPreOwnerUserId, userId)
                    .set(CrmCustomer::getPoolTime, new Date())
                    .set(CrmCustomer::getIsReceive, null)
                    .eq(CrmCustomer::getCustomerId, crmCustomer.getCustomerId()).update();
            CrmCustomerPoolRelation relation = new CrmCustomerPoolRelation();
            relation.setCustomerId(id);
            relation.setPoolId(poolBO.getPoolId());
            poolRelationList.add(relation);
            actionRecordUtil.addPutIntoTheOpenSeaRecord(id, getLabel(), crmCustomer.getCustomerName());
        }
        if (ownerRecordList.size() > 0) {
            ApplicationContextHolder.getBean(ICrmOwnerRecordService.class).saveBatch(ownerRecordList);
        }
        if (poolRelationList.size() > 0) {
            customerPoolRelationService.saveBatch(poolRelationList);
        }
        LambdaUpdateWrapper<CrmContacts> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CrmContacts::getOwnerUserId, null);
        wrapper.in(CrmContacts::getCustomerId, poolBO.getIds());
        crmContactsService.update(wrapper);
        putInPool(poolBO);
    }

    /**
     * 标星
     *
     * @param customerId 客户id
     */
    @Override
    public void star(Integer customerId) {
        LambdaQueryWrapper<CrmCustomerUserStar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmCustomerUserStar::getCustomerId, customerId);
        wrapper.eq(CrmCustomerUserStar::getUserId, UserUtil.getUserId());
        CrmCustomerUserStar star = crmCustomerUserStarService.getOne(wrapper);
        if (star == null) {
            star = new CrmCustomerUserStar();
            star.setCustomerId(customerId);
            star.setUserId(UserUtil.getUserId());
            crmCustomerUserStarService.save(star);
        } else {
            crmCustomerUserStarService.removeById(star.getId());
        }
    }

    /**
     * 设置首要联系人
     *
     * @param contactsBO data
     */
    @Override
    public void setContacts(CrmFirstContactsBO contactsBO) {
        lambdaUpdate().set(CrmCustomer::getContactsId, contactsBO.getContactsId())
                .eq(CrmCustomer::getCustomerId, contactsBO.getCustomerId()).update();
    }

    /**
     * 领取或分配客户
     *
     * @param poolBO    bo
     * @param isReceive 领取还是分配
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void getCustomersByIds(CrmCustomerPoolBO poolBO, Integer isReceive) {
        if (poolBO.getIds().size() == 0) {
            return;
        }
        if (poolBO.getUserId() == null) {
            poolBO.setUserId(UserUtil.getUserId());
        }
        if (AuthUtil.isPoolAdmin(poolBO.getPoolId()) && isReceive == 1) {
            throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_DISTRIBUTE_ERROR);
        }
        if (!isMaxOwner(poolBO.getUserId(), poolBO.getIds())) {
            throw new CrmException(CrmCodeEnum.THE_NUMBER_OF_CUSTOMERS_HAS_REACHED_THE_LIMIT);
        }
        CrmCustomerPool pool = crmCustomerPoolService.getById(poolBO.getPoolId());
        if (isReceive == 2) {
            if (pool.getReceiveSetting() != null && pool.getReceiveSetting() == 1) {
                if (poolBO.getIds().size() > pool.getReceiveNum()) {
                    throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_RECEIVE_ERROR);
                }
                Redis redis = BaseUtil.getRedis();
                String key = "receiveNum:poolId_" + poolBO.getPoolId() + ":userId_" + poolBO.getUserId();
                Integer num = redis.get(key);
                if (ObjectUtil.isNotEmpty(num) && (num + poolBO.getIds().size() > pool.getReceiveNum())) {
                    throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_RECEIVE_NUMBER_ERROR);
                }
                long expireTime = (DateUtil.endOfDay(DateUtil.date()).getTime() - System.currentTimeMillis()) / 1000;
                redis.setex(key, (int) expireTime, ObjectUtil.isEmpty(num) ? poolBO.getIds().size() : num + poolBO.getIds().size());
            }
        }
        List<CrmOwnerRecord> records = new ArrayList<>();
        for (Integer id : poolBO.getIds()) {
            CrmCustomer customer = query().select("customer_id", "customer_name").eq("customer_id", id).one();
            actionRecordUtil.addDistributionRecord(id, CrmEnum.CUSTOMER, isReceive.equals(1) ? poolBO.getUserId() : null, customer.getCustomerName());
            //前负责人领取限制，从前负责人脱手开始计算天数
            if (isReceive == 2) {
                if (pool.getPreOwnerSetting() == 1) {
                    Integer days = getBaseMapper().queryOutDays(id, poolBO.getUserId());
                    if (days != null && days <= pool.getPreOwnerSettingDay()) {
                        throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_PRE_USER_RECEIVE_ERROR);
                    }
                }
            }
            CrmOwnerRecord crmOwnerRecord = new CrmOwnerRecord();
            crmOwnerRecord.setTypeId(id);
            crmOwnerRecord.setType(CrmEnum.CUSTOMER_POOL.getType());
            crmOwnerRecord.setPostOwnerUserId(poolBO.getUserId());
            crmOwnerRecord.setCreateTime(DateUtil.date());
            records.add(crmOwnerRecord);
        }
        ApplicationContextHolder.getBean(ICrmOwnerRecordService.class).saveBatch(records);
        LambdaQueryWrapper<CrmCustomerPoolRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmCustomerPoolRelation::getCustomerId, poolBO.getIds());
        customerPoolRelationService.remove(wrapper);
        List<Integer> contactsIds = crmContactsService.lambdaQuery().select(CrmContacts::getContactsId).in(CrmContacts::getCustomerId, poolBO.getIds()).list()
                .stream().map(CrmContacts::getContactsId).collect(Collectors.toList());
        crmContactsService.lambdaUpdate().set(CrmContacts::getOwnerUserId, poolBO.getUserId()).in(CrmContacts::getCustomerId, poolBO.getIds()).update();
        lambdaUpdate()
                .set(CrmCustomer::getOwnerUserId, poolBO.getUserId())
                .set(CrmCustomer::getFollowup, 0)
                .set(CrmCustomer::getReceiveTime, new Date())
                .set(CrmCustomer::getUpdateTime, new Date())
                .set(CrmCustomer::getIsReceive, isReceive)
                .in(CrmCustomer::getCustomerId, poolBO.getIds())
                .update();
        receiveCustomer(poolBO, isReceive, contactsIds);
    }

    /**
     * 下载导入模板
     *
     * @param response resp
     * @throws IOException ex
     */
    @Override
    public void downloadExcel(boolean isPool,HttpServletResponse response) throws IOException {
        List<CrmModelFiledVO> crmModelFiledList = queryField(null);
        int k = 0;
        for (int i = 0; i < crmModelFiledList.size(); i++) {
            if(crmModelFiledList.get(i).getFieldName().equals("customerName")){
                k=i;break;
            }
        }
        crmModelFiledList.add(k+1,new CrmModelFiledVO("ownerUserId",FieldEnum.TEXT,"负责人",1).setIsNull(isPool ? 0 : 1));
        removeFieldByType(crmModelFiledList);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("客户导入表");
        sheet.setDefaultRowHeight((short) 400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < crmModelFiledList.size() + 4; i++) {
            if (crmModelFiledList.size() > i && Objects.equals(crmModelFiledList.get(i).getType(), FieldEnum.DATE.getType())) {
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATE_PATTERN));
                sheet.setDefaultColumnStyle(i, dateStyle);
            } else if (crmModelFiledList.size() > i && Objects.equals(crmModelFiledList.get(i).getType(), FieldEnum.DATETIME.getType())) {
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATETIME_PATTERN));
                sheet.setDefaultColumnStyle(i, dateStyle);
            } else {
                sheet.setDefaultColumnStyle(i, textStyle);
            }
            sheet.setColumnWidth(i, 20 * 256);
        }
        HSSFRow titleRow = sheet.createRow(0);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        titleRow.createCell(0).setCellValue("客户导入模板(*)为必填项");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, crmModelFiledList.size() + 3);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < crmModelFiledList.size(); i++) {
                CrmModelFiledVO record = crmModelFiledList.get(i);
                String fieldName = "_" + record.getFieldName();
                fieldName = fieldName.replaceAll("[\\n`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]", "");
                //省市区需特殊处理
                if ("_mapAddress".equals(fieldName)) {
                    HSSFCell cell1 = row.createCell(i);
                    cell1.setCellValue("省");
                    HSSFCell cell2 = row.createCell(i + 1);
                    cell2.setCellValue("市");
                    HSSFCell cell3 = row.createCell(i + 2);
                    cell3.setCellValue("区");
                    HSSFCell cell4 = row.createCell(i + 3);
                    cell4.setCellValue("详细地址");
                    HSSFSheet hideSheet = wb.createSheet(fieldName);
                    wb.setSheetHidden(wb.getSheetIndex(hideSheet), true);
                    int rowId = 0;
                    // 设置第一行，存省的信息
                    Row provinceRow = hideSheet.createRow(rowId++);
                    provinceRow.createCell(0).setCellValue("省列表");
                    List<String> provinceList = getBaseMapper().queryCityList(100000);
                    for (int x = 0; x < provinceList.size(); x++) {
                        Cell provinceCell = provinceRow.createCell(x + 1);
                        provinceCell.setCellValue(provinceList.get(x));
                    }
                    // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
                    Map<String, List<String>> areaMap = CrmExcelUtil.getAreaMap();
                    for (String key : areaMap.keySet()) {
                        List<String> son = areaMap.get(key);
                        Row subRow = hideSheet.createRow(rowId++);
                        subRow.createCell(0).setCellValue(key);
                        for (int x = 0; x < son.size(); x++) {
                            Cell cell = subRow.createCell(x + 1);
                            cell.setCellValue(son.get(x));
                        }
                        // 添加名称管理器
                        String range = CrmExcelUtil.getRange(1, rowId, son.size());
                        Name name = wb.createName();
                        // key不可重复
                        name.setNameName(key);
                        String formula = fieldName + "!" + range;
                        name.setRefersToFormula(formula);
                    }
                    // 省级下拉框
                    CellRangeAddressList provRangeAddressList = new CellRangeAddressList(2, Integer.MAX_VALUE, i, i);
                    String[] arr = provinceList.toArray(new String[]{});
                    DVConstraint provConstraint = DVConstraint.createExplicitListConstraint(arr);
                    HSSFDataValidation provinceDataValidation = new HSSFDataValidation(provRangeAddressList, provConstraint);
                    provinceDataValidation.createErrorBox("error", "请选择正确的省份");
                    sheet.addValidationData(provinceDataValidation);
                    //市 区下拉框
                    for (int x = 2; x < 10000; x++) {
                        CrmExcelUtil.setDataValidation(CrmExcelUtil.getCorrespondingLabel(i + 1), sheet, x, i + 1);
                        CrmExcelUtil.setDataValidation(CrmExcelUtil.getCorrespondingLabel(i + 2), sheet, x, i + 2);
                    }
                } else {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(record.getName() + (record.getIsNull() == 1 ? "(*)" : ""));
                    if (record.getSetting() != null && record.getSetting().size() != 0) {
                        List<String> setting = record.getSetting().stream().map(Object::toString).collect(Collectors.toList());
                        HSSFSheet hidden = wb.createSheet(fieldName);
                        HSSFCell sheetCell = null;
                        for (int j = 0, length = setting.size(); j < length; j++) {
                            String name = setting.get(j);
                            HSSFRow sheetRow = hidden.createRow(j);
                            sheetCell = sheetRow.createCell(0);
                            sheetCell.setCellValue(name);
                        }
                        Name namedCell = wb.createName();
                        namedCell.setNameName(fieldName);
                        namedCell.setRefersToFormula(fieldName + "!$A$1:$A$" + setting.size());
                        CellRangeAddressList regions = new CellRangeAddressList(2, Integer.MAX_VALUE, i, i);
                        DVConstraint constraint = DVConstraint.createFormulaListConstraint(fieldName);
                        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
                        wb.setSheetHidden(wb.getSheetIndex(hidden), true);
                        sheet.addValidationData(dataValidation);
                    }
                }
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=customer_import.xls");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            wb.close();
        }
    }

    /**
     * 保存客户规则设置
     *
     * @param customerSetting setting
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void customerSetting(CrmCustomerSetting customerSetting) {
        ICrmCustomerSettingUserService settingUserService = ApplicationContextHolder.getBean(ICrmCustomerSettingUserService.class);
        settingUserService.removeByMap(new JSONObject().fluentPut("setting_id", customerSetting.getSettingId()));
        customerSetting.setCreateTime(DateUtil.date());
        crmCustomerSettingService.saveOrUpdate(customerSetting);
        Integer type = customerSetting.getType();
        List<Integer> settingIds = crmCustomerSettingService.lambdaQuery()
                .select(CrmCustomerSetting::getSettingId)
                .eq(CrmCustomerSetting::getType, type).list()
                .stream().map(CrmCustomerSetting::getSettingId).collect(Collectors.toList());
        List<CrmCustomerSettingUser> userList = new ArrayList<>();
        for (SimpleDept dept : customerSetting.getDeptIds()) {
            Integer count = settingUserService.lambdaQuery()
                    .eq(CrmCustomerSettingUser::getDeptId, dept.getId())
                    .eq(CrmCustomerSettingUser::getType, 2)
                    .in(CrmCustomerSettingUser::getSettingId, settingIds).count();
            if (count > 0) {
                throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_SETTING_USER_EXIST_ERROR);
            }
            CrmCustomerSettingUser crmCustomerSettingUser = new CrmCustomerSettingUser();
            crmCustomerSettingUser.setDeptId(dept.getId());
            crmCustomerSettingUser.setSettingId(customerSetting.getSettingId());
            crmCustomerSettingUser.setType(2);
            userList.add(crmCustomerSettingUser);
        }
        for (SimpleUser user : customerSetting.getUserIds()) {
            Integer count = settingUserService.lambdaQuery()
                    .eq(CrmCustomerSettingUser::getUserId, user.getUserId())
                    .eq(CrmCustomerSettingUser::getType, 1)
                    .in(CrmCustomerSettingUser::getSettingId, settingIds).count();
            if (count > 0) {
                throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_SETTING_USER_EXIST_ERROR);
            }
            CrmCustomerSettingUser crmCustomerSettingUser = new CrmCustomerSettingUser();
            crmCustomerSettingUser.setUserId(user.getUserId());
            crmCustomerSettingUser.setSettingId(customerSetting.getSettingId());
            crmCustomerSettingUser.setType(1);
            userList.add(crmCustomerSettingUser);
        }
        settingUserService.saveBatch(userList);
    }

    /**
     * 删除客户规则设置
     *
     * @param settingId settingId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomerSetting(Integer settingId) {
        CrmCustomerSetting setting = crmCustomerSettingService.getById(settingId);
        if (setting != null) {
            ICrmCustomerSettingUserService settingUserService = ApplicationContextHolder.getBean(ICrmCustomerSettingUserService.class);
            settingUserService.removeByMap(new JSONObject().fluentPut("setting_id", settingId));
            crmCustomerSettingService.removeById(settingId);
        }
    }

    @Override
    public List<CrmModelFiledVO> information(Integer customerId, Integer poolId) {
        List<CrmModelFiledVO> collect = queryField(customerId, true);
        if (ObjectUtil.isNotEmpty(poolId)) {
            LambdaQueryWrapper<CrmCustomerPoolFieldSetting> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmCustomerPoolFieldSetting::getName, CrmCustomerPoolFieldSetting::getIsHidden);
            wrapper.eq(CrmCustomerPoolFieldSetting::getPoolId, poolId);
            List<CrmCustomerPoolFieldSetting> fieldSettings = ApplicationContextHolder.getBean(ICrmCustomerPoolFieldSettingService.class).list(wrapper);
            List<String> nameList = fieldSettings.stream().filter(setting -> setting.getIsHidden().equals(1)).map(CrmCustomerPoolFieldSetting::getName).collect(Collectors.toList());
            //查询新增的自定义字段并且公海还没有进行设置的字段名称
            List<String> collect1 = collect.stream().map(CrmModelFiledVO::getName).collect(Collectors.toList());
            List<String> collect2 = fieldSettings.stream().map(CrmCustomerPoolFieldSetting::getName).collect(Collectors.toList());
            Collection<String> disjunction = CollUtil.disjunction(collect1, collect2);
            collect.removeIf(r -> disjunction.contains(r.getName()) || nameList.contains(r.getName()) || "owner_user_name".equals(r.getFieldName()) || "receive_time".equals(r.getFieldName()));
        } else {
            collect.removeIf(r -> "owner_user_name".equals(r.getFieldName()));
        }
        return collect;
    }

    /**
     * 查询客户规则设置
     *
     * @param pageEntity entity
     * @param type       type
     */
    @Override
    public BasePage<CrmCustomerSetting> queryCustomerSetting(PageEntity pageEntity, Integer type) {
        BasePage<CrmCustomerSetting> page = crmCustomerSettingService.lambdaQuery().eq(CrmCustomerSetting::getType, type).page(pageEntity.parse());
        ICrmCustomerSettingUserService settingUserService = ApplicationContextHolder.getBean(ICrmCustomerSettingUserService.class);
        page.getList().forEach(crmCustomerSetting -> {
            List<CrmCustomerSettingUser> list = settingUserService.lambdaQuery().eq(CrmCustomerSettingUser::getSettingId, crmCustomerSetting.getSettingId()).list();
            List<Integer> deptIds = new ArrayList<>();
            List<Long> userIds = new ArrayList<>();
            list.forEach(settingUser -> {
                if (settingUser.getType().equals(1)) {
                    userIds.add(settingUser.getUserId());
                } else if (settingUser.getType().equals(2)) {
                    deptIds.add(settingUser.getDeptId());
                }
            });
            if (userIds.size() > 0) {
                List<SimpleUser> data = adminService.queryUserByIds(userIds).getData();
                crmCustomerSetting.setUserIds(data);
                crmCustomerSetting.setRange(data.stream().map(SimpleUser::getRealname).collect(Collectors.joining(Const.SEPARATOR)));
            } else {
                crmCustomerSetting.setUserIds(new ArrayList<>());
            }

            if (deptIds.size() > 0) {
                List<SimpleDept> data = adminService.queryDeptByIds(deptIds).getData();
                crmCustomerSetting.setDeptIds(data);
                String range = crmCustomerSetting.getRange();
                if (StrUtil.isNotEmpty(range)) {
                    range = range + Const.SEPARATOR;
                } else {
                    range = "";
                }
                range = range + data.stream().map(SimpleDept::getName).collect(Collectors.joining(Const.SEPARATOR));
                crmCustomerSetting.setRange(range);
            } else {
                crmCustomerSetting.setDeptIds(new ArrayList<>());
            }
        });
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDealStatus(Integer dealStatus, List<Integer> ids) {
        BulkRequest bulkRequest = new BulkRequest();
        String index = CrmEnum.CUSTOMER.getIndex();
        for (Integer id : ids) {
            if (!UserUtil.getUserId().equals(UserUtil.getSuperUser()) && !UserUtil.getUser().getRoles().contains(UserUtil.getSuperRole())
                    && getBaseMapper().queryIsRoUser(id, UserUtil.getUserId()) > 0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            CrmCustomer byId = getById(id);
            if (byId != null) {
                byId.setDealStatus(dealStatus);
                byId.setDealTime(new Date());
                updateById(byId);
                UpdateRequest updateRequest = new UpdateRequest(index, "_doc", id.toString());
                Map<String, Object> map = new HashMap<>();
                map.put("dealTime", DateUtil.formatDateTime(new Date()));
                map.put("dealStatus", dealStatus);
                updateRequest.doc(map);
                bulkRequest.add(updateRequest);
            }
        }
        try {
            elasticsearchRestTemplate.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            elasticsearchRestTemplate.refresh(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BasePage<CrmContacts> queryContacts(CrmContactsPageBO pageEntity) {
        BasePage<CrmContacts> contactsBasePage = pageEntity.parse();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CONTACTS, 1,CrmAuthEnum.READ);
        return getBaseMapper().queryContacts(contactsBasePage, pageEntity.getCustomerId(), pageEntity.getSearch(), conditions);
    }

    @Autowired
    private ICrmBusinessTypeService businessTypeService;

    @Override
    public BasePage<Map<String, Object>> queryBusiness(CrmContactsPageBO pageEntity) {
        BasePage<Map<String, Object>> basePage = pageEntity.parse();
        String condition = AuthUtil.getCrmAuthSql(CrmEnum.BUSINESS, "a", 1,CrmAuthEnum.READ);
        BasePage<Map<String, Object>> page = getBaseMapper().queryBusiness(basePage, pageEntity.getCustomerId(), pageEntity.getSearch(), condition);
        for (Map<String, Object> map : page.getList()) {
            Integer isEnd = TypeUtils.castToInt(map.get("isEnd"));
            CrmListBusinessStatusVO crmListBusinessStatusVO = businessTypeService.queryListBusinessStatus((Integer) map.get("typeId"), (Integer) map.get("statusId"), isEnd);
            map.put("businessStatusCount", crmListBusinessStatusVO);
            if(Objects.equals(1,isEnd)) {
                map.put("statusName","赢单");
            } else if(Objects.equals(2,isEnd)) {
                map.put("statusName","输单");
            } else if(Objects.equals(3,isEnd)) {
                map.put("statusName","无效");
            }
        }
        return page;
    }

    @Override
    public BasePage<Map<String, Object>> queryContract(CrmContactsPageBO pageEntity) {
        BasePage<Map<String, Object>> basePage = pageEntity.parse();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CONTRACT, "a", 1,CrmAuthEnum.READ);
        BasePage<Map<String, Object>> page = getBaseMapper().queryContract(basePage, pageEntity.getCustomerId(), pageEntity.getSearch(), pageEntity.getCheckStatus(), conditions);
        for (Map<String, Object> map : page.getList()) {
            Double contractMoney = map.get("money") != null ? Double.parseDouble(map.get("money").toString()) : 0D;
            BigDecimal receivedProgress = new BigDecimal(100);
            if (!(contractMoney.intValue() == 0)) {
                receivedProgress = ((map.get("receivedMoney") != null ? (BigDecimal) map.get("receivedMoney") : new BigDecimal(0)).divide(new BigDecimal(contractMoney), 4, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
            }
            map.put("receivedProgress", receivedProgress);
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lock(Integer status, List<String> ids) {
        if (status != 1 && status != 2) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }

        for (String id : ids) {
            if (!UserUtil.isAdmin() && getBaseMapper().queryIsRoUser(Integer.parseInt(id), UserUtil.getUserId()) > 0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
        }
        if (status == 2) {
            QueryWrapper<CrmCustomer> wrapper = new QueryWrapper<>();
            wrapper.select("count(owner_user_id) as num", "owner_user_id");
            wrapper.isNotNull("owner_user_id").eq("status", 1);
            wrapper.in("customer_id", ids).groupBy("owner_user_id");
            List<Map<String, Object>> maps = listMaps(wrapper);
            for (Map<String, Object> map : maps) {
                boolean b = crmCustomerSettingService.queryCustomerSettingNum(2, (Long) map.get("ownerUserId"), TypeUtils.castToInt(map.get("num")));
                if (!b) {
                    throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_LOCK_MAX_ERROR);
                }
            }
        }
        actionRecordUtil.addIsLockRecord(ids, CrmEnum.CUSTOMER, status);
        lambdaUpdate().set(CrmCustomer::getStatus, status).in(CrmCustomer::getCustomerId, ids).update();
        updateField("status", status, ids.stream().map(Integer::valueOf).collect(Collectors.toList()));
    }

    @Override
    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<CrmCustomer> list = lambdaQuery().select(CrmCustomer::getCustomerId,CrmCustomer::getCustomerName).in(CrmCustomer::getCustomerId, ids).list();
        return list.stream().map(crmCustomer -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmCustomer.getCustomerId());
            simpleCrmEntity.setName(crmCustomer.getCustomerName());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SimpleCrmEntity> queryByNameCustomerInfo(String name) {
        List<CrmCustomer> list = lambdaQuery().select(CrmCustomer::getCustomerId,CrmCustomer::getCustomerName).like(CrmCustomer::getCustomerName, name).list();
        return list.stream().map(crmCustomer -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmCustomer.getCustomerId());
            simpleCrmEntity.setName(crmCustomer.getCustomerName());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SimpleCrmEntity> queryNameCustomerInfo(String name) {
        List<CrmCustomer> list = query().select("customer_id", "customer_name").eq("customer_name", name).list();
        return list.stream().map(crmCustomer -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmCustomer.getCustomerId());
            simpleCrmEntity.setName(crmCustomer.getCustomerName());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
    }

    /**
     * 跟进客户名称查询客户
     *
     * @param name name
     * @return data
     */
    @Override
    public SimpleCrmEntity queryFirstCustomerByName(String name) {
        CrmCustomer crmCustomer = query().select("customer_id", "customer_name").eq("customer_name", name).eq("status", 1).one();
        if (crmCustomer != null) {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmCustomer.getCustomerId());
            simpleCrmEntity.setName(crmCustomer.getCustomerName());
            return simpleCrmEntity;
        } else {
            return null;
        }
    }

    /**
     * 查询文件数量
     *
     * @param customerId id
     * @return data
     */
    @Override
    public CrmInfoNumVO num(Integer customerId) {
        CrmCustomer customer = getById(customerId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        List<CrmField> crmFields = crmFieldService.queryFileField();
        List<String> batchIdList = new ArrayList<>();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmCustomerData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmCustomerData::getValue);
            wrapper.eq(CrmCustomerData::getBatchId, customer.getBatchId());
            wrapper.in(CrmCustomerData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            batchIdList.addAll(crmCustomerDataService.listObjs(wrapper, Object::toString));
        }
        batchIdList.add(customer.getBatchId());
        batchIdList.addAll(crmActivityService.queryFileBatchId(customer.getCustomerId(), getLabel().getType()));
        String businessCon = AuthUtil.getCrmAuthSql(CrmEnum.BUSINESS, 1,CrmAuthEnum.READ);
        String contractCon = AuthUtil.getCrmAuthSql(CrmEnum.CONTRACT, 1,CrmAuthEnum.READ);
        String receivablesCon = AuthUtil.getCrmAuthSql(CrmEnum.RECEIVABLES, 1,CrmAuthEnum.READ);
        String contactsCon = AuthUtil.getCrmAuthSql(CrmEnum.CONTACTS, 1,CrmAuthEnum.READ);
        String returnVisitCon = AuthUtil.getCrmAuthSql(CrmEnum.RETURN_VISIT, 1,CrmAuthEnum.READ);
        String invoiceCon = AuthUtil.getCrmAuthSql(CrmEnum.INVOICE, 1,CrmAuthEnum.READ);
        Map<String, Object> map = new HashMap<>();
        map.put("businessCon", businessCon);
        map.put("contractCon", contractCon);
        map.put("receivablesCon", receivablesCon);
        map.put("contactsCon", contactsCon);
        map.put("returnVisitCon", returnVisitCon);
        map.put("invoiceCon", invoiceCon);
        map.put("customerId", customerId);
        CrmInfoNumVO infoNumVO = getBaseMapper().queryNum(map);
        infoNumVO.setFileCount(fileService.queryNum(batchIdList).getData());
        infoNumVO.setMemberCount(ApplicationContextHolder.getBean(ICrmTeamMembersService.class).queryMemberCount(getLabel(),customer.getCustomerId(),customer.getOwnerUserId()));
        return infoNumVO;
    }

    /**
     * 查询文件列表
     *
     * @param customerId id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer customerId) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmCustomer crmCustomer = getById(customerId);
        boolean auth = AuthUtil.isRwAuth(customerId, CrmEnum.CUSTOMER,CrmAuthEnum.READ);

        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmCustomer.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            if (auth && !fileEntity.getCreateUserId().equals(UserUtil.getUserId())) {
                fileEntity.setReadOnly(1);
            } else {
                fileEntity.setReadOnly(0);
            }
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmCustomerData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmCustomerData::getValue);
            wrapper.eq(CrmCustomerData::getBatchId, crmCustomer.getBatchId());
            wrapper.in(CrmCustomerData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(crmCustomerDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("客户详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        List<String> stringList = crmActivityService.queryFileBatchId(crmCustomer.getCustomerId(), getLabel().getType());
        if (stringList.size() > 0) {
            List<FileEntity> data = fileService.queryFileList(stringList).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("跟进记录");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        return fileEntityList;
    }


    /**
     * 获取客户名称
     *
     * @param customerId id
     * @return data
     */
    @Override
    public String getCustomerName(Integer customerId) {
        if (customerId == null) {
            return "";
        }
        return lambdaQuery().select(CrmCustomer::getCustomerName).eq(CrmCustomer::getCustomerId, customerId)
                .oneOpt().map(CrmCustomer::getCustomerName).orElse("");
    }

    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"customerName", "telephone", "mobile"};
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    @Override
    public CrmEnum getLabel() {
        return CrmEnum.CUSTOMER;
    }

    /**
     * 查询所有字段
     *
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        List<CrmModelFiledVO> filedList = crmFieldService.queryField(getLabel().getType());
        filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("lastContent", FieldEnum.TEXTAREA, 1));
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("dealTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("receiveTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("poolTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("status", FieldEnum.TEXT, 1));
        filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
        filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
        filedList.add(new CrmModelFiledVO("teamMemberIds", FieldEnum.USER, 0));
        return filedList;
    }

    @Override
    public boolean isMaxOwner(Long ownerUserId, List<Integer> ids) {
        Integer number = getBaseMapper().ownerNum(ids, ownerUserId);
        return crmCustomerSettingService.queryCustomerSettingNum(1, ownerUserId, number);
    }


    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer customerId = updateInformationBO.getId();
        if (!UserUtil.isAdmin() && getBaseMapper().queryIsRoUser(customerId, UserUtil.getUserId()) > 0) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        updateInformationBO.getList().forEach(record -> {
            CrmCustomer oldCustomer = getById(customerId);
            uniqueFieldIsAbnormal(record.getString("name"),record.getInteger("fieldId"),record.getString("value"),batchId);
            Map<String, Object> oldCustomerMap = BeanUtil.beanToMap(oldCustomer);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmCustomerMap = new HashMap<>(oldCustomerMap);
                crmCustomerMap.put(record.getString("fieldName"), record.get("value"));
                CrmCustomer crmCustomer = BeanUtil.mapToBean(crmCustomerMap, CrmCustomer.class, true);
                actionRecordUtil.updateRecord(oldCustomerMap, crmCustomerMap, CrmEnum.CUSTOMER, crmCustomer.getCustomerName(), crmCustomer.getCustomerId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("customer_id", updateInformationBO.getId()).update();
                if ("customerName".equals(record.getString("fieldName"))) {
                    ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(), "customer", crmCustomer.getCustomerId().toString(), crmCustomer.getCustomerName());
                }
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                CrmCustomerData customerData = crmCustomerDataService.lambdaQuery().select(CrmCustomerData::getValue,CrmCustomerData::getId).eq(CrmCustomerData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmCustomerData::getBatchId, batchId).last("limit 1").one();
                String value = customerData != null ? customerData.getValue() : null;
                actionRecordUtil.publicContentRecord(CrmEnum.CUSTOMER, BehaviorEnum.UPDATE, customerId, oldCustomer.getCustomerName(), record,value);
                String newValue = fieldService.convertObjectValueToString(record.getInteger("type"),record.get("value"),record.getString("value"));
                CrmCustomerData crmCustomerData = new CrmCustomerData();
                crmCustomerData.setId(customerData != null ? customerData.getId() : null);
                crmCustomerData.setFieldId(record.getInteger("fieldId"));
                crmCustomerData.setName(record.getString("fieldName"));
                crmCustomerData.setValue(newValue);
                crmCustomerData.setCreateTime(new Date());
                crmCustomerData.setBatchId(batchId);
                crmCustomerDataService.saveOrUpdate(crmCustomerData);

            }
            updateField(record, customerId);
        });
        this.lambdaUpdate().set(CrmCustomer::getUpdateTime,new Date()).eq(CrmCustomer::getCustomerId,customerId).update();
    }

    @Override
    public List<CrmDataCheckVO> dataCheck(CrmDataCheckBO dataCheckBO) {
        List<CrmDataCheckVO> list = getBaseMapper().dataCheck(dataCheckBO);
        for (CrmDataCheckVO crmDataCheckVO : list) {
            if (StrUtil.isNotEmpty(crmDataCheckVO.getPoolIds())){
                List<String> poolIds = StrUtil.splitTrim(crmDataCheckVO.getPoolIds(), Const.SEPARATOR);
                List<Integer> poolIdList = poolIds.stream().map(Integer::valueOf).collect(Collectors.toList());
                crmDataCheckVO.setPoolAuthList(crmCustomerPoolService.getOnePoolAuthByPoolIds(poolIdList));
            }
            if (crmDataCheckVO.getOwnerUserId() != null) {
                crmDataCheckVO.setOwnerUserName(UserCacheUtil.getUserName(crmDataCheckVO.getOwnerUserId()));
            }
            if (crmDataCheckVO.getContactsId() != null) {
                crmDataCheckVO.setContactsName(Optional.ofNullable(crmContactsService.getById(crmDataCheckVO.getContactsId())).map(CrmContacts::getName).orElse(null));
            }
        }
        return list;
    }

    @Override
    public BasePage<JSONObject> queryReceivablesPlan(CrmRelationPageBO crmRelationPageBO) {
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CONTRACT, "c", 1,CrmAuthEnum.READ);
        return getBaseMapper().queryReceivablesPlan(crmRelationPageBO.parse(), crmRelationPageBO.getCustomerId(), conditions);
    }

    @Override
    public BasePage<JSONObject> queryReceivables(CrmRelationPageBO crmRelationPageBO) {
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.RECEIVABLES, "a", 1,CrmAuthEnum.READ);
        BasePage<JSONObject> jsonObjects = getBaseMapper().queryReceivables(crmRelationPageBO.parse(), crmRelationPageBO.getCustomerId(), conditions);
        for (JSONObject jsonObject : jsonObjects.getList()) {
            String ownerUserName = UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId"));
            jsonObject.put("ownerUserName", ownerUserName);
        }
        return jsonObjects;
    }

    @Override
    public BasePage<JSONObject> queryReturnVisit(CrmRelationPageBO crmRelationPageBO) {
        List<CrmField> nameList = crmFieldService.lambdaQuery().select(CrmField::getFieldId, CrmField::getFieldName).eq(CrmField::getLabel, CrmEnum.RETURN_VISIT.getType())
                .eq(CrmField::getIsHidden, 0).ne(CrmField::getFieldType, 1).list();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.RETURN_VISIT, "a", 1,CrmAuthEnum.READ);
        BasePage<JSONObject> jsonObjects = getBaseMapper().queryReturnVisit(crmRelationPageBO.parse(), crmRelationPageBO.getCustomerId(), conditions, nameList);
        for (JSONObject jsonObject : jsonObjects.getList()) {
            String ownerUserName = UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId"));
            jsonObject.put("ownerUserName", ownerUserName);
        }
        return jsonObjects;
    }

    @Override
    public BasePage<JSONObject> queryInvoice(CrmRelationPageBO crmRelationPageBO) {
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.INVOICE, "a", 0,CrmAuthEnum.READ);
        BasePage<JSONObject> jsonObjects = getBaseMapper().queryInvoice(crmRelationPageBO.parse(), crmRelationPageBO.getCustomerId(), conditions);
        for (JSONObject jsonObject : jsonObjects.getList()) {
            String ownerUserName = UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId"));
            jsonObject.put("ownerUserName", ownerUserName);
        }
        return jsonObjects;
    }

    @Override
    public BasePage<JSONObject> queryInvoiceInfo(CrmRelationPageBO crmRelationPageBO) {
        BasePage<JSONObject> jsonObjects = getBaseMapper().queryInvoiceInfo(crmRelationPageBO.parse(), crmRelationPageBO.getCustomerId());
        for (JSONObject jsonObject : jsonObjects.getList()) {
            String ownerUserName = UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId"));
            jsonObject.put("ownerUserName", ownerUserName);
        }
        return jsonObjects;
    }

    @Override
    public BasePage<JSONObject> queryCallRecord(CrmRelationPageBO crmRelationPageBO) {
        BasePage<JSONObject> jsonObjects = getBaseMapper().queryCallRecord(crmRelationPageBO.parse(), crmRelationPageBO.getCustomerId());
        for (JSONObject jsonObject : jsonObjects.getList()) {
            String ownerUserName = UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId"));
            jsonObject.put("ownerUserName", ownerUserName);
        }
        return jsonObjects;
    }


    @Override
    public List<JSONObject> nearbyCustomer(String lng, String lat, Integer type, Integer radius, Long ownerUserId) {
        Integer menuId = adminService.queryMenuId("crm", "customer", "nearbyCustomer").getData();
        List<Long> authUserIdList = AuthUtil.getUserIdByAuth(menuId);
        List<Integer> poolIdList = new ArrayList<>();
        if (ObjectUtil.isEmpty(type) || type.equals(9)) {
            if (UserUtil.isAdmin()) {
                poolIdList = crmCustomerPoolService.lambdaQuery().select(CrmCustomerPool::getPoolId).list().stream().map(CrmCustomerPool::getPoolId).collect(Collectors.toList());
            } else {
                poolIdList = crmCustomerPoolService.queryPoolIdByUserId();
            }
        }
        List<JSONObject> jsonObjects = getBaseMapper().nearbyCustomer(lng, lat, type, radius, ownerUserId, authUserIdList, poolIdList);
        for (JSONObject jsonObject : jsonObjects) {
            String ownerUserName = UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId"));
            jsonObject.put("ownerUserName", ownerUserName);
        }
        return jsonObjects;
    }

    @Override
    public List<String> eventCustomer(CrmEventBO crmEventBO) {
        return getBaseMapper().eventCustomer(crmEventBO);
    }

    @Override
    public BasePage<Map<String, Object>> eventCustomerPageList(QueryEventCrmPageBO eventCrmPageBO) {
        Long userId = eventCrmPageBO.getUserId();
        Long time = eventCrmPageBO.getTime();
        if (userId == null) {
            userId = UserUtil.getUserId();
        }
        List<Integer> customerIds = getBaseMapper().eventCustomerList(userId, new Date(time));
        if (customerIds.size() == 0) {
            return new BasePage<>();
        }
        List<String> collect = customerIds.stream().map(Object::toString).collect(Collectors.toList());
        CrmSearchBO crmSearchBO = new CrmSearchBO();
        crmSearchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "text", CrmSearchBO.FieldSearchEnum.ID, collect)));
        crmSearchBO.setLabel(CrmEnum.CUSTOMER.getType());
        crmSearchBO.setPage(eventCrmPageBO.getPage());
        crmSearchBO.setLimit(eventCrmPageBO.getLimit());
        BasePage<Map<String, Object>> page = queryPageList(crmSearchBO);
        return page;
    }

    @Override
    public List<Integer> forgottenCustomer(Integer day, List<Long> userIds, String search) {
        return getBaseMapper().forgottenCustomer(day, userIds, search);
    }

    @Override
    public List<Integer> unContactCustomer(String search, List<Long> userIds) {
        return getBaseMapper().unContactCustomer(search, userIds);
    }
}
