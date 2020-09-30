package com.kakarote.oa.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.oa.entity.BO.QueryEventTaskBO;
import com.kakarote.oa.entity.PO.OaCalendarType;
import com.kakarote.oa.entity.VO.EventTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 日历类型 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface OaCalendarTypeMapper extends BaseMapper<OaCalendarType> {

    List<EventTaskVO> queryEventTask(@Param("data") QueryEventTaskBO eventTaskBO);

    List<String> queryFixedTypeByUserId(Long userId);
}
