package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmInsuranceScheme;
import com.kakarote.hrm.entity.VO.InsuranceSchemeListVO;

import java.util.Map;

/**
 * <p>
 * 社保方案表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmInsuranceSchemeMapper extends BaseMapper<HrmInsuranceScheme> {


    BasePage<InsuranceSchemeListVO> queryInsuranceSchemePageList(BasePage<InsuranceSchemeListVO> parse);

    /**
     * 查询社保方案统计
     * @param schemeId
     * @return
     */
    Map<String,Object> queryInsuranceSchemeCountById(Integer schemeId);
}
