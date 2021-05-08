package com.kakarote.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.entity.DataTypeEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.BiAuthority;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.BiTimeUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmBackLogEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.MonthEnum;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmSearchParamsBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.mapper.CrmActivityMapper;
import com.kakarote.crm.mapper.CrmInstrumentMapper;
import com.kakarote.crm.service.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ICrmBusinessService crmBusinessService;

    @Autowired
    private ICrmLeadsService crmLeadsService;

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmContactsService crmContactsService;

    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private CrmActivityMapper crmActivityMapper;

    @Override
    public JSONObject queryBulletin(BiParams biParams) {
        BiTimeUtil.BiTimeEntity biTimeEntity = BiTimeUtil.analyzeTime(biParams);
        BiAuthority biAuthority = handleDataType(biParams);
        Map<String, Object> record = new HashMap<>();
        record.put("businessUserIds", AuthUtil.filterUserIdList(CrmEnum.BUSINESS, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        record.put("contactsUserIds", AuthUtil.filterUserIdList(CrmEnum.CONTACTS, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        record.put("customerUserIds", AuthUtil.filterUserIdList(CrmEnum.CUSTOMER, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        record.put("receivablesUserIds", AuthUtil.filterUserIdList(CrmEnum.RECEIVABLES, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        record.put("contractUserIds", AuthUtil.filterUserIdList(CrmEnum.CONTRACT, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        record.put("leadsUserIds", AuthUtil.filterUserIdList(CrmEnum.LEADS, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        record.put("recordUserIds", AuthUtil.filterUserIdList(null, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        Map<String, Object> info = crmInstrumentMapper.queryBulletin(biTimeEntity, record);
        this.handleMapValue(info);
        boolean isCustom = !StrUtil.isAllEmpty(biParams.getStartTime(), biParams.getEndTime());
        Map<String, Object> prevRecord = new HashMap<>();
        BigDecimal bigDecimal = new BigDecimal("0");
        if (!isCustom) {
            BiTimeUtil.BiTimeEntity timeEntity = BiTimeUtil.prevAnalyzeType(biParams);
            prevRecord = crmInstrumentMapper.queryBulletin(timeEntity, record);
            this.handleMapValue(prevRecord);
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
     * 销售简报去小数 - 四舍五入
     * */
    private void handleMapValue(Map<String, Object> map){
         map.forEach((key,value) ->{
            map.put(key,new BigDecimal(value.toString()).setScale(0,BigDecimal.ROUND_HALF_DOWN));
         });
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
        CrmEnum crmEnum = CrmEnum.parse(biParams.getLabel());
        List<Long> userIds = AuthUtil.filterUserIdList(crmEnum, CrmAuthEnum.LIST, biAuthority.getUserIds());
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
                searchBO.getSearchList().add(new CrmSearchBO.Search("orderDate", "date", CrmSearchBO.FieldSearchEnum.RANGE, Arrays.asList(DateUtil.formatDate(biTimeEntity.getBeginDate()), DateUtil.formatDate(DateUtil.offsetDay(biTimeEntity.getEndDate(),1)))));
            } else if (biParams.getMoneyType() == 2) {
                searchBO.getSearchList().add(new CrmSearchBO.Search("returnTime", "date", CrmSearchBO.FieldSearchEnum.RANGE, Arrays.asList(DateUtil.formatDate(biTimeEntity.getBeginDate()), DateUtil.formatDate(DateUtil.offsetDay(biTimeEntity.getEndDate(),1)))));
            }
        } else {
            searchBO.getSearchList().add(new CrmSearchBO.Search("createTime", "datetime", CrmSearchBO.FieldSearchEnum.RANGE, Arrays.asList(DateUtil.formatDateTime(biTimeEntity.getBeginDate()), DateUtil.formatDateTime(DateUtil.endOfDay(biTimeEntity.getEndDate())))));
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
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = AuthUtil.filterUserIdList(CrmEnum.CUSTOMER,CrmAuthEnum.LIST, biAuthority.getUserIds());
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
        List<Long> userIds = handleDataType(biParams).getUserIds();
        if (userIds.size() == 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list", new ArrayList<>());
            jsonObject.put("sum_money", 0);
            jsonObject.put("sum_shu", 0);
            jsonObject.put("sum_ying", 0);
            return jsonObject;
        }
        Map<String, Object> map = record.toMap();
        map.put("userIds",userIds);
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
        entity2.setFormType(FieldEnum.TEXT.getFormType());
        entity2.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
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
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = new ArrayList<>();
        if (Objects.equals(biParams.getLabel(), 1)) {
            userIds.addAll(AuthUtil.filterUserIdList(CrmEnum.CONTRACT,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        } else if (Objects.equals(biParams.getLabel(), 2)) {
            userIds.addAll(AuthUtil.filterUserIdList(CrmEnum.RECEIVABLES,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        }
        for (int i = 1; i <= cycleNum; i++) {
            JSONObject object = new JSONObject();
            object.put("month", MonthEnum.valueOf(DateUtil.parse(beginTime.toString(), record.getDateFormat()).monthStartFromOne()));
            object.put("year", DateUtil.year(DateUtil.parse(beginTime.toString(), record.getDateFormat())));
            object.put("label", biParams.getLabel());
            object.put("userIds", userIds);
            object.put("deptIds", AuthUtil.filterDeptId(biAuthority.getDeptIds()));
            Map<String, Object> objectMap = crmInstrumentMapper.salesTrend(record, object);
            if(StrUtil.isNotEmpty(biParams.getType())) {
                if("yyyyMMdd".equals(record.getDateFormat())) {
                    objectMap.put("type",DateUtil.parse(beginTime.toString(),record.getDateFormat()).toDateStr().substring(5));
                }else if("yyyyMM".equals(record.getDateFormat())) {
                    objectMap.put("type",DateUtil.parse(beginTime.toString(),record.getDateFormat()).monthStartFromOne()+"月");
                }
            } else {
                objectMap.put("type",DateUtil.parse(beginTime.toString(),"yyyyMM").toString("yyyy-MM"));
            }
            recordList.add(objectMap);
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
        BiTimeUtil.BiTimeEntity timeEntity = BiTimeUtil.analyzeTime(biParams);
        Map<String, Object> costumer = crmInstrumentMapper.dataInfoCustomer(timeEntity, AuthUtil.filterUserIdList(CrmEnum.CUSTOMER,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        Map<String, Object> activity = crmInstrumentMapper.dataInfoActivity(timeEntity, AuthUtil.filterUserIdList(CrmEnum.CUSTOMER,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        Map<String, Object> business = crmInstrumentMapper.dataInfoBusiness(timeEntity, AuthUtil.filterUserIdList(CrmEnum.BUSINESS,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        Map<String, Object> contract = crmInstrumentMapper.dataInfoContract(timeEntity, AuthUtil.filterUserIdList(CrmEnum.CONTRACT,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        Map<String, Object> receivables = crmInstrumentMapper.dataInfoReceivables(timeEntity, AuthUtil.filterUserIdList(CrmEnum.RECEIVABLES,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        timeEntity.setEndDate(DateUtil.endOfDay(timeEntity.getEndDate()));
        costumer.put("receiveNum",crmInstrumentMapper.dataInfoCustomerPoolNum(timeEntity,AuthUtil.filterUserIdList(CrmEnum.CUSTOMER,CrmAuthEnum.LIST, biAuthority.getUserIds()),2));
        costumer.put("putInPoolNum",crmInstrumentMapper.dataInfoCustomerPoolNum(timeEntity,AuthUtil.filterUserIdList(CrmEnum.CUSTOMER,CrmAuthEnum.LIST, biAuthority.getUserIds()),1));
        AdminConfig adminConfig = adminService.queryFirstConfigByName("expiringContractDays").getData();
        if (adminConfig != null && Objects.equals(1, adminConfig.getStatus())) {
            Date startTime = new Date();
            DateTime dateTime = DateUtil.offsetDay(startTime, Integer.valueOf(adminConfig.getValue()));
            contract.putAll(crmInstrumentMapper.dataInfoEndContractNum(startTime,DateUtil.endOfDay(dateTime),AuthUtil.filterUserIdList(CrmEnum.CONTRACT,CrmAuthEnum.LIST, biAuthority.getUserIds())));
        }
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
                userIdList.addAll(adminService.queryUserList(1).getData());
                deptIdList.addAll(adminService.queryChildDeptId(0).getData());
            }
        } else {
            if (1 == biParams.getIsUser()) {
                if (biParams.getUserId() == null) {
                    if (UserUtil.isAdmin()) {
                        userIdList.addAll(adminService.queryUserList(1).getData());
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
        BiAuthority biAuthority = handleDataType(biParams);
        if (ObjectUtil.equal(status, 1)) {
            kv.put("userIds", AuthUtil.filterUserIdList(CrmEnum.CONTRACT,CrmAuthEnum.LIST, biAuthority.getUserIds()));
        } else if (ObjectUtil.equal(status, 2)) {
            kv.put("userIds", AuthUtil.filterUserIdList(CrmEnum.RECEIVABLES,CrmAuthEnum.LIST, biAuthority.getUserIds()));
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
        String startTime = biParams.getStartTime();
        String endTime = biParams.getEndTime();
        Integer crmType = biParams.getLabel();
        Integer queryType = biParams.getQueryType();
        String search = biParams.getSearch() == null ? null : biParams.getSearch().trim();
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = biAuthority.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return new BasePage<>();
        }
        Integer type = BiTimeUtil.analyzeType(biParams.getType());
        userIds = this.handleUserIds(biParams,userIds);
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
    public List<CrmActivity> exportRecordList(BiParams biParams) {
        Integer crmType = biParams.getLabel();
        String endTime = biParams.getEndTime();
        String startTime = biParams.getStartTime();
        Integer queryType = biParams.getQueryType();
        String search = biParams.getSearch() == null ? null : biParams.getSearch().trim();
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = biAuthority.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        userIds = this.handleUserIds(biParams,userIds);
        Integer type = BiTimeUtil.analyzeType(biParams.getType());
        return crmActivityMapper.exportRecordList( Dict.create().set("crmType", crmType).set("type", type).set("userIds", userIds).set("startTime", startTime)
                .set("endTime", endTime).set("queryType", queryType).set("search", search));
    }

    private List<Long> handleUserIds(BiParams biParams,List<Long> userIds){
        Integer subUser = biParams.getSubUser();
        Long userId = biParams.getUserId();
        Integer deptId = biParams.getDeptId();
        Integer dataType = biParams.getDataType();
        Integer crmType = biParams.getLabel();
        if (subUser != null) {
            if (subUser == 1 && ObjectUtil.isAllEmpty(userId, deptId)) {
                userIds = adminService.queryChildUserId(UserUtil.getUserId()).getData();
            } else if (subUser == 0) {
                userIds = Collections.singletonList(UserUtil.getUserId());
            }
        }
        if (UserUtil.isAdmin() && ObjectUtil.isAllEmpty(userId, deptId) && subUser == null && dataType == null) {
            userIds = adminService.queryUserList(1).getData();
        }
        if (!UserUtil.isAdmin()) {
            Integer followRecordReadMenuId = adminService.queryMenuId("crm", "followRecord", "read").getData();
            List<Long> authUserIdList = AuthUtil.getUserIdByAuth(followRecordReadMenuId);
            if (authUserIdList.size() == 0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
        }
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        List<Long> authUserIds  =  new ArrayList<>(userIds);
        if(crmEnum != null) {
            List<Long> longList = AuthUtil.queryAuthUserList(crmEnum, CrmAuthEnum.LIST);
            authUserIds.retainAll(longList);
        }
        return authUserIds;
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
        kv.put("businessUserIds", AuthUtil.filterUserIdList(CrmEnum.BUSINESS, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        kv.put("contactsUserIds", AuthUtil.filterUserIdList(CrmEnum.CONTACTS, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        kv.put("customerUserIds", AuthUtil.filterUserIdList(CrmEnum.CUSTOMER, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        kv.put("receivablesUserIds", AuthUtil.filterUserIdList(CrmEnum.RECEIVABLES, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        kv.put("contractUserIds", AuthUtil.filterUserIdList(CrmEnum.CONTRACT, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        kv.put("leadsUserIds", AuthUtil.filterUserIdList(CrmEnum.LEADS, CrmAuthEnum.LIST,biAuthority.getUserIds()));
        kv.put("recordUserIds", AuthUtil.filterUserIdList(null, CrmAuthEnum.LIST,biAuthority.getUserIds()));
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

    @Override
    public BasePage<Map<String, Object>> forgottenCustomerPageList(BiParams biParams) {
        BiAuthority biAuthority = handleDataType(biParams);
        List<Long> userIds = biAuthority.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return new BasePage<>();
        }
        Integer day = biParams.getDay();
        List<Long> authUserList = AuthUtil.queryAuthUserList(CrmEnum.CUSTOMER, CrmAuthEnum.LIST);
        authUserList.retainAll(userIds);
        List<Integer> customerIds = crmCustomerService.forgottenCustomer(day, authUserList, biParams.getSearch());
        if (customerIds.size() == 0) {
            return new BasePage<>();
        }
        CrmSearchBO searchBO = new CrmSearchBO();
        searchBO.setPage(biParams.getPage());
        searchBO.setLimit(biParams.getLimit());
        searchBO.setLabel(CrmEnum.CUSTOMER.getType());
        List<String> collect = customerIds.stream().map(Object::toString).collect(Collectors.toList());
        searchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "text", CrmSearchBO.FieldSearchEnum.ID, collect)));
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
        List<Long> authUserList = AuthUtil.queryAuthUserList(CrmEnum.CUSTOMER, CrmAuthEnum.LIST);
        authUserList.retainAll(userIds);
        List<Integer> customerIds = crmCustomerService.unContactCustomer(search, authUserList);
        if (customerIds.size() == 0) {
            return new BasePage<>();
        }
        CrmSearchBO searchBO = new CrmSearchBO();
        searchBO.setPage(biParams.getPage());
        searchBO.setLimit(biParams.getLimit());
        searchBO.setLabel(CrmEnum.CUSTOMER.getType());
        List<String> collect = customerIds.stream().map(Object::toString).collect(Collectors.toList());
        searchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "text", CrmSearchBO.FieldSearchEnum.ID, collect)));
        return crmCustomerService.queryPageList(searchBO);
    }

    @Override
    public JSONObject importRecordList(MultipartFile file, Integer crmType) {
        List<List<Object>> errList = new ArrayList<>();
        if (!Arrays.asList(1,2,3,5,6).contains(crmType)){
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        String filePath = getFilePath(file);
        List<UserInfo> userInfos = adminService.queryUserInfoList().getData();
        Map<String, Long> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getRealname, UserInfo::getUserId, (k1, k2) -> k1));
        AtomicReference<Integer> num = new AtomicReference<>(0);
        Integer size = crmType == 2 ? 7 : 5;
        //客户 ：跟进内容（必填）、创建人（必填）、所属客户（必填）、跟进时间、跟进方式、相关联系人、相关商机
        //非客户：跟进内容（必填）、创建人（必填）、所属线索/联系人/商机/合同（必填）、跟进时间、跟进方式
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
            if (rowList.size() < size) {
                for (int i = rowList.size(); i < size; i++) {
                    rowList.add(null);
                }
            }
            if (rowIndex > 1) {
                num.getAndSet(num.get() + 1);
                if (StrUtil.isEmptyIfStr(rowList.get(0))) {
                    rowList.add(0, "跟进内容不能为空");
                    errList.add(rowList);
                    return;
                }
                if (StrUtil.isEmptyIfStr(rowList.get(1))) {
                    rowList.add(0, "创建人不能为空");
                    errList.add(rowList);
                    return;
                }
                if (StrUtil.isEmptyIfStr(rowList.get(2))) {
                    rowList.add(0, "所属"+crmEnum.getRemarks()+"不能为空");
                    errList.add(rowList);
                    return;
                }
                String content = rowList.get(0).toString().trim();
                String createUserName = rowList.get(1).toString().trim();
                Long createUserId = map.get(createUserName);
                if (createUserId == null){
                    rowList.add(0, "创建人不存在");
                    errList.add(rowList);
                    return;
                }
                String crmTypeName = rowList.get(2).toString().trim();
                String createTime = Optional.ofNullable(rowList.get(3)).orElse("").toString().trim();
                Date createDate = null;
                if (StrUtil.isNotEmpty(createTime)){
                    createDate = DateUtil.parse(createTime, DatePattern.NORM_DATE_PATTERN);
                }
                String category = Optional.ofNullable(rowList.get(4)).orElse("").toString().trim();
                String contactsNames = null;
                String businessNames = null;
                Integer crmTypeId = null;
                switch (crmEnum){
                    case CUSTOMER:
                        contactsNames = Optional.ofNullable(rowList.get(5)).orElse("").toString().trim();
                        businessNames = Optional.ofNullable(rowList.get(6)).orElse("").toString().trim();
                        CrmCustomer crmCustomer = crmCustomerService.lambdaQuery().select(CrmCustomer::getCustomerId)
                                .eq(CrmCustomer::getCustomerName, crmTypeName).ne(CrmCustomer::getStatus,3)
                                .orderByDesc(CrmCustomer::getCreateTime).last(" limit 1 ").one();
                        if (crmCustomer == null){
                            rowList.add(0, "所属客户不存在");
                            errList.add(rowList);
                            return;
                        }
                        crmTypeId = crmCustomer.getCustomerId();
                        if (StrUtil.isNotEmpty(contactsNames)){
                            List<String> names;
                            if(contactsNames.contains("/")){
                                names = Arrays.asList(contactsNames.split("/"));
                            }else {
                                names = ListUtil.toList(contactsNames);
                            }
                            names.removeIf(StrUtil::isEmpty);
                            List<Integer> contactsList = new ArrayList<>(names.size());
                            for (String name : names) {
                                Optional<CrmContacts> crmContacts = crmContactsService.lambdaQuery().select(CrmContacts::getContactsId)
                                        .eq(CrmContacts::getName, name).last(" limit 1").oneOpt();
                                crmContacts.ifPresent(crmContacts1 -> contactsList.add(crmContacts1.getContactsId()));
                            }
                            contactsNames = StrUtil.join(Const.SEPARATOR, contactsList);
                        }

                        if (StrUtil.isNotEmpty(businessNames)){
                            List<String> names;
                            if(businessNames.contains("/")){
                                names = Arrays.asList(businessNames.split("/"));
                            }else {
                                names = ListUtil.toList(businessNames);
                            }
                            names.removeIf(StrUtil::isEmpty);
                            List<CrmBusiness> list = crmBusinessService.lambdaQuery().select(CrmBusiness::getBusinessId)
                                    .in(CrmBusiness::getBusinessName, names).ne(CrmBusiness::getStatus,3).list();
                            if (CollUtil.isNotEmpty(list)){
                                List<Integer> businessIdList = list.stream().map(CrmBusiness::getBusinessId).collect(Collectors.toList());
                                businessNames = StrUtil.join(Const.SEPARATOR, businessIdList);
                            }
                        }

                        break;
                    case LEADS:
                        CrmLeads crmLeads = crmLeadsService.lambdaQuery().select(CrmLeads::getLeadsId)
                                .eq(CrmLeads::getLeadsName, crmTypeName).orderByDesc(CrmLeads::getCreateTime).last(" limit 1 ").one();
                        if (crmLeads == null){
                            rowList.add(0, "所属线索不存在");
                            errList.add(rowList);
                            return;
                        }
                        crmTypeId = crmLeads.getLeadsId();
                        break;
                    case CONTACTS:
                        CrmContacts crmContacts = crmContactsService.lambdaQuery().select(CrmContacts::getContactsId)
                                .eq(CrmContacts::getName, crmTypeName).orderByDesc(CrmContacts::getCreateTime).last(" limit 1 ").one();
                        if (crmContacts == null){
                            rowList.add(0, "所属联系人不存在");
                            errList.add(rowList);
                            return;
                        }
                        crmTypeId = crmContacts.getContactsId();
                        break;
                    case BUSINESS:
                        CrmBusiness crmBusiness = crmBusinessService.lambdaQuery().select(CrmBusiness::getBusinessId)
                                .eq(CrmBusiness::getBusinessName, crmTypeName).ne(CrmBusiness::getStatus,3)
                                .orderByDesc(CrmBusiness::getCreateTime).last(" limit 1 ").one();
                        if (crmBusiness == null){
                            rowList.add(0, "所属商机不存在");
                            errList.add(rowList);
                            return;
                        }
                        crmTypeId = crmBusiness.getBusinessId();
                        break;
                    case CONTRACT:
                        CrmContract crmContract = crmContractService.lambdaQuery().select(CrmContract::getContractId)
                                .eq(CrmContract::getNum, crmTypeName).ne(CrmContract::getCheckStatus,7)
                                .orderByDesc(CrmContract::getCreateTime).last(" limit 1 ").one();
                        if (crmContract == null){
                            rowList.add(0, "所属合同不存在");
                            errList.add(rowList);
                            return;
                        }
                        crmTypeId = crmContract.getContractId();
                        break;
                    default:
                        break;
                }
                UserInfo user = UserUtil.getUser();
                CrmActivity crmActivity = new CrmActivity();
                crmActivity.setContent(content);
                crmActivity.setCategory(category);
                crmActivity.setType(1);
                crmActivity.setActivityType(crmEnum.getType());
                crmActivity.setActivityTypeId(crmTypeId);
                crmActivity.setContactsIds(contactsNames);
                crmActivity.setBusinessIds(businessNames);

                String batchId = StrUtil.isEmpty(crmActivity.getBatchId()) ? IdUtil.simpleUUID() : crmActivity.getBatchId();
                crmActivity.setType(1);
                crmActivity.setCreateUserId(createUserId);
                crmActivity.setBatchId(batchId);
                crmActivity.setCreateTime(createDate != null ? createDate : new Date());
                crmActivityService.save(crmActivity);
                if (crmActivity.getType() == 1) {
                    crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.TODAY_CUSTOMER, crmActivity.getActivityTypeId());
                    crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.FOLLOW_LEADS, crmActivity.getActivityTypeId());
                    crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.FOLLOW_CUSTOMER, crmActivity.getActivityTypeId());
                    crmBackLogDealService.deleteByType(user.getUserId(), CrmEnum.parse(crmActivity.getActivityType()), CrmBackLogEnum.TO_ENTER_CUSTOMER_POOL, crmActivity.getActivityTypeId());
                }
                actionRecordUtil.addFollowupActionRecord(crmActivity.getActivityType(), crmActivity.getActivityTypeId(), "");
            }
        });
        FileUtil.del(filePath);
        JSONObject result = new JSONObject().fluentPut("totalSize", num.get()).fluentPut("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(FileUtil.getTmpDirPath() + "/" + token);
                int columnNum = crmType == 2 ? 7 : 5;
                writer.merge(columnNum, "系统用户导入模板(*)为必填项");
                for (int i = 0; i < columnNum + 1; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                if (crmType == 2) {
                    writer.writeHeadRow(Arrays.asList("错误信息", "跟进内容(*)", "创建人(*)", "所属客户(*)", "跟进时间", "跟进方式","相关联系人","相关商机"));
                }else {
                    writer.writeHeadRow(Arrays.asList("错误信息", "跟进内容(*)", "创建人(*)", "所属" + crmEnum.getRemarks() + "(*)", "跟进时间", "跟进方式"));
                }
                writer.write(errList);
                result.fluentPut("errSize", errList.size()).fluentPut("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }
}
