package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmEmployeeContacts;
import com.kakarote.hrm.mapper.HrmEmployeeContactsMapper;
import com.kakarote.hrm.service.IHrmEmployeeContactsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工联系人 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmEmployeeContactsServiceImpl extends BaseServiceImpl<HrmEmployeeContactsMapper, HrmEmployeeContacts> implements IHrmEmployeeContactsService {

    @Override
    public Integer verifyUnique(String fieldName, String value, Integer contactsId) {
        return getBaseMapper().verifyUnique(fieldName,value,contactsId);
    }
}
