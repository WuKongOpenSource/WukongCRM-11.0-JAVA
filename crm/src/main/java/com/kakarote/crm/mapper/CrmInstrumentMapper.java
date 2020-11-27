package com.kakarote.crm.mapper;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.utils.BiTimeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CrmInstrumentMapper {

    public List<JSONObject> sellFunnel(Map<String,Object> map);

    public List<String> sellFunnelBusinessList(Map<String,Object> map);
    /**
     * 查询销售简报
     *
     * @param biTimeEntity time
     * @param map          map
     * @return data
     */
    public Map<String, Object> queryBulletin(@Param("time") BiTimeUtil.BiTimeEntity biTimeEntity, @Param("map") Map<String, Object> map);

    /**
     * 遗忘客户提现
     *
     * @param day     天数
     * @param userIds ids
     * @return data
     */
    public Integer forgottenCustomerCount(@Param("day") Integer day, @Param("userIds") List<Long> userIds);

    /**
     * 未联系客户
     *
     * @param userIds ids
     * @return data
     */
    public Integer unContactCustomerCount(@Param("userIds") List<Long> userIds);

    public Map<String, Object> salesTrend(@Param("time") BiTimeUtil.BiTimeEntity entity, @Param("data") JSONObject object);

    /**
     * 客户数据汇总
     * @param entity time
     * @param ids ids
     * @return data
     */
    public Map<String, Object> dataInfoCustomer(@Param("time") BiTimeUtil.BiTimeEntity entity, @Param("ids") List<Long> ids);

    /**
     * 商机数据汇总
     * @param entity time
     * @param ids ids
     * @return data
     */
    public Map<String, Object> dataInfoBusiness(@Param("time") BiTimeUtil.BiTimeEntity entity, @Param("ids") List<Long> ids);

    /**
     * 合同数据汇总
     * @param entity time
     * @param ids ids
     * @return data
     */
    public Map<String, Object> dataInfoContract(@Param("time") BiTimeUtil.BiTimeEntity entity, @Param("ids") List<Long> ids);

    /**
     * 回款数据汇总
     * @param entity time
     * @param ids ids
     * @return data
     */
    public Map<String, Object> dataInfoReceivables(@Param("time") BiTimeUtil.BiTimeEntity entity, @Param("ids") List<Long> ids);

    /**
     * 回款数据汇总
     * @param entity time
     * @param ids ids
     * @return data
     */
    public Map<String, Object> dataInfoActivity(@Param("time") BiTimeUtil.BiTimeEntity entity, @Param("ids") List<Long> ids);

    /**
     * 业绩指标
     * @param entity time
     * @return data
     */
    public JSONObject queryPerformance(Map<String,Object> entity);

    List<JSONObject> queryRecordCount(@Param("data") Dict kv);
}
