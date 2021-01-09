package com.kakarote.examine.service.impl;

import com.kakarote.core.common.Const;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.examine.constant.ExamineTypeEnum;
import com.kakarote.examine.entity.BO.ExamineDataSaveBO;
import com.kakarote.examine.entity.BO.ExamineUserBO;
import com.kakarote.examine.entity.PO.ExamineFlow;
import com.kakarote.examine.entity.PO.ExamineFlowMember;
import com.kakarote.examine.entity.VO.ExamineFlowVO;
import com.kakarote.examine.mapper.ExamineFlowMemberMapper;
import com.kakarote.examine.service.ExamineTypeService;
import com.kakarote.examine.service.IExamineFlowMemberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批流程指定成员记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service("memberService")
public class ExamineFlowMemberServiceImpl extends BaseServiceImpl<ExamineFlowMemberMapper, ExamineFlowMember> implements IExamineFlowMemberService, ExamineTypeService {

    /**
     * 保存额外的审批流程data对象
     *
     * @param dataSaveBO data
     * @param flowId     审批流程ID
     */
    @Override
    public void saveExamineFlowData(ExamineDataSaveBO dataSaveBO, Integer flowId, String batchId) {
        List<ExamineFlowMember> IExamineFlowMemberList = new ArrayList<>();
        int i = 0;
        for (Long userId : dataSaveBO.getUserList()) {
            ExamineFlowMember IExamineFlowMember = new ExamineFlowMember();
            IExamineFlowMember.setUserId(userId);
            IExamineFlowMember.setFlowId(flowId);
            IExamineFlowMember.setSort(i++);
            IExamineFlowMember.setType(dataSaveBO.getType());
            IExamineFlowMember.setBatchId(batchId);
            IExamineFlowMemberList.add(IExamineFlowMember);
        }
        saveBatch(IExamineFlowMemberList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 查询审批用户，不会存在条件审批的情况
     *
     * @param createUserId 创建人
     * @param recordId     审核记录ID
     * @param examineFlow  当前审批流程
     * @return data
     */
    public ExamineUserBO queryFlowUser(Long createUserId, Integer recordId, ExamineFlow examineFlow) {
        List<ExamineFlowMember> flowMembers = lambdaQuery().eq(ExamineFlowMember::getFlowId, examineFlow.getFlowId()).orderByAsc(ExamineFlowMember::getSort).list();
        ExamineUserBO examineUserBO = new ExamineUserBO();
        examineUserBO.setType(flowMembers.get(0).getType());
        List<Long> userIds = flowMembers.stream().map(ExamineFlowMember::getUserId).collect(Collectors.toList());
        examineUserBO.setUserList(handleUserList(userIds,examineFlow.getExamineId()));
        return examineUserBO;
    }

    /**
     * 通过batchId查询所有flow关联对象
     *
     * @param map     缓存对象
     * @param batchId batchId
     */
    @Override
    public void queryFlowListByBatchId(Map<String, Object> map, String batchId) {
        List<ExamineFlowMember> continuousSuperiors = lambdaQuery().eq(ExamineFlowMember::getBatchId, batchId).list();
        Map<Integer, List<ExamineFlowMember>> collect = continuousSuperiors.stream().collect(Collectors.groupingBy(ExamineFlowMember::getFlowId));
        map.put(ExamineTypeEnum.MEMBER.getServerName(), collect);
    }

    /**
     * 查询详情页需要的审批详情
     *
     * @param examineFlow 当前审批流程
     * @param map         缓存的map
     * @return data
     */
    @Override
    @SuppressWarnings("unchecked")
    public ExamineFlowVO createFlowInfo(ExamineFlow examineFlow, Map<String, Object> map, List<UserInfo> allUserList,Long ownerUserId) {
        Map<Integer, List<ExamineFlowMember>> collect = (Map<Integer, List<ExamineFlowMember>>) map.get(ExamineTypeEnum.MEMBER.getServerName());
        List<ExamineFlowMember> flowMembers = collect.get(examineFlow.getFlowId());
        if (flowMembers == null || flowMembers.size() == 0) {
            return null;
        }
        //排序
        flowMembers.sort(((f1, f2) -> f1.getSort() > f2.getSort() ? 1 : -1));
        ExamineFlowVO examineFlowVO = new ExamineFlowVO();
        examineFlowVO.setExamineType(examineFlow.getExamineType());
        examineFlowVO.setFlowId(examineFlow.getFlowId());
        examineFlowVO.setName(examineFlow.getName());
        examineFlowVO.setType(flowMembers.get(0).getType());
        examineFlowVO.setExamineErrorHandling(examineFlow.getExamineErrorHandling());
        List<SimpleUser> userList = new ArrayList<>();
        List<Long> userIds = flowMembers.stream().map(ExamineFlowMember::getUserId).collect(Collectors.toList());
        List<Long> userIdList = handleUserList(userIds,examineFlow.getExamineId());

        for (Long userId : userIdList) {
            for (UserInfo userInfo : allUserList) {
                if(userInfo.getUserId().equals(userId)){
                    userList.add(toSimPleUser(userInfo));
                    break;
                }
            }
        }

        examineFlowVO.setUserList(userList);
        return examineFlowVO;
    }
}
