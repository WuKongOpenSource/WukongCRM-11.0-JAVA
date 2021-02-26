package com.kakarote.oa.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.BO.ExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamineField;

import java.util.List;
import java.util.Map;

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

    List<List<OaExamineField>> queryFormPositionField(Integer id);

    Boolean updateFieldCategoryId(Long newCategoryId,Long oldCategoryId);

    /**
     * 根据batchId查询values
     * @param batchId batchId
     * @return valuesMap
     */
    public Map<Integer,String> queryFieldData(String batchId);

    void transferFieldList(List<OaExamineField> recordList, Integer isDetail);

    void saveField(ExamineFieldBO examineFieldBO);

    void saveDefaultField(Long categoryId);
}
