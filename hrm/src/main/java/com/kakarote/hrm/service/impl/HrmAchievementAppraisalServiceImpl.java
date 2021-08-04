package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.TransferUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.EmployeeEntryStatus;
import com.kakarote.hrm.constant.IsEnum;
import com.kakarote.hrm.constant.MenuIdConstant;
import com.kakarote.hrm.constant.achievement.AppraisalEmployeeType;
import com.kakarote.hrm.constant.achievement.AppraisalStatus;
import com.kakarote.hrm.constant.achievement.EmployeeAppraisalStatus;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.*;
import com.kakarote.hrm.mapper.HrmAchievementAppraisalMapper;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.service.actionrecord.impl.EmployeeAppraisalActionRecordServiceImpl;
import com.kakarote.hrm.utils.AchievementUtil;
import com.kakarote.hrm.utils.EmployeeCacheUtil;
import com.kakarote.hrm.utils.EmployeeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 绩效考核 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmAchievementAppraisalServiceImpl extends BaseServiceImpl<HrmAchievementAppraisalMapper, HrmAchievementAppraisal> implements IHrmAchievementAppraisalService {

    @Autowired
    private IHrmAchievementAppraisalTargetConfirmorsService targetConfirmorsService;

    @Autowired
    private IHrmAchievementAppraisalEvaluatorsService evaluatorsService;

    @Autowired
    private IHrmAchievementAppraisalScoreLevelService scoreLevelService;

    @Autowired
    private IHrmAchievementEmployeeAppraisalService employeeAppraisalService;

    @Autowired
    private IHrmAchievementEmployeeEvaluatoService employeeEvaluatoService;

    @Autowired
    private IHrmAchievementEmployeeResultConfirmorsService employeeResultConfirmorsService;

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private HrmAchievementAppraisalMapper appraisalMapper;

    @Autowired
    private IHrmAchievementEmployeeSegService employeeSegService;

    @Resource
    private EmployeeAppraisalActionRecordServiceImpl employeeAppraisalActionRecordService;

    @Autowired
    private IHrmAchievementEmployeeEvaluatoSegService employeeEvaluatoSegService;

    @Autowired
    private IHrmAchievementEmployeeSegItemService employeeSegItemService;

    @Autowired
    private AchievementUtil achievementUtil;

    @Autowired
    private EmployeeUtil employeeUtil;
    @Autowired
    private AdminMessageService adminMessageService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAppraisal(SetAppraisalBO setAppraisalBO) {
        HrmAchievementAppraisal achievementAppraisal = BeanUtil.copyProperties(setAppraisalBO, HrmAchievementAppraisal.class);
        achievementAppraisal.setResultConfirmors(TagUtil.fromSet(setAppraisalBO.getResultConfirmors()));
        achievementAppraisal.setDeptIds(TagUtil.fromSet(setAppraisalBO.getDeptIds()));
        achievementAppraisal.setEmployeeIds(TagUtil.fromSet(setAppraisalBO.getEmployeeIds()));
        achievementAppraisal.setStatus(0);
        save(achievementAppraisal);
        saveOther(setAppraisalBO, achievementAppraisal.getAppraisalId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAppraisal(SetAppraisalBO setAppraisalBO) {
        HrmAchievementAppraisal achievementAppraisal = BeanUtil.copyProperties(setAppraisalBO, HrmAchievementAppraisal.class);
        Integer appraisalId = achievementAppraisal.getAppraisalId();
        HrmAchievementAppraisal nowAppraisal = getById(appraisalId);
        if (nowAppraisal.getStatus().equals(AppraisalStatus.FILLING_IN.getValue())) {
            List<HrmAchievementAppraisalTargetConfirmors> oldTargetConfirmorsList = targetConfirmorsService.lambdaQuery().eq(HrmAchievementAppraisalTargetConfirmors::getAppraisalId, appraisalId)
                    .orderByAsc(HrmAchievementAppraisalTargetConfirmors::getSort).list();
            List<HrmAchievementAppraisalTargetConfirmors> newTargetConfirmorsList = setAppraisalBO.getTargetConfirmorsList();
            for (int i = 0; i < oldTargetConfirmorsList.size(); i++) {
                HrmAchievementAppraisalTargetConfirmors oldAppraisalTargetConfirmors = oldTargetConfirmorsList.get(i);
                HrmAchievementAppraisalTargetConfirmors newAppraisalTargetConfirmors = newTargetConfirmorsList.get(i);
                if (oldAppraisalTargetConfirmors.getType() == AppraisalEmployeeType.DESIGNATION.getValue() &&
                        !oldAppraisalTargetConfirmors.getEmployeeId().equals(newAppraisalTargetConfirmors.getEmployeeId())) {
                    employeeAppraisalService.lambdaUpdate().set(HrmAchievementEmployeeAppraisal::getFollowUpEmployeeId, newAppraisalTargetConfirmors.getEmployeeId())
                            .eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.PENDING_CONFIRMATION.getValue())
                            .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalId)
                            .eq(HrmAchievementEmployeeAppraisal::getFollowUpEmployeeId, oldAppraisalTargetConfirmors.getEmployeeId())
                            .update();
                }
            }
        }
        if (nowAppraisal.getStatus().equals(AppraisalStatus.UNDER_EVALUATION.getValue())) {
            List<HrmAchievementAppraisalEvaluators> oldEvaluatorsList = evaluatorsService.lambdaQuery().eq(HrmAchievementAppraisalEvaluators::getAppraisalId, appraisalId)
                    .orderByAsc(HrmAchievementAppraisalEvaluators::getSort).list();
            List<HrmAchievementAppraisalEvaluators> newEvaluatorsList = setAppraisalBO.getEvaluatorsList();
            for (int i = 0; i < oldEvaluatorsList.size(); i++) {
                HrmAchievementAppraisalEvaluators oldAppraisalEvaluators = oldEvaluatorsList.get(i);
                HrmAchievementAppraisalEvaluators newAppraisalEvaluators = newEvaluatorsList.get(i);
                if (oldAppraisalEvaluators.getType() == AppraisalEmployeeType.DESIGNATION.getValue() &&
                        !oldAppraisalEvaluators.getEmployeeId().equals(newAppraisalEvaluators.getEmployeeId())) {
                    employeeAppraisalService.lambdaUpdate().set(HrmAchievementEmployeeAppraisal::getFollowUpEmployeeId, newAppraisalEvaluators.getEmployeeId())
                            .eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.TO_BE_ASSESSED.getValue())
                            .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalId)
                            .eq(HrmAchievementEmployeeAppraisal::getFollowUpEmployeeId, oldAppraisalEvaluators.getEmployeeId())
                            .update();
                    employeeEvaluatoService.lambdaUpdate().set(HrmAchievementEmployeeEvaluato::getEmployeeId, newAppraisalEvaluators.getEmployeeId())
                            .eq(HrmAchievementEmployeeEvaluato::getAppraisalId, appraisalId)
                            .eq(HrmAchievementEmployeeEvaluato::getStatus, 0)
                            .eq(HrmAchievementEmployeeEvaluato::getEmployeeId, oldAppraisalEvaluators.getEmployeeId())
                            .eq(HrmAchievementEmployeeEvaluato::getSort, oldAppraisalEvaluators.getSort()).update();
                }
            }
        }
        if (nowAppraisal.getStatus().equals(AppraisalStatus.CONFIRMING.getValue())) {
            List<Integer> oldResultConfirmors = Lists.newArrayList(TagUtil.toSet(nowAppraisal.getResultConfirmors()));
            List<Integer> newResultConfirmors = Lists.newArrayList(setAppraisalBO.getResultConfirmors());
            Collection<Integer> intersection = CollUtil.intersection(oldResultConfirmors, newResultConfirmors);
            Collection<Integer> deleteList = CollUtil.disjunction(oldResultConfirmors, intersection);
            Collection<Integer> addList = CollUtil.disjunction(newResultConfirmors, intersection);
            Integer count = employeeResultConfirmorsService.lambdaQuery()
                    .eq(HrmAchievementEmployeeResultConfirmors::getAppraisalId, appraisalId)
                    .eq(HrmAchievementEmployeeResultConfirmors::getStatus, 1)
                    .count();
            if (count == intersection.size()){
                employeeAppraisalService.lambdaUpdate()
                        .set(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.COMPLETE.getValue())
                        .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalId).update();
            }
            if (CollUtil.isNotEmpty(deleteList)){
                employeeResultConfirmorsService.lambdaUpdate()
                        .eq(HrmAchievementEmployeeResultConfirmors::getAppraisalId, appraisalId)
                        .eq(HrmAchievementEmployeeResultConfirmors::getStatus, 0)
                        .in(HrmAchievementEmployeeResultConfirmors::getEmployeeId, deleteList)
                        .remove();
            }
            for (Integer add : addList) {
                HrmAchievementEmployeeResultConfirmors employeeResultConfirmors = new HrmAchievementEmployeeResultConfirmors();
                employeeResultConfirmors.setEmployeeId(add);
                employeeResultConfirmors.setAppraisalId(appraisalId);
                employeeResultConfirmorsService.save(employeeResultConfirmors);
            }
        }
        //未开启考核时，绩效考核设置可以编辑
        targetConfirmorsService.lambdaUpdate().eq(HrmAchievementAppraisalTargetConfirmors::getAppraisalId, appraisalId).remove();
        evaluatorsService.lambdaUpdate().eq(HrmAchievementAppraisalEvaluators::getAppraisalId, appraisalId).remove();
        achievementAppraisal.setResultConfirmors(TagUtil.fromSet(setAppraisalBO.getResultConfirmors()));
        achievementAppraisal.setDeptIds(TagUtil.fromSet(setAppraisalBO.getDeptIds()));
        achievementAppraisal.setEmployeeIds(TagUtil.fromSet(setAppraisalBO.getEmployeeIds()));
        updateById(achievementAppraisal);
        List<HrmAchievementAppraisalTargetConfirmors> targetConfirmorsList = setAppraisalBO.getTargetConfirmorsList();
        for (int i = 0; i < targetConfirmorsList.size(); i++) {
            HrmAchievementAppraisalTargetConfirmors targetConfirmors = targetConfirmorsList.get(i);
            targetConfirmors.setAppraisalId(appraisalId);
            targetConfirmors.setSort(i + 1);
            targetConfirmors.setTargetConfirmorsId(null);
        }
        targetConfirmorsService.saveBatch(targetConfirmorsList);
        List<HrmAchievementAppraisalEvaluators> evaluatorsList = setAppraisalBO.getEvaluatorsList();
        for (int i = 0; i < evaluatorsList.size(); i++) {
            HrmAchievementAppraisalEvaluators evaluators = evaluatorsList.get(i);
            evaluators.setAppraisalId(appraisalId);
            evaluators.setSort(i + 1);
            evaluators.setEvaluatorsId(null);
        }
        evaluatorsService.saveBatch(evaluatorsList);
        if (nowAppraisal.getStatus().equals(AppraisalStatus.UNOPENED.getValue())) {
            scoreLevelService.lambdaUpdate().eq(HrmAchievementAppraisalScoreLevel::getAppraisalId, appraisalId).remove();
            List<HrmAchievementAppraisalScoreLevel> scoreLevelList = setAppraisalBO.getScoreLevelList();
            for (int i = 0; i < scoreLevelList.size(); i++) {
                HrmAchievementAppraisalScoreLevel scoreLevel = scoreLevelList.get(i);
                scoreLevel.setAppraisalId(appraisalId);
                scoreLevel.setSort(i + 1);
                scoreLevel.setLevelId(null);
            }
            scoreLevelService.saveBatch(scoreLevelList);
        }
    }

    /**
     * 保存绩效其他数据(目标确认人,结果评定人,分数等级)
     */
    private void saveOther(SetAppraisalBO setAppraisalBO, Integer appraisalId) {
        List<HrmAchievementAppraisalTargetConfirmors> targetConfirmorsList = setAppraisalBO.getTargetConfirmorsList();
        for (int i = 0; i < targetConfirmorsList.size(); i++) {
            HrmAchievementAppraisalTargetConfirmors targetConfirmors = targetConfirmorsList.get(i);
            targetConfirmors.setAppraisalId(appraisalId);
            targetConfirmors.setSort(i + 1);
            targetConfirmors.setTargetConfirmorsId(null);
        }
        targetConfirmorsService.saveBatch(targetConfirmorsList);
        List<HrmAchievementAppraisalEvaluators> evaluatorsList = setAppraisalBO.getEvaluatorsList();
        for (int i = 0; i < evaluatorsList.size(); i++) {
            HrmAchievementAppraisalEvaluators evaluators = evaluatorsList.get(i);
            evaluators.setAppraisalId(appraisalId);
            evaluators.setSort(i + 1);
            evaluators.setEvaluatorsId(null);
        }
        evaluatorsService.saveBatch(evaluatorsList);
        List<HrmAchievementAppraisalScoreLevel> scoreLevelList = setAppraisalBO.getScoreLevelList();
        for (int i = 0; i < scoreLevelList.size(); i++) {
            HrmAchievementAppraisalScoreLevel scoreLevel = scoreLevelList.get(i);
            scoreLevel.setAppraisalId(appraisalId);
            scoreLevel.setSort(i + 1);
            scoreLevel.setLevelId(null);
        }
        scoreLevelService.saveBatch(scoreLevelList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAppraisal(Integer appraisalId) {
        HrmAchievementAppraisal appraisal = getById(appraisalId);
        if (!appraisal.getStatus().equals(AppraisalStatus.UNOPENED.getValue()) && !appraisal.getIsStop().equals(1)) {
            throw new CrmException(HrmCodeEnum.CAN_ONLY_DELETE_PERFORMANCES_THAT_HAVE_NOT_BEEN_EVALUATED);
        }
        removeById(appraisalId);
        targetConfirmorsService.lambdaUpdate().eq(HrmAchievementAppraisalTargetConfirmors::getAppraisalId, appraisalId).remove();
        evaluatorsService.lambdaUpdate().eq(HrmAchievementAppraisalEvaluators::getAppraisalId, appraisalId).remove();
        scoreLevelService.lambdaUpdate().eq(HrmAchievementAppraisalScoreLevel::getAppraisalId, appraisalId).remove();
        employeeResultConfirmorsService.lambdaUpdate().eq(HrmAchievementEmployeeResultConfirmors::getAppraisalId, appraisalId).remove();
        List<Integer> employeeAppraisalIdList = employeeAppraisalService.lambdaQuery().select(HrmAchievementEmployeeAppraisal::getEmployeeAppraisalId)
                .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalId).list()
                .stream().map(HrmAchievementEmployeeAppraisal::getEmployeeAppraisalId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(employeeAppraisalIdList)) {
            employeeEvaluatoService.lambdaUpdate().in(HrmAchievementEmployeeEvaluato::getEmployeeAppraisalId, employeeAppraisalIdList).remove();
            employeeEvaluatoSegService.lambdaUpdate().in(HrmAchievementEmployeeEvaluatoSeg::getEmployeeAppraisalId, employeeAppraisalIdList).remove();
            employeeSegService.lambdaUpdate().in(HrmAchievementEmployeeSeg::getEmployeeAppraisalId, employeeAppraisalIdList).remove();
            List<Integer> segIds = employeeSegService.lambdaQuery().select(HrmAchievementEmployeeSeg::getSegId)
                    .in(HrmAchievementEmployeeSeg::getEmployeeAppraisalId, employeeAppraisalIdList).list()
                    .stream().map(HrmAchievementEmployeeSeg::getSegId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(segIds)) {
                employeeSegItemService.lambdaUpdate().in(HrmAchievementEmployeeSegItem::getSegId, segIds).remove();
                employeeSegService.removeByIds(segIds);
            }
            employeeAppraisalService.removeByIds(employeeAppraisalIdList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopAppraisal(Integer appraisalId) {
        HrmAchievementAppraisal appraisal = new HrmAchievementAppraisal();
        appraisal.setAppraisalId(appraisalId);
        appraisal.setStopTime(new Date());
        appraisal.setIsStop(IsEnum.YES.getValue());
        updateById(appraisal);
        List<Integer> employeeAppraisalIds = employeeAppraisalService.lambdaQuery().select(HrmAchievementEmployeeAppraisal::getEmployeeAppraisalId)
                .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalId).list()
                .stream().map(HrmAchievementEmployeeAppraisal::getEmployeeAppraisalId).collect(Collectors.toList());
        employeeAppraisalService.lambdaUpdate().set(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.STOP.getValue())
                .in(HrmAchievementEmployeeAppraisal::getEmployeeAppraisalId, employeeAppraisalIds).update();
        employeeResultConfirmorsService.lambdaUpdate().eq(HrmAchievementEmployeeResultConfirmors::getAppraisalId, appraisalId).remove();
        employeeEvaluatoService.lambdaUpdate().eq(HrmAchievementEmployeeEvaluato::getAppraisalId, appraisalId).remove();
        employeeEvaluatoSegService.lambdaUpdate().in(HrmAchievementEmployeeEvaluatoSeg::getEmployeeAppraisalId, employeeAppraisalIds).remove();
        HrmAchievementAppraisal appraisal1 = getById(appraisalId);
        employeeAppraisalActionRecordService.stopAppraisal(appraisal1.getAppraisalName(),employeeAppraisalIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAppraisalStatus(UpdateAppraisalStatusBO updateAppraisalStatusBO) {
        HrmAchievementAppraisal appraisal = getById(updateAppraisalStatusBO.getAppraisalId());
        Integer oldAppraisalStatus = appraisal.getStatus();
        lambdaUpdate().set(HrmAchievementAppraisal::getStatus, updateAppraisalStatusBO.getStatus())
                .eq(HrmAchievementAppraisal::getAppraisalId, updateAppraisalStatusBO.getAppraisalId()).update();
        HrmAchievementAppraisal newAppraisal = getById(updateAppraisalStatusBO.getAppraisalId());
        AppraisalStatus appraisalStatus = AppraisalStatus.parse(updateAppraisalStatusBO.getStatus());
        switch (appraisalStatus) {
            case FILLING_IN:
                //     * 开启考核      1
                startAppraisal(newAppraisal);
                lambdaUpdate().set(HrmAchievementAppraisal::getAppraisalSteps, 0).set(HrmAchievementAppraisal::getActivateSteps, 1).eq(HrmAchievementAppraisal::getAppraisalId, updateAppraisalStatusBO.getAppraisalId()).update();
                break;
            case UNDER_EVALUATION:
                //     * 开启评定      2
                startEvaluato(newAppraisal);
                lambdaUpdate().set(HrmAchievementAppraisal::getActivateSteps, 2).eq(HrmAchievementAppraisal::getAppraisalId, updateAppraisalStatusBO.getAppraisalId()).update();
                break;
            case CONFIRMING:
                //     * 开启结果确认   3
                startResultConfirm(newAppraisal);
                lambdaUpdate().set(HrmAchievementAppraisal::getActivateSteps, 3).eq(HrmAchievementAppraisal::getAppraisalId, updateAppraisalStatusBO.getAppraisalId()).update();
                break;
            case ARCHIVE:
                //     * 归档         4
                Integer completeCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, updateAppraisalStatusBO.getAppraisalId())
                        .and(i -> i.eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.STOP.getValue())
                                .or().eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.COMPLETE.getValue()))
                        .count();
                Integer allCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, updateAppraisalStatusBO.getAppraisalId()).count();
                if (!completeCount.equals(allCount)) {
                    throw new CrmException(HrmCodeEnum.CAN_T_ARCHIVE);
                }
                lambdaUpdate().set(HrmAchievementAppraisal::getActivateSteps,oldAppraisalStatus).eq(HrmAchievementAppraisal::getAppraisalId, updateAppraisalStatusBO.getAppraisalId()).update();
                break;
            default:
                break;
        }
    }

    /**
     * 开启考核
     * 需要给员工端生成考核,状态为待填写
     *
     * @param appraisal
     */
    private void startAppraisal(HrmAchievementAppraisal appraisal) {
        Set<Integer> employeeIds = TagUtil.toSet(appraisal.getEmployeeIds());
        if (StrUtil.isNotEmpty(appraisal.getDeptIds())) {
            Set<Integer> deptEmployeeIds = employeeService.lambdaQuery().select(HrmEmployee::getEmployeeId)
                    .in(HrmEmployee::getDeptId, TagUtil.toSet(appraisal.getDeptIds()))
                    .eq(HrmEmployee::getIsDel, IsEnum.NO.getValue())
                    .list()
                    .stream().map(HrmEmployee::getEmployeeId).collect(Collectors.toSet());
            employeeIds.addAll(deptEmployeeIds);
        }
        if (CollUtil.isEmpty(employeeIds)) {
            throw new CrmException(HrmCodeEnum.NO_EXAMINER);
        }
        employeeIds = employeeService.filterDeleteEmployeeIds(employeeIds);
        List<HrmAchievementEmployeeAppraisal> employeeAppraisalList = new ArrayList<>();
        employeeIds.forEach(employeeId -> {
            HrmAchievementEmployeeAppraisal employeeAppraisal = new HrmAchievementEmployeeAppraisal();
            employeeAppraisal.setEmployeeId(employeeId);
            employeeAppraisal.setAppraisalId(appraisal.getAppraisalId());
            employeeAppraisal.setStatus(EmployeeAppraisalStatus.TO_BE_FILLED.getValue());
            employeeAppraisal.setFollowUpEmployeeId(employeeId);
            employeeAppraisal.setFollowSort(1);
            employeeAppraisal.setIsDraft(0);
            employeeAppraisal.setReadStatus(0);
            employeeAppraisalList.add(employeeAppraisal);
        });
        employeeAppraisalService.saveBatch(employeeAppraisalList);
        List<Integer> employeeAppraisalIds = employeeAppraisalService.lambdaQuery().select(HrmAchievementEmployeeAppraisal::getEmployeeAppraisalId)
                .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisal.getAppraisalId()).list()
                .stream().map(HrmAchievementEmployeeAppraisal::getEmployeeAppraisalId).collect(Collectors.toList());
        employeeAppraisalActionRecordService.startAppraisalRecord(appraisal.getAppraisalName(),employeeAppraisalIds);
        //发送考核待填写通知
        List<HrmAchievementEmployeeAppraisal> employeeAppraisals = employeeAppraisalService.lambdaQuery()
                .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisal.getAppraisalId()).list()
                .stream().collect(Collectors.toList());
        employeeAppraisals.forEach(employeeAppraisal -> {
            sendMessage(UserUtil.getUserId(),employeeAppraisal.getEmployeeId(),employeeAppraisal.getEmployeeAppraisalId(), AdminMessageEnum.HRM_EMPLOYEE_APPRAISAL_WRITE.getType(),appraisal.getAppraisalName());
        });
    }

    /**
     * 开启评定
     * 查询员工考核状态为待结果评定的考核,为第一个结果评定人添加结果评定记录
     *
     * @param appraisal
     */
    private void startEvaluato(HrmAchievementAppraisal appraisal) {
        List<HrmAchievementEmployeeAppraisal> employeeAppraisalList = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisal.getAppraisalId())
                .eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.TO_BE_ASSESSED.getValue())
                .list();
        HrmAchievementAppraisalEvaluators evaluators = evaluatorsService.lambdaQuery().eq(HrmAchievementAppraisalEvaluators::getAppraisalId, appraisal.getAppraisalId())
                .eq(HrmAchievementAppraisalEvaluators::getSort, 1).last("limit 1").one();
        List<HrmAchievementEmployeeEvaluato> employeeEvaluatoList = new ArrayList<>();
        employeeAppraisalList.forEach(employeeAppraisal -> {
            Integer evaluatoEmployeeId = achievementUtil.findEmployeeIdByType(evaluators.getType(), employeeAppraisal.getEmployeeId(), evaluators.getEmployeeId(), "开启评定");
            HrmAchievementEmployeeEvaluato employeeEvaluato = new HrmAchievementEmployeeEvaluato();
            employeeEvaluato.setEmployeeAppraisalId(employeeAppraisal.getEmployeeAppraisalId());
            employeeEvaluato.setEmployeeId(evaluatoEmployeeId);
            employeeEvaluato.setWeight(evaluators.getWeight());
            employeeEvaluato.setAppraisalId(employeeAppraisal.getAppraisalId());
            employeeEvaluato.setSort(1);
            employeeEvaluato.setStatus(0);
            employeeEvaluatoList.add(employeeEvaluato);
            employeeAppraisal.setFollowUpEmployeeId(evaluatoEmployeeId);
            employeeAppraisal.setFollowSort(1);
            //确认目标且开启评定后，结果评定人收到通知
            sendMessage(EmployeeCacheUtil.getUserId(employeeAppraisal.getEmployeeId()),evaluatoEmployeeId,employeeAppraisal.getEmployeeAppraisalId(),AdminMessageEnum.HRM_EMPLOYEE_APPRAISAL_ASSESSED.getType(),appraisal.getAppraisalName());
        });
        if (CollUtil.isNotEmpty(employeeAppraisalList)) {
            employeeAppraisalService.updateBatchById(employeeAppraisalList);
        }
        employeeEvaluatoService.saveBatch(employeeEvaluatoList);
    }

    /**
     * 开启结果评定
     *
     * @param appraisal
     */
    private void startResultConfirm(HrmAchievementAppraisal appraisal) {
        List<HrmAchievementEmployeeAppraisal> employeeAppraisalList = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisal.getAppraisalId())
                .eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.TO_BE_CONFIRMED.getValue())
                .list();
        List<Integer> resultConfirmorsList = Lists.newArrayList(TagUtil.toSet(appraisal.getResultConfirmors()));
        Integer unConfirmedCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getStatus,EmployeeAppraisalStatus.TO_BE_CONFIRMED.getValue()).eq(HrmAchievementEmployeeAppraisal::getAppraisalId,appraisal.getAppraisalId()).count();
        Integer count=employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId,appraisal.getAppraisalId()).count();
        //生成考评结果确认
        resultConfirmorsList.forEach(employeeId -> {
            HrmAchievementEmployeeResultConfirmors employeeResultConfirmors = new HrmAchievementEmployeeResultConfirmors();
            HrmEmployee employee = employeeService.getById(employeeId);
            //判断员工不存在 || 删除 || 已离职
            if (employee.getIsDel() == 1 || employee.getEntryStatus() == EmployeeEntryStatus.ALREADY_LEAVE.getValue()) {
                throw new CrmException(HrmCodeEnum.RESULT_CONFIRM_EMPLOYEE_NO_EXIST, employee.getEmployeeName());
            }
            employeeResultConfirmors.setEmployeeId(employeeId);
            employeeResultConfirmors.setAppraisalId(appraisal.getAppraisalId());
            employeeResultConfirmorsService.save(employeeResultConfirmors);
            if (unConfirmedCount.equals(count)) {
                sendMessage(UserUtil.getUserId(),employeeId,appraisal.getAppraisalId(),AdminMessageEnum.HRM_EMPLOYEE_APPRAISAL_CONFIRMED.getType(),appraisal.getAppraisalName());
            }
        });
        //计算每个员工的绩效结果,并保存
        employeeAppraisalList.forEach(this::computeScore);
    }

    /**
     * 计算分数并保存评定结果记录
     *
     * @param employeeAppraisal 员工考核信息
     */
    @Override
    public void computeScore(HrmAchievementEmployeeAppraisal employeeAppraisal) {
        //绩效分数列表
        List<HrmAchievementAppraisalScoreLevel> scoreLevelList = scoreLevelService.lambdaQuery()
                .eq(HrmAchievementAppraisalScoreLevel::getAppraisalId, employeeAppraisal.getAppraisalId())
                .orderByAsc(HrmAchievementAppraisalScoreLevel::getSort).list();
        //生成每个分数段对应的levelId Map
        RangeMap<BigDecimal, Integer> scoreRangeMap = TreeRangeMap.create();
        scoreLevelList.forEach(scoreLevel -> scoreRangeMap.put(Range.closed(scoreLevel.getMinScore(), scoreLevel.getMaxScore()), scoreLevel.getLevelId()));
        //员工绩效评分列表
        List<HrmAchievementEmployeeEvaluato> allEmployeeEvaluatoList = employeeEvaluatoService.lambdaQuery()
                .eq(HrmAchievementEmployeeEvaluato::getEmployeeAppraisalId, employeeAppraisal.getEmployeeAppraisalId())
                .list();
        //计算每个员工平均分
        BigDecimal averageScore = new BigDecimal(0);
        for (HrmAchievementEmployeeEvaluato evaluato : allEmployeeEvaluatoList) {
            BigDecimal score = evaluato.getScore();
            BigDecimal weight = evaluato.getWeight();
            averageScore = averageScore.add(score.multiply(weight));
        }
        averageScore = averageScore.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        Integer levelId = 0;
        employeeAppraisal.setScore(averageScore);
        if (allEmployeeEvaluatoList.size()==1){ //评定人为1人时，以评定时的等级为准
            HrmAchievementEmployeeEvaluato evaluato = allEmployeeEvaluatoList.get(0);
            levelId= evaluato.getLevelId();
        }else{//评定人大于1人时，以综合得分自动计算的等级为准
            levelId=  scoreRangeMap.get(averageScore);
        }
        employeeAppraisal.setLevelId(levelId);
        employeeAppraisalService.updateById(employeeAppraisal);
    }

    @Override
    public Map<Integer, Long> queryAppraisalStatusNum() {
        TreeMap<Integer, Long> collect = lambdaQuery().select(HrmAchievementAppraisal::getStatus)
                .list()
                .stream().collect(Collectors.groupingBy(HrmAchievementAppraisal::getStatus, TreeMap::new, Collectors.counting()));
        for (AppraisalStatus value : AppraisalStatus.values()) {
            if (!collect.containsKey(value.getValue())) {
                collect.put(value.getValue(), 0L);
            }
        }
        //进行中的绩效
        collect.put(5, (long) lambdaQuery().notIn(HrmAchievementAppraisal::getStatus, AppraisalStatus.ARCHIVE.getValue(), AppraisalStatus.UNOPENED.getValue()).count());
        return collect;
    }

    @Override
    public BasePage<AppraisalPageListVO> queryAppraisalPageList(QueryAppraisalPageListBO queryAppraisalPageListBO) {
        BasePage<AppraisalPageListVO> page = appraisalMapper.queryAppraisalPageList(queryAppraisalPageListBO.parse(), queryAppraisalPageListBO);
        page.getList().forEach(appraisalPageListVO -> {
            Integer status = appraisalPageListVO.getStatus();
            //完成和终止数量
            Integer completeCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalPageListVO.getAppraisalId())
                    .and(i -> i.eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.STOP.getValue())
                            .or().eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.COMPLETE.getValue()))
                    .count();
            //总数量
            Integer allCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalPageListVO.getAppraisalId()).count();
            //待结果评定数量
            Integer toAssessedCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalPageListVO.getAppraisalId())
                    .eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.TO_BE_ASSESSED.getValue()).count();
            //待结果确认数量
            Integer toConfirmedCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalPageListVO.getAppraisalId())
                    .eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.TO_BE_CONFIRMED.getValue()).count();
            //完成数量
            Integer succeedCount = employeeAppraisalService.lambdaQuery().eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalPageListVO.getAppraisalId())
                    .eq(HrmAchievementEmployeeAppraisal::getStatus, EmployeeAppraisalStatus.COMPLETE.getValue())
                    .count();
            TreeMap<Integer, Long> statisticsMap;
            if (status.equals(AppraisalStatus.UNOPENED.getValue())) {
                Set<Integer> employeeIds = TagUtil.toSet(appraisalPageListVO.getEmployeeIds());
                if (StrUtil.isNotEmpty(appraisalPageListVO.getDeptIds())) {
                    Set<Integer> deptEmployeeIds = employeeService.lambdaQuery().select(HrmEmployee::getEmployeeId)
                            .in(HrmEmployee::getDeptId, TagUtil.toSet(appraisalPageListVO.getDeptIds()))
                            .eq(HrmEmployee::getIsDel, IsEnum.NO.getValue())
                            .list()
                            .stream().map(HrmEmployee::getEmployeeId).collect(Collectors.toSet());
                    employeeIds.addAll(deptEmployeeIds);
                }
                statisticsMap = new TreeMap<>();
                statisticsMap.put(0, (long) employeeIds.size());
                for (EmployeeAppraisalStatus value : EmployeeAppraisalStatus.values()) {
                    if (!value.equals(EmployeeAppraisalStatus.STOP) && !value.equals(EmployeeAppraisalStatus.COMPLETE)) {
                        statisticsMap.put(value.getValue(), 0L);
                    }
                }
            } else {
                List<Integer> statusList = employeeAppraisalService.lambdaQuery().select(HrmAchievementEmployeeAppraisal::getStatus)
                        .eq(HrmAchievementEmployeeAppraisal::getAppraisalId, appraisalPageListVO.getAppraisalId())
                        .list()
                        .stream().map(HrmAchievementEmployeeAppraisal::getStatus).collect(Collectors.toList());
                statisticsMap = statusList
                        .stream().collect(Collectors.groupingBy(i -> i, TreeMap::new, Collectors.counting()));
                statisticsMap.put(0, (long) statusList.size());
                for (EmployeeAppraisalStatus value : EmployeeAppraisalStatus.values()) {
                    if (!value.equals(EmployeeAppraisalStatus.STOP) && !value.equals(EmployeeAppraisalStatus.COMPLETE)) {
                        if (!statisticsMap.containsKey(value.getValue())) {
                            statisticsMap.put(value.getValue(), 0L);
                        }
                    }
                }
                if (appraisalPageListVO.getIsStop() == 0) {
                    if (allCount == toAssessedCount) {
                        appraisalPageListVO.setAppraisalSteps(1);
                    } else if (allCount == toConfirmedCount) {
                        appraisalPageListVO.setAppraisalSteps(2);
                    } else if (allCount == succeedCount && !status.equals(AppraisalStatus.ARCHIVE.getValue())) {
                        appraisalPageListVO.setAppraisalSteps(3);
                    }
                    //更新考核步骤进度
                    lambdaUpdate().set(HrmAchievementAppraisal::getAppraisalSteps, appraisalPageListVO.getAppraisalSteps()).eq(HrmAchievementAppraisal::getAppraisalId, appraisalPageListVO.getAppraisalId()).update();
                }
            }
            appraisalPageListVO.setStatistics(statisticsMap);
            appraisalPageListVO.setIsArchive(completeCount.equals(allCount) ? 1 : 0);
        });
        return page;
    }

    @Override
    public AppraisalInformationBO queryAppraisalById(Integer appraisalId) {
        HrmAchievementAppraisal appraisal = getById(appraisalId);
        AppraisalInformationBO informationBO = BeanUtil.copyProperties(appraisal, AppraisalInformationBO.class);
        informationBO.setResultConfirmors(employeeService.queryAllSimpleEmployeeList(TagUtil.toSet(appraisal.getResultConfirmors())));
        informationBO.setDeptIds(TagUtil.toSet(appraisal.getDeptIds()));
        informationBO.setEmployeeIds(employeeService.queryAllSimpleEmployeeList(TagUtil.toSet(appraisal.getEmployeeIds())));
        List<HrmAchievementAppraisalTargetConfirmors> targetConfirmorsList = targetConfirmorsService.lambdaQuery().eq(HrmAchievementAppraisalTargetConfirmors::getAppraisalId, appraisalId).list();
        targetConfirmorsList.forEach(targetConfirmors -> {
            if (targetConfirmors.getType().equals(AppraisalEmployeeType.DESIGNATION.getValue())) {
                HrmEmployee employee = employeeService.getById(targetConfirmors.getEmployeeId());
                SimpleHrmEmployeeVO simpleHrmEmployeeVO = employeeService.transferSimpleEmp(employee);
                targetConfirmors.setStatus(simpleHrmEmployeeVO.getStatus());
                targetConfirmors.setEmployeeName(employee.getEmployeeName());
            }
        });
        informationBO.setTargetConfirmorsList(targetConfirmorsList);
        List<HrmAchievementAppraisalEvaluators> evaluatorsList = evaluatorsService.lambdaQuery().eq(HrmAchievementAppraisalEvaluators::getAppraisalId, appraisalId).list();
        evaluatorsList.forEach(evaluators -> {
            if (evaluators.getType().equals(AppraisalEmployeeType.DESIGNATION.getValue())) {
                HrmEmployee employee = employeeService.getById(evaluators.getEmployeeId());
                SimpleHrmEmployeeVO simpleHrmEmployeeVO = employeeService.transferSimpleEmp(employee);
                evaluators.setStatus(simpleHrmEmployeeVO.getStatus());
                evaluators.setEmployeeName(employee.getEmployeeName());
            }
        });
        informationBO.setEvaluatorsList(evaluatorsList);
        List<HrmAchievementAppraisalScoreLevel> scoreLevelList = scoreLevelService.lambdaQuery().eq(HrmAchievementAppraisalScoreLevel::getAppraisalId, appraisalId).list();
        informationBO.setScoreLevelList(scoreLevelList);
        return informationBO;
    }

    @Override
    public BasePage<EmployeeListByAppraisalIdVO> queryEmployeeListByAppraisalId(QueryEmployeeListByAppraisalIdBO employeeListByAppraisalIdBO) {
        return appraisalMapper.queryEmployeeListByAppraisalId(employeeListByAppraisalIdBO.parse(), employeeListByAppraisalIdBO);
    }

    @Override
    public BasePage<AppraisalEmployeeListVO> queryAppraisalEmployeeList(QueryAppraisalEmployeeListBO employeeListBO) {
        Collection<Integer> dataAuthEmployeeIds = employeeUtil.queryDataAuthEmpIdByMenuId(MenuIdConstant.ACHIEVEMENT_MENU_ID);
        BasePage<AppraisalEmployeeListVO> page = appraisalMapper.queryEmployeePageList(employeeListBO.parse(), employeeListBO,dataAuthEmployeeIds);
        List<AppraisalEmployeeListVO> appraisalEmployeeListVOList = new ArrayList<>();
        page.getList().forEach(appraisalEmployeeListVO -> {
            Map<String, Object> map = appraisalMapper.queryEmployeeLastAppraisal(appraisalEmployeeListVO.getEmployeeId());
            appraisalEmployeeListVOList.add(BeanUtil.fillBeanWithMap(map, appraisalEmployeeListVO, true));
        });
        page.setList(appraisalEmployeeListVOList);
        return page;
    }

    @Override
    public BasePage<EmployeeAppraisalVO> queryEmployeeAppraisal(QueryEmployeeAppraisalBO queryEmployeeAppraisalBO) {
        BasePage<EmployeeAppraisalVO> page = appraisalMapper.queryEmployeeAppraisal(queryEmployeeAppraisalBO.parse(), queryEmployeeAppraisalBO.getEmployeeId());
        page.getList().forEach(employeeAppraisalVO -> {
            employeeAppraisalVO.setAppraisalTime(AchievementUtil.appraisalTimeFormat(employeeAppraisalVO.getStartTime(), employeeAppraisalVO.getEndTime()));
        });
        return page;
    }

    @Override
    public EmployeeAppraisalDetailVO queryEmployeeDetail(Integer employeeAppraisalId) {
        EmployeeAppraisalDetailVO employeeAppraisalDetailVO = new EmployeeAppraisalDetailVO();
        HrmAchievementEmployeeAppraisal employeeAppraisal = employeeAppraisalService.getById(employeeAppraisalId);
        HrmAchievementAppraisal appraisal1 = getById(employeeAppraisal.getAppraisalId());
        HrmEmployee employee = employeeService.lambdaQuery()
                .select(HrmEmployee::getEmployeeId, HrmEmployee::getEmployeeName, HrmEmployee::getMobile)
                .eq(HrmEmployee::getEmployeeId, employeeAppraisal.getEmployeeId()).last("limit 1").one();
        BeanUtil.copyProperties(employee, employeeAppraisalDetailVO);
        employeeAppraisalDetailVO.setAppraisalStatus(appraisal1.getStatus());
        employeeAppraisalDetailVO.setFullScore(appraisal1.getFullScore());
        employeeAppraisalDetailVO.setStatus(employeeAppraisal.getStatus());
        employeeAppraisalDetailVO.setScore(employeeAppraisal.getScore());
        if (employeeAppraisal.getLevelId() != null) {
            employeeAppraisalDetailVO.setLevelId(employeeAppraisal.getLevelId());
            HrmAchievementAppraisalScoreLevel scoreLevel = scoreLevelService.getById(employeeAppraisal.getLevelId());
            employeeAppraisalDetailVO.setLevelName(scoreLevel.getLevelName());
        }
        Integer appraisalId = employeeAppraisal.getAppraisalId();
        Integer employeeId = employee.getEmployeeId();
        //查询目标确认人
        List<HrmAchievementAppraisalTargetConfirmors> targetConfirmorsList = targetConfirmorsService.lambdaQuery()
                .eq(HrmAchievementAppraisalTargetConfirmors::getAppraisalId, appraisalId).orderByAsc(HrmAchievementAppraisalTargetConfirmors::getSort).list();
        List<SimpleHrmEmployeeVO> confirmors = new ArrayList<>();
        targetConfirmorsList.forEach(targetConfirmors -> {
            Integer findEmployeeId;
            try {
                findEmployeeId = achievementUtil.findEmployeeIdByType(targetConfirmors.getType(), employeeId, targetConfirmors.getEmployeeId(), "目标确认");
            } catch (CrmException e) {
                findEmployeeId = null;
            }
            HrmEmployee one = employeeService.lambdaQuery().select(HrmEmployee::getEmployeeId, HrmEmployee::getEmployeeName).eq(HrmEmployee::getEmployeeId, findEmployeeId).last("limit 1").one();
            if (one != null) {
                confirmors.add(BeanUtil.copyProperties(one, SimpleHrmEmployeeVO.class));
            }
        });
        employeeAppraisalDetailVO.setConfirmorsList(confirmors);
        //查询结果评定人
        List<EmployeeAppraisalDetailVO.EvaluatorsVO> evaluators = new ArrayList<>();
        List<HrmAchievementAppraisalEvaluators> evaluatorsList = evaluatorsService.lambdaQuery()
                .eq(HrmAchievementAppraisalEvaluators::getAppraisalId, appraisalId).orderByAsc(HrmAchievementAppraisalEvaluators::getSort).list();
        evaluatorsList.forEach(evaluators1 -> {
            Integer findEmployeeId;
            try {
                findEmployeeId = achievementUtil.findEmployeeIdByType(evaluators1.getType(), employeeId, evaluators1.getEmployeeId(), "评定");
            } catch (CrmException e) {
                findEmployeeId = null;
            }
            HrmEmployee one = employeeService.lambdaQuery().select(HrmEmployee::getEmployeeId, HrmEmployee::getEmployeeName).eq(HrmEmployee::getEmployeeId, findEmployeeId).last("limit 1").one();
            if (one != null) {
                EmployeeAppraisalDetailVO.EvaluatorsVO evaluatorsVO = new EmployeeAppraisalDetailVO.EvaluatorsVO();
                evaluatorsVO.setEmployeeId(one.getEmployeeId());
                evaluatorsVO.setEmployeeName(one.getEmployeeName());
                evaluatorsVO.setWeight(evaluators1.getWeight());
                evaluators.add(evaluatorsVO);
            }
        });
        employeeAppraisalDetailVO.setEvaluatorsList(evaluators);
        //查询结果确认人
        HrmAchievementAppraisal appraisal = getById(appraisalId);
        Set<Integer> resultConfirmorsList = TagUtil.toSet(appraisal.getResultConfirmors());
        List<HrmEmployee> resultConfirmorsEmployees = employeeService.lambdaQuery().select(HrmEmployee::getEmployeeId, HrmEmployee::getEmployeeName).in(HrmEmployee::getEmployeeId, resultConfirmorsList).list();
        List<SimpleHrmEmployeeVO> resultConfirmors = TransferUtil.transferList(resultConfirmorsEmployees, SimpleHrmEmployeeVO.class);
        employeeAppraisalDetailVO.setResultConfirmors(resultConfirmors);

        //查询考核项
        List<HrmAchievementEmployeeSeg> hrmAchievementEmployeeSegs = employeeSegService.queryAppraisalSeg(employeeAppraisalId);
        Map<Integer, List<HrmAchievementEmployeeSeg>> collect = hrmAchievementEmployeeSegs.stream().collect(Collectors.groupingBy(HrmAchievementEmployeeSeg::getIsFixed));
        if (employeeAppraisal.getStatus() == 1 && employeeAppraisal.getIsDraft() == 1) {
            //待填写状态并且是草稿
            if (EmployeeHolder.getEmployeeId().equals(employeeAppraisal.getEmployeeId())) {
                employeeAppraisalDetailVO.setFixedSegList(collect.get(IsEnum.YES.getValue()));
                employeeAppraisalDetailVO.setNoFixedSegList(collect.get(IsEnum.NO.getValue()));
            }
        } else {
            employeeAppraisalDetailVO.setFixedSegList(collect.get(IsEnum.YES.getValue()));
            employeeAppraisalDetailVO.setNoFixedSegList(collect.get(IsEnum.NO.getValue()));
        }
        //查询评价结果
        List<EvaluatoResultVO> evaluatoResultVOS = employeeEvaluatoService.queryEvaluatoList(employeeAppraisalId);
        employeeAppraisalDetailVO.setEvaluatoResultList(evaluatoResultVOS);
        return employeeAppraisalDetailVO;
    }

    @Override
    public Map<String, Object> queryEmployeeAppraisalCount(Integer employeeId) {
        return appraisalMapper.queryEmployeeLastAppraisal(employeeId);
    }
    public void sendMessage(Long userId,Integer employeeId,Integer typeId,Integer type,String title) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateUser(userId);
        adminMessage.setCreateTime(DateUtil.formatDateTime(new Date()));
        adminMessage.setRecipientUser(EmployeeCacheUtil.getUserId(employeeId));
        adminMessage.setTypeId(typeId);
        adminMessage.setLabel(8);
        adminMessage.setType(type);
        adminMessage.setTitle(title);
        adminMessageService.save(adminMessage);
    }
}
