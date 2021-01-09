package com.kakarote.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.kakarote.admin.common.AdminConst;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.AdminRole;
import com.kakarote.admin.entity.VO.AdminRoleVO;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminRoleService extends BaseService<AdminRole> {
    /**
     * 查询用户所属权限
     *
     * @param userId 当前用户ID
     * @return obj
     */
//    @Cached(name = AdminConst.DEFAULT_AUTH_CACHE_NAME, key = "args[0]",timeUnit = TimeUnit.MINUTES,expire = 5)
    public JSONObject auth(Long userId);

    /**
     * 清除用户缓存
     *
     * @param userIds ids
     */
    @CacheInvalidate(name = AdminConst.DEFAULT_AUTH_CACHE_NAME, key = "args[0]",multi = true)
    public void authInvalidate(List<Long> userIds);

    /**
     * 通过用户ID查询角色列表
     * @param userId 用户ID
     * @return data
     */
    public List<AdminRole> queryRoleListByUserId(Long userId);

    /**
     * 通过用户ID查询角色列表
     * @param userIds 用户ID
     * @return data
     */
    public List<AdminRole> queryRoleListByUserId(List<Long> userIds);

    /**
     * 根据类型查询角色
     * @param roleTypeEnum type
     * @return data
     */
    public List<AdminRole> getRoleByType(AdminRoleTypeEnum roleTypeEnum);

    /**
     * 查询全部角色
     * @return data
     */
    public List<AdminRoleVO> getAllRoleList();

    /**
     * 查询数据权限
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return 权限
     */
    public Integer queryDataType(Long userId,Integer menuId);

    /**
     * 查询下属用户
     * @param userId 用户ID
     * @param realm 权限标识
     * @return 权限
     */
    public List<Long> queryUserByAuth(Long userId,String realm);

    /**
     * 保存角色
     * @param adminRole role
     */
    public void add(AdminRole adminRole);

    /**
     * 删除角色
     * @param roleId roleId
     */
    public void delete(Integer roleId);

    /**
     * 复制角色
     * @param roleId roleId
     */
    public void copy(Integer roleId);

    /**
     * 用户关联角色
     * @param userIds 用户列表
     * @param roleIds 角色列表
     */
    public void relatedUser(List<Long> userIds, List<Integer> roleIds);

    void relatedDeptUser(List<Long> userIds, List<Integer> deptIds, List<Integer> roleIds);

    /**
     * 取消用户关联角色
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    public void unbindingUser(Long userId, Integer roleId);

    /**
     * 修改角色菜单关系
     * @param adminRole adminrole
     */
    public void updateRoleMenu(AdminRole adminRole);

    /**
     * 查询项目管理的角色
     * @param label label
     * @return roleId
     */
    public Integer queryWorkRole(Integer label);


    /**
     * 保存项目管理角色
     * @param object obj
     */
    public void setWorkRole(JSONObject object);

    /**
     * 删除项目管理角色
     * @param roleId roleId
     */
    public void deleteWorkRole(Integer roleId);

    /**
     * 查询项目管理角色
     * @return
     */
    public List<AdminRole> queryProjectRoleList();

    Integer queryMaxDataType(Long userId, Integer menuId);

    List<AdminRole> queryRoleList();

    /**
     * 获取用户未授权菜单
     * */
    List<String> queryNoAuthMenu(Long userId);

    List<AdminRole> queryRoleByRoleTypeAndUserId(Integer type);

    Integer queryHrmDataAuthType(Integer menuId);
}
