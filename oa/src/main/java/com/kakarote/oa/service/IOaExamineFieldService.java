package com.kakarote.oa.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.BO.ExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamineField;

import java.util.List;

/**
 * <p>
 * 自定义字段表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-22
 */
public interface IOaExamineFieldService extends BaseService<OaExamineField> {

    List<OaExamineField> queryField(Integer id);

    void recordToFormType(List<OaExamineField> list);

    Boolean updateFieldCategoryId(Long newCategoryId,Long oldCategoryId);

    String queryFieldValueByBatchId(Integer fieldId, String batchId);

    void transferFieldList(List<OaExamineField> recordList, Integer isDetail);

    void saveField(ExamineFieldBO examineFieldBO);

    void saveDefaultField(Long categoryId);
}
