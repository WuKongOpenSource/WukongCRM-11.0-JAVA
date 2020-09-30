package com.kakarote.crm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmContract;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface CrmContractMapper extends BaseMapper<CrmContract> {

    /**
     * 通过id查询商机数据
     *
     * @param id id
     * @param userId 用户ID
     * @return data
     */
    public CrmModel queryById(@Param("id") Integer id, @Param("userId") Long userId);

    /**
     * 查询详情页数量
     * @param map map
     */
    public CrmInfoNumVO queryNum(Map<String,Object> map);

    void deleteMember(@Param("memberId") String memberId,@Param("id") Integer id);

    List<CrmReceivablesPlan> queryReceivablesPlansByContractId(Integer contractId);

    CrmReceivablesPlan queryReceivablesPlansByReceivablesId(Integer receivablesId);

    JSONObject querySubtotalByContractId(@Param("contractId")Integer contractId);

    BasePage<JSONObject> queryProductPageList(BasePage<Object> parse,@Param("contractId") Integer contractId);

    BasePage<JSONObject> queryReturnVisit(BasePage<Object> parse, @Param("contractId")Integer contractId, @Param("conditions")String conditions,@Param("nameList") List<CrmField> nameList);

    List<String> endContract(@Param("data") CrmEventBO crmEventBO);

    List<String> receiveContract(@Param("data") CrmEventBO crmEventBO);

    List<Integer> endContractList(@Param("userId") Long userId,@Param("time") Date time,@Param("expiringDay") Integer expiringDay);

    List<JSONObject> receiveContractList(@Param("userId") Long userId,@Param("time") Date time);
}
