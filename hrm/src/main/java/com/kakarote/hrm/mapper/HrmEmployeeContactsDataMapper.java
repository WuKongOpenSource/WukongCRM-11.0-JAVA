package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmEmployeeContactsData;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户扩展字段数据表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmEmployeeContactsDataMapper extends BaseMapper<HrmEmployeeContactsData> {

    Integer verifyUnique(@Param("fieldId") Integer fieldId, @Param("value") String value, @Param("contactsId") Integer contactsId);
}
