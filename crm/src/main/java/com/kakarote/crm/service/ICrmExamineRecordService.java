package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.feign.crm.entity.SimpleCrmInfo;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.examine.entity.ExamineMessageBO;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.BO.CrmExamineData;
import com.kakarote.crm.entity.PO.CrmExamineRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审核记录表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface ICrmExamineRecordService extends BaseService<CrmExamineRecord> {
    /**
     * 保存审批记录
     * @param type 类型 1合同，2回款
     * @param userId 审核用户ID
     * @param ownerUserId 服务人id
     * @param recordId id
     * @param status 审核状态
     * @return data
     */
    public CrmExamineData saveExamineRecord(Integer type, Long userId, Long ownerUserId, Integer recordId, Integer status);
    /**
     * 删除审批记录
     * @param recordId id
     */
    public void deleteExamine(Integer recordId);

    /**
     * 更新合同回款金额
     * @param id id
     */
    public void updateContractMoney(Integer id);

    /**
     * 更新合同未回款金额
     * @param id id
     */
    public void updateUnreceivedMoney(Integer id);

    /**
     * 查询审批记录列表
     * @param recordId 记录ID
     * @param ownerUserId 负责人ID
     */
    public JSONObject queryExamineRecordList(Integer recordId, Long ownerUserId);

    /**
     * 查询审批流程列表
     * @param recordId 记录ID
     */
    public List<JSONObject> queryExamineLogList(Integer recordId,String ownerUserId);

    /**
     * 审核审批申请
     * @param recordId 审批记录ID
     * @param status 状态
     * @param remarks 备注
     * @param id id
     * @param nextUserId 下一审批人
     */
    public void auditExamine(Integer recordId, Integer status, String remarks, Integer id, Long nextUserId);

    /**
     * 添加消息
     * @param categoryType categoryType
     * @param examineType  1 待审核 2 通过 3 拒绝
     */
    public void addMessage(Integer categoryType, Integer examineType, Object examineObj, Long ownerUserId);



    /**
     * 自定义审批添加消息
     * @param categoryType categoryType
     * @param examineType  1 待审核 2 通过 3 拒绝
     */
    public void addMessageForNewExamine(Integer categoryType, Integer examineType, Object examineObj, Long ownerUserId);

    public void addMessageForNewExamine(ExamineMessageBO examineMessageBO);

    public Map<String, Object> getDataMapForNewExamine(ExamineConditionDataBO examineConditionDataBO);

    public Boolean updateCheckStatusByNewExamine(ExamineConditionDataBO examineConditionDataBO);


    SimpleCrmInfo getCrmSimpleInfo(ExamineConditionDataBO examineConditionDataBO);
}
