package com.kakarote.examine.service;

import com.kakarote.core.feign.examine.entity.ExamineRecordReturnVO;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.examine.entity.BO.ExamineBO;
import com.kakarote.examine.entity.PO.ExamineRecord;
import com.kakarote.examine.entity.VO.ExamineRecordVO;

/**
 * <p>
 * 审核记录表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-19
 */
public interface IExamineRecordService extends BaseService<ExamineRecord> {

    /**
     * 添加审批记录
     * @param examineRecordSaveBO data
     */
    public ExamineRecordReturnVO addExamineRecord(ExamineRecordSaveBO examineRecordSaveBO);

    /**
     * 进行审批，此方法采用异步同步的方式同步到其他模块
     * @param examineBO data
     */
    public void auditExamine(ExamineBO examineBO);


    /**
     * 预览审批完整流程
     * @param recordId
     * @param ownerUserId
     * */
    public ExamineRecordVO queryExamineRecordList(Integer recordId, Long ownerUserId);

    /**
     * 获取未结束审批流程的待审核人
     * @param recordId
     * */
    public ExamineRecordReturnVO queryExamineRecordInfo(Integer recordId);

    /**
     * 删除审批记录以及自选人员
     * @param recordId
     * */
    Boolean deleteExamineRecord(Integer recordId);

    /**
     * 修改审核记录状态
     * @param recordId
     * @param examineStatus
     * */
    Boolean updateExamineRecord(Integer recordId,Integer examineStatus);

    /**
     * 根据类型批量删除历史记录 - 初始化数据专用
     * 有默认流程 故其他表未做清空处理
     * @param label
     * */
    Boolean deleteExamineRecordAndLog(Integer label);
}
