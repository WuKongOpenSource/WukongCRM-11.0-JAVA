package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.hrm.constant.CandidateStatusEnum;
import com.kakarote.hrm.constant.HrmActionBehaviorEnum;
import com.kakarote.hrm.constant.MenuIdConstant;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.entity.PO.HrmRecruitCandidate;
import com.kakarote.hrm.entity.PO.HrmRecruitInterview;
import com.kakarote.hrm.entity.VO.CandidatePageListVO;
import com.kakarote.hrm.mapper.HrmRecruitCandidateMapper;
import com.kakarote.hrm.service.IHrmEmployeeService;
import com.kakarote.hrm.service.IHrmRecruitCandidateService;
import com.kakarote.hrm.service.IHrmRecruitInterviewService;
import com.kakarote.hrm.service.actionrecord.impl.CandidateActionRecordServiceImpl;
import com.kakarote.hrm.utils.EmployeeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 招聘候选人表 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmRecruitCandidateServiceImpl extends BaseServiceImpl<HrmRecruitCandidateMapper, HrmRecruitCandidate> implements IHrmRecruitCandidateService {

    @Autowired
    private HrmRecruitCandidateMapper recruitCandidateMapper;

    @Resource
    private CandidateActionRecordServiceImpl candidateActionRecordService;

    @Autowired
    private IHrmRecruitInterviewService recruitInterviewService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private EmployeeUtil employeeUtil;
    @Autowired
    private IHrmEmployeeService employeeService;

    @Override
    public BasePage<CandidatePageListVO> queryCandidateList(QueryCandidatePageListBO queryCandidatePageListBO) {
        Collection<Integer> deptIds = employeeUtil.queryDataAuthDeptIdByMenuId(MenuIdConstant.RECRUIT_MENU_ID);
        BasePage<CandidatePageListVO> page = recruitCandidateMapper.queryPageList(queryCandidatePageListBO.parse(), queryCandidatePageListBO, deptIds);
        page.getList().forEach(obj -> {
            SimpleUser data = UserCacheUtil.getSimpleUser(obj.getCreateUserId());
            obj.setCreateUserName(data.getRealname());
            if(obj.getStatus()<CandidateStatusEnum.ARRANGE_AN_INTERVIEW.getValue()){
                obj.setInterviewEmployeeId(null);
                obj.setOtherInterviewEmployeeIds(null);
                obj.setInterviewTime(null);
                obj.setInterviewEmployeeName(null);
                obj.setOtherInterviewEmployeeName(null);
                obj.setStageNum(0);
            }
            Optional<HrmRecruitInterview> hrmInterview = recruitInterviewService.lambdaQuery().eq(HrmRecruitInterview::getCandidateId,obj.getCandidateId()).orderByDesc(HrmRecruitInterview::getCreateTime).last("limit 1").oneOpt();
            if(hrmInterview.isPresent()){
                obj.setInterviewResult(hrmInterview.get().getResult());
            }else {
                obj.setInterviewResult(1);
            }
        });
        return page;
    }

    @Override
    public CandidatePageListVO queryById(String candidateId) {
        return recruitCandidateMapper.getById(candidateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCandidate(HrmRecruitCandidate hrmRecruitCandidate) {
        String batchId = StrUtil.isEmpty(hrmRecruitCandidate.getBatchId()) ? IdUtil.simpleUUID() : hrmRecruitCandidate.getBatchId();
        hrmRecruitCandidate.setBatchId(batchId);
        hrmRecruitCandidate.setStatusUpdateTime(new Date());
        save(hrmRecruitCandidate);
        candidateActionRecordService.addOrDeleteRecord(HrmActionBehaviorEnum.ADD, hrmRecruitCandidate.getCandidateId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setCandidate(HrmRecruitCandidate hrmRecruitCandidate) {
        candidateActionRecordService.entityUpdateRecord(BeanUtil.beanToMap(getById(hrmRecruitCandidate.getCandidateId())), BeanUtil.beanToMap(hrmRecruitCandidate), hrmRecruitCandidate.getCandidateId(), hrmRecruitCandidate.getCandidateName());
        updateById(hrmRecruitCandidate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer candidateId) {
        removeById(candidateId);
        recruitInterviewService.lambdaUpdate().eq(HrmRecruitInterview::getCandidateId, candidateId).remove();
        candidateActionRecordService.addOrDeleteRecord(HrmActionBehaviorEnum.DELETE, candidateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCandidateStatus(UpdateCandidateStatusBO updateCandidateStatusBO) {
        if (CollUtil.isEmpty(updateCandidateStatusBO.getCandidateIds())) {
            return;
        }
        candidateActionRecordService.updateCandidateStatusRecord(updateCandidateStatusBO);
        Integer status = updateCandidateStatusBO.getStatus();
        List<Integer> candidateIds = updateCandidateStatusBO.getCandidateIds();
        lambdaUpdate().set(HrmRecruitCandidate::getStatus, status)
                .set(HrmRecruitCandidate::getStatusUpdateTime, new Date())
                .in(HrmRecruitCandidate::getCandidateId, candidateIds)
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCandidatePost(UpdateCandidatePostBO updateCandidatePostBO) {
        candidateActionRecordService.updateCandidatePostRecord(updateCandidatePostBO);
        lambdaUpdate().set(HrmRecruitCandidate::getPostId, updateCandidatePostBO.getPostId())
                .in(HrmRecruitCandidate::getCandidateId, updateCandidatePostBO.getCandidateIds())
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCandidateRecruitChannel(UpdateCandidateRecruitChannelBO updateCandidateRecruitChannelBO) {
        candidateActionRecordService.updateCandidateRecruitChannel(updateCandidateRecruitChannelBO);
        lambdaUpdate().set(HrmRecruitCandidate::getChannelId, updateCandidateRecruitChannelBO.getChannelId())
                .in(HrmRecruitCandidate::getCandidateId, updateCandidateRecruitChannelBO.getCandidateIds())
                .update();
    }

    @Override
    public List<Integer> queryCleanCandidateIds(QueryCleanCandidateBO queryCleanCandidateBO) {
        QueryWrapper<HrmRecruitCandidate> wrapper = new QueryWrapper<HrmRecruitCandidate>().select("candidate_id")
                .in("status", queryCleanCandidateBO.getStatus())
                .apply("to_days(now()) - to_days(status_update_time) > {0}", queryCleanCandidateBO.getDay());
        return listObjs(wrapper, o -> Integer.valueOf(o.toString()));
    }

    @Override
    public Result<List<FileEntity>> queryFile(Integer candidateId) {
        HrmRecruitCandidate candidate = getById(candidateId);
        return adminFileService.queryFileList(candidate.getBatchId());
    }

    @Override
    public Map<Integer, Long> queryCandidateStatusNum() {
        TreeMap<Integer, Long> collect = new TreeMap<>();
        Collection<Integer> deptIds = employeeUtil.queryDataAuthDeptIdByMenuId(MenuIdConstant.RECRUIT_MENU_ID);
        List<Map<String, Object>> statusMap = getBaseMapper().queryDataAuthCandidateStatus(deptIds);
        for (Map<String, Object> map : statusMap) {
            collect.put((Integer) map.get("status"), (Long) map.get("count"));
        }
        for (CandidateStatusEnum value : CandidateStatusEnum.values()) {
            if (!collect.containsKey(value.getValue())) {
                collect.put(value.getValue(), 0L);
            }
        }
        return collect;
    }

    @Override
    public void eliminateCandidate(EliminateCandidateBO eliminateCandidateBO) {
        lambdaUpdate().set(HrmRecruitCandidate::getEliminate, eliminateCandidateBO.getEliminate())
                .set(HrmRecruitCandidate::getRemark, eliminateCandidateBO.getRemarks())
                .set(HrmRecruitCandidate::getStatusUpdateTime, new Date())
                .set(HrmRecruitCandidate::getStatus, CandidateStatusEnum.OBSOLETE.getValue())
                .in(HrmRecruitCandidate::getCandidateId, eliminateCandidateBO.getCandidateIds())
                .update();
        employeeService.lambdaUpdate().set(HrmEmployee::getIsDel, 1).in(HrmEmployee::getCandidateId,eliminateCandidateBO.getCandidateIds()).update();
        candidateActionRecordService.eliminateCandidateBORecord(eliminateCandidateBO);
    }

    @Override
    public void deleteByIds(List<Integer> candidateIds) {
        removeByIds(candidateIds);
        recruitInterviewService.lambdaUpdate().in(HrmRecruitInterview::getCandidateId, candidateIds).remove();
    }

    @Override
    public List<Map<String, Object>> queryRecruitListByTime(Date time, Collection<Integer> deptIds) {
        return recruitCandidateMapper.queryRecruitListByTime(time, deptIds);
    }

    @Override
    public Set<String> queryRecruitStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> deptIds) {
        return recruitCandidateMapper.queryRecruitStatusList(queryNotesStatusBO, deptIds);
    }
}
