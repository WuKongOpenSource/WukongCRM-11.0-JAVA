package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmEmployeeFile;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 员工附件表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmEmployeeFileMapper extends BaseMapper<HrmEmployeeFile> {


    Map<String, Object> queryFileNum(@Param("employeeId") Integer employeeId);
}
