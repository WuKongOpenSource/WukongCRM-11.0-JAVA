package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.SetChangeTemplateBO;
import com.kakarote.hrm.entity.PO.HrmSalaryChangeTemplate;
import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import com.kakarote.hrm.entity.VO.QueryChangeTemplateListVO;

import java.util.List;

/**
 * <p>
 * 调薪模板 服务类
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
public interface IHrmSalaryChangeTemplateService extends BaseService<HrmSalaryChangeTemplate> {

    /**
     * 查询调薪默认项
     * @return
     */
    List<ChangeSalaryOptionVO> queryChangeSalaryOption();

    /**
     * 设置定薪/调薪模板
     * @param setChangeTemplateBO
     */
    void setChangeTemplate(SetChangeTemplateBO setChangeTemplateBO);

    /**
     * 查询模板列表
     * @return
     */
    List<QueryChangeTemplateListVO> queryChangeTemplateList();

    /**
     * 删除模板
     * @param id
     */
    void deleteChangeTemplate(Integer id);
}
