package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.AddDeptBO;
import com.kakarote.hrm.entity.BO.QueryDeptListBO;
import com.kakarote.hrm.entity.BO.QueryEmployeeByDeptIdBO;
import com.kakarote.hrm.entity.PO.HrmDept;
import com.kakarote.hrm.entity.VO.DeptEmployeeVO;
import com.kakarote.hrm.entity.VO.DeptVO;
import com.kakarote.hrm.entity.VO.QueryEmployeeListByDeptIdVO;
import com.kakarote.hrm.entity.VO.SimpleHrmDeptVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmDeptService extends BaseService<HrmDept> {

    /**
     * 添加活修改部门
     * @param addDeptBO
     */
    void addOrUpdate(AddDeptBO addDeptBO);

    /**
     * 查询部门详情
     * @param deptId
     * @return
     */
    DeptVO queryById(Integer deptId);

    /**
     * 查询部门列表
     * @param queryDeptListBO
     * @return
     */
    List<DeptVO> queryTreeList(QueryDeptListBO queryDeptListBO);

    /**
     * 查询部门简要字段列表
     * @param deptIds
     * @return
     */
    List<SimpleHrmDeptVO> querySimpleDeptList(Collection<Integer> deptIds);

    /**
     * 通过部门id查询员工列表
     * @param employeeByDeptIdBO
     * @return
     */
    BasePage<QueryEmployeeListByDeptIdVO> queryEmployeeByDeptId(QueryEmployeeByDeptIdBO employeeByDeptIdBO);

    /**
     * 删除部门
     * @param deptId
     */
    void deleteDeptById(String deptId);

    /**
     * 查询部门下的子部门id
     * @param deptIds
     * @return
     */
    Set<Integer> queryChildDeptId(Collection<Integer> deptIds);

    /**
     * 查询部门下的父部门id
     * @param deptId
     * @return
     */
    Set<Integer> queryParentDeptId(Integer deptId);

    /**
     * 查询所有部门(员工部门表单使用)
     * @return
     */
    List<DeptEmployeeVO> queryDeptEmployeeList();

}
