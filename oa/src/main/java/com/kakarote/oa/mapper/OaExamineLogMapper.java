package com.kakarote.oa.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.oa.entity.PO.OaExamineLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 审核日志表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface OaExamineLogMapper extends BaseMapper<OaExamineLog> {

    OaExamineLog queryExamineLog(@Param("recordId") Integer examineRecordId,@Param("examineUser") Long userId,@Param("stepId") Integer examineStepId);

    Integer queryCountByStepId(@Param("recordId") Integer recordId,@Param("stepId") Integer stepId);

    JSONObject queryUserByRecordId(@Param("recordId") String recordId);

    JSONObject queryRecordAndId(String recordId);

    JSONObject queryRecordByUserIdAndStatus(@Param("createUser") Long createUser,@Param("examineTime") Date examineTime);

    List<JSONObject> queryExamineLogAndUserByRecordId(String recordId);

    JSONObject queryExamineLogAndUserByLogId(Integer logId);

    List<JSONObject> queryUserByRecordIdAndStepIdAndStatus(@Param("recordId") String recordId,@Param("stepId") long stepId);

    List<JSONObject> queryUserByUserId(Long createUserId);

    JSONObject queryUserByUserIdAndStatus(Long userId);

    List<JSONObject> queryExamineLogByRecordIdByStep1(Integer recordId);

    List<JSONObject> queryExamineLogByRecordIdByStep(Integer recordId);
}
