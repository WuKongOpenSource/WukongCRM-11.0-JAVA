package com.kakarote.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminMenuService extends BaseService<AdminMenu> {
    /**
     * 查询用户所拥有的菜单权限
     *
     * @param userId 用户列表
     * @return 菜单权限的并集
     */
    public List<AdminMenu> queryMenuList(Long userId);

    /**
     * 查询公海菜单权限
     *
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return data
     */
    public Map<String, Long> queryPoolReadAuth(Long userId, Integer deptId);

    /**
     * 根据类型查询菜单
     * @param typeEnum type
     * @return data
     */
    public JSONObject getMenuListByType(AdminRoleTypeEnum typeEnum);

    Integer queryMenuId(String realm1, String realm2, String realm3);
}
