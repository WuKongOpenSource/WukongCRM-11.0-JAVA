package com.kakarote.crm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmExamineRecord;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审核记录表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface CrmExamineRecordMapper extends BaseMapper<CrmExamineRecord> {
    public JSONObject queryExamineRecordById(@Param("recordId") Integer recordId);

    public JSONObject queryInfoByRecordId(@Param("recordId") Integer recordId,@Param("tableName")String tableName);
}
