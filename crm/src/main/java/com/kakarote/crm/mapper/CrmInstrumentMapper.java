package com.kakarote.crm.mapper;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.core.utils.BiTimeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    public List<JSONObject> queryRecordCount(@Param("data") Dict kv);

    /**
     * 查询数据汇总里面进入公海数量以及从公海领取数量
     * @param type 1 放入公海 2 从公海领取
     * @return 对应的数量
     */
    public Integer dataInfoCustomerPoolNum(@Param("time") BiTimeUtil.BiTimeEntity entity, @Param("ids") List<Long> ids,@Param("type") Integer type);

    /**
     * 查询即将到期的合同
     * @param startTime 开始时间，不受仪表盘筛选限制
     * @param endTime 结束时间，不受仪表盘筛选限制
     * @param ids 用户列表
     * @return data
     */
    public Map<String,Object> dataInfoEndContractNum(@Param("startTime") Date startTime,@Param("endTime") Date endTime, @Param("ids") List<Long> ids);
}
