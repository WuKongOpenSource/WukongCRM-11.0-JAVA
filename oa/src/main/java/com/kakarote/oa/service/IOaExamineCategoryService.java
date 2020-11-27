package com.kakarote.oa.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.BO.SetExamineCategoryBO;
import com.kakarote.oa.entity.BO.UpdateCategoryStatus;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import com.kakarote.oa.entity.PO.OaExamineSort;
import com.kakarote.oa.entity.VO.OaExamineCategoryVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批类型表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaExamineCategoryService extends BaseService<OaExamineCategory> {

    Map<String,Integer> setExamineCategory(SetExamineCategoryBO setExamineCategoryBO);

    BasePage<OaExamineCategoryVO> queryExamineCategoryList(PageEntity pageEntity);

    List<OaExamineCategory> queryAllExamineCategoryList();

    void saveOrUpdateOaExamineSort(List<OaExamineSort> oaExamineSortList);

    void deleteExamineCategory(Integer id);

    void updateStatus(UpdateCategoryStatus updateCategoryStatus);
}
