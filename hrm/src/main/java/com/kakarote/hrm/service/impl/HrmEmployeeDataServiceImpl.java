package com.kakarote.hrm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmEmployeeData;
import com.kakarote.hrm.mapper.HrmEmployeeDataMapper;
import com.kakarote.hrm.service.IHrmEmployeeDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户扩展字段数据表 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmEmployeeDataServiceImpl extends BaseServiceImpl<HrmEmployeeDataMapper, HrmEmployeeData> implements IHrmEmployeeDataService {

    @Override
    public List<HrmEmployeeData> queryListByEmployeeId(Integer employeeId) {
        return list(new QueryWrapper<HrmEmployeeData>().select("field_id", "field_value", "field_value_desc").eq("employee_id", employeeId));
    }

    @Override
    public List<JSONObject> queryFiledListByEmployeeId(Integer employeeId) {
        return getBaseMapper().queryFiledListByEmployeeId(employeeId);
    }

    @Override
    public Integer verifyUnique(Integer fieldId, String value, Integer id) {
        return getBaseMapper().verifyUnique(fieldId,value,id);
    }
}
