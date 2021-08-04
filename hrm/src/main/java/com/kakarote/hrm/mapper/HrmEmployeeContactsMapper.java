package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmEmployeeContacts;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 员工联系人 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmEmployeeContactsMapper extends BaseMapper<HrmEmployeeContacts> {

    Integer verifyUnique(@Param("fieldName") String fieldName,@Param("value") String value,@Param("contactsId") Integer contactsId);

}
