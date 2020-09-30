package com.kakarote.crm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmMarketing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 营销表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-08-12
 */
public interface CrmMarketingMapper extends BaseMapper<CrmMarketing> {

    BasePage<CrmMarketing> queryPageList(BasePage<Object> parse, @Param("userIds") List<Long> userIds,@Param("crmType") Integer crmType,
                                         @Param("search") String search,@Param("isAdmin") boolean isAdmin,
                                         @Param("timeType") Integer timeType,@Param("status") Integer status);

    BasePage<JSONObject> census(BasePage<Object> parse,@Param("marketingId") Integer marketingId,@Param("userIds") List<Long> userIds,@Param("status") Integer status);
}
