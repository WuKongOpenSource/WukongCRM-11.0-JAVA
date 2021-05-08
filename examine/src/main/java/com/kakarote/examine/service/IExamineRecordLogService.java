package com.kakarote.examine.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.examine.entity.PO.ExamineRecordLog;
import com.kakarote.examine.entity.VO.ExamineRecordLogVO;

import java.util.List;

/**
 * <p>
 * 审核日志表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-19
 */
public interface IExamineRecordLogService extends BaseService<ExamineRecordLog> {

    /**
     * 查询当前审批层级是否还有其他同级审批
     * @param batchId 批次ID
     * @param sort 当前排序
     * @return log
     */
    public ExamineRecordLog queryNextExamineRecordLog(String batchId,Integer sort,Integer logId);


    /**
     * 查询指定的审批历史数据
     * @param examineLogId
     * @return log
     */
    public ExamineRecordLog queryExamineLogById(Integer examineLogId);

    /**
     * 获取完整审批历史记录
     * @param recordId
     * @param ownerUserId
     * @return log
     */
    List<ExamineRecordLogVO> queryExamineRecordLog(Integer recordId, String ownerUserId);
}
