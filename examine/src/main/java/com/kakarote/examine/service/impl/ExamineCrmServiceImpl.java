package com.kakarote.examine.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.crm.service.CrmExamineService;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.examine.constant.ExamineCodeEnum;
import com.kakarote.examine.entity.VO.ExamineFlowConditionDataVO;
import com.kakarote.examine.service.ExamineModuleService;
import com.kakarote.examine.service.IExamineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("examineCrmService")
public class ExamineCrmServiceImpl implements ExamineModuleService {

    @Autowired
    private IExamineService examineService;

    @Autowired
    private CrmService crmService;

    @Autowired
    private CrmExamineService crmExamineService;

    /**
     * 查询审批可用的字段
     * @param label label
     * @param categoryId categoryId
     * @return data
     */
    @Override
    public List<ExamineField> queryExamineField(Integer label, Integer categoryId) {
        if (label != 3) {
            List<ExamineField> examineFields = crmService.queryExamineField(label).getData();
            examineFields.forEach(examineField -> examineField.setFieldName(StrUtil.toCamelCase(examineField.getFieldName())));
            return examineFields;
        }else {
            //发票条件字段由前端处理【开票金额】、【开票类型】
            List<ExamineField> examineFields = new ArrayList<>();
            examineFields.add(new ExamineField(-1,"开票金额","invoiceMoney",6,1));
            return examineFields;
        }
    }

    /**
     * 修改审核状态
     *
     * @param label       类型
     * @param typeId      对应类型主键ID
     * @param checkStatus 审核状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废
     */
    @Override
    public void updateCheckStatus(Integer label, Integer typeId, Integer checkStatus) {
        ExamineConditionDataBO examineConditionDataBO = new ExamineConditionDataBO();
        examineConditionDataBO.setLabel(label);
        examineConditionDataBO.setTypeId(typeId);
        examineConditionDataBO.setCheckStatus(checkStatus);
        crmExamineService.updateCheckStatusByNewExamine(examineConditionDataBO);
    }

    /**
     * 校验当前状态是否可以进行，不可进行直接抛异常即可
     *
     * @param label          类型
     * @param typeId         对应类型ID
     * @param checkStatus    审核状态
     * @param oldCheckStatus 之前的审核状态
     */
    @Override
    public void checkStatus(Integer label, Integer typeId, Integer checkStatus, Integer oldCheckStatus) {
        if (checkStatus == 4) {
            //当前审核已通过不可撤回
            if (oldCheckStatus == 1) {
                throw new CrmException(ExamineCodeEnum.EXAMINE_RECHECK_PASS_ERROR);
            }
        }
    }

    /**
     * 获取条件审核需要的数据entity
     *
     * @param label  类型
     * @param typeId 对应类型ID
     * @return map
     */
    @Override
    public Map<String, Object> getConditionMap(Integer label, Integer typeId,Integer recordId) {
        ExamineConditionDataBO examineConditionDataBO = new ExamineConditionDataBO();
        examineConditionDataBO.setLabel(label);
        examineConditionDataBO.setTypeId(typeId);
        List<String> fieldList = new ArrayList<>();
        List<ExamineFlowConditionDataVO> conditionDataVoS = examineService.previewFiledName(label,recordId,null);
        if (conditionDataVoS != null){
            fieldList = conditionDataVoS.stream().map(ExamineFlowConditionDataVO::getFieldName).collect(Collectors.toList());
            fieldList.removeIf(StrUtil::isEmpty);
        }
        examineConditionDataBO.setFieldList(fieldList);
        return crmExamineService.getDataMapForNewExamine(examineConditionDataBO).getData();
    }


}
