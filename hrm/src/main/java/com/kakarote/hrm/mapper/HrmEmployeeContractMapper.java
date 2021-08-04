package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmEmployeeContract;

import java.util.List;

/**
 * <p>
 * 员工合同 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmEmployeeContractMapper extends BaseMapper<HrmEmployeeContract> {

    List<Integer> queryToExpireContractCount();


}
