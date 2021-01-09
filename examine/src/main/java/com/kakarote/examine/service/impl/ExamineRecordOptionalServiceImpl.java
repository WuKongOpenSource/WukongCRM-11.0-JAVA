package com.kakarote.examine.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.examine.entity.PO.ExamineRecordOptional;
import com.kakarote.examine.mapper.ExamineRecordOptionalMapper;
import com.kakarote.examine.service.IExamineRecordOptionalService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 审核自选成员选择成员表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-12-02
 */
@Service
public class ExamineRecordOptionalServiceImpl extends BaseServiceImpl<ExamineRecordOptionalMapper, ExamineRecordOptional> implements IExamineRecordOptionalService {

    /**
     * 保存自选成员审批对象
     *
     * @param flowId   审批流程ID
     * @param recordId 审批记录ID
     * @param userList 用户列表
     */
    @Override
    public void saveRecordOptionalInfo(Integer flowId, Integer recordId, List<Long> userList) {
        List<ExamineRecordOptional> recordOptionalList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            ExamineRecordOptional examineRecordOptional = new ExamineRecordOptional();
            examineRecordOptional.setUserId(userList.get(i));
            examineRecordOptional.setSort(i);
            examineRecordOptional.setRecordId(recordId);
            examineRecordOptional.setFlowId(flowId);
            recordOptionalList.add(examineRecordOptional);
        }
        saveBatch(recordOptionalList);
    }
}
