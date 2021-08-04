package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmSalaryChangeTemplate;
import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;

import java.util.List;

/**
 * <p>
 * 调薪模板 Mapper 接口
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
public interface HrmSalaryChangeTemplateMapper extends BaseMapper<HrmSalaryChangeTemplate> {

    List<ChangeSalaryOptionVO> queryChangeSalaryOption();

}
