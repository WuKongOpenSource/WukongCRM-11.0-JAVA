package com.kakarote.admin.service.impl;

import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.admin.entity.PO.AdminRoleAuth;
import com.kakarote.admin.entity.PO.AdminRoleMenu;
import com.kakarote.admin.mapper.AdminRoleAuthMapper;
import com.kakarote.admin.service.IAdminRoleAuthService;
import com.kakarote.admin.service.IAdminRoleMenuService;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2021-04-23
 */
@Service
public class AdminRoleAuthServiceImpl extends BaseServiceImpl<AdminRoleAuthMapper, AdminRoleAuth> implements IAdminRoleAuthService {

    /**
     * 保存角色权限设置
     *
     * @param roleId 角色ID
     * @param authRoleIds 可查看角色ID
     */
    @Override
    public void saveRoleAuth(Integer roleId, List<Integer> authRoleIds) {
        removeByMap(Collections.singletonMap("role_id",roleId));
        List<AdminRoleAuth> authList = authRoleIds.stream().map(id -> {
            AdminRoleAuth adminRoleAuth = new AdminRoleAuth();
            adminRoleAuth.setRoleId(roleId);
            /* 暂时只有角色查看控制需要这个功能，目前菜单写死935 */
            adminRoleAuth.setMenuId(935);
            adminRoleAuth.setAuthRoleId(id);
            return adminRoleAuth;
        }).collect(Collectors.toList());
        saveBatch(authList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 根据roleId查询
     *
     * @param roleId roleId
     * @return data
     */
    @Override
    public List<Integer> queryByRoleId(Integer roleId) {
        LambdaQueryWrapper<AdminRoleAuth> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AdminRoleAuth::getAuthRoleId);
        wrapper.eq(AdminRoleAuth::getRoleId,roleId);
        return listObjs(wrapper,TypeUtils::castToInt);
    }

    /**
     * 查询当前用户的能访问角色列表
     *
     * @return roleIds
     */
    @Override
    public Set<Integer> queryAuthByUser() {
        List<Integer> roles = UserUtil.getUser().getRoles();
        LambdaQueryWrapper<AdminRoleAuth> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AdminRoleAuth::getAuthRoleId);
        wrapper.in(AdminRoleAuth::getRoleId,roles);
        return new HashSet<>(listObjs(wrapper,TypeUtils::castToInt));
    }

    /**
     * 是否可以查询全部角色
     *
     * @return true为可以
     */
    @Override
    public boolean isQueryAllRole() {
        IAdminRoleMenuService roleMenuService = ApplicationContextHolder.getBean(IAdminRoleMenuService.class);
        LambdaQueryWrapper<AdminRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRoleMenu::getMenuId,175).in(AdminRoleMenu::getRoleId,UserUtil.getUser().getRoles());
        return UserUtil.isAdmin() || roleMenuService.count(wrapper) > 0;
    }
}
