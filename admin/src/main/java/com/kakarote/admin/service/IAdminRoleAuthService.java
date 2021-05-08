package com.kakarote.admin.service;

import com.kakarote.admin.entity.PO.AdminRoleAuth;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2021-04-23
 */
public interface IAdminRoleAuthService extends BaseService<AdminRoleAuth> {

    /**
     * 保存角色权限设置
     */
    public void saveRoleAuth(Integer roleId, List<Integer> authRoleIds);

    /**
     * 根据roleId查询
     * @param roleId roleId
     * @return data
     */
    public List<Integer> queryByRoleId(Integer roleId);

    /**
     * 查询当前用户的能访问角色列表
     * @return
     */
    public Set<Integer> queryAuthByUser();


    /**
     * 是否可以查询全部角色
     * @return true为可以
     */
    public boolean isQueryAllRole();
}
