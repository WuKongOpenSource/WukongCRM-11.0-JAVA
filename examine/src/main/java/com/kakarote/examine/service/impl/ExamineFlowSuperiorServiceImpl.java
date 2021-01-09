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
import com.kakarote.examine.entity.PO.ExamineFlowSuperior;
import com.kakarote.examine.entity.VO.ExamineFlowVO;
import com.kakarote.examine.mapper.ExamineFlowSuperiorMapper;
import com.kakarote.examine.service.ExamineTypeService;
import com.kakarote.examine.service.IExamineFlowSuperiorService;
import com.kakarote.examine.service.IExamineManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批流程主管审批记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service("superiorService")
public class ExamineFlowSuperiorServiceImpl extends BaseServiceImpl<ExamineFlowSuperiorMapper, ExamineFlowSuperior> implements IExamineFlowSuperiorService, ExamineTypeService {

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
        ExamineFlowSuperior IExamineFlowSuperior = new ExamineFlowSuperior();
        IExamineFlowSuperior.setParentLevel(dataSaveBO.getParentLevel());
        IExamineFlowSuperior.setFlowId(flowId);
        IExamineFlowSuperior.setType(dataSaveBO.getType());
        IExamineFlowSuperior.setBatchId(batchId);
        save(IExamineFlowSuperior);
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
        List<UserInfo> userInfoList = adminService.queryUserInfoList().getData();
        /*
        查询当前审批条件
         */
        ExamineFlowSuperior flowSuperior = lambdaQuery()
                .eq(ExamineFlowSuperior::getFlowId, examineFlow.getFlowId())
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
        ExamineUserBO examineUserBO = new ExamineUserBO();
        Integer examineErrorHandling = examineFlow.getExamineErrorHandling();
        List<Long> ids;
        if (userInfo.getParentId() == null || userInfo.getParentId() == 0){
            ids = new ArrayList<>();
        }else {
            List<Long> longList = queryUser(userInfoList, userInfo.getParentId());
            /*
            说明该员工并没有那么多上级
             */
            ids = new ArrayList<>();
            if (flowSuperior.getParentLevel() > longList.size()) {
                ids.add(longList.get(longList.size() - 1));
            } else {
                ids.add(longList.get(flowSuperior.getParentLevel() - 1));
            }
        }
        examineUserBO.setUserList(handleUserList(ids,examineFlow.getExamineId()));
        examineUserBO.setType(3);
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
        List<ExamineFlowSuperior> continuousSuperiors = lambdaQuery().eq(ExamineFlowSuperior::getBatchId, batchId).list();
        Map<Integer, List<ExamineFlowSuperior>> collect = continuousSuperiors.stream().collect(Collectors.groupingBy(ExamineFlowSuperior::getFlowId));
        map.put(ExamineTypeEnum.SUPERIOR.getServerName(), collect);
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
        Map<Integer, List<ExamineFlowSuperior>> collect = (Map<Integer, List<ExamineFlowSuperior>>) map.get(ExamineTypeEnum.SUPERIOR.getServerName());
        List<ExamineFlowSuperior> superiors = collect.get(examineFlow.getFlowId());
        if (superiors == null || superiors.size() == 0) {
            return null;
        }
        ExamineFlowSuperior continuousSuperior = superiors.get(0);
        List<SimpleUser> userList = new ArrayList<>();
        examineFlowVO.setExamineType(examineFlow.getExamineType());
        Integer examineErrorHandling = examineFlow.getExamineErrorHandling();
        examineFlowVO.setExamineErrorHandling(examineErrorHandling);
        examineFlowVO.setType(continuousSuperior.getType());
        List<Long> userIds;
        if (userInfo.getParentId() == null || userInfo.getParentId() == 0){
            userIds = new ArrayList<>();
        }else {
            List<Long> longList = queryUser(userInfoList, userInfo.getParentId());
            /*
            说明该员工并没有那么多上级
             */
            Long userId;
            if (continuousSuperior.getParentLevel() > longList.size()) {
                userId = longList.get(longList.size() - 1);
            } else {
                userId = longList.get(continuousSuperior.getParentLevel() - 1);
            }
            userIds = new ArrayList<>();
            userIds.add(userId);
        }
        Long userId = handleUserList(userIds,examineFlow.getExamineId()).get(0);
        for (UserInfo info : userInfoList) {
            if (userId.equals(info.getUserId())) {
                userList.add(toSimPleUser(info));
                break;
            }
        }
        examineFlowVO.setFlowId(examineFlow.getFlowId());
        examineFlowVO.setName(examineFlow.getName());
        examineFlowVO.setParentLevel(continuousSuperior.getParentLevel());
        examineFlowVO.setUserList(userList);
        return examineFlowVO;
    }

    private List<Long> queryUser(List<UserInfo> userInfoList, Long userId) {
        List<Long> idList = new ArrayList<>();
        //管理员没有上级
        if (userId == null || userId == 0) {
            idList.add(userId);
            return idList;
        }
        for (UserInfo userInfo : userInfoList) {
            if (userId.equals(userInfo.getUserId())) {
                idList.add(userId);
                if (userInfo.getParentId() == null || userInfo.getParentId() == 0) {
                    return idList;
                }
                idList.addAll(queryUser(userInfoList, userInfo.getParentId()));
            }
        }
        return idList;
    }
}
