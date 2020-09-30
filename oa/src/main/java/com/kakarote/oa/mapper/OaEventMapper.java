package com.kakarote.oa.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.oa.entity.BO.QueryEventListBO;
import com.kakarote.oa.entity.PO.OaEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 日程表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface OaEventMapper extends BaseMapper<OaEvent> {

    List<OaEvent> queryList(@Param("queryEventListBO") QueryEventListBO queryEventListBO);
}
