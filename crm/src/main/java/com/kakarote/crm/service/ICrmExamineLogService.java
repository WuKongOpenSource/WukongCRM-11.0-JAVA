package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmExamineLog;

import java.util.List;

/**
 * <p>
 * 审核日志表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface ICrmExamineLogService extends BaseService<CrmExamineLog> {

    List<JSONObject> queryByRecordId(Integer recordId);
}
