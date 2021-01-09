package com.kakarote.examine.service.impl;

import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.examine.constant.ExamineTypeEnum;
import com.kakarote.examine.entity.BO.ExamineDataSaveBO;
import com.kakarote.examine.entity.BO.ExamineUserBO;
import com.kakarote.examine.entity.PO.ExamineFlow;
import com.kakarote.examine.entity.PO.ExamineFlowRole;
import com.kakarote.examine.entity.VO.ExamineFlowVO;
import com.kakarote.examine.mapper.ExamineFlowRoleMapper;
import com.kakarote.examine.service.ExamineTypeService;
import com.kakarote.examine.service.IExamineFlowRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批流程角色审批记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service("roleService")
public class ExamineFlowRoleServiceImpl extends BaseServiceImpl<ExamineFlowRoleMapper, ExamineFlowRole> implements IExamineFlowRoleService, ExamineTypeService {

    @Autowired
    private AdminService adminService;


    /**
     * 保存额外的审批流程data对象
     *
     * @param dataSaveBO data
     * @param flowId     审批流程ID
     */
    @Override
    public void saveExamineFlowData(ExamineDataSaveBO dataSaveBO, Integer flowId, String batchId) {
        ExamineFlowRole IExamineFlowRole = new ExamineFlowRole();
        IExamineFlowRole.setFlowId(flowId);
        IExamineFlowRole.setRoleId(dataSaveBO.getRoleId());
        IExamineFlowRole.setType(dataSaveBO.getType());
        IExamineFlowRole.setBatchId(batchId);
        save(IExamineFlowRole);
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
        ExamineFlowRole examineFlowRole = lambdaQuery().eq(ExamineFlowRole::getFlowId, examineFlow.getFlowId()).last(" limit 1").one();
        ExamineUserBO examineUserBO = new ExamineUserBO();
        examineUserBO.setType(examineFlowRole.getType());
        examineUserBO.setRoleId(examineFlowRole.getRoleId());
        List<Long> userList = new ArrayList<>();
        List<UserInfo> userInfoList = adminService.queryUserInfoList().getData();
        for (UserInfo userInfo : userInfoList) {
            if (userInfo.getRoles().contains(examineFlowRole.getRoleId())) {
                userList.add(userInfo.getUserId());
            }
        }
        examineUserBO.setUserList(handleUserList(userList,examineFlow.getExamineId()));
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
        List<ExamineFlowRole> continuousSuperiors = lambdaQuery().eq(ExamineFlowRole::getBatchId, batchId).list();
        Map<Integer, List<ExamineFlowRole>> collect = continuousSuperiors.stream().collect(Collectors.groupingBy(ExamineFlowRole::getFlowId));
        map.put(ExamineTypeEnum.ROLE.getServerName(), collect);
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
    public ExamineFlowVO createFlowInfo(ExamineFlow examineFlow, Map<String, Object> map, List<UserInfo> userInfoList,Long ownerUserId) {
        Map<Integer, List<ExamineFlowRole>> collect = (Map<Integer, List<ExamineFlowRole>>) map.get(ExamineTypeEnum.ROLE.getServerName());
        List<ExamineFlowRole> flowRoles = collect.get(examineFlow.getFlowId());
        if (flowRoles == null || flowRoles.size() == 0) {
            return null;
        }
        ExamineFlowRole examineFlowRole = flowRoles.get(0);
        ExamineFlowVO examineFlowVO = new ExamineFlowVO();
        examineFlowVO.setType(examineFlowRole.getType());
        examineFlowVO.setName(examineFlow.getName());
        examineFlowVO.setExamineType(examineFlow.getExamineType());
        examineFlowVO.setFlowId(examineFlow.getFlowId());
        examineFlowVO.setExamineErrorHandling(examineFlow.getExamineErrorHandling());
        examineFlowVO.setRoleId(examineFlowRole.getRoleId());
        List<SimpleUser> userList = new ArrayList<>();
        List<Long> userIdList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            if (userInfo.getRoles().contains(examineFlowRole.getRoleId())) {
                userIdList.add(userInfo.getUserId());
            }
        }
        userIdList = handleUserList(userIdList,examineFlow.getExamineId());
        for (Long userId : userIdList) {
            for (UserInfo userInfo : userInfoList) {
                if (Objects.equals(userInfo.getUserId(),userId)) {
                    userList.add(toSimPleUser(userInfo));
                    break;
                }
            }
        }
        examineFlowVO.setUserList(userList);
        return examineFlowVO;
    }
}
