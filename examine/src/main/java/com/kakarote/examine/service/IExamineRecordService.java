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



    public ExamineRecordVO queryExamineRecordList(Integer recordId, Long ownerUserId);

    public ExamineRecordReturnVO queryExamineRecordInfo(Integer recordId);

    Boolean deleteExamineRecord(Integer recordId);

    Boolean updateExamineRecord(Integer recordId,Integer examineStatus);

    Boolean deleteExamineRecordAndLog(Integer label);
}
