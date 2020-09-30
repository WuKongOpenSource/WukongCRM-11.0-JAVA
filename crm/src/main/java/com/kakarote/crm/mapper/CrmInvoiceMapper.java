package com.kakarote.crm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmInvoice;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
public interface CrmInvoiceMapper extends BaseMapper<CrmInvoice> {

    BasePage<Map<String, Object>> queryPageList(BasePage<Object> parse,@Param("sql") String sql);

    CrmInvoice queryById(@Param("id")Integer id);
}
