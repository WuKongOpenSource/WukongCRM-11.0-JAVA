package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmEmployeeContactsData;
import com.kakarote.hrm.mapper.HrmEmployeeContactsDataMapper;
import com.kakarote.hrm.service.IHrmEmployeeContactsDataService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户扩展字段数据表 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmEmployeeContactsDataServiceImpl extends BaseServiceImpl<HrmEmployeeContactsDataMapper, HrmEmployeeContactsData> implements IHrmEmployeeContactsDataService {

    @Override
    public Integer verifyUnique(Integer fieldId, String value, Integer id) {
        return getBaseMapper().verifyUnique(fieldId,value,id);
    }
}
