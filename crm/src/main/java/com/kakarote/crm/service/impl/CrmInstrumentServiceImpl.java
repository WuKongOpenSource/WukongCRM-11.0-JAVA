package com.kakarote.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.DataTypeEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.BiAuthority;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.BiTimeUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.constant.MonthEnum;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmSearchParamsBO;
import com.kakarote.crm.entity.PO.CrmActivity;
import com.kakarote.crm.mapper.CrmActivityMapper;
import com.kakarote.crm.mapper.CrmInstrumentMapper;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 仪表盘 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-04
 */
@Service
public class CrmInstrumentServiceImpl implements CrmInstrumentService {

    @Autowired
    private CrmInstrumentMapper crmInstrumentMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmBusinessService crmBusinessService;

    @Autowired
    private CrmActivityMapper crmActivityMapper;

    @Override
    public JSONObject queryBulletin(BiParams biParams) {
        BiTimeUtil.BiTimeEntity biTimeEntity = BiTimeUtil.analyzeTime(biParams);
        BiAuthority biAuthority = handleDataType(biParams);
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        Map<String, Object> record = new HashMap<>();
        record.put("businessUserIds", AuthUtil.filterUserIdListByDataType(map.get("business"), biAuthority.getUserIds()));
        record.put("contactsUserIds", AuthUtil.filterUserIdListByDataType(map.get("contacts"), biAuthority.getUserIds()));
        record.put("customerUserIds", AuthUtil.filterUserIdListByDataType(map.get("customer"), biAuthority.getUserIds()));
        record.put("receivablesUserIds", AuthUtil.filterUserIdListByDataType(map.get("receivables"), biAuthority.getUserIds()));
        record.put("contractUserIds", AuthUtil.filterUserIdListByDataType(map.get("contract"), biAuthority.getUserIds()));
        record.put("leadsUserIds", AuthUtil.filterUserIdListByDataType(map.get("leads"), biAuthority.getUserIds()));
        record.put("recordUserIds", AuthUtil.filterUserIdListByDataType(map.get("record"), biAuthority.getUserIds()));
        Map<String, Object> info = crmInstrumentMapper.queryBulletin(biTimeEntity, record);
        boolean isCustom = !StrUtil.isAllEmpty(biParams.getStartTime(), biParams.getEndTime());
        Map<String, Object> prevRecord = new HashMap<>();
        BigDecimal bigDecimal = new BigDecimal("0");
        if (!isCustom) {
            BiTimeUtil.BiTimeEntity timeEntity = BiTimeUtil.prevAnalyzeType(biParams);
            prevRecord = crmInstrumentMapper.queryBulletin(timeEntity, record);
            for (String columnName : info.keySet()) {
                BigDecimal decimal = new BigDecimal(prevRecord.get(columnName).toString());
                BigDecimal newdecimal = new BigDecimal(info.get(columnName).toString());
                if (decimal.equals(bigDecimal) || newdecimal.equals(bigDecimal)) {
                    prevRecord.put(columnName, bigDecimal);
                } else {
                    if (decimal.compareTo(BigDecimal.ZERO) == 0 || newdecimal.compareTo(BigDecimal.ZERO) == 0) {
                        prevRecord.put(columnName, 0);
                    } else {
                        prevRecord.put(columnName, newdecimal.subtract(decimal).divide(decimal, 4, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } else {
            for (String columnName : info.keySet()) {
                prevRecord.put(columnName, bigDecimal);
            }
        }
        return new JSONObject().fluentPut("data", info).fluentPut("prev", prevRecord);

    }

    /**
     * 销售简报的详情
     *
     * @param biParams 参数
     * @return data
     */
    @Override
    public BasePage<Map<String, Object>> queryBulletinInfo(BiParams biParams) {
        BiTimeUtil.BiTimeEntity biTimeEntity = BiTimeUtil.analyzeTime(biParams);
        BiAuthority biAuthority = handleDataType(biParams);
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        CrmEnum crmEnum = CrmEnum.parse(biParams.getLabel());
        List<Long> userIds = new ArrayList<>();
        switch (crmEnum) {
            case CUSTOMER:
                userIds = AuthUtil.filterUserIdListByDataType(map.get("customer"), biAuthority.getUserIds());
                break;
            case CONTACTS:
                userIds = AuthUtil.filterUserIdListByDataType(map.get("contacts"), biAuthority.getUserIds());
                break;
            case BUSINESS:
                userIds = AuthUtil.filterUserIdListByDataType(map.get("business"), biAuthority.getUserIds());
                break;
            case CONTRACT:
                userIds = AuthUtil.filterUserIdListByDataType(map.get("contract"), biAuthority.getUserIds());
                break;
            case RECEIVABLES:
                userIds = AuthUtil.filterUserIdListByDataType(map.get("receivables"), biAuthority.getUserIds());
                break;
            default:
                break;
        }
        if (userIds.isEmpty()) {
            return new BasePage<>();
        }
        CrmSearchBO searchBO = new CrmSearchBO();
        searchBO.setPage(biParams.getPage());
        searchBO.setLimit(biParams.getLimit());
        searchBO.setSearch(biParams.getSearch());
        searchBO.setLabel(biParams.getLabel());
        searchBO.setOrder(biParams.getOrder());
        searchBO.setSortField(biParams.getSortField());
        searchBO.getSearchList().add(new CrmSearchBO.Search("ownerUserId", "text", CrmSearchBO.FieldSearchEnum.IS, userIds.stream().map(Object::toString).collect(Collectors.toList())));
        if (biParams.getMoneyType() != null) {
            if (biParams.getMoneyType() == 1) {
                searchBO.getSearchList().add(new CrmSearchBO.Search("orderDate", "date", CrmSearchBO.FieldSearchEnum.DATE, Arrays.asList(DateUtil.formatDate(biTimeEntity.getBeginDate()), DateUtil.formatDate(DateUtil.offsetDay(biTimeEntity.getEndDate(),1)))));
            } else if (biParams.getMoneyType() == 2) {
                searchBO.getSearchList().add(new CrmSearchBO.Search("returnTime", "date", CrmSearchBO.FieldSearchEnum.DATE, Arrays.asList(DateUtil.formatDate(biTimeEntity.getBeginDate()), DateUtil.formatDate(DateUtil.offsetDay(biTimeEntity.getEndDate(),1)))));
            }
        } else {
            searchBO.getSearchList().add(new CrmSearchBO.Search("createTime", "date", CrmSearchBO.FieldSearchEnum.DATE_TIME, Arrays.asList(DateUtil.formatDateTime(biTimeEntity.getBeginDate()), DateUtil.formatDateTime(DateUtil.endOfDay(biTimeEntity.getEndDate())))));
        }
        if (biParams.getCheckStatus() != null && biParams.getCheckStatus() == 1) {
            searchBO.getSearchList().add(new CrmSearchBO.Search("checkStatus", "text", CrmSearchBO.FieldSearchEnum.IS, Collections.singletonList("1")));
        }
        switch (crmEnum) {
            case CUSTOMER:
                return ApplicationContextHolder.getBean(ICrmCustomerService.class).queryPageList(searchBO);
            case CONTACTS:
                return ApplicationContextHolder.getBean(ICrmContactsService.class).queryPageList(searchBO);
            case BUSINESS:
                return ApplicationContextHolder.getBean(ICrmBusinessService.class).queryPageList(searchBO);
            case CONTRACT:
                searchBO.getSearchList().add(new CrmSearchBO.Search("checkStatus", "text", CrmSearchBO.FieldSearchEnum.IS, Collections.singletonList("1")));
                return ApplicationContextHolder.getBean(ICrmContractService.class).queryPageList(searchBO);
            case RECEIVABLES:
                return ApplicationContextHolder.getBean(ICrmReceivablesService.class).queryPageList(searchBO);
            default:
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
    }

    @Override
    public JSONObject forgottenCustomerCount(BiParams biParams) {
        JSONObject object = new JSONObject();
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = AuthUtil.filterUserIdListByDataType(map.get("customer"), biAuthority.getUserIds());
        Integer sevenDays = crmInstrumentMapper.forgottenCustomerCount(7, userIds);
        Integer fifteenDays = crmInstrumentMapper.forgottenCustomerCount(15, userIds);
        Integer oneMonth = crmInstrumentMapper.forgottenCustomerCount(30, userIds);
        Integer threeMonth = crmInstrumentMapper.forgottenCustomerCount(90, userIds);
        Integer sixMonth = crmInstrumentMapper.forgottenCustomerCount(180, userIds);
        Integer unContactCustomerCount = crmInstrumentMapper.unContactCustomerCount(userIds);
        object.put("sevenDays", sevenDays);
        object.put("fifteenDays", fifteenDays);
        object.put("oneMonth", oneMonth);
        object.put("threeMonth", threeMonth);
        object.put("sixMonth", sixMonth);
        object.put("unContactCustomerCount", unContactCustomerCount);
        return object;
    }

    @Override
    public JSONObject sellFunnel(BiParams biParams) {
        Integer menuId = 103;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.size() == 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list", new ArrayList<>());
            jsonObject.put("sum_money", 0);
            jsonObject.put("sum_shu", 0);
            jsonObject.put("sum_ying", 0);
            return jsonObject;
        }
        Map<String, Object> map = record.toMap();
        map.put("typeId", biParams.getTypeId());
        List<JSONObject> list = crmInstrumentMapper.sellFunnel(map);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        map.put("isEnd", 1);
        List<JSONObject> otherList = crmInstrumentMapper.sellFunnel(map);
        BigDecimal sum_money = new BigDecimal("0");
        BigDecimal sum_shu = new BigDecimal("0");
        BigDecimal sum_ying = new BigDecimal("0");
        for (JSONObject object : otherList) {
            sum_money = sum_money.add(object.getBigDecimal("money"));
            sum_shu = sum_shu.add(object.getBigDecimal("lose"));
            sum_ying = sum_ying.add(object.getBigDecimal("win"));
        }
        ;
        jsonObject.put("sum_money", sum_money);
        jsonObject.put("sum_shu", sum_shu);
        jsonObject.put("sum_ying", sum_ying);
        return jsonObject;
    }


    @Override
    public BasePage<Map<String, Object>> sellFunnelBusinessList(CrmSearchParamsBO crmSearchParamsBO) {
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(crmSearchParamsBO);
        List<Long> userIds = record.getUserIds();
        if (CollUtil.isEmpty(userIds)){
            return new BasePage<>();
        }
        Map<String, Object> map = record.toMap();
        CrmSearchBO search = new CrmSearchBO();
        search.setPage(crmSearchParamsBO.getPage());
        search.setLimit(crmSearchParamsBO.getLimit());
        search.setLabel(CrmEnum.BUSINESS.getType());
        List<CrmSearchBO.Search> searchList = new ArrayList<>();
        if (StrUtil.isNotEmpty(crmSearchParamsBO.getSearch())){
            map.put("name", crmSearchParamsBO.getSearch());
        }
        CrmSearchBO.Search entity = crmSearchParamsBO.getEntity();
        map.put("typeId", entity.getValues().get(0));
        map.put("statusId", entity.getValues().get(1));
        List<String> businessIdList = crmInstrumentMapper.sellFunnelBusinessList(map);

        CrmSearchBO.Search entity2 = new CrmSearchBO.Search();
        entity2.setName("businessId");
        entity2.setFormType(FieldEnum.TEXT.getFormType());
        entity2.setSearchEnum(CrmSearchBO.FieldSearchEnum.IS);
        entity2.setValues(businessIdList);
        searchList.add(entity2);

        search.setSearchList(searchList);
        return crmBusinessService.queryPageList(search);
    }

    @Override
    public JSONObject salesTrend(BiParams biParams) {
        BiTimeUtil.BiTimeEntity record = null;
        if (StrUtil.isNotEmpty(biParams.getType())) {
            if ("year".equals(biParams.getType()) || "lastYear".equals(biParams.getType())) {
                biParams.setType(biParams.getType());
                record = BiTimeUtil.analyzeTime(biParams);
            } else {
                String endTime = DateUtil.format(new Date(), "yyyy-MM-dd");
                String startTime = DateUtil.format(DateUtil.offsetMonth(new Date(), -12), "yyyy-MM-dd");
                biParams.setStartTime(startTime);
                biParams.setEndTime(endTime);
            }
        }
        if (StrUtil.isNotEmpty(biParams.getStartTime()) && StrUtil.isNotEmpty(biParams.getEndTime())) {
            Integer startMonth = Integer.valueOf(DateUtil.format(DateUtil.parse(biParams.getStartTime()), "yyyyMM"));
            Integer endMonth = Integer.valueOf(DateUtil.format(DateUtil.parse(biParams.getEndTime()), "yyyyMM"));
            if (endMonth - startMonth < 100) {
                String endTime = DateUtil.format(new Date(), "yyyy-MM-dd");
                String startTime = DateUtil.format(DateUtil.offsetMonth(new Date(), -12), "yyyy-MM-dd");
                biParams.setStartTime(startTime);
                biParams.setEndTime(endTime);
            }
            record = BiTimeUtil.analyzeTime(biParams);
        }
        if (record == null) {
            return new JSONObject().fluentPut("list", new ArrayList<>());
        }
        Integer cycleNum = record.getCycleNum();
        Integer beginTime = record.getBeginTime();
        List<Map<String, Object>> recordList = new ArrayList<>();
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = new ArrayList<>();
        if (Objects.equals(biParams.getLabel(), 1)) {
            userIds.addAll(AuthUtil.filterUserIdListByDataType(map.get("contract"), biAuthority.getUserIds()));
        } else if (Objects.equals(biParams.getLabel(), 2)) {
            userIds.addAll(AuthUtil.filterUserIdListByDataType(map.get("receivables"), biAuthority.getUserIds()));
        }
        for (int i = 1; i <= cycleNum; i++) {
            JSONObject object = new JSONObject();
            object.put("month", MonthEnum.valueOf(DateUtil.parse(beginTime.toString(), record.getDateFormat()).monthStartFromOne()));
            object.put("year", DateUtil.year(DateUtil.parse(beginTime.toString(), record.getDateFormat())));
            object.put("label", biParams.getLabel());
            object.put("userIds", userIds);
            object.put("deptIds", AuthUtil.filterDeptId(biAuthority.getDeptIds()));
            recordList.add(crmInstrumentMapper.salesTrend(record, object));
            beginTime = BiTimeUtil.estimateTime(beginTime);
            record.setBeginTime(beginTime);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", recordList);
        return jsonObject;
    }

    @Override
    public JSONObject queryDataInfo(BiParams biParams) {
        BiAuthority biAuthority = handleDataType(biParams);
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        BiTimeUtil.BiTimeEntity timeEntity = BiTimeUtil.analyzeTime(biParams);
        Map<String, Object> costumer = crmInstrumentMapper.dataInfoCustomer(timeEntity, AuthUtil.filterUserIdListByDataType(map.get("customer"), biAuthority.getUserIds()));
        Map<String, Object> activity = crmInstrumentMapper.dataInfoActivity(timeEntity, AuthUtil.filterUserIdListByDataType(map.get("customer"), biAuthority.getUserIds()));
        Map<String, Object> business = crmInstrumentMapper.dataInfoBusiness(timeEntity, AuthUtil.filterUserIdListByDataType(map.get("business"), biAuthority.getUserIds()));
        Map<String, Object> contract = crmInstrumentMapper.dataInfoContract(timeEntity, AuthUtil.filterUserIdListByDataType(map.get("contract"), biAuthority.getUserIds()));
        Map<String, Object> receivables = crmInstrumentMapper.dataInfoReceivables(timeEntity, AuthUtil.filterUserIdListByDataType(map.get("receivables"), biAuthority.getUserIds()));
        JSONObject object = new JSONObject();
        object.putAll(costumer);
        object.putAll(activity);
        object.putAll(business);
        object.putAll(contract);
        object.putAll(receivables);
        return object;
    }

    private BiAuthority handleDataType(BiParams biParams) {
        //数据类型
        DataTypeEnum typeEnum = DataTypeEnum.parse(biParams.getDataType());
        List<Long> userIdList = new ArrayList<>();
        List<Integer> deptIdList = new ArrayList<>();
        if (typeEnum != null) {
            if (typeEnum == DataTypeEnum.SELF) {
                userIdList.add(UserUtil.getUserId());
            } else if (typeEnum == DataTypeEnum.SELF_AND_CHILD) {
                userIdList.addAll(adminService.queryChildUserId(UserUtil.getUserId()).getData());
                userIdList.add(UserUtil.getUserId());
            } else if (typeEnum == DataTypeEnum.DEPT) {
                deptIdList.add(UserUtil.getUser().getDeptId());
                userIdList.addAll(adminService.queryUserByDeptIds(deptIdList).getData());
            } else if (typeEnum == DataTypeEnum.DEPT_AND_CHILD) {
                deptIdList.addAll(adminService.queryChildDeptId(UserUtil.getUser().getDeptId()).getData());
                deptIdList.add(UserUtil.getUser().getDeptId());
                userIdList.addAll(adminService.queryUserByDeptIds(deptIdList).getData());
            } else {
                userIdList.addAll(adminService.queryUserList().getData());
                deptIdList.addAll(adminService.queryChildDeptId(0).getData());
            }
        } else {
            if (1 == biParams.getIsUser()) {
                if (biParams.getUserId() == null) {
                    if (UserUtil.isAdmin()) {
                        userIdList.addAll(adminService.queryUserList().getData());
                    } else {
                        userIdList.addAll(adminService.queryChildUserId(UserUtil.getUserId()).getData());
                        userIdList.add(UserUtil.getUserId());
                    }
                } else {
                    if (UserUtil.isAdmin()) {
                        userIdList.add(biParams.getUserId());
                    } else {
                        UserInfo userInfo = adminService.queryLoginUserInfo(biParams.getUserId()).getData();
                        boolean isAdmin = userInfo.getUserId().equals(UserUtil.getSuperUser()) || Optional.ofNullable(userInfo.getRoles()).orElse(new ArrayList<>()).contains(userInfo.getSuperRoleId());
                        if (!isAdmin) {
                            userIdList.add(biParams.getUserId());
                        }
                    }
                }
            } else if (0 == biParams.getIsUser() && biParams.getDeptId() != null) {
                List<Integer> data = adminService.queryChildDeptId(biParams.getDeptId()).getData();
                data.add(biParams.getDeptId());
                deptIdList.addAll(data);
                userIdList.addAll(adminService.queryUserByDeptIds(deptIdList).getData());
            }
        }
        BiAuthority authority = new BiAuthority();
        authority.setUserIds(userIdList);
        authority.setDeptIds(deptIdList);
        return authority;
    }

    /**
     * 业绩指标
     */
    @Override
    public JSONObject queryPerformance(BiParams biParams) {
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeTime(biParams);
        Integer cycleNum = record.getCycleNum();
        boolean b = true;
        if (StrUtil.isNotEmpty(biParams.getStartTime()) && StrUtil.isNotEmpty(biParams.getEndTime())) {
            Date start = DateUtil.parseDate(biParams.getStartTime());
            Date end = DateUtil.parseDate(biParams.getEndTime());
            Integer startMonth = Integer.valueOf(DateUtil.format(start, "yyyyMM"));
            Integer endMonth = Integer.valueOf(DateUtil.format(end, "yyyyMM"));
            b = !startMonth.equals(endMonth);
            if (start.getTime() == DateUtil.beginOfMonth(start).getTime() && end.getTime() == DateUtil.beginOfDay(DateUtil.endOfMonth(start)).getTime()) {
                String sqlDateFormat = "%Y%m";
                String dateFormat = "yyyyMM";
                Integer beginTime = Integer.valueOf(DateUtil.format(start, dateFormat));
                Integer finalTime = Integer.valueOf(DateUtil.format(end, dateFormat));
                record.setCycleNum(1).setBeginTime(beginTime).setFinalTime(finalTime).setSqlDateFormat(sqlDateFormat).setDateFormat(dateFormat);
                b = true;
            }
        }
        Integer beginTime = record.getBeginTime();
        Integer status = biParams.getLabel();
        String type = biParams.getType();
        if ("month".equals(type) || "lastMonth".equals(type)) {
            cycleNum = 1;
        }
        List<String> mouthList = new ArrayList<>();
        for (int i = 1; i <= cycleNum; i++) {
            String month = MonthEnum.valueOf(DateUtil.parse(beginTime.toString(), record.getDateFormat()).monthStartFromOne());
            mouthList.add(month);
            beginTime = BiTimeUtil.estimateTime(beginTime);
        }
        JSONObject kv = new JSONObject(record.toMap()).fluentPut("label", status);
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        BiAuthority biAuthority = handleDataType(biParams);
        if (ObjectUtil.equal(status, 1)) {
            kv.put("userIds", AuthUtil.filterUserIdListByDataType(map.get("contract"), biAuthority.getUserIds()));
        } else if (ObjectUtil.equal(status, 2)) {
            kv.put("userIds", AuthUtil.filterUserIdListByDataType(map.get("receivables"), biAuthority.getUserIds()));
        }
        if ("week".equals(type) || "lastWeek".equals(type) || "today".equals(type) || "yesterday".equals(type)) {
            b = false;
        }
        kv.put("deptIds", biAuthority.getDeptIds());
        kv.put("monthList", mouthList);
        kv.put("year", DateUtil.year(DateUtil.parse(record.getBeginTime().toString(), record.getDateFormat())));
        kv.put("queryItem", b);
        JSONObject dataRecord = crmInstrumentMapper.queryPerformance(kv);
        BigDecimal achievementMoneys = dataRecord.getBigDecimal("achievementMoneys");
        if (achievementMoneys != null) {
            if (achievementMoneys.compareTo(BigDecimal.ZERO) == 0) {
                dataRecord.put("proportion", new BigDecimal("100"));
            } else {
                BigDecimal bigDecimal = dataRecord.getBigDecimal("money").divide(achievementMoneys, 4, BigDecimal.ROUND_HALF_UP);
                bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
                dataRecord.put("proportion", bigDecimal);
            }
        }
        dataRecord.put(1 == status ? "contractMoneys" : "receivablesMoneys", dataRecord.get("money"));
        return dataRecord;
    }


    @Override
    public BasePage<CrmActivity> queryRecordList(BiParams biParams) {
        Long userId = biParams.getUserId();
        Integer deptId = biParams.getDeptId();
        String startTime = biParams.getStartTime();
        String endTime = biParams.getEndTime();
        Integer dataType = biParams.getDataType();
        Integer crmType = biParams.getLabel();
        Integer queryType = biParams.getQueryType();
        Integer subUser = biParams.getSubUser();
        String search = biParams.getSearch() == null ? null : biParams.getSearch().trim();
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = biAuthority.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return new BasePage<>();
        }
        if (subUser != null) {
            if (subUser == 1 && ObjectUtil.isAllEmpty(userId, deptId)) {
                userIds = adminService.queryChildUserId(UserUtil.getUserId()).getData();
            } else if (subUser == 0) {
                userIds = Collections.singletonList(UserUtil.getUserId());
            }
        }
        if (UserUtil.isAdmin() && ObjectUtil.isAllEmpty(userId, deptId) && subUser == null && dataType == null) {
            userIds = adminService.queryUserList().getData();
        }
        Integer type = BiTimeUtil.analyzeType(biParams.getType());
        if (!UserUtil.isAdmin()) {
            Integer followRecordReadMenuId = adminService.queryMenuId("crm", "followRecord", "read").getData();
            List<Long> authUserIdList = AuthUtil.getUserIdByAuth(followRecordReadMenuId);
            if (authUserIdList.size() == 0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
        }
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        if (crmEnum != null) {
            switch (crmEnum) {
                case CUSTOMER:
                    AuthUtil.filterUserIdListByDataType(map.get("customer"), userIds);
                    break;
                case CONTACTS:
                    AuthUtil.filterUserIdListByDataType(map.get("contacts"), userIds);
                    break;
                case BUSINESS:
                    AuthUtil.filterUserIdListByDataType(map.get("business"), userIds);
                    break;
                case CONTRACT:
                    AuthUtil.filterUserIdListByDataType(map.get("contract"), userIds);
                    break;
                case RECEIVABLES:
                    AuthUtil.filterUserIdListByDataType(map.get("receivables"), userIds);
                    break;
                default:
                    break;
            }
        }
        BasePage<CrmActivity> page = crmActivityMapper.queryRecordList(biParams.parse(), Dict.create().set("crmType", crmType).set("type", type).set("userIds", userIds).set("startTime", startTime)
                .set("endTime", endTime).set("queryType", queryType).set("search", search));
        for (CrmActivity crmActivity : page.getList()) {
            SimpleUser data = adminService.queryUserById(crmActivity.getCreateUserId()).getData();
            crmActivity.setUserImg(data.getImg());
            crmActivity.setRealname(data.getRealname());
            crmActivityService.buildActivityRelation(crmActivity);
        }
        return page;
    }

    @Override
    public List<JSONObject> queryRecordCount(BiParams biParams) {
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = biAuthority.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        //1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年
        Integer type = BiTimeUtil.analyzeType(biParams.getType());
        Dict kv = Dict.create().set("type", type).set("startTime", biParams.getStartTime()).set("endTime", biParams.getEndTime());
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        kv.set("businessUserIds", AuthUtil.filterUserIdListByDataType(map.get("business"), userIds));
        kv.set("contactsUserIds", AuthUtil.filterUserIdListByDataType(map.get("contacts"), userIds));
        kv.set("customerUserIds", AuthUtil.filterUserIdListByDataType(map.get("customer"), userIds));
        kv.set("receivablesUserIds", AuthUtil.filterUserIdListByDataType(map.get("receivables"), userIds));
        kv.set("contractUserIds", AuthUtil.filterUserIdListByDataType(map.get("contract"), userIds));
        kv.set("leadsUserIds", AuthUtil.filterUserIdListByDataType(map.get("leads"), userIds));
        kv.set("recordUserIds", AuthUtil.filterUserIdListByDataType(map.get("record"), userIds));
        List<JSONObject> recordList = crmInstrumentMapper.queryRecordCount(kv);
        if (recordList.stream().noneMatch(record -> CrmEnum.LEADS.getType().equals(record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", CrmEnum.LEADS.getType()).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> CrmEnum.CUSTOMER.getType().equals(record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", CrmEnum.CUSTOMER.getType()).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> CrmEnum.CONTACTS.getType().equals(record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", CrmEnum.CONTACTS.getType()).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> CrmEnum.BUSINESS.getType().equals(record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", CrmEnum.BUSINESS.getType()).fluentPut("count", 0));
        }
        if (recordList.stream().noneMatch(record -> CrmEnum.CONTRACT.getType().equals(record.getInteger("crmType")))) {
            recordList.add(new JSONObject().fluentPut("crmType", CrmEnum.CONTRACT.getType()).fluentPut("count", 0));
        }
        return recordList;
    }

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Override
    public BasePage<Map<String, Object>> forgottenCustomerPageList(BiParams biParams) {
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = biAuthority.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return new BasePage<>();
        }
        Integer day = biParams.getDay();
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        AuthUtil.filterUserIdListByDataType(map.get("customer"), userIds);
        List<Integer> customerIds = crmCustomerService.forgottenCustomer(day, userIds, biParams.getSearch());
        if (customerIds.size() == 0) {
            return new BasePage<>();
        }
        CrmSearchBO searchBO = new CrmSearchBO();
        searchBO.setPage(biParams.getPage());
        searchBO.setLimit(biParams.getLimit());
        searchBO.setLabel(CrmEnum.CUSTOMER.getType());
        List<String> collect = customerIds.stream().map(Object::toString).collect(Collectors.toList());
        searchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "id", CrmSearchBO.FieldSearchEnum.ID, collect)));
        return crmCustomerService.queryPageList(searchBO);
    }

    @Override
    public BasePage<Map<String, Object>> unContactCustomerPageList(BiParams biParams) {
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = biAuthority.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return new BasePage<>();
        }
        String search = biParams.getSearch();
        Map<String, Integer> map = AuthUtil.getDataTypeByUserId();
        AuthUtil.filterUserIdListByDataType(map.get("customer"), userIds);
        List<Integer> customerIds = crmCustomerService.unContactCustomer(search, userIds);
        if (customerIds.size() == 0) {
            return new BasePage<>();
        }
        CrmSearchBO searchBO = new CrmSearchBO();
        searchBO.setPage(biParams.getPage());
        searchBO.setLimit(biParams.getLimit());
        searchBO.setLabel(CrmEnum.CUSTOMER.getType());
        List<String> collect = customerIds.stream().map(Object::toString).collect(Collectors.toList());
        searchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "id", CrmSearchBO.FieldSearchEnum.ID, collect)));
        return crmCustomerService.queryPageList(searchBO);
    }
}
