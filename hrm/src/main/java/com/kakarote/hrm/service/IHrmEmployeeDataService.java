package com.kakarote.hrm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmEmployeeData;

import java.util.List;

/**
 * <p>
 * 客户扩展字段数据表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmEmployeeDataService extends BaseService<HrmEmployeeData> {

    /**
     * 查询员工自定义字段值
     * @param employeeId
     * @return
     */
    List<HrmEmployeeData> queryListByEmployeeId(Integer employeeId);


    /**
     * 查询员工自定义字段值
     * @param employeeId
     * @return
     */
    List<JSONObject> queryFiledListByEmployeeId(Integer employeeId);

    /**
     * 验证字段唯一
     * @param fieldId
     * @param value
     * @param id
     * @return
     */
    Integer verifyUnique(Integer fieldId, String value, Integer id);
}
