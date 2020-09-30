package com.kakarote.crm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CallRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 通话记录管理
 * @author Ian
 */
public interface CallRecordMapper extends BaseMapper<CallRecord> {

    /**
     * 查询通话记录
     */
    CallRecord queryRecord(@Param("number") String number, @Param("startTime") Date startTime, @Param("ownerUserId")Long ownerUserId);

    /**
     * 查询通话记录列表
     */
    BasePage<JSONObject> pageCallRecordList(BasePage<JSONObject> page, @Param("sqlDateFormat") String sqlDateFormat,
                                            @Param("userIds") List<Long> userIds, @Param("talkTime") Long talkTime,
                                            @Param("talkTimeCondition") String talkTimeCondition,
                                            @Param("beginTime") Integer beginTime, @Param("finalTime") Integer finalTime);

    /**
     * 查询联系人
     */
    List<JSONObject> queryContactsByCustomerId( @Param("customerId") Integer customerId);

    /**
     * 查询线索自定义字段
     */
    List<JSONObject> searchFieldValueByLeadsId( @Param("leadsId") Integer leadsId);

    /**
     * 查询联系人自定义字段
     */
    List<JSONObject> searchFieldValueByContactsId( @Param("contactsId") Integer contactsId);

    /**
     * 通话记录分析
     */
    BasePage<JSONObject> analysis(BasePage<JSONObject> page,@Param("userIds") List<Long> userIds,
                                            @Param("sqlDateFormat") String sqlDateFormat,
                                            @Param("beginTime") Integer beginTime, @Param("finalTime") Integer finalTime);

}
