package com.kakarote.examine.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.examine.constant.ExamineTypeEnum;
import com.kakarote.examine.entity.PO.ExamineCondition;
import com.kakarote.examine.entity.PO.ExamineFlow;
import com.kakarote.examine.mapper.ExamineFlowMapper;
import com.kakarote.examine.service.IExamineConditionService;
import com.kakarote.examine.service.IExamineFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 审批流程表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service
public class ExamineFlowServiceImpl extends BaseServiceImpl<ExamineFlowMapper, ExamineFlow> implements IExamineFlowService {

    @Autowired
    private IExamineConditionService examineConditionService;

    /**
     * 查询下一审批流程
     *
     * @param examineId    审批流ID
     * @param flowId       当前流程ID，如果查询第一层传null
     * @param conditionMap 条件判断数据信息，只有条件流程需要
     * @return data
     */
    @Override
    public ExamineFlow queryNextExamineFlow(Long examineId, Integer flowId, Map<String, Object> conditionMap) {
        /*
        查询第一层审批流
         */
        if (flowId == null) {
            ExamineFlow examineFlow = lambdaQuery().eq(ExamineFlow::getExamineId, examineId).gt(ExamineFlow::getSort, 0).orderByAsc(ExamineFlow::getSort).last(" limit 1").one();
            if (examineFlow == null){
                return null;
            }
            if (!ExamineTypeEnum.CONDITION.getType().equals(examineFlow.getExamineType())) {
                return examineFlow;
            }
            return examineConditionService.queryNextExamineFlowByCondition(examineFlow, conditionMap);
        }

        /*
          查询当前层级审批流
         */
        ExamineFlow examineFlow = getById(flowId);
//        if (examineFlow.getExamineType().equals(ExamineTypeEnum.CONDITION.getType())) {
//            return examineConditionService.queryNextExamineFlowByCondition(examineFlow, conditionMap);
//        }
        /*
         查询下层审批流，注意这块可能是条件审批下的审批流
         */
        ExamineFlow nextExamineFlow = lambdaQuery()
                .eq(ExamineFlow::getExamineId, examineFlow.getExamineId())
                .eq(ExamineFlow::getConditionId, examineFlow.getConditionId())
                .gt(ExamineFlow::getSort, examineFlow.getSort())
                .orderByAsc(ExamineFlow::getSort)
                .last(" limit 1").one();

        if (nextExamineFlow != null && examineFlow.getExamineType().equals(ExamineTypeEnum.CONDITION.getType())) {
            return nextExamineFlow;
        }

        if (nextExamineFlow == null && examineFlow.getConditionId() != 0) {
            /*
             当前条件层级已完成，跳到上一层审核流程
             */
            return queryUpperExamineFlow(examineFlow.getConditionId(), conditionMap);
        }
        /*
        如果是条件审批由条件审批去判断
         */
        if (nextExamineFlow != null && Objects.equals(nextExamineFlow.getExamineType(), ExamineTypeEnum.CONDITION.getType())) {
            return examineConditionService.queryNextExamineFlowByCondition(nextExamineFlow, conditionMap);
        }
        return nextExamineFlow;
    }


    /**
     * 查询上一层审批流
     *
     * @param conditionId 条件ID
     * @return data
     */
    public ExamineFlow queryUpperExamineFlow(Integer conditionId, Map<String, Object> conditionMap) {
        ExamineCondition condition = examineConditionService.getById(conditionId);
        ExamineFlow examineFlow = getById(condition.getFlowId());
        /*
         查询下层审批流，注意这块可能是条件审批下的审批流
         */
        ExamineFlow nextExamineFlow = lambdaQuery()
                .eq(ExamineFlow::getExamineId, examineFlow.getExamineId())
                .eq(ExamineFlow::getConditionId, examineFlow.getConditionId())
                .gt(ExamineFlow::getSort, examineFlow.getSort())
                .last(" limit 1").one();
        if (nextExamineFlow != null && Objects.equals(examineFlow.getExamineType(), ExamineTypeEnum.CONDITION.getType())) {
            return nextExamineFlow;
        }
        /*
          如果下一审批流为空，但是当前审批流程的条件ID不为空，说明当前是条件审批下的审批流程，跳到上一层
         */
        if (nextExamineFlow == null && examineFlow.getConditionId() != 0) {
            /*
             当前条件层级已完成，跳到上一层审核流程
             */
            return queryUpperExamineFlow(examineFlow.getConditionId(), conditionMap);
        }
         /*
        如果是条件审批由条件审批去判断
         */
        if (nextExamineFlow != null && Objects.equals(nextExamineFlow.getExamineType(), ExamineTypeEnum.CONDITION.getType())) {
            return examineConditionService.queryNextExamineFlowByCondition(nextExamineFlow, conditionMap);
        }
        return nextExamineFlow;
    }
}
