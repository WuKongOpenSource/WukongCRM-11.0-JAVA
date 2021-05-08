package com.kakarote.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BiTimeUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.oa.common.OaCodeEnum;
import com.kakarote.oa.entity.BO.LogBO;
import com.kakarote.oa.entity.PO.*;
import com.kakarote.oa.mapper.OaLogMapper;
import com.kakarote.oa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作日志表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class OaLogServiceImpl extends BaseServiceImpl<OaLogMapper, OaLog> implements IOaLogService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private CrmService crmService;

    @Autowired
    private IOaLogRuleService logRuleService;

    @Autowired
    private IOaLogRelationService logRelationService;

    @Autowired
    private IOaLogRecordService logRecordService;

    @Autowired
    private IOaLogBulletinService logBulletinService;

    @Autowired
    private IOaLogUserFavourService oaLogUserFavourService;

    @Override
    public BasePage<JSONObject> queryList(LogBO bo) {
        JSONObject object = getLogListObject(bo);
        BasePage<JSONObject> basePage = getBaseMapper().queryList(bo.parse(), object);
        basePage.getList().forEach((record -> {
            queryLogDetail(record, UserUtil.getUserId());
        }));
        return basePage;
    }

    /**
     * 随机获取一条日志欢迎语
     *
     * @return data
     */
    @Override
    public String getLogWelcomeSpeech() {
        List<AdminConfig> speech = adminService.queryConfigByName("logWelcomeSpeech").getData();
        if (CollUtil.isEmpty(speech)) {
            return "";
        }
        return RandomUtil.randomEle(speech).getValue();
    }

    /**
     * 查询日志统计信息
     *
     * @return data
     */
    @Override
    public JSONObject queryLogBulletin() {
        Date beginTime = DateUtil.beginOfMonth(new Date());
        Date endTime = DateUtil.endOfMonth(new Date());
        Long userId = UserUtil.getUserId();
        return getBaseMapper().queryLogBulletin(userId, beginTime, endTime);
    }

    /**
     * 查询日志完成情况统计
     */
    @Override
    public JSONObject queryCompleteStats(Integer type) {
        OaLogRule rule = logRuleService.lambdaQuery().eq(OaLogRule::getType, type).last(" limit 1").one();
        if (rule == null || Objects.equals(0, rule.getStatus())) {
            return new JSONObject().fluentPut("status", 0);
        }
        JSONObject kv = getCycleTime(type);
        if (kv.isEmpty() || kv.getJSONArray("userIds").isEmpty()) {
            return new JSONObject().fluentPut("totalNum", 0).fluentPut("completeNum", 0);
        }
        Integer completeNum = getBaseMapper().queryCompleteStats(kv);
        return new JSONObject().fluentPut("totalNum", kv.get("totalNum")).fluentPut("completeNum", completeNum);
    }

    @Override
    public BasePage<JSONObject> queryCompleteOaLogList(LogBO bo) {
        Integer type = bo.getCompleteType();
        JSONObject kv = getCycleTime(type);
        if (ObjectUtil.isEmpty(kv) || kv.getJSONArray("userIds").isEmpty()) {
            return new BasePage<>();
        }
        String search = bo.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            kv.fluentPut("search", search);
        }
        JSONObject object = getLogListObject(bo);
        kv.fluentPut("isAdmin", object.get("isAdmin")).fluentPut("userIds1", object.get("userIds1")).fluentPut("userIds2", object.get("userIds2")).fluentPut("send_user_ids", object.get("send_user_ids"));
        BasePage<JSONObject> page = getBaseMapper().queryCompleteOaLogList(bo.parse(), kv);
        page.getList().forEach((log -> {
            queryLogDetail(log, UserUtil.getUserId());
        }));
        return page;
    }

    @Override
    public BasePage<SimpleUser> queryIncompleteOaLogList(LogBO bo) {
        Integer type = bo.getCompleteType();
        JSONObject kv = getCycleTime(type);
        if (ObjectUtil.isEmpty(kv) || kv.getJSONArray("userIds").isEmpty()) {
            return new BasePage<>();
        }
        String search = bo.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            kv.put("search", search);
        }
        List<OaLog> list = lambdaQuery().select(OaLog::getCreateUserId).eq(OaLog::getCategoryId, type).between(OaLog::getCreateTime, kv.getString("start"), kv.getString("end")).in(OaLog::getCreateUserId, kv.getJSONArray("userIds")).list();
        List<Long> ids = list.stream().map(OaLog::getCreateUserId).collect(Collectors.toList());
        if (ids.size() == 0) {
            ids.add(0L);
        }
        kv.put("notIds", ids);
        return getBaseMapper().queryIncompleteOaLogList(bo.parse(), kv);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndUpdate(JSONObject object) {
        UserInfo user = UserUtil.getUser();
        Integer type = object.getInteger("getBulletin");
        OaLog oaLog = object.toJavaObject(OaLog.class);
        OaLogRelation oaLogRelation = object.toJavaObject(OaLogRelation.class);
        oaLog.setCreateUserId(user.getUserId());
        oaLog.setReadUserIds(",,");
        oaLog.setSendUserIds(TagUtil.fromString(oaLog.getSendUserIds()));
        oaLog.setSendDeptIds(TagUtil.fromString(oaLog.getSendDeptIds()));
        if (oaLog.getLogId() != null) {
            OaLog oldLog = getById(oaLog.getLogId());
            if (oldLog == null || !Objects.equals(user.getUserId(), oldLog.getCreateUserId())) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            updateById(oaLog);

        } else {
            oaLog.setCreateTime(new Date());
            save(oaLog);
            if (Objects.equals(type, 1)) {
                saveLogRecord(oaLog.getLogId());
            }
        }
        AdminMessageBO adminMessageBO = new AdminMessageBO();
        adminMessageBO.setTitle(DateUtil.today());
        adminMessageBO.setUserId(UserUtil.getUserId());
        adminMessageBO.setTypeId(oaLog.getLogId());
        adminMessageBO.setMessageType(AdminMessageEnum.OA_LOG_SEND.getType());
        List<Long> ids = new ArrayList<>(StrUtil.splitTrim(oaLog.getSendUserIds(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList()));
        List<String> deptIds = StrUtil.splitTrim(oaLog.getSendDeptIds(), Const.SEPARATOR);
        if (deptIds.size() > 0) {
            List<Long> longList = adminService.queryUserByDeptIds(deptIds.stream().map(Integer::valueOf).collect(Collectors.toList())).getData();
            ids.addAll(longList);
        }
        if (ids.size() > 0) {
            adminMessageBO.setIds(ids);
            ApplicationContextHolder.getBean(AdminMessageService.class).sendMessage(adminMessageBO);
        }

        if (oaLogRelation != null) {
            logRelationService.removeByMap(new JSONObject().fluentPut("log_id", oaLog.getLogId()));
            oaLogRelation.setLogId(oaLog.getLogId());
            oaLogRelation.setBusinessIds(TagUtil.fromString(oaLogRelation.getBusinessIds()));
            oaLogRelation.setContactsIds(TagUtil.fromString(oaLogRelation.getContactsIds()));
            oaLogRelation.setContractIds(TagUtil.fromString(oaLogRelation.getContractIds()));
            oaLogRelation.setCustomerIds(TagUtil.fromString(oaLogRelation.getCustomerIds()));
            oaLogRelation.setCreateTime(DateUtil.date());
            logRelationService.save(oaLogRelation);
            crmService.addActivity(2, 8, oaLog.getLogId());
        }
    }

    @Override
    public BasePage<JSONObject> queryLogBulletinByType(LogBO bo) {
        UserInfo userInfo = UserUtil.getUser();
        String search = bo.getSearch();
        Integer crmType = bo.getCrmType();
        String camelField = bo.getSortField();
        String sortField = StrUtil.toUnderlineCase(camelField);
        if (!isValid(sortField)) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        Integer order = bo.getOrder();
        Integer today = bo.getToday();
        String orderType = "asc";
        if (order != null) {
            orderType = order == 1 ? "asc" : "desc";
        }
        Integer type = Integer.valueOf(bo.getType());
        List<Integer> typeIds = new ArrayList<>();
        if (Objects.equals(today, 1)) {
            List<OaLogBulletin> bulletinList = getBaseMapper().queryBulletinByLogInfo(Collections.singletonList(userInfo.getUserId()));
            bulletinList.forEach(oaLogBulletin -> {
                if (oaLogBulletin.getType().equals(type)) {
                    typeIds.add(oaLogBulletin.getTypeId());
                }
            });
        } else {
            List<OaLogBulletin> oaLogBulletins = logBulletinService.lambdaQuery().select(OaLogBulletin::getTypeId).eq(OaLogBulletin::getLogId, bo.getLogId()).eq(OaLogBulletin::getType, type).list();
            typeIds.addAll(oaLogBulletins.stream().map(OaLogBulletin::getTypeId).collect(Collectors.toList()));
        }
        if (typeIds.size() == 0) {
            return new BasePage<>();
        }
        JSONObject object = new JSONObject()
                .fluentPut("type", type)
                .fluentPut("typeIds", typeIds)
                .fluentPut("search", search)
                .fluentPut("sortField", sortField)
                .fluentPut("orderType", orderType)
                .fluentPut("crmType", crmType);
        BasePage<JSONObject> paginate = getBaseMapper().queryLogBulletinByType(bo.parse(), object);
        if (type == 5) {
            paginate.getList().forEach(this::buildActivityRelation);
        }
        return paginate;
    }

    @Override
    public List<JSONObject> queryLogRecordCount(Integer logId, Integer today) {
        List<Integer> typeIds = new ArrayList<>();
        if (Objects.equals(today, 1)) {
            List<OaLogBulletin> bulletinList = getBaseMapper().queryBulletinByLogInfo(Collections.singletonList(UserUtil.getUserId()));
            bulletinList.forEach(oaLogBulletin -> {
                if (oaLogBulletin.getType().equals(5)) {
                    typeIds.add(oaLogBulletin.getTypeId());
                }
            });
        } else {
            List<OaLogBulletin> oaLogBulletins = logBulletinService.lambdaQuery().select(OaLogBulletin::getTypeId).eq(OaLogBulletin::getLogId, logId).eq(OaLogBulletin::getType, 5).list();
            typeIds.addAll(oaLogBulletins.stream().map(OaLogBulletin::getTypeId).collect(Collectors.toList()));
        }
        if (typeIds.size() == 0) {
            typeIds.add(0);
        }
        List<JSONObject> recordList = getBaseMapper().queryLogRecordCount(typeIds);
        if (recordList.stream().noneMatch(record -> Objects.equals(1, record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", 1).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> Objects.equals(2, record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", 2).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> Objects.equals(3, record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", 3).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> Objects.equals(5, record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", 5).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> Objects.equals(6, record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", 6).fluentPut("count", 0));
        }
        return recordList;
    }

    private void buildActivityRelation(JSONObject record) {
        if (record.getInteger("status") == 1) {
            List<FileEntity> fileEntities = adminFileService.queryFileList(record.getString("batchId")).getData();
            Map<String, List<FileEntity>> collect = fileEntities.stream().collect(Collectors.groupingBy(FileEntity::getFileType));
            if (collect.isEmpty()) {
                record.put("img", new ArrayList<>());
                record.put("file", new ArrayList<>());
            } else {
                record.putAll(collect);
            }
        }
        if (record.getInteger("activityType") == 2) {
            String businessIds = record.getString("businessIds");
            List<SimpleCrmEntity> businessList = new ArrayList<>();
            if (businessIds != null) {
                List<String> businessIdsArr = StrUtil.splitTrim(businessIds, ",");
                businessList.addAll(crmService.queryBusinessInfo(businessIdsArr).getData());
            }
            String contactsIds = record.getString("contactsIds");
            List<SimpleCrmEntity> contactsList = new ArrayList<>();
            if (contactsIds != null) {
                List<String> contactsIdsArr = StrUtil.splitTrim(contactsIds, ",");
                contactsList.addAll(crmService.queryContactsInfo(contactsIdsArr).getData());
            }
            record.fluentPut("businessList", businessList).fluentPut("contactsList", contactsList);
        }
    }

    private boolean isValid(String param) {
        if (param == null) {
            return true;
        }
        String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)| (\\b(select|update|union|and|or|delete|insert|trancate|char|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
        Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        return !sqlPattern.matcher(param).find();
    }

    private JSONObject getLogListObject(LogBO bo) {
        JSONObject object = new JSONObject(BeanUtil.beanToMap(bo));
        UserInfo user = UserUtil.getUser();
        //getIntValue 为空会返回0
        Integer by = bo.getBy();
        if (by == null) {
            by = 0;
        }
        BiParams biParams = new BiParams();
        biParams.setType(bo.getType());
        BiTimeUtil.BiTimeEntity recordInfo = BiTimeUtil.analyzeTime(biParams);
        if ("recent30".equals(bo.getType())) {
            object.fluentPut("recentDays", "30");
        } else if ("recent60".equals(bo.getType())) {
            object.fluentPut("recentDays", "60");
        }
        object.put("by", by);
        String createUserIdOne = bo.getCreateUserId();
        String createUserIdTwo = bo.getCreateUserId();
        if (StrUtil.isEmpty(createUserIdOne) && CollUtil.isEmpty(bo.getDeptIds())) {
            object.put("isAdmin", UserUtil.isAdmin());
        }
        List<Long> longList = adminService.queryChildUserId(user.getUserId()).getData();
        longList.add(user.getUserId());
        if (!UserUtil.isAdmin()) {
            List<Long> userIdList = StrUtil.splitTrim(createUserIdOne, Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList());
            userIdList.retainAll(longList);
            createUserIdOne = StrUtil.join(Const.SEPARATOR, userIdList);
        }
        List<Long> createUserIdOneList = new ArrayList<>();
        if (StrUtil.isNotEmpty(createUserIdOne) && !",".equals(createUserIdOne)) {
            createUserIdOneList = Arrays.stream(createUserIdOne.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        List<Long> createUserIdTwoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(createUserIdTwo) && !",".equals(createUserIdTwo)) {
            createUserIdTwoList = Arrays.stream(createUserIdTwo.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        if(CollUtil.isNotEmpty(bo.getDeptIds())){
            List<Long> userIds = adminService.queryUserByDeptIds(bo.getDeptIds()).getData();
            if(!UserUtil.isAdmin()){
                userIds.retainAll(longList);
            }
            createUserIdOneList.addAll(userIds);
            createUserIdTwoList.addAll(userIds);
        }
        List<Long> userIds = new ArrayList<>();
        if (by.equals(1)) {
            userIds.add(user.getUserId());
        } else if (by.equals(2)) {
            object.put("send_user_ids", user.getUserId());
            if (createUserIdTwoList.size() > 0) {
                userIds.addAll(createUserIdTwoList);
            }
        } else if (by.equals(0)) {
            if (createUserIdTwoList.size() > 0) {
                object.put("userIds1", createUserIdOneList);
                object.put("userIds2", createUserIdTwoList);
            } else {
                userIds.addAll(longList);
                object.put("userIds1", userIds);
            }
            object.put("send_user_ids", user.getUserId());
        }
        if (!"recent30".equals(bo.getType()) && !"recent60".equals(bo.getType()) && bo.getType() != null) {
            object.putAll(BeanUtil.beanToMap(recordInfo));
        }
        object.put("userIds", userIds);
        if (bo.getType() == null) {
            //type== null 是自定义时间 需要把时间放到参数中
            BeanUtil.beanToMap(bo).forEach((key,value) -> {
                if(value != null){
                    object.put(key, value);
                }
            });
        }
        return object;
    }

    /**
     * 保存日志和销售简报关联信息
     *
     * @param logId 日志ID
     */
    private void saveLogRecord(Integer logId) {
        UserInfo userInfo = UserUtil.getUser();
        JSONObject record = getBaseMapper().queryBulletinByLog(Collections.singletonList(userInfo.getUserId()));
        OaLogRecord logRecord = BeanUtil.mapToBean(record, OaLogRecord.class, true);
        logRecord.setLogId(logId);
        logRecord.setCreateTime(new Date());
        logRecordService.save(logRecord);
        List<OaLogBulletin> bulletinList = getBaseMapper().queryBulletinByLogInfo(Collections.singletonList(userInfo.getUserId()));
        Date date = new Date();
        bulletinList.forEach(bulletin -> {
            bulletin.setLogId(logId);
            bulletin.setCreateTime(date);
        });
        logBulletinService.saveBatch(bulletinList);
    }

    /**
     * 根据id删除日志
     *
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Integer logId) {
        OaLog oaLog = getById(logId);
        if (oaLog != null) {
            logRelationService.removeByMap(new JSONObject().fluentPut("log_id", logId));
            adminFileService.delete(oaLog.getBatchId());
            removeById(logId);
            //commentService.deleteComment(2, logId);
        }
    }

    private JSONObject getCycleTime(Integer type) {
        OaLogRule rule = logRuleService.lambdaQuery().eq(OaLogRule::getType, type).last(" limit 1").one();
        List<Long> userIdList = new ArrayList<>();
        //为空代表范围是全部员工
        if (StrUtil.isEmpty(rule.getMemberUserId())) {
            userIdList.addAll(adminService.queryUserList(2).getData());
        } else {
            userIdList.addAll(Arrays.stream(rule.getMemberUserId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
        if(!UserUtil.isAdmin()) {
            List<Long> data = adminService.queryChildUserId(UserUtil.getUserId()).getData();
            userIdList.retainAll(data);
        }
        Integer totalNum = userIdList.size();
        String start = "";
        String end = "";
        if (type.equals(1)) {
            Integer dayOfWeek = DateUtil.dayOfWeek(new Date());
            //dayOfWeek 是 1 周日 2-7 周一到周六 数据库是 1-6 周一到周六 7 周日 所以要转换下
            dayOfWeek = dayOfWeek.equals(0) ? 7 : dayOfWeek - 1;
            String[] effectiveDayArr = rule.getEffectiveDay().split(",");
            List<Integer> dayList = Arrays.stream(effectiveDayArr).map(Integer::parseInt).collect(Collectors.toList());
            if (!dayList.contains(dayOfWeek)) {
                return new JSONObject();
            }
            String nowDay = DateUtil.format(new Date(), "yyyy-MM-dd");
            start = nowDay + " " + rule.getStartTime();
            end = nowDay + " " + rule.getEndTime();
        } else if (type.equals(2)) {
            Date beginDay = DateUtil.beginOfWeek(new Date());
            DateTime startDate = DateUtil.beginOfDay(DateUtil.offsetDay(beginDay, rule.getStartDay() - 1));
            start = DateUtil.format(startDate, "yyyy-MM-dd") + " 00:00";
            DateTime endDate = DateUtil.endOfDay(DateUtil.offsetDay(beginDay, rule.getEndDay() - 1));
            end = DateUtil.format(endDate, "yyyy-MM-dd") + " 12:00";
        } else if (type.equals(3)) {
            Date beginDay = DateUtil.beginOfMonth(new Date());
            DateTime startDate = DateUtil.beginOfDay(DateUtil.offsetDay(beginDay, rule.getStartDay() - 1));
            start = DateUtil.format(startDate, "yyyy-MM-dd") + " 00:00";
            Integer day = DateUtil.dayOfMonth(DateUtil.endOfMonth(new Date()));
            DateTime endDate = DateUtil.endOfDay(DateUtil.offsetDay(beginDay, day > rule.getEndDay() ? rule.getEndDay() - 1 : day));
            end = DateUtil.format(endDate, "yyyy-MM-dd") + " 12:00";
        }
        return new JSONObject().fluentPut("start", start).fluentPut("end", end).fluentPut("userIds", userIdList).fluentPut("totalNum", totalNum).fluentPut("type", type);
    }

    private void queryLogDetail(JSONObject object, Long userId) {
        List<FileEntity> fileEntities = new ArrayList<>();
        if (StrUtil.isNotEmpty(object.getString("batchId"))) {
            fileEntities.addAll(adminFileService.queryFileList(object.getString("batchId")).getData());
        }
        Map<String, List<FileEntity>> collect = fileEntities.stream().collect(Collectors.groupingBy(FileEntity::getFileType));
        if (collect.isEmpty()) {
            object.put("img", new ArrayList<>());
            object.put("file", new ArrayList<>());
        } else {
            object.putAll(collect);
            if(collect.get("img") == null){
                object.put("img", new ArrayList<>());
            }
            if(collect.get("file") == null){
                object.put("file", new ArrayList<>());
            }
        }

        Integer logId = object.getInteger("logId");
        object.put("favourUser",oaLogUserFavourService.queryFavourLogUserList(logId));
        object.put("isFavour",oaLogUserFavourService.queryUserWhetherFavourLog(logId));

        List<String> sendUserIds = StrUtil.splitTrim(object.getString("sendUserIds"), Const.SEPARATOR);
        if (sendUserIds.size() > 0) {
            object.put("sendUserList", adminService.queryUserByIds(sendUserIds.stream().map(Long::valueOf).collect(Collectors.toList())).getData());
        } else {
            object.put("sendUserList", new ArrayList<>());
        }
        List<String> customerIds = StrUtil.splitTrim(object.getString("customerIds"), Const.SEPARATOR);
        if (customerIds.size() > 0) {
            object.put("customerList", crmService.queryCustomerInfo(customerIds).getData());
        } else {
            object.put("customerList", new ArrayList<>());
        }
        List<String> businessIds = StrUtil.splitTrim(object.getString("businessIds"), Const.SEPARATOR);
        if (businessIds.size() > 0) {
            object.put("businessList", crmService.queryBusinessInfo(businessIds).getData());
        } else {
            object.put("businessList", new ArrayList<>());
        }
        List<String> contactsIds = StrUtil.splitTrim(object.getString("contactsIds"), Const.SEPARATOR);
        if (contactsIds.size() > 0) {
            object.put("contactsList", crmService.queryContactsInfo(contactsIds).getData());
        } else {
            object.put("contactsList", new ArrayList<>());
        }
        List<String> contractIds = StrUtil.splitTrim(object.getString("contractIds"), Const.SEPARATOR);
        if (contractIds.size() > 0) {
            object.put("contractList", crmService.queryContractInfo(contractIds).getData());
        } else {
            object.put("contractList", new ArrayList<>());
        }
        object.put("createUser", adminService.queryUserById(object.getLong("createUserId")).getData());
        int isEdit = userId.equals(object.getLong("createUserId")) ? 1 : 0;
        int isDel = 0;
        if ((System.currentTimeMillis() - 1000 * 3600 * 72) > object.getDate("createTime").getTime()) {
            if (UserUtil.isAdmin()) {
                isDel = 1;
            }
        } else {
            if (isEdit == 1) {
                isDel = 1;
            }
        }
        object.put("permission", new JSONObject().fluentPut("is_update", isEdit).fluentPut("is_delete", isDel));
        if (!Objects.equals(0, object.getInteger("getBulletin"))) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customerCount", object.get("customerCount"));
            jsonObject.put("businessCount", object.get("businessCount"));
            jsonObject.put("contractCount", object.get("contractCount"));
            jsonObject.put("receivablesMoney", object.get("receivablesMoney"));
            jsonObject.put("recordCount", object.get("recordCount"));
            object.put("bulletin", jsonObject);
        }
    }

    @Override
    public JSONObject queryById(Integer logId) {
        OaLog byId = getById(logId);
        if (byId == null) {
            throw new CrmException(OaCodeEnum.LOG_ALREADY_DELETE);
        }
        JSONObject jsonObject = getBaseMapper().queryById(logId);
        queryLogDetail(jsonObject, UserUtil.getUserId());
        return jsonObject;
    }

    @Override
    public List<Map<String, Object>> export(LogBO logBO) {
        JSONObject object = getLogListObject(logBO);
        List<JSONObject> recordList = getBaseMapper().queryExportList(object);
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach((record -> {
            //拼接接收者内容
            String sendName = "";
            if (StrUtil.isNotEmpty(record.getString("sendUserName"))) {
                sendName = sendName + record.getString("sendUserName") + ",";
            }
            if (StrUtil.isNotEmpty(record.getString("sendDeptName"))) {
                sendName = sendName + record.getString("sendDeptName") + ",";
            }
            if (StrUtil.isNotEmpty(sendName)) {
                sendName = sendName.substring(0, sendName.length() - 1);
            }
            //拼接关联业务内容
            String relateCrmWork = "";
            if (StrUtil.isNotEmpty(record.getString("customerIds"))) {
                relateCrmWork = relateCrmWork + "客户 【" + record.getString("customerName") + "】\n";
            }
            if (StrUtil.isNotEmpty(record.getString("contactsIds"))) {
                relateCrmWork = relateCrmWork + "联系人 【" + record.getString("contactsName") + "】\n";
            }
            if (StrUtil.isNotEmpty(record.getString("businessIds"))) {
                relateCrmWork = relateCrmWork + "商机 【" + record.getString("businessName") + "】\n";
            }
            if (StrUtil.isNotEmpty(record.getString("contractIds"))) {
                relateCrmWork = relateCrmWork + "合同 【" + record.getString("contractName") + "】\n";
            }
            //拼接评论回复内容
            List<JSONObject> commentList = getBaseMapper().queryCommentList(record.getInteger("logId"));
            StringBuilder stringBuilder = new StringBuilder();
            commentList.forEach(comment -> {
                stringBuilder.append(comment.getString("createUserName")).append("：");
                if (!comment.getInteger("mainId").equals(0)) {
                    stringBuilder.append("@").append(comment.getString("replyName")).append(" ");
                }
                stringBuilder.append(comment.getString("content")).append("\n");
            });
            list.add(record.fluentPut("sendName", sendName).fluentPut("relateCrmWork", relateCrmWork.trim()).fluentPut("comment", stringBuilder.toString().trim()));
        }));
        return list;
    }
}
