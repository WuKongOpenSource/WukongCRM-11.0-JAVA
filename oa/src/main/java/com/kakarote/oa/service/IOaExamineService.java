package com.kakarote.oa.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.examine.entity.ExamineInfoVo;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.BO.AuditExamineBO;
import com.kakarote.oa.entity.BO.ExamineExportBO;
import com.kakarote.oa.entity.BO.ExaminePageBO;
import com.kakarote.oa.entity.BO.GetExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamine;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import com.kakarote.oa.entity.PO.OaExamineField;
import com.kakarote.oa.entity.VO.ExamineVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaExamineService extends BaseService<OaExamine> {

    BasePage<ExamineVO> myInitiate(ExaminePageBO examinePageBO);

    BasePage<ExamineVO> myOaExamine(ExaminePageBO examinePageBO);

    List<OaExamineField> getField(GetExamineFieldBO getExamineFieldBO);

    void setOaExamine(JSONObject jsonObject);

    void oaExamine(AuditExamineBO auditExamineBO);

    ExamineVO queryOaExamineInfo(String examineId);

    JSONObject queryExamineRecordList(String recordId);

    List<JSONObject> queryExamineLogList(Integer recordId);

    void deleteOaExamine(Integer examineId);

    OaExamineCategory queryExaminStep(String categoryId);

    List<Map<String, Object>> export(ExamineExportBO examineExportBO, ExamineInfoVo examineInfoVo , List<OaExamineField> fieldList);

    public List<ExamineVO> transfer(List<ExamineVO> recordList);

    Map<String, Object> getDataMapForNewExamine(ExamineConditionDataBO examineConditionDataBO);

    Boolean updateCheckStatusByNewExamine(ExamineConditionDataBO examineConditionDataBO);

    ExamineVO getOaExamineById(Integer oaExamineId);
}
