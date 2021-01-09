package com.kakarote.examine.service.impl;

import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.examine.constant.ExamineTypeEnum;
import com.kakarote.examine.entity.BO.ExamineDataSaveBO;
import com.kakarote.examine.entity.BO.ExamineUserBO;
import com.kakarote.examine.entity.PO.ExamineFlow;
import com.kakarote.examine.entity.PO.ExamineFlowContinuousSuperior;
import com.kakarote.examine.entity.VO.ExamineFlowVO;
import com.kakarote.examine.mapper.ExamineFlowContinuousSuperiorMapper;
import com.kakarote.examine.service.ExamineTypeService;
import com.kakarote.examine.service.IExamineFlowContinuousSuperiorService;
import com.kakarote.examine.service.IExamineManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批流程连续多级主管审批记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service("continuousSuperiorService")
public class ExamineFlowContinuousSuperiorServiceImpl extends BaseServiceImpl<ExamineFlowContinuousSuperiorMapper, ExamineFlowContinuousSuperior> implements IExamineFlowContinuousSuperiorService, ExamineTypeService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private IExamineManagerUserService examineManagerUserService;

    /**
     * 保存额外的审批流程data对象
     *
     * @param dataSaveBO data
     * @param flowId     审批流程ID
     */
    @Override
    public void saveExamineFlowData(ExamineDataSaveBO dataSaveBO, Integer flowId, String batchId) {
        ExamineFlowContinuousSuperior IExamineFlowContinuousSuperior = new ExamineFlowContinuousSuperior();
        IExamineFlowContinuousSuperior.setMaxLevel(dataSaveBO.getParentLevel());
        IExamineFlowContinuousSuperior.setFlowId(flowId);
        IExamineFlowContinuousSuperior.setRoleId(dataSaveBO.getRoleId());
        IExamineFlowContinuousSuperior.setType(dataSaveBO.getType());
        IExamineFlowContinuousSuperior.setBatchId(batchId);
        save(IExamineFlowContinuousSuperior);
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
        ExamineUserBO examineUserBO = new ExamineUserBO();
        List<UserInfo> userInfoList = adminService.queryUserInfoList().getData();
        /*
        查询当前审批条件
         */
        ExamineFlowContinuousSuperior continuousSuperior = lambdaQuery()
                .eq(ExamineFlowContinuousSuperior::getFlowId, examineFlow.getFlowId())
                .last(" limit 1").one();
        UserInfo userInfo = null;
        /*
        获取当前审核人
         */
        for (UserInfo info : userInfoList) {
            if (info.getUserId().equals(createUserId)) {
                userInfo = info;
            }
        }
        /*
        当前审核人肯定会存在于所有员工内
         */
        if (userInfo == null) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        Integer examineErrorHandling = examineFlow.getExamineErrorHandling();
        List<Long> userList;
        if (userInfo.getParentId() == null || userInfo.getParentId() == 0){
            userList = new ArrayList<>();
            examineUserBO.setType(3);
        }else {
            userList = queryUser(userInfoList, continuousSuperior.getMaxLevel(), continuousSuperior.getRoleId(), userInfo.getParentId());
            /*
             相当于多人依次审批
            */
            examineUserBO.setType(1);
        }
        examineUserBO.setUserList(handleUserList(userList,examineFlow.getExamineId()));
        return examineUserBO;
    }

    /**
     * 通过batchId查询所有flow关联对象
     *
     * @param batchId batchId
     */
    @Override
    public void queryFlowListByBatchId(Map<String, Object> map, String batchId) {
        List<ExamineFlowContinuousSuperior> continuousSuperiors = lambdaQuery().eq(ExamineFlowContinuousSuperior::getBatchId, batchId).list();
        Map<Integer, List<ExamineFlowContinuousSuperior>> collect = continuousSuperiors.stream().collect(Collectors.groupingBy(ExamineFlowContinuousSuperior::getFlowId));
        map.put(ExamineTypeEnum.CONTINUOUS_SUPERIOR.getServerName(), collect);
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
        UserInfo userInfo;
        if (ownerUserId != null){
            userInfo = adminService.getUserInfo(ownerUserId).getData();
        }else {
            userInfo = adminService.getUserInfo(UserUtil.getUserId()).getData();
        }
        ExamineFlowVO examineFlowVO = new ExamineFlowVO();
        Map<Integer, List<ExamineFlowContinuousSuperior>> collect = (Map<Integer, List<ExamineFlowContinuousSuperior>>) map.get(ExamineTypeEnum.CONTINUOUS_SUPERIOR.getServerName());
        List<ExamineFlowContinuousSuperior> superiors = collect.get(examineFlow.getFlowId());
        if (superiors == null || superiors.size() == 0) {
            return null;
        }
        ExamineFlowContinuousSuperior continuousSuperior = superiors.get(0);
        examineFlowVO.setExamineType(examineFlow.getExamineType());
        Integer examineErrorHandling = examineFlow.getExamineErrorHandling();
        examineFlowVO.setExamineErrorHandling(examineErrorHandling);
        List<Long> queryUser;
        if (userInfo.getParentId() == null || userInfo.getParentId() == 0){
            queryUser = new ArrayList<>();
        }else {
            queryUser = queryUser(userInfoList, continuousSuperior.getMaxLevel(), continuousSuperior.getRoleId(), userInfo.getParentId());
        }
        queryUser = handleUserList(queryUser,examineFlow.getExamineId());
        examineFlowVO.setType(continuousSuperior.getType());
        examineFlowVO.setFlowId(examineFlow.getFlowId());
        examineFlowVO.setName(examineFlow.getName());
        examineFlowVO.setParentLevel(continuousSuperior.getMaxLevel());
        examineFlowVO.setRoleId(continuousSuperior.getRoleId());
        List<SimpleUser> userList = new ArrayList<>();
        for (Long userId : queryUser) {
            for (UserInfo info : userInfoList) {
                if(info.getUserId().equals(userId)){
                    userList.add(toSimPleUser(info));
                    break;
                }
            }
        }
        examineFlowVO.setUserList(userList);
        return examineFlowVO;
    }

    private List<Long> queryUser(List<UserInfo> userInfoList, Integer maxLevel, Integer roleId, Long userId) {
        List<Long> idList = new ArrayList<>();
        if (maxLevel != null) {
            if (maxLevel < 0) {
                return idList;
            }
            maxLevel--;
        }
        for (UserInfo userInfo : userInfoList) {
            if (userId.equals(userInfo.getUserId())) {
                idList.add(userId);
                if (userInfo.getRoles().contains(roleId)) {
                    return idList;
                }
                if (userInfo.getParentId() == null || userInfo.getParentId() == 0) {
                    return idList;
                }
                idList.addAll(queryUser(userInfoList, maxLevel, roleId, userInfo.getParentId()));
            }
        }
        return idList;
    }
}
