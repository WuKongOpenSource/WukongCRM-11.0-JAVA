package com.kakarote.examine.service;

import cn.hutool.core.collection.CollUtil;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.examine.entity.BO.ExamineDataSaveBO;
import com.kakarote.examine.entity.BO.ExamineUserBO;
import com.kakarote.examine.entity.PO.ExamineFlow;
import com.kakarote.examine.entity.VO.ExamineFlowVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 审批类型service
 */
public interface ExamineTypeService {


    /**
     * 保存额外的审批流程data对象
     *
     * @param dataSaveBO data
     * @param flowId     审批流程ID
     */
    public void saveExamineFlowData(ExamineDataSaveBO dataSaveBO, Integer flowId, String batchId);


    /**
     * 查询审批用户，不会存在条件审批的情况
     *
     * @param createUserId 创建人
     * @param recordId     审核记录ID
     * @param examineFlow  当前审批流程
     * @return data
     */
    public ExamineUserBO queryFlowUser(Long createUserId, Integer recordId, ExamineFlow examineFlow);

    /**
     * 通过batchId查询所有flow关联对象
     *
     * @param map     缓存对象
     * @param batchId batchId
     */
    public void queryFlowListByBatchId(Map<String, Object> map, String batchId);


    /**
     * 查询详情页需要的审批详情
     * @param examineFlow 当前审批流程
     * @param map 缓存的map
     * @return data
     */
    public ExamineFlowVO createFlowInfo(ExamineFlow examineFlow, Map<String, Object> map, List<UserInfo> allUserList,Long ownerUserId);

    default public SimpleUser toSimPleUser(UserInfo userInfo){
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.setUserId(userInfo.getUserId());
        simpleUser.setRealname(userInfo.getRealname());
        simpleUser.setImg(userInfo.getImg());
        return simpleUser;
    }


    default public List<Long> handleUserList(List<Long> userIds,Long examineId){
        List<Long> userList = new ArrayList<>();
        if (CollUtil.isNotEmpty(userIds)){
            AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);
            userList = adminService.queryNormalUserByIds(userIds).getData();
        }
        if (userList.size() == 0){
            IExamineManagerUserService examineManagerUserService = ApplicationContextHolder.getBean(IExamineManagerUserService.class);
            userList = examineManagerUserService.queryExamineUser(examineId);
        }
        return userList;
    }


}
