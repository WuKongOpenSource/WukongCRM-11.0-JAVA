package com.kakarote.crm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmReceivables;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 回款表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface CrmReceivablesMapper extends BaseMapper<CrmReceivables> {
    /**
     * 通过id查询回款数据
     *
     * @param id id
     * @param userId 用户ID
     * @return data
     */
    public CrmModel queryById(@Param("id") Integer id, @Param("userId") Long userId);

    BasePage<JSONObject> queryListByContractId(BasePage<JSONObject> page,@Param("contractId") Integer contractId, @Param("conditions") String conditions);
}
