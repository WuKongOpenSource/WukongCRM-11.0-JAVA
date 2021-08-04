package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.QueryEmployFieldManageBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeFieldManage;
import com.kakarote.hrm.entity.VO.EmployeeFieldManageVO;

import java.util.List;

/**
 * <p>
 * 自定义字段管理表 服务类
 * </p>
 *
 * @author guomenghao
 * @since 2021-04-14
 */
public interface IHrmEmployeeFieldManageService extends BaseService<HrmEmployeeFieldManage> {
    /**
     * 查询管理可设置员工字段列表
     * @param queryEmployFieldManageBO
     * @return
     */
    List<EmployeeFieldManageVO> queryEmployeeManageField(QueryEmployFieldManageBO queryEmployFieldManageBO);

    /**
     * 修改管理可以设置员工字段
     * @param manageFields
     */
    void setEmployeeManageField(List<EmployeeFieldManageVO> manageFields);

    /**
     * 查询管理员不可见字段
     * @param entryStatus
     * @return
     */
    List<HrmEmployeeFieldManage> queryManageField(Integer entryStatus);
}
