package com.kakarote.core.feign.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.crm.entity.CrmExamineData;
import com.kakarote.core.feign.crm.entity.CrmSaveExamineRecordBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmInfo;
import com.kakarote.core.feign.crm.service.CrmExamineService;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.examine.entity.ExamineMessageBO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FeignCrmExamineServiceImpl implements CrmExamineService {
    @Override
    public Result<CrmExamineData> saveExamineRecord(CrmSaveExamineRecordBO examineRecordBO) {
        return null;
    }

    @Override
    public Result<List<JSONObject>> queryByRecordId(Integer recordId) {
        return null;
    }

    @Override
    public Result<Boolean> queryExamineStepIsExist(Integer categoryType) {
        return null;
    }

    @Override
    public Result<JSONObject> queryExamineRecordList(Integer recordId, Long ownerUserId) {
        return null;
    }

    @Override
    public Result<Map<String, Object>> getDataMapForNewExamine(ExamineConditionDataBO examineConditionDataBO) {
        return null;
    }

    @Override
    public Result<Boolean> updateCheckStatusByNewExamine(ExamineConditionDataBO examineConditionDataBO) {
        return null;
    }

    @Override
    public Result addMessageForNewExamine(ExamineMessageBO examineMessageBO) {
        return null;
    }

    @Override
    public Result<SimpleCrmInfo> getCrmSimpleInfo(ExamineConditionDataBO examineConditionDataBO) {
        return null;
    }
}
