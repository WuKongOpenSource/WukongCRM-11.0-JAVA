package com.kakarote.examine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.examine.constant.ExamineStatusEnum;
import com.kakarote.examine.entity.PO.ExamineRecordLog;
import com.kakarote.examine.entity.VO.ExamineRecordLogVO;
import com.kakarote.examine.mapper.ExamineRecordLogMapper;
import com.kakarote.examine.service.IExamineRecordLogService;
import com.kakarote.examine.service.IExamineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审核日志表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-19
 */
@Service
public class ExamineRecordLogServiceImpl extends BaseServiceImpl<ExamineRecordLogMapper, ExamineRecordLog> implements IExamineRecordLogService {

    @Autowired
    private IExamineRecordService examineRecordService;

    @Autowired
    private AdminService adminService;

    /**
     * 查询当前审批层级是否还有其他同级审批未处理
     *
     * @param batchId 批次ID
     * @param sort    当前排序
     * @return num
     */
    @Override
    public ExamineRecordLog queryNextExamineRecordLog(String batchId, Integer sort,Integer logId) {
        LambdaQueryChainWrapper<ExamineRecordLog> queryChainWrapper = lambdaQuery().eq(ExamineRecordLog::getBatchId, batchId);
        /*
         查询待审核或者审核中的
         */
        queryChainWrapper.in(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.UNDERWAY.getStatus(),ExamineStatusEnum.AWAIT.getStatus());
        if (sort != null) {
            queryChainWrapper.gt(ExamineRecordLog::getSort, sort);
        }
        //排除自身
        queryChainWrapper.ne(ExamineRecordLog::getLogId,logId);
        return queryChainWrapper.last(" limit 1").one();
    }

    @Override
    public ExamineRecordLog queryExamineLogById(Integer examineLogId) {
        LambdaQueryWrapper<ExamineRecordLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ExamineRecordLog::getLogId,examineLogId);
        lambdaQueryWrapper.in(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.UNDERWAY.getStatus(),ExamineStatusEnum.AWAIT.getStatus());
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<ExamineRecordLogVO> queryExamineRecordLog(Integer recordId, String ownerUserId) {
        List<ExamineRecordLogVO> examineRecordLogVoS = new ArrayList<>();

        LambdaQueryWrapper<ExamineRecordLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ExamineRecordLog::getRecordId,recordId);
        List<Integer> statusList = ListUtil.toList(ExamineStatusEnum.AWAIT.getStatus(),
                ExamineStatusEnum.UN_SUBMITTED.getStatus(),ExamineStatusEnum.INVALID.getStatus());
        lambdaQueryWrapper.notIn(ExamineRecordLog::getExamineStatus,statusList);
        lambdaQueryWrapper.orderByAsc(ExamineRecordLog::getCreateTime);
        List<ExamineRecordLog> examineRecordLogs = this.list(lambdaQueryWrapper);
        Map<Long, String> map = new HashMap<>(16);
        ExamineRecordLogVO recordLogVO = new ExamineRecordLogVO();
        for (ExamineRecordLog examineRecordLog : examineRecordLogs) {
            ExamineRecordLogVO examineRecordLogV0 = new ExamineRecordLogVO();
            BeanUtil.copyProperties(examineRecordLog, examineRecordLogV0);
            examineRecordLogV0.setExamineTime(examineRecordLog.getUpdateTime());
            if (ExamineStatusEnum.CREATE.getStatus().equals(examineRecordLog.getExamineStatus())) {
                Long createUserId = examineRecordLog.getCreateUserId();
                if (map.containsKey(createUserId)) {
                    examineRecordLogV0.setExamineUserName(map.get(createUserId));
                } else {
                    String userName = adminService.queryUserName(createUserId).getData();
                    examineRecordLogV0.setExamineUserName(userName);
                    map.put(createUserId, userName);
                }
                recordLogVO = examineRecordLogV0;
                recordLogVO.setExamineTime(examineRecordLogV0.getCreateTime());
            } else {
                Long examineUserId;
                if (ExamineStatusEnum.RECHECK.getStatus().equals(examineRecordLog.getExamineStatus())){
                    examineUserId = examineRecordLog.getCreateUserId();
                }else {
                    examineUserId = examineRecordLog.getExamineUserId();
                }
                if (map.containsKey(examineUserId)) {
                    examineRecordLogV0.setExamineUserName(map.get(examineUserId));
                } else {
                    String userName = adminService.queryUserName(examineUserId).getData();
                    examineRecordLogV0.setExamineUserName(userName);
                    map.put(examineUserId, userName);
                }
                examineRecordLogVoS.add(examineRecordLogV0);
            }
        }
        examineRecordLogVoS.add(0,recordLogVO);
        return examineRecordLogVoS;
    }
}
