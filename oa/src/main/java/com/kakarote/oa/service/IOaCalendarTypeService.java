package com.kakarote.oa.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.BO.QueryEventCrmBO;
import com.kakarote.oa.entity.BO.QueryEventTaskBO;
import com.kakarote.oa.entity.BO.UpdateTypeUserBO;
import com.kakarote.oa.entity.PO.OaCalendarType;
import com.kakarote.oa.entity.VO.EventTaskVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日历类型 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaCalendarTypeService extends BaseService<OaCalendarType> {

    void addOrUpdateType(OaCalendarType oaCalendarType);

    void deleteType(Integer typeId);

    List<OaCalendarType> queryTypeList();

    List<OaCalendarType> queryTypeListByUser(Long userId);

    void updateTypeUser(UpdateTypeUserBO updateTypeUserBO);

    List<EventTaskVO>  eventTask(QueryEventTaskBO eventTaskBO);

    JSONObject eventCrm(QueryEventCrmBO queryEventCrmBO);

    List<String> queryFixedTypeByUserId(Long userId);

    BasePage<Map<String,Object>> eventCustomer(QueryEventCrmPageBO eventCrmPageBO);

    BasePage<Map<String, Object>> eventContract(QueryEventCrmPageBO eventCrmPageBO);

    BasePage<Map<String, Object>> eventLeads(QueryEventCrmPageBO eventCrmPageBO);

    BasePage<Map<String, Object>> eventBusiness(QueryEventCrmPageBO eventCrmPageBO);

    BasePage<Map<String, Object>> eventDealBusiness(QueryEventCrmPageBO eventCrmPageBO);
}
