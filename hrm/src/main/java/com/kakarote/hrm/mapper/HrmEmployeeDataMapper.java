package com.kakarote.hrm.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmEmployeeData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户扩展字段数据表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmEmployeeDataMapper extends BaseMapper<HrmEmployeeData> {

    Integer verifyUnique(@Param("fieldId") Integer fieldId,@Param("value") String value,@Param("employeeId") Integer employeeId);

    public List<JSONObject> queryFiledListByEmployeeId(@Param("employeeId") Integer employeeId);
}
