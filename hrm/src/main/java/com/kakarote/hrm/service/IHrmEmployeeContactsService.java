package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmEmployeeContacts;

/**
 * <p>
 * 员工联系人 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmEmployeeContactsService extends BaseService<HrmEmployeeContacts> {

    /**
     * 校验字段唯一
     * @param fieldName
     * @param value
     * @param contactsId
     * @return
     */
    Integer verifyUnique(String fieldName, String value, Integer contactsId);
}
