package com.kakarote.crm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmBusiness;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface CrmBusinessMapper extends BaseMapper<CrmBusiness> {
    /**
     * 通过id查询商机数据
     *
     * @param id id
     * @param userId 用户ID
     * @return data
     */
    public CrmModel queryById(@Param("id") Integer id, @Param("userId") Long userId);

    public BasePage<CrmContacts> queryContacts(BasePage<CrmContacts> crmContactsBaseMapper, @Param("businessId") Integer businessId);

    /**
     * 查询详情页数量
     * @param map map
     */
    public CrmInfoNumVO queryNum(Map<String,Object> map);

    JSONObject querySubtotalByBusinessId(Integer businessId);

    BasePage<JSONObject> queryProduct(BasePage<Object> parse,@Param("businessId") Integer businessId);

    BasePage<JSONObject> queryContract(BasePage<Object> parse,@Param("businessId") Integer businessId,@Param("conditions") String conditions);

    void deleteMember(@Param("memberId") String memberId,@Param("id") Integer id);


    List<String> eventDealBusiness(@Param("data") CrmEventBO crmEventBO);

    List<Integer> eventDealBusinessPageList(@Param("userId") Long userId, @Param("time") Date time);

    List<String> eventBusiness(@Param("data") CrmEventBO crmEventBO);

    List<Integer> eventBusinessPageList(@Param("userId") Long userId, @Param("time") Date time);
}
