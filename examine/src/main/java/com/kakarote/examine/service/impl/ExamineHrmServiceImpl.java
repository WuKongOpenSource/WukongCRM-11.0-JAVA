package com.kakarote.examine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.hrm.entity.HrmSalaryMonthRecord;
import com.kakarote.core.feign.hrm.service.SalaryRecordService;
import com.kakarote.examine.constant.ExamineCodeEnum;
import com.kakarote.examine.constant.ExamineConst;
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
 * @date 2020/12/25
 */
@Service("examineHrmService")
public class ExamineHrmServiceImpl implements ExamineModuleService {

    @Autowired
    private SalaryRecordService salaryRecordService;

    @Autowired
    private IExamineService examineService;

    @Override
    public List<ExamineField> queryExamineField(Integer label, Integer categoryId) {
        //年月 实发工资
        List<ExamineField> examineFields = new ArrayList<>();
        examineFields.add(new ExamineField(-1,"计薪年","year",5,1));
        examineFields.add(new ExamineField(-1,"计薪月","month",5,1));
        examineFields.add(new ExamineField(-1,"实发工资","realPaySalary",6,1));
        return examineFields;
    }

    @Override
    public void updateCheckStatus(Integer label, Integer typeId, Integer checkStatus) {
        salaryRecordService.updateCheckStatus(typeId,checkStatus);
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
        HrmSalaryMonthRecord hrmSalaryMonthRecord = salaryRecordService.querySalaryRecordById(typeId).getData();
        Map<String, Object> beanMap = BeanUtil.beanToMap(hrmSalaryMonthRecord);
        fieldList.forEach(field -> map.put(field,beanMap.get(field)));
        map.put(ExamineConst.CREATE_USER_ID,hrmSalaryMonthRecord.getCreateUserId());
        return map;
    }
}
