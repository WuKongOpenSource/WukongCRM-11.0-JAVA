package com.kakarote.examine.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.examine.entity.PO.ExamineFlow;

import java.util.Map;

/**
 * <p>
 * 审批流程表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
public interface IExamineFlowService extends BaseService<ExamineFlow> {

    /**
     * 查询下一审批流程
     * @param examineId 审批流ID
     * @param flowId 当前流程ID，如果查询第一层传null
     * @param conditionMap 条件判断数据信息，只有条件流程需要
     * @return data
     */
    public ExamineFlow queryNextExamineFlow(Long examineId, Integer flowId, Map<String,Object> conditionMap);

    /**
     * 查询上一层审批流
     *
     * @param conditionId 条件ID
     * @return data
     */
    public ExamineFlow queryUpperExamineFlow(Integer conditionId, Map<String, Object> conditionMap);
}
