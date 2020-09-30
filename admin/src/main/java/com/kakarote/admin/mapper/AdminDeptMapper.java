package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.BO.DeptVO;
import com.kakarote.admin.entity.PO.AdminDept;
import com.kakarote.core.servlet.BaseMapper;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminDeptMapper extends BaseMapper<AdminDept> {

    List<DeptVO> queryDeptUserList();
}
