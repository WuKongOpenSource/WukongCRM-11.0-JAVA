package com.kakarote.examine.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.oa.OaService;
import com.kakarote.examine.constant.ExamineCodeEnum;
import com.kakarote.examine.entity.PO.ExamineRecord;
import com.kakarote.examine.entity.VO.ExamineFlowConditionDataVO;
import com.kakarote.examine.service.ExamineModuleService;
import com.kakarote.examine.service.IExamineRecordService;
import com.kakarote.examine.service.IExamineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2020/12/19
 */
@Slf4j
@Service("examineOaService")
public class ExamineOaServiceImpl implements ExamineModuleService {

    @Autowired
    private IExamineRecordService examineRecordService;

    @Autowired
    private IExamineService examineService;

    @Autowired
    private OaService oaService;

    @Override
    public List<ExamineField> queryExamineField(Integer label, Integer categoryId) {
        if (categoryId == null){
            return new ArrayList<>();
        }
        List<ExamineField> examineFields = oaService.queryExamineField(categoryId).getData();
        examineFields.forEach(examineField -> examineField.setFieldName(StrUtil.toCamelCase(examineField.getFieldName())));
        return examineFields;
    }

    @Override
    public void updateCheckStatus(Integer label, Integer typeId, Integer checkStatus) {
        ExamineConditionDataBO examineConditionDataBO = new ExamineConditionDataBO();
        examineConditionDataBO.setLabel(label);
        examineConditionDataBO.setTypeId(typeId);
        examineConditionDataBO.setCheckStatus(checkStatus);
        oaService.updateCheckStatusByNewExamine(examineConditionDataBO);
    }

    @Override
    public void checkStatus(Integer label, Integer typeId, Integer checkStatus, Integer oldCheckStatus) {
        if (checkStatus == 4) {
            //当前审核已通过不可撤回
            if (oldCheckStatus == 1) {
                throw new CrmException(ExamineCodeEnum.EXAMINE_RECHECK_PASS_ERROR);
            }
        }
    }

    @Override
    public Map<String, Object> getConditionMap(Integer label, Integer typeId,Integer recordId) {
        ExamineRecord record = examineRecordService.getById(recordId);
        if (record == null){
            log.error("没有找到对应的审核记录！查找的记录id为{}",recordId);
            return new HashMap<>(2);
        }
        ExamineConditionDataBO examineConditionDataBO = new ExamineConditionDataBO();
        examineConditionDataBO.setLabel(label);
        examineConditionDataBO.setTypeId(typeId);
        Long examineId = record.getExamineId();
        examineConditionDataBO.setCategoryId(examineId.intValue());
        List<String> fieldList = new ArrayList<>();
        List<ExamineFlowConditionDataVO> conditionDataVoS = examineService.previewFiledName(label,recordId,null);
        if (conditionDataVoS != null){
            fieldList = conditionDataVoS.stream().map(ExamineFlowConditionDataVO::getFieldName).collect(Collectors.toList());
            fieldList.removeIf(StrUtil::isEmpty);
        }
        examineConditionDataBO.setFieldList(fieldList);
        return oaService.getDataMapForNewExamine(examineConditionDataBO).getData();
    }
}
