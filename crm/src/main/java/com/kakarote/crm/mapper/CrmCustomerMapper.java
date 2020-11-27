package com.kakarote.crm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.CrmDataCheckBO;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.VO.CrmDataCheckVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
public interface CrmCustomerMapper extends BaseMapper<CrmCustomer> {
    /**
     * 通过id查询客户数据
     *
     * @param id     id
     * @param userId 用户ID
     * @return data
     */
    public CrmModel queryById(@Param("id") Integer id, @Param("userId") Long userId);

    /**
     * 查询是否是只读成员
     *
     * @param id     id
     * @param userId 用户ID
     * @return data
     */
    public Integer queryIsRoUser(@Param("id") Integer id, @Param("userId") Long userId);

    /**
     * 查询是否是只读成员
     *
     * @param id id
     * @return data
     */
    @Select("select city_name from wk_crm_area where parent_id = #{id}")
    public List<String> queryCityList(@Param("id") Integer id);

    public BasePage<CrmContacts> queryContacts(BasePage<CrmContacts> crmContactsBaseMapper, @Param("customerId") Integer customerId, @Param("search") String search
            , @Param("condition") String condition);

    public BasePage<Map<String, Object>> queryBusiness(BasePage<Map<String, Object>> crmContactsBaseMapper, @Param("customerId") Integer customerId,
                                                       @Param("search") String search, @Param("condition") String condition);

    public BasePage<Map<String, Object>> queryContract(BasePage<Map<String, Object>> crmContactsBaseMapper, @Param("customerId") Integer customerId,
                                                       @Param("search") String search, @Param("checkStatus") Integer checkStatus,@Param("condition") String condition);

    /**
     * 查询详情页数量
     *
     * @param map map
     */
    public CrmInfoNumVO queryNum(Map<String, Object> map);

    Integer ownerNum(@Param("ids") List<Integer> ids, @Param("ownerUserId") Long ownerUserId);

    void deleteMember(@Param("memberId") String memberId, @Param("id") Integer id);

    List<CrmDataCheckVO> dataCheck(@Param("data") CrmDataCheckBO dataCheckBO);

    String queryPoolIdsByCustomer(@Param("customerId") Integer customerId);

    BasePage<JSONObject> queryReceivablesPlan(BasePage<JSONObject> page, @Param("customerId") Integer customerId,@Param("conditions") String conditions);


    BasePage<JSONObject> queryReceivables(BasePage<JSONObject> page, @Param("customerId") Integer customerId, @Param("conditions") String conditions);

    BasePage<JSONObject> queryReturnVisit(BasePage<JSONObject> page, @Param("customerId") Integer customerId, @Param("conditions") String conditions, @Param("nameList") List<CrmField> nameList);

    BasePage<JSONObject> queryInvoice(BasePage<JSONObject> page, @Param("customerId") Integer customerId, @Param("conditions") String conditions);

    BasePage<JSONObject> queryInvoiceInfo(BasePage<JSONObject> page, @Param("customerId") Integer customerId);

    BasePage<JSONObject> queryCallRecord(BasePage<JSONObject> page, @Param("customerId") Integer customerId);

    public Integer queryOutDays(@Param("customerId") Integer customerId,@Param("userId") Long userId);

    List<JSONObject> nearbyCustomer(@Param("lng") String lng, @Param("lat") String lat, @Param("type") Integer type,
                                    @Param("radius") Integer radius, @Param("ownerUserId") Long ownerUserId, @Param("userIds") List<Long> authUserIdList,
                                    @Param("poolIdList") List<Integer> poolIdList);

    List<String> eventCustomer(@Param("data") CrmEventBO crmEventBO);

    List<Integer> eventCustomerList(@Param("userId") Long userId,@Param("time") Date time);

    List<Integer> forgottenCustomer(@Param("day") Integer day, @Param("userIds") List<Long> userIds,@Param("search") String search);

    List<Integer> unContactCustomer(@Param("search") String search,@Param("userIds") List<Long> userIds);
}
