package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmSalaryOption;
import com.kakarote.hrm.entity.PO.HrmSalaryOptionTemplate;

import java.util.List;

/**
 * <p>
 * 系统薪资项 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface HrmSalaryOptionMapper extends BaseMapper<HrmSalaryOption> {

    List<HrmSalaryOptionTemplate> querySalaryOptionTemplateList();

}
