package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.AddInsuranceSchemeBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceScaleBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceTypeBO;
import com.kakarote.hrm.entity.PO.HrmInsuranceScheme;
import com.kakarote.hrm.entity.VO.InsuranceSchemeListVO;
import com.kakarote.hrm.entity.VO.InsuranceSchemeVO;

import java.util.List;

/**
 * <p>
 * 社保方案表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmInsuranceSchemeService extends BaseService<HrmInsuranceScheme> {

    /**
     * 添加社保方案
     * @param addInsuranceSchemeBO
     */
    void setInsuranceScheme(AddInsuranceSchemeBO addInsuranceSchemeBO);

    /**
     * 修改社保方案
     * @param schemeId
     */
    void deleteInsuranceScheme(Integer schemeId);

    /**
     * 查询参保方案列表
     * @param pageEntity
     * @return
     */
    BasePage<InsuranceSchemeListVO> queryInsuranceSchemePageList(PageEntity pageEntity);

    /**
     * 查询参保方案详情
     * @param schemeId
     * @return
     */
    InsuranceSchemeVO queryInsuranceSchemeById(Integer schemeId);

    /**
     * 查询社保方案列表
     * @return
     */
    List<HrmInsuranceScheme> queryInsuranceSchemeList();


    /**
     * 查询社保类型(调用社保100)
     * @param queryInsuranceTypeBO
     * @return
     */
    String queryInsuranceType(QueryInsuranceTypeBO queryInsuranceTypeBO);

    /**
     * 查询社保比例(调用社保100)
     * @param queryInsuranceScaleBO
     * @return
     */
    String queryInsuranceScale(QueryInsuranceScaleBO queryInsuranceScaleBO);

}
