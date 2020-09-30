package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmMyExamineBO;
import com.kakarote.crm.entity.BO.CrmQueryExamineStepBO;
import com.kakarote.crm.entity.BO.CrmSaveExamineBO;
import com.kakarote.crm.entity.PO.CrmExamine;
import com.kakarote.crm.entity.VO.CrmQueryAllExamineVO;
import com.kakarote.crm.entity.VO.CrmQueryExamineStepVO;

/**
 * <p>
 * 审批流程表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface ICrmExamineService extends BaseService<CrmExamine> {
    /**
     * 查询当前开启状态的审批数量
     * @param crmEnum enum
     * @return data
     */
    public Integer queryCount(CrmEnum crmEnum);

    void saveExamine(CrmSaveExamineBO crmSaveExamineBO);

    BasePage<CrmQueryAllExamineVO> queryAllExamine(PageEntity pageEntity);

    CrmQueryAllExamineVO queryExamineById(String examineId);

    void updateStatus(CrmExamine crmExamine);

    CrmQueryExamineStepVO queryExamineStep(CrmQueryExamineStepBO queryExamineStepBO);

    Boolean queryExamineStepByType(Integer categoryType);

    BasePage<JSONObject> myExamine(CrmMyExamineBO crmMyExamineBO);

}
