package com.kakarote.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.entity.PO.CrmExamineLog;
import com.kakarote.crm.mapper.CrmExamineLogMapper;
import com.kakarote.crm.service.ICrmExamineLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审核日志表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Service
public class CrmExamineLogServiceImpl extends BaseServiceImpl<CrmExamineLogMapper, CrmExamineLog> implements ICrmExamineLogService {

    @Override
    public List<JSONObject> queryByRecordId(Integer recordId) {
        return baseMapper.queryByRecordIdAndStatus(recordId);
    }
}
