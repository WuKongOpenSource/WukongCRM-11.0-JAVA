package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryEmployFieldManageBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeFieldManage;
import com.kakarote.hrm.entity.VO.EmployeeFieldManageVO;

import java.util.List;

/**
 * <p>
 * 自定义字段管理表 Mapper 接口
 * </p>
 *
 * @author guomenghao
 * @since 2021-04-14
 */
public interface HrmEmployeeFieldManageMapper extends BaseMapper<HrmEmployeeFieldManage> {
    /**
     * 查询管理可设置员工字段列表
     * @param queryEmployFieldManageBO
     * @return
     */
    List<EmployeeFieldManageVO> queryEmployeeManageField(QueryEmployFieldManageBO queryEmployFieldManageBO);
}
