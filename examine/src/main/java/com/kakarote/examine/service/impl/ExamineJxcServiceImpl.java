package com.kakarote.examine.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.jxc.service.JxcExamineService;
import com.kakarote.examine.constant.ExamineCodeEnum;
import com.kakarote.examine.entity.VO.ExamineFlowConditionDataVO;
import com.kakarote.examine.service.ExamineModuleService;
import com.kakarote.examine.service.IExamineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2020/12/22
 */

@Service("examineJxcService")
public class ExamineJxcServiceImpl implements ExamineModuleService {

    @Autowired
    private JxcExamineService jxcExamineService;

    @Autowired
    private IExamineService examineService;

    @Override
    public List<ExamineField> queryExamineField(Integer label, Integer categoryId) {
        if (ListUtil.toList(5,6,7,8).contains(label)){
            label = label - 2;
        }
        List<ExamineField> examineFields = jxcExamineService.queryExamineField(label).getData();
        examineFields.forEach(examineField -> examineField.setFieldName(StrUtil.toCamelCase(examineField.getFieldName())));
        return examineFields;
    }

    @Override
    public void updateCheckStatus(Integer label, Integer typeId, Integer checkStatus) {
        jxcExamineService.examine(label,checkStatus,typeId);
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
    public Map<String, Object> getConditionMap(Integer label, Integer typeId, Integer recordId) {
        Map<String, Object> map = new HashMap<>(4);
        List<String> fieldList = new ArrayList<>();
        List<ExamineFlowConditionDataVO> conditionDataVoS = examineService.previewFiledName(label,recordId,null);
        if (conditionDataVoS != null){
            fieldList = conditionDataVoS.stream().map(ExamineFlowConditionDataVO::getFieldName).collect(Collectors.toList());
            fieldList.removeIf(StrUtil::isEmpty);
        }
        if (CollUtil.isEmpty(fieldList)){
            return map;
        }
        Map<String, Object> beanMap = jxcExamineService.examineFieldDataMap(label, typeId).getData();
        fieldList.forEach(field -> map.put(field,beanMap.get(field)));
        map.put("createUserId",map.get("createUserId"));
        return map;
    }
}
