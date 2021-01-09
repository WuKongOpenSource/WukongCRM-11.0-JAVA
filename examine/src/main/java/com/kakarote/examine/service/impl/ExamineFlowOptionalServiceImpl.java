package com.kakarote.examine.service.impl;

import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.examine.constant.ExamineCodeEnum;
import com.kakarote.examine.constant.ExamineTypeEnum;
import com.kakarote.examine.entity.BO.ExamineDataSaveBO;
import com.kakarote.examine.entity.BO.ExamineUserBO;
import com.kakarote.examine.entity.PO.ExamineFlow;
import com.kakarote.examine.entity.PO.ExamineFlowOptional;
import com.kakarote.examine.entity.PO.ExamineRecordOptional;
import com.kakarote.examine.entity.VO.ExamineFlowVO;
import com.kakarote.examine.mapper.ExamineFlowOptionalMapper;
import com.kakarote.examine.service.ExamineTypeService;
import com.kakarote.examine.service.IExamineFlowOptionalService;
import com.kakarote.examine.service.IExamineRecordOptionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批流程自选成员记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service("optionalService")
public class ExamineFlowOptionalServiceImpl extends BaseServiceImpl<ExamineFlowOptionalMapper, ExamineFlowOptional> implements IExamineFlowOptionalService, ExamineTypeService {

    @Autowired
    private IExamineRecordOptionalService examineRecordOptionalService;

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
        Integer rangeType = dataSaveBO.getRangeType();
        if (Objects.equals(rangeType,3)) {
            Integer roleId = dataSaveBO.getRoleId();
            if (roleId != null) {
//                adminService.queryUserIdByRealName()
                List<UserInfo> userInfoList = adminService.queryUserInfoList().getData();
                boolean isNoUser = true;
                for (UserInfo userInfo : userInfoList) {
                    if (userInfo.getRoles().contains(roleId)) {
                        isNoUser = false;
                        break;
                    }
                }
                if (isNoUser){
                    throw new CrmException(ExamineCodeEnum.EXAMINE_ROLE_NO_USER_ERROR);
                }
                ExamineFlowOptional examineFlowOptional = new ExamineFlowOptional();
                examineFlowOptional.setChooseType(dataSaveBO.getChooseType());
                examineFlowOptional.setFlowId(flowId);
                examineFlowOptional.setRoleId(dataSaveBO.getRoleId());
                examineFlowOptional.setSort(1);
                examineFlowOptional.setType(dataSaveBO.getType());
                examineFlowOptional.setRangeType(dataSaveBO.getRangeType());
                examineFlowOptional.setBatchId(batchId);
                save(examineFlowOptional);
            }
        }else if (Objects.equals(rangeType,2)){
            if (dataSaveBO.getUserList().size() == 0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
            }
            for (Long userId : dataSaveBO.getUserList()) {
                ExamineFlowOptional examineFlowOptional = new ExamineFlowOptional();
                examineFlowOptional.setChooseType(dataSaveBO.getChooseType());
                examineFlowOptional.setFlowId(flowId);
                examineFlowOptional.setUserId(userId);
                examineFlowOptional.setSort(1);
                examineFlowOptional.setType(dataSaveBO.getType());
                examineFlowOptional.setRangeType(dataSaveBO.getRangeType());
                examineFlowOptional.setBatchId(batchId);
                save(examineFlowOptional);
            }
        }else {
            //全公司
            ExamineFlowOptional examineFlowOptional = new ExamineFlowOptional();
            examineFlowOptional.setChooseType(dataSaveBO.getChooseType());
            examineFlowOptional.setFlowId(flowId);
            examineFlowOptional.setSort(1);
            examineFlowOptional.setType(dataSaveBO.getType());
            examineFlowOptional.setRangeType(dataSaveBO.getRangeType());
            examineFlowOptional.setBatchId(batchId);
            save(examineFlowOptional);
        }

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
        List<ExamineFlowOptional> examineFlowOptionalList = lambdaQuery().eq(ExamineFlowOptional::getFlowId, examineFlow.getFlowId()).orderByAsc(ExamineFlowOptional::getSort).list();
        ExamineUserBO examineUserBO = new ExamineUserBO();
        examineUserBO.setType(examineFlowOptionalList.get(0).getType());
        if (recordId != null) {
           /*
          查询用户提交审批时设置的审批成员
         */
            List<ExamineRecordOptional> recordOptionals = examineRecordOptionalService.lambdaQuery()
                    .select(ExamineRecordOptional::getUserId)
                    .eq(ExamineRecordOptional::getFlowId, examineFlow.getFlowId())
                    .eq(ExamineRecordOptional::getRecordId, recordId)
                    .orderByAsc(ExamineRecordOptional::getSort)
                    .list();
            List<Long> userIds = recordOptionals.stream().map(ExamineRecordOptional::getUserId).collect(Collectors.toList());
            examineUserBO.setUserList(handleUserList(userIds,examineFlow.getExamineId()));
        }
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
        List<ExamineFlowOptional> continuousSuperiors = lambdaQuery().eq(ExamineFlowOptional::getBatchId, batchId).list();
        Map<Integer, List<ExamineFlowOptional>> collect = continuousSuperiors.stream().collect(Collectors.groupingBy(ExamineFlowOptional::getFlowId));
        map.put(ExamineTypeEnum.OPTIONAL.getServerName(), collect);
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
        Map<Integer, List<ExamineFlowOptional>> collect = (Map<Integer, List<ExamineFlowOptional>>) map.get(ExamineTypeEnum.OPTIONAL.getServerName());
        List<ExamineFlowOptional> flowOptionals = collect.get(examineFlow.getFlowId());
        if (flowOptionals == null || flowOptionals.size() == 0) {
            return null;
        }
        ExamineFlowVO examineFlowVO = new ExamineFlowVO();
        examineFlowVO.setChooseType(flowOptionals.get(0).getChooseType());
        examineFlowVO.setName(examineFlow.getName());
        examineFlowVO.setExamineType(examineFlow.getExamineType());
        examineFlowVO.setFlowId(examineFlow.getFlowId());
        examineFlowVO.setType(flowOptionals.get(0).getType());
        examineFlowVO.setRangeType(flowOptionals.get(0).getRangeType());
        examineFlowVO.setExamineErrorHandling(examineFlow.getExamineErrorHandling());
        examineFlowVO.setRoleId(flowOptionals.get(0).getRoleId());
        List<SimpleUser> userList = new ArrayList<>();
        Integer rangeType = flowOptionals.get(0).getRangeType();
        if (Objects.equals(rangeType,3)) {
            //按照角色选择
            Integer roleId = flowOptionals.get(0).getRoleId();
            if (roleId != null) {
                for (UserInfo userInfo : userInfoList) {
                    if (userInfo.getRoles().contains(roleId)) {
                        userList.add(toSimPleUser(userInfo));
                    }
                }
            }
        }else if (Objects.equals(rangeType,2)){
            for (ExamineFlowOptional flowOptional : flowOptionals) {
                for (UserInfo userInfo : userInfoList) {
                    if (flowOptional.getUserId().equals(userInfo.getUserId())) {
                        userList.add(toSimPleUser(userInfo));
                        break;
                    }
                }
            }
        }
        examineFlowVO.setUserList(userList);
        return examineFlowVO;
    }


}
