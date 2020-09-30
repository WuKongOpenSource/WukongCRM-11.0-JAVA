package com.kakarote.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.crm.service.CrmEventService;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.oa.common.OaCodeEnum;
import com.kakarote.oa.common.repetitionstrategy.RepetitionFactory;
import com.kakarote.oa.common.repetitionstrategy.RepetitionStrategy;
import com.kakarote.oa.constart.RepetitionType;
import com.kakarote.oa.entity.BO.*;
import com.kakarote.oa.entity.PO.*;
import com.kakarote.oa.entity.VO.EventTaskVO;
import com.kakarote.oa.entity.VO.QueryEventByIdVO;
import com.kakarote.oa.mapper.OaEventMapper;
import com.kakarote.oa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 日程表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class OaEventServiceImpl extends BaseServiceImpl<OaEventMapper, OaEvent> implements IOaEventService {

    @Autowired
    private RepetitionFactory repetitionFactory;

    @Autowired
    private IOaEventRelationService eventRelationService;

    @Autowired
    private IOaEventNoticeService eventNoticeService;

    @Autowired
    private IOaEventUpdateRecordService eventUpdateRecordService;

    @Autowired
    private OaEventMapper eventMapper;

    @Autowired
    private IOaCalendarTypeService calendarTypeService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CrmService crmService;

    @Autowired
    private CrmEventService crmEventService;


    @Autowired
    private AdminMessageService adminMessageService;

    @Override
    public void saveEvent(SetEventBO setEventBO) {
        OaEvent event = setEventBO.getEvent();
        OaEventRelation eventRelation = setEventBO.getRelation();
        List<OaEventNotice> notices = setEventBO.getNotice();
        RepetitionStrategy repetitionStrategy = repetitionFactory.getRepetitionStrategy(event.getRepetitionType());
        event = repetitionStrategy.processTime(event);
        String batchId = IdUtil.simpleUUID();
        event.setBatchId(batchId);
        save(event);
        if (eventRelation != null) {
            eventRelation.setEventId(event.getEventId());
            eventRelationService.save(eventRelation);
        }
        for (OaEventNotice notice : notices) {
            notice.setEventId(event.getEventId());
        }
        eventNoticeService.saveBatch(notices, 10);
        eventNoticeUpdate(event, event.getEventId());
    }

    /**
     * 添加修改重新推算日程提醒
     */
    private void eventNoticeUpdate(OaEvent oaEvent, Integer oldEventId) {
        //TODO 添加日程提醒
        adminMessageService.deleteEventMessage(oldEventId);
        eventNotice(CollUtil.toList(oaEvent));
    }


    @Override
    public void eventNotice(List<OaEvent> oaEventList) {
        DateTime nowDate = DateUtil.date();
        long startTime = DateUtil.beginOfDay(nowDate).getTime();
        long endTime = DateUtil.endOfDay(nowDate.offsetNew(DateField.DAY_OF_YEAR, 30)).getTime();
        List<OaEventDTO> recordList = queryList(oaEventList, startTime, endTime);
        AdminMessageEnum messageEnum = AdminMessageEnum.OA_EVENT_MESSAGE;
        for (OaEventDTO event : recordList) {
            Integer eventId = event.getEventId();
            List<OaEventNotice> noticeList = eventNoticeService.lambdaQuery().eq(OaEventNotice::getEventId, eventId).list();
            if (CollUtil.isEmpty(noticeList)) {
                continue;
            }
            Long eventStartTime = event.getStartTime();
            String title = event.getTitle();
            String ownerUserIds = event.getOwnerUserIds();
            for (OaEventNotice notice : noticeList) {
                Integer type = notice.getType();
                Integer value = notice.getValue();
                long millis = 0L;
                switch (type) {
                    case 1:
                        millis = DateUnit.MINUTE.getMillis() * value;
                        break;
                    case 2:
                        millis = DateUnit.HOUR.getMillis() * value;
                        break;
                    case 3:
                        millis = DateUnit.DAY.getMillis() * value;
                        break;
                    default:
                        break;
                }
                Date createTime = new Date(eventStartTime - millis);
                if (DateUtil.isSameDay(createTime, nowDate)) {
                    //TODO 添加日程提醒
                    for (Long userId : TagUtil.toLongSet(ownerUserIds)) {
                        AdminMessage adminMessage = new AdminMessage();
                        adminMessage.setCreateUser(event.getCreateUserId());
                        adminMessage.setCreateTime(DateUtil.formatDateTime(createTime));
                        adminMessage.setRecipientUser(userId);
                        adminMessage.setContent(JSON.toJSONString(Dict.create().set("startTime", event.getStartTime()).set("endTime", event.getEndTime()).set("type", type).set("value", value)));
                        adminMessage.setTypeId(eventId);
                        adminMessage.setTitle(title);
                        adminMessage.setType(messageEnum.getType());
                        adminMessage.setLabel(messageEnum.getLabel());
                        adminMessageService.save(adminMessage);
                    }
                }
            }
        }
    }

    @Override
    public void updateEvent(SetEventBO setEventBO) {
        Integer type = setEventBO.getType();
        Long time = setEventBO.getTime();
        OaEvent event = setEventBO.getEvent();
        Integer eventId = event.getEventId();
        if (type == 2) {
            //修改本次
            RepetitionStrategy repetitionStrategy = repetitionFactory.getRepetitionStrategy(event.getRepetitionType());
            event = repetitionStrategy.processTime(event);
            event.setEventId(null);
            save(event);
            Optional<OaEventUpdateRecord> oaEventUpdateRecord = eventUpdateRecordService.lambdaQuery().eq(OaEventUpdateRecord::getNewEventId, eventId)
                    .eq(OaEventUpdateRecord::getStatus, 2).last("limit 1").oneOpt();
            if (oaEventUpdateRecord.isPresent()) {
                removeById(eventId);
                OaEventUpdateRecord eventUpdateRecord = oaEventUpdateRecord.get();
                eventUpdateRecord.setNewEventId(event.getEventId());
                eventUpdateRecordService.updateById(eventUpdateRecord);
            } else {
                OaEvent oldEvent = getById(eventId);
                if (oldEvent.getRepetitionType().equals(RepetitionType.NO_REPETITION.getType())) {
                    removeById(oldEvent.getEventId());
                    eventRelationService.lambdaUpdate().eq(OaEventRelation::getEventId, oldEvent.getEventId()).remove();
                } else {
                    OaEventUpdateRecord eventUpdateRecord = new OaEventUpdateRecord();
                    eventUpdateRecord.setEventId(eventId);
                    eventUpdateRecord.setTime(time);
                    eventUpdateRecord.setStatus(type);
                    eventUpdateRecord.setBatchId(event.getBatchId());
                    eventUpdateRecord.setNewEventId(event.getEventId());
                    eventUpdateRecordService.save(eventUpdateRecord);
                }
            }
        } else {
            //修改本次及以后
            List<Integer> eventIds = eventUpdateRecordService.lambdaQuery().select(OaEventUpdateRecord::getNewEventId).gt(OaEventUpdateRecord::getTime, time)
                    .eq(OaEventUpdateRecord::getBatchId, event.getBatchId()).list()
                    .stream().map(OaEventUpdateRecord::getNewEventId).collect(Collectors.toList());
            removeByIds(eventIds);
            eventUpdateRecordService.lambdaUpdate().gt(OaEventUpdateRecord::getTime, time)
                    .eq(OaEventUpdateRecord::getBatchId, event.getBatchId()).remove();
            lambdaUpdate().set(OaEvent::getRepeatEndTime, new Date(time)).eq(OaEvent::getEventId, eventId).update();
            RepetitionStrategy repetitionStrategy = repetitionFactory.getRepetitionStrategy(event.getRepetitionType());
            event = repetitionStrategy.processTime(event);
            event.setEventId(null);
            event.setCreateUserId(UserUtil.getUserId());
            event.setCreateTime(new Date());
            save(event);
            OaEventUpdateRecord eventUpdateRecord = new OaEventUpdateRecord();
            eventUpdateRecord.setEventId(eventId);
            eventUpdateRecord.setTime(time);
            eventUpdateRecord.setStatus(type);
            eventUpdateRecord.setBatchId(event.getBatchId());
            eventUpdateRecord.setNewEventId(event.getEventId());
            eventUpdateRecordService.save(eventUpdateRecord);
        }
        OaEventRelation eventRelation = setEventBO.getRelation();
        List<OaEventNotice> notices = setEventBO.getNotice();
        if (eventRelation != null) {
            eventRelation.setEventId(event.getEventId());
            eventRelationService.save(eventRelation);
        }
        for (OaEventNotice notice : notices) {
            notice.setEventId(event.getEventId());
        }
        eventNoticeService.saveBatch(notices, 10);
        if (time <= System.currentTimeMillis()) {
            eventNoticeUpdate(event, eventId);
        }
    }

    @Override
    public void delete(DeleteEventBO deleteEventBO) {
        Integer type = deleteEventBO.getType();
        Integer eventId = deleteEventBO.getEventId();
        String batchId = deleteEventBO.getBatchId();
        if (type == 1) {
            OaEvent oaEvent = getById(eventId);
            if (oaEvent.getRepetitionType().equals(RepetitionType.NO_REPETITION.getType())) {
                removeById(eventId);
            } else {
                OaEventUpdateRecord eventUpdateRecord = new OaEventUpdateRecord();
                eventUpdateRecord.setEventId(eventId);
                eventUpdateRecord.setTime(deleteEventBO.getTime());
                eventUpdateRecord.setStatus(type);
                eventUpdateRecord.setBatchId(batchId);
                eventUpdateRecordService.save(eventUpdateRecord);
            }
        } else {
            List<Integer> eventIds = lambdaQuery().select(OaEvent::getEventId).eq(OaEvent::getBatchId, batchId).list()
                    .stream().map(OaEvent::getEventId).collect(Collectors.toList());
            eventRelationService.lambdaUpdate().in(OaEventRelation::getEventId, eventIds).remove();
            removeByIds(eventIds);
        }
    }

    @Override
    public List<OaEventDTO> queryList(QueryEventListBO queryEventListBO) {
        long startTime = queryEventListBO.getStartTime();
        long endTime = queryEventListBO.getEndTime();
        if (queryEventListBO.getUserId() == null) {
            queryEventListBO.setUserId(UserUtil.getUserId());
        }
        List<OaEvent> oaEventList = eventMapper.queryList(queryEventListBO);
        return queryList(oaEventList, startTime, endTime);
    }

    private List<OaEventDTO> queryList(List<OaEvent> oaEventList, long startTime, long endTime) {
        List<OaEventDTO> recordList = new ArrayList<>();
        Set<String> batchIds = oaEventList.stream().map(OaEvent::getBatchId).collect(Collectors.toSet());
        if (batchIds.size() == 0) {
            batchIds.add("0");
        }
        List<OaEventUpdateRecord> oaEventUpdateRecordList = eventUpdateRecordService.lambdaQuery().in(OaEventUpdateRecord::getBatchId, batchIds).list();
        Map<String, Integer> map = new HashMap<>();
        oaEventUpdateRecordList.forEach(settingEvent -> {
            String key = settingEvent.getEventId() + "_" + DateUtil.formatDate(new Date(settingEvent.getTime()));
            map.put(key, settingEvent.getStatus());
        });
        Map<Integer, List<OaEvent>> collect = oaEventList.stream().collect(Collectors.groupingBy(OaEvent::getRepetitionType));
        collect.forEach((repetitionType, list) -> {
            RepetitionStrategy repetitionStrategy = repetitionFactory.getRepetitionStrategy(repetitionType);
            repetitionStrategy.setSpecialDayMap(map);
            List<OaEventDTO> resultList = repetitionStrategy.processQuery(list, startTime, endTime);
            recordList.addAll(resultList);
        });
        return recordList;
    }


    @Override
    public Set<String> queryListStatus(QueryEventListBO queryEventListBO) {
        List<OaEventDTO> recordList = queryList(queryEventListBO);
        Set<String> dateList = new HashSet<>();
        recordList.forEach(eventDTO -> {
            Long startTime = eventDTO.getStartTime();
            Long endTime = eventDTO.getEndTime();
            Set<String> collect = DateUtil.rangeToList(DateUtil.date(startTime), DateUtil.date(endTime), DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).collect(Collectors.toSet());
            dateList.addAll(collect);
        });
        long startTime = queryEventListBO.getStartTime();
        long endTime = queryEventListBO.getEndTime();
        Long userId = queryEventListBO.getUserId();
        if (userId == null) {
            userId = UserUtil.getUserId();
        }
        List<String> types = calendarTypeService.queryFixedTypeByUserId(userId);
        if (types.contains("3")) {
            AdminConfig adminConfig = adminService.queryFirstConfigByName("expiringContractDays").getData();
            CrmEventBO crmEventBO = new CrmEventBO();
            crmEventBO.setUserId(userId);
            crmEventBO.setStartTime(new Date(startTime));
            crmEventBO.setEndTime(new Date(endTime));
            crmEventBO.setExpiringDay(Integer.valueOf(adminConfig.getValue()));
            if (ObjectUtil.isNotEmpty(adminConfig) && adminConfig.getStatus() == 1) {
                List<String> endContractTimeList = crmEventService.endContract(crmEventBO).getData();
                dateList.addAll(endContractTimeList);
            }
        }
        if (types.contains("2")) {
            CrmEventBO crmEventBO = new CrmEventBO();
            crmEventBO.setUserId(userId);
            crmEventBO.setStartTime(new Date(startTime));
            crmEventBO.setEndTime(new Date(endTime));
            List<String> customerTimeList = crmEventService.eventCustomer(crmEventBO).getData();
            dateList.addAll(customerTimeList);
        }
        if (types.contains("4")) {
            CrmEventBO crmEventBO = new CrmEventBO();
            crmEventBO.setUserId(userId);
            crmEventBO.setStartTime(new Date(startTime));
            crmEventBO.setEndTime(new Date(endTime));
            List<String> receiveContractTimeList = crmEventService.receiveContract(crmEventBO).getData();
            dateList.addAll(receiveContractTimeList);
        }
        if (types.contains("1")) {
            QueryEventTaskBO queryEventTaskBO = BeanUtil.copyProperties(queryEventListBO, QueryEventTaskBO.class);
            List<EventTaskVO> taskList = calendarTypeService.eventTask(queryEventTaskBO);
            taskList.forEach(task -> {
                Set<String> collect = DateUtil.rangeToList(DateUtil.date(task.getStartTime()), DateUtil.date(task.getEndTime()), DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).collect(Collectors.toSet());
                dateList.addAll(collect);
            });
        }
        if (types.contains("5")) {
            CrmEventBO crmEventBO = new CrmEventBO();
            crmEventBO.setUserId(userId);
            crmEventBO.setStartTime(new Date(startTime));
            crmEventBO.setEndTime(new Date(endTime));
            List<String> leadsTimeList = crmEventService.eventLeads(crmEventBO).getData();
            dateList.addAll(leadsTimeList);
        }
        if (types.contains("6")) {
            CrmEventBO crmEventBO = new CrmEventBO();
            crmEventBO.setUserId(userId);
            crmEventBO.setStartTime(new Date(startTime));
            crmEventBO.setEndTime(new Date(endTime));
            List<String> businessTimeList = crmEventService.eventBusiness(crmEventBO).getData();
            dateList.addAll(businessTimeList);
        }
        return dateList;
    }

    @Override
    public QueryEventByIdVO queryById(QueryEventByIdBO queryEventByIdBO) {
        Integer eventId = queryEventByIdBO.getEventId();
        long startTime = queryEventByIdBO.getStartTime();
        long endTime = queryEventByIdBO.getEndTime();
        OaEvent oaEvent = getById(eventId);
        if (oaEvent == null) {
            throw new CrmException(OaCodeEnum.EVENT_ALREADY_DELETE);
        }
        oaEvent.setStartTime(new Date(startTime));
        oaEvent.setEndTime(new Date(endTime));
        OaCalendarType calendarType = calendarTypeService.getById(oaEvent.getTypeId());
        QueryEventByIdVO queryEventByIdVO = BeanUtil.copyProperties(oaEvent, QueryEventByIdVO.class);
        queryEventByIdVO.setTypeName(calendarType.getTypeName());
        queryEventByIdVO.setColor(calendarType.getColor());
        UserInfo createUserInfo = adminService.getUserInfo(oaEvent.getCreateUserId()).getData();
        queryEventByIdVO.setCreateUserName(createUserInfo.getRealname());
        List<SimpleUser> ownerUserList = adminService.queryUserByIds(TagUtil.toLongSet(oaEvent.getOwnerUserIds())).getData();
        queryEventByIdVO.setOwnerUserList(ownerUserList);
        List<OaEventNotice> noticeList = eventNoticeService.lambdaQuery().eq(OaEventNotice::getEventId, eventId).list();
        queryEventByIdVO.setNoticeList(noticeList);
        Optional<OaEventRelation> oaEventRelationOpt = eventRelationService.lambdaQuery().eq(OaEventRelation::getEventId, eventId).oneOpt();
        if (oaEventRelationOpt.isPresent()) {
            OaEventRelation oaEventRelation = oaEventRelationOpt.get();
            List<SimpleCrmEntity> customerList = crmService.queryCustomerInfo(TagUtil.toSet(oaEventRelation.getCustomerIds())).getData();
            queryEventByIdVO.setCustomerList(customerList);
            List<SimpleCrmEntity> contactsList = crmService.queryContactsInfo(TagUtil.toSet(oaEventRelation.getContactsIds())).getData();
            queryEventByIdVO.setContactsList(contactsList);
            List<SimpleCrmEntity> contractList = crmService.queryContractInfo(TagUtil.toSet(oaEventRelation.getContractIds())).getData();
            queryEventByIdVO.setContractList(contractList);
            List<SimpleCrmEntity> businessList = crmService.queryBusinessInfo(TagUtil.toSet(oaEventRelation.getBusinessIds())).getData();
            queryEventByIdVO.setBusinessList(businessList);
        }
        return queryEventByIdVO;
    }
}
