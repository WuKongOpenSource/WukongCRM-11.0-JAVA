package com.kakarote.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.common.AdminModuleEnum;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.*;
import com.kakarote.admin.entity.VO.AdminRoleVO;
import com.kakarote.admin.mapper.AdminRoleMapper;
import com.kakarote.admin.service.*;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.DataAuthEnum;
import com.kakarote.core.common.cache.AdminCacheKey;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.feign.hrm.service.HrmService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    @Autowired
    private IAdminMenuService adminMenuService;

    @Autowired
    private IAdminConfigService adminConfigService;

    @Autowired
    private IAdminUserRoleService adminUserRoleService;

    @Autowired
    private IAdminRoleMenuService adminRoleMenuService;

    @Autowired
    private IAdminUserService adminUserService;

    @Autowired
    private IAdminDeptService adminDeptService;

    @Autowired
    private IAdminRoleService adminRoleService;

    @Autowired
    private IAdminRoleAuthService adminRoleAuthService;

    @Autowired
    private CrmService crmService;

    @Autowired
    private Redis redis;

    @Autowired
    private HrmService hrmService;

    /**
     * 查询用户所属权限
     *
     * @return obj
     */
    @Override
    public JSONObject auth(Long userId) {
        String cacheKey = AdminCacheKey.USER_AUTH_CACHE_KET + UserUtil.getUserId();
        if (redis.exists(cacheKey)) {
            return redis.get(cacheKey);
        }
        List<AdminMenu> adminMenus = adminMenuService.queryMenuList(userId);
        List<AdminMenu> menus = adminMenuService.list();
        for (int i = 0; i < menus.size(); i++) {
            if (Objects.equals(0, menus.get(i).getParentId())) {
                adminMenus.add(menus.get(i));
                for (AdminMenu menu : menus) {
                    if (Objects.equals(menu.getParentId(), menus.get(i).getMenuId())) {
                        adminMenus.add(menu);
                    }
                }
            }
        }
        JSONObject jsonObject = createMenu(new HashSet<>(adminMenus), 0);
        List<AdminConfig> adminConfigList = adminConfigService.queryConfigListByName((Object[]) AdminModuleEnum.getValues());

        //为crm模块根据用户权限添加公海权限
        if (jsonObject.containsKey(AdminModuleEnum.CRM.getValue())) {
            JSONObject authObject = new JSONObject();
            UserInfo userInfo = UserUtil.getUser();
            Map<String, Long> read = adminMenuService.queryPoolReadAuth(userInfo.getUserId(), userInfo.getDeptId());
            if (UserUtil.isAdmin() || read.get("adminNum") > 0 || read.get("userNum") > 0) {
                authObject.fluentPut("index", true).fluentPut("receive", true);
                if (UserUtil.isAdmin() || read.get("adminNum") > 0) {
                    authObject.fluentPut("distribute", true).fluentPut("excelexport", true).fluentPut("delete", true);
                }
            }
            jsonObject.getJSONObject(AdminModuleEnum.CRM.getValue()).put("pool", authObject);
            List data = crmService.queryPoolNameListByAuth().getData();
            if (CollUtil.isEmpty(data)) {
                JSONObject crm = jsonObject.getJSONObject("crm");
                if (crm != null) {
                    crm.remove("pool");
                    jsonObject.put("crm", crm);
                }
            }
        }

        if (jsonObject.containsKey("jxc")) {
            JSONObject jxc = jsonObject.getJSONObject("jxc");
            if (jxc.containsKey("bi") && !jxc.getJSONObject("bi").isEmpty()) {
                JSONObject jxcBi = jxc.getJSONObject("bi");
                jxc.remove("bi");
                if (jsonObject.containsKey("bi")) {
                    JSONObject bi = jsonObject.getJSONObject("bi");
                    bi.putAll(jxcBi);
                    jsonObject.put("bi", bi);
                } else {
                    jsonObject.put("bi", jxcBi);
                }
            }
        }

        /*
          循环模块配置，把禁用的模块菜单隐藏掉
         */
        adminConfigList.forEach(adminConfig -> {
            //是否开启该模块
            Integer status = adminConfig.getStatus();
            //需要特殊处理的模块
            if (AdminModuleEnum.CALL.getValue().equals(adminConfig.getName())) {
                JSONObject object = jsonObject.getJSONObject(AdminModuleEnum.BI.getValue());
                if (object != null && status != 1) {
                    object.remove(AdminModuleEnum.CALL.getValue());
                }
                return;
            }
            //需要特殊处理的模块
            List<String> oaArray = Arrays.asList(AdminModuleEnum.TASK_EXAMINE.getValue(), AdminModuleEnum.LOG.getValue(), AdminModuleEnum.BOOK.getValue(), AdminModuleEnum.CALENDAR.getValue());
            if (oaArray.contains(adminConfig.getName())) {
                if (!jsonObject.containsKey(AdminModuleEnum.OA.getValue())) {
                    jsonObject.put(AdminModuleEnum.OA.getValue(), new JSONObject());
                }
                JSONObject object = jsonObject.getJSONObject(AdminModuleEnum.OA.getValue());
                if (status == 0) {
                    object.remove(adminConfig.getName());
                } else {
                    if (!AdminModuleEnum.BOOK.getValue().equals(adminConfig.getName())) {
                        object.put(adminConfig.getName(), new JSONObject());
                    }
                }
                return;
            }
            if (Objects.equals(0, adminConfig.getStatus())) {
                jsonObject.remove(adminConfig.getName());
            } else {
                if (!jsonObject.containsKey(adminConfig.getName())) {
                    jsonObject.put(adminConfig.getName(), new JSONObject());
                }
            }
        });
        if (jsonObject.containsKey("hrm") && jsonObject.getJSONObject("hrm").isEmpty() && !UserUtil.isAdmin()) {
            List<AdminRole> roles = queryRoleByRoleTypeAndUserId(9);
            Boolean isInHrm = hrmService.queryIsInHrm().getData();
            //不在人资员工并且人资没有角色不展示人资导航
            if (!isInHrm && CollUtil.isEmpty(roles)) {
                jsonObject.remove("hrm");
            }
        }
        redis.setex(cacheKey, 300, jsonObject);
        return jsonObject;
    }

    /**
     * 查询用户所属权限
     *
     * @param userIds ids
     */
    @Override
    public void authInvalidate(List<Long> userIds) {

    }

    /**
     * 通过用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return data
     */
    @Override
    public List<AdminRole> queryRoleListByUserId(Long userId) {
        QueryWrapper<AdminUserRole> wrapper = new QueryWrapper<>();
        wrapper.select("role_id");
        wrapper.eq("user_id", userId);
        List<Integer> roleIdList = adminUserRoleService.list(wrapper).stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        if (roleIdList.size() > 0) {
            return listByIds(roleIdList);
        }
        return new ArrayList<>();
    }

    @Override
    public List<AdminRole> queryRoleListByUserId(List<Long> userIds) {
        QueryWrapper<AdminUserRole> wrapper = new QueryWrapper<>();
        wrapper.select("role_id");
        wrapper.in("user_id", userIds);
        List<Integer> roleIdList = adminUserRoleService.list(wrapper).stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        if (roleIdList.size() > 0) {
            roleIdList = roleIdList.stream().distinct().collect(Collectors.toList());
            return listByIds(roleIdList);
        }
        return new ArrayList<>();
    }

    /**
     * 根据类型查询角色
     *
     * @param roleTypeEnum type
     * @return data
     */
    @Override
    public List<AdminRole> getRoleByType(AdminRoleTypeEnum roleTypeEnum) {
        List<AdminRole> recordList = lambdaQuery().eq(AdminRole::getRoleType, roleTypeEnum.getType()).list();
        String realm;
        switch (roleTypeEnum) {
            case MANAGER:
                realm = "manage";
                break;
            case CUSTOMER_MANAGER:
                realm = "crm";
                break;
            case OA:
                realm = "oa";
                break;
            case PROJECT:
                realm = "project";
                break;
            case HRM:
                realm = "hrm";
                break;
            case JXC:
                realm = "jxc";
                break;
            default:
                return new ArrayList<>();
        }
        LambdaQueryWrapper<AdminMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AdminMenu::getMenuId);
        wrapper.eq(AdminMenu::getParentId, 0);
        wrapper.eq(AdminMenu::getRealm, realm);
        Integer pid = adminMenuService.getOne(wrapper).getMenuId();
        recordList.forEach(record -> {
            Map<String, List<Integer>> map = new HashMap<>();
            List<Integer> data = getBaseMapper().getRoleMenu(pid, record.getRoleId());
            List<Integer> bi = getBaseMapper().getRoleMenu(2, record.getRoleId());
            map.put("data", data);
            map.put("bi", bi);
            record.setRules(map);
        });
        return recordList;
    }

    /**
     * 查询全部角色
     *
     * @return data
     */
    @Override
    public List<AdminRoleVO> getAllRoleList() {
        List<AdminRoleVO> records = new ArrayList<>();
        for (AdminRoleTypeEnum typeEnum : AdminRoleTypeEnum.values()) {
            if (Arrays.asList(0, 3, 4, 5).contains(typeEnum.getType())) {
                continue;
            }
            AdminRoleVO record = new AdminRoleVO();
            record.setName(roleTypeCaseName(typeEnum.getType()));
            record.setPid(typeEnum.getType());
            List<AdminRole> recordList = getRoleByType(typeEnum);
            record.setList(recordList);
            records.add(record);
        }
        return records;
    }

    /**
     * 查询新增员工时的可查询角色
     *
     * @return 角色列表
     */
    @Override
    public List<AdminRoleVO> getRoleList() {
        List<AdminRoleVO> records = new ArrayList<>();
        boolean queryAllRole = adminRoleAuthService.isQueryAllRole();
        if(queryAllRole) {
            /* 可以查询全部直接走查询全部方法 */
            return getAllRoleList();
        }
        Set<Integer> roleIds = adminRoleAuthService.queryAuthByUser();
        for (AdminRoleTypeEnum typeEnum : AdminRoleTypeEnum.values()) {
            if (Arrays.asList(0, 3, 4, 5).contains(typeEnum.getType())) {
                continue;
            }
            AdminRoleVO record = new AdminRoleVO();
            record.setName(roleTypeCaseName(typeEnum.getType()));
            record.setPid(typeEnum.getType());
            List<AdminRole> recordList = getRoleByType(typeEnum);
            recordList.removeIf(adminRole -> !roleIds.contains(adminRole.getRoleId()));
            if(recordList.size() == 0) {
                continue;
            }
            record.setList(recordList);
            records.add(record);
        }
        return records;
    }

    @Override
    public Integer queryDataType(Long userId, Integer menuId) {
        Integer dataType = getBaseMapper().queryDataType(userId, menuId);
        return dataType != null ? dataType : 1;
    }

    /**
     * 查询下属用户
     *
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return 权限
     */
    @Override
    public Collection<Long> queryUserByAuth(Long userId, Integer menuId) {
        if (UserUtil.isAdmin()) {
            List<AdminUser> adminUsers = adminUserService.lambdaQuery().select(AdminUser::getUserId).list();
            return adminUsers.stream().map(AdminUser::getUserId).collect(Collectors.toList());
        }
        Integer dataType = queryDataType(userId, menuId);
        Set<Long> userSet = new HashSet<>();
        userSet.add(userId);
        switch (DataAuthEnum.valueOf(dataType)) {
            case MYSELF: {
                return userSet;
            }
            case MYSELF_AND_SUBORDINATE: {
                userSet.addAll(adminUserService.queryChildUserId(userId));
                break;
            }
            case THIS_DEPARTMENT: {
                AdminUser adminUser = adminUserService.getById(userId);
                userSet.addAll(adminUserService.queryUserByDeptIds(Collections.singletonList(adminUser.getDeptId())));
                break;
            }
            case THIS_DEPARTMENT_AND_SUBORDINATE: {
                AdminUser adminUser = adminUserService.getById(userId);
                List<Integer> deptIds = adminDeptService.queryChildDept(adminUser.getDeptId());
                deptIds.add(adminUser.getDeptId());
                userSet.addAll(adminUserService.queryUserByDeptIds(deptIds));
                break;
            }
            case ALL: {
                List<AdminUser> adminUsers = adminUserService.lambdaQuery().select(AdminUser::getUserId).list();
                userSet.addAll(adminUsers.stream().map(AdminUser::getUserId).collect(Collectors.toSet()));
            }
        }
        return userSet;
    }

    /**
     * 保存角色
     *
     * @param adminRole role
     */
    @Override
    public void add(AdminRole adminRole) {
        Integer count = lambdaQuery().eq(AdminRole::getRoleName, adminRole.getRoleName()).eq(AdminRole::getRoleType, adminRole.getRoleType()).count();
        if (count > 0) {
            throw new CrmException(AdminCodeEnum.ADMIN_ROLE_NAME_EXIST_ERROR);
        }
        if (adminRole.getRoleId() != null) {
            updateById(adminRole);
        } else {
            adminRole.setRoleId(null);
            adminRole.setStatus(1);
            adminRole.setRemark(null);
            save(adminRole);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer roleId) {
        removeById(roleId);
        JSONObject object = new JSONObject().fluentPut("role_id", roleId);
        adminUserRoleService.removeByMap(object);
        adminRoleMenuService.removeByMap(object);
    }

    /**
     * @author zhangzhiwei
     * 复制
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void copy(Integer roleId) {
        AdminRole adminRole = getById(roleId);
        LambdaQueryWrapper<AdminRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AdminRoleMenu::getMenuId);
        wrapper.eq(AdminRoleMenu::getRoleId, roleId);
        List<Integer> menuIdsList = adminRoleMenuService.listObjs(wrapper, obj -> Integer.valueOf(obj.toString()));
        String roleName = adminRole.getRoleName().trim();
        String pre = ReUtil.delFirst("[(]\\d+[)]$", roleName);
        List<AdminRole> adminRoleList;
        if (!ReUtil.contains("^[(]\\d+[)]$", roleName)) {
            adminRoleList = lambdaQuery().likeRight(AdminRole::getRoleName, pre).list();
        } else {
            adminRoleList = lambdaQuery().last(" role_name regexp '^[(]\\d+[)]$'").list();
        }
        StringBuilder numberSb = new StringBuilder();
        for (AdminRole dbAdminRole : adminRoleList) {
            String endCode = ReUtil.get("[(]\\d+[)]$", dbAdminRole.getRoleName(), 0);
            if (endCode != null) {
                numberSb.append(endCode);
            }
        }
        int i = 1;
        if (numberSb.length() == 0) {
            while (numberSb.toString().contains("(" + i + ")")) {
                i++;
            }
        }
        adminRole.setRoleName(pre + "(" + i + ")");
        adminRole.setRoleId(null);
        adminRole.setRemark(null);
        save(adminRole);
        adminRoleMenuService.saveRoleMenu(adminRole.getRoleId(), menuIdsList);
        if (adminRole.getRoleType().equals(AdminRoleTypeEnum.MANAGER.getType()) && menuIdsList.contains(935)) {
            List<Integer> authRoleIds = adminRoleAuthService.queryByRoleId(adminRole.getRoleId());
            adminRoleAuthService.saveRoleAuth(adminRole.getRoleId(),authRoleIds);
        }
    }

    /**
     * 用户关联角色
     *
     * @param userIds 用户列表
     * @param roleIds 角色列表
     */
    @Override
    public void relatedUser(List<Long> userIds, List<Integer> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            roleIds = roleIds.stream().distinct().collect(Collectors.toList());
        } else {
            throw new CrmException(AdminCodeEnum.ADMIN_ROLE_NOT_EXIST_ERROR);
        }
        List<AdminUserRole> adminUserRoleList = new ArrayList<>();
        for (Long userId : userIds) {
            for (Integer roleId : roleIds) {
                Integer count = adminUserRoleService.lambdaQuery().eq(AdminUserRole::getRoleId, roleId).eq(AdminUserRole::getUserId, userId).count();
                if (count == 0) {
                    AdminUserRole userRole = new AdminUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    adminUserRoleList.add(userRole);
                }
            }
        }
        adminUserRoleService.saveBatch(adminUserRoleList, Const.BATCH_SAVE_SIZE);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relatedDeptUser(List<Long> userIds, List<Integer> deptIds, List<Integer> roleIds) {
        Set<Long> userIdList = new HashSet<>();
        if (CollUtil.isNotEmpty(userIds)) {
            userIdList.addAll(userIds);
        }
        if (CollUtil.isNotEmpty(deptIds)) {
            List<Long> list = adminUserService.queryUserByDeptIds(deptIds);
            userIdList.addAll(list);
        }
        if (CollUtil.isNotEmpty(roleIds)) {
            roleIds = roleIds.stream().distinct().collect(Collectors.toList());
        } else {
            throw new CrmException(AdminCodeEnum.ADMIN_ROLE_NOT_EXIST_ERROR);
        }
        List<AdminUserRole> adminUserRoleList = new ArrayList<>();
        for (Long userId : userIdList) {
            adminUserRoleService.lambdaUpdate().eq(AdminUserRole::getUserId, userId).remove();
            for (Integer roleId : roleIds) {
                AdminUserRole userRole = new AdminUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                adminUserRoleList.add(userRole);
            }
        }
        boolean queryAllRole = adminRoleAuthService.isQueryAllRole();
        if(!queryAllRole){
            Set<Integer> authByUser = adminRoleAuthService.queryAuthByUser();
            adminUserRoleList.removeIf(userRole->!authByUser.contains(userRole.getRoleId()));
        }
        adminUserRoleService.saveBatch(adminUserRoleList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 取消用户关联角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Override
    public void unbindingUser(Long userId, Integer roleId) {
        Integer count = adminUserRoleService.lambdaQuery().eq(AdminUserRole::getUserId, userId).ne(AdminUserRole::getRoleId, roleId).count();
        if (count == 0) {
            throw new CrmException(AdminCodeEnum.ADMIN_USER_NEEDS_AT_LEAST_ONE_ROLE);
        }
        adminUserRoleService.lambdaUpdate().eq(AdminUserRole::getRoleId, roleId).eq(AdminUserRole::getUserId, userId).remove();
    }

    /**
     * 修改角色菜单关系
     *
     * @param adminRole adminrole
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleMenu(AdminRole adminRole) {
        updateById(adminRole);
        adminRoleMenuService.removeByMap(Collections.singletonMap("role_id", adminRole.getRoleId()));
        adminRoleMenuService.saveRoleMenu(adminRole.getRoleId(), adminRole.getMenuIds());
    }

    /**
     * 查询项目管理的角色
     *
     * @param label label
     * @return roleId
     */
    @Override
    public Integer queryWorkRole(Integer label) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AdminRole::getRoleId);
        if (label == null) {
            wrapper.eq(AdminRole::getRemark, "project");
        } else {
            wrapper.eq(AdminRole::getLabel, label);
        }
        AdminRole adminRole = getOne(wrapper);
        return adminRole != null ? adminRole.getRoleId() : 1;
    }

    /**
     * 保存项目管理角色
     *
     * @param object obj
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setWorkRole(JSONObject object) {
        Integer roleId = object.getInteger("roleId");
        String roleName = object.getString("roleName");
        String remark = object.getString("remark");
        JSONArray menuIds = object.getJSONArray("menuIds");
        AdminRole adminRole;
        if (roleId == null) {
            adminRole = new AdminRole();
            adminRole.setRoleName(roleName);
            adminRole.setRemark(remark);
            adminRole.setRoleType(6);
            save(adminRole);
        } else {
            adminRole = getById(roleId);
            adminRole.setRoleName(roleName);
            adminRole.setRemark(remark);
            adminRole.setRoleId(roleId);
            updateById(adminRole);
            adminRoleMenuService.removeByMap(new JSONObject().fluentPut("role_id", roleId));
        }
        adminRoleMenuService.saveRoleMenu(adminRole.getRoleId(), menuIds.toJavaList(Integer.class));
    }

    /**
     * 删除项目管理角色
     *
     * @param roleId roleId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkRole(Integer roleId) {
        removeById(roleId);
        adminRoleMenuService.removeByMap(new JSONObject().fluentPut("role_id", roleId));
        getBaseMapper().deleteWorkRole(queryWorkRole(3), roleId);
    }

    /**
     * 查询项目管理角色
     *
     * @return
     */
    @Override
    public List<AdminRole> queryProjectRoleList() {
        List<AdminRole> roleList = lambdaQuery().in(AdminRole::getRoleType, Arrays.asList(5, 6)).eq(AdminRole::getIsHidden, 1).list();
        roleList.forEach(record -> {
            LambdaQueryChainWrapper<AdminRoleMenu> chainWrapper = adminRoleMenuService.lambdaQuery().select(AdminRoleMenu::getMenuId).eq(AdminRoleMenu::getRoleId, record.getRoleId());
            List<Integer> rules = chainWrapper.list().stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toList());
            record.setMenuIds(rules);
        });
        return roleList;
    }


    /**
     * 角色类型转换名称
     *
     * @param type 类型
     * @return 角色名称
     */
    private String roleTypeCaseName(Integer type) {
        String name;
        switch (type) {
            case 1:
                name = "系统管理角色";
                break;
            case 2:
                name = "客户管理角色";
                break;
            case 7:
                name = "办公管理角色";
                break;
            case 8:
                name = "项目管理角色";
                break;
            case 9:
                name = "人力资源管理角色";
                break;
            case 10:
                name = "进销存管理角色";
                break;
            default:
                name = "自定义角色";
        }
        return name;
    }

    private JSONObject createMenu(Set<AdminMenu> adminMenuList, Integer parentId) {
        JSONObject jsonObject = new JSONObject();
        adminMenuList.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())) {
                if (Objects.equals(1, adminMenu.getMenuType())) {
                    JSONObject object = createMenu(adminMenuList, adminMenu.getMenuId());
                    if (!object.isEmpty()) {
                        jsonObject.put(adminMenu.getRealm(), object);
                    }
                } else {
                    jsonObject.put(adminMenu.getRealm(), Boolean.TRUE);
                }
            }
        });
        return jsonObject;
    }

    @Override
    public List<AdminRole> queryRoleList() {
        return lambdaQuery().eq(AdminRole::getStatus, 1).in(AdminRole::getRoleType, Arrays.asList(5, 6)).list();

    }

    @Override
    public List<String> queryNoAuthMenu(Long userId) {
        long superUserId = UserUtil.getSuperUser();
        AdminRole role = adminRoleService.lambdaQuery()
                .eq(AdminRole::getRemark, "admin")
                .last(" limit 1")
                .one();
        Integer superRoleId = role.getRoleId();

        List<Integer> roles = adminUserService.queryUserRoleIds(userId);
        String key = userId.toString();
        List<String> noAuthMenuUrls = new ArrayList<>();
        if (userId == superUserId || roles.contains(superRoleId)) {
            //缓存
            redis.setex(key, 60 * 30, noAuthMenuUrls);
            return noAuthMenuUrls;
        }

        List<AdminMenu> adminMenus = adminMenuService.queryMenuList(userId);
        if (adminMenus.isEmpty()) {
            noAuthMenuUrls.add("/*/**");
            //缓存
            redis.setex(key, 60 * 30, noAuthMenuUrls);
            return noAuthMenuUrls;
        }

        List<Integer> menuIdList = adminMenus.stream().map(AdminMenu::getMenuId).collect(Collectors.toList());
        LambdaQueryWrapper<AdminMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //非空
        lambdaQueryWrapper.notIn(AdminMenu::getMenuId, menuIdList);
        List<AdminMenu> noAuthMenus = adminMenuService.list(lambdaQueryWrapper);
        noAuthMenus.removeIf(node -> StringUtils.isEmpty(node.getRealmUrl()));
        if (!noAuthMenus.isEmpty()) {
            noAuthMenuUrls.addAll(noAuthMenus.stream().map(AdminMenu::getRealmUrl).collect(Collectors.toList()));
            //缓存
            redis.setex(key, 60 * 30, noAuthMenuUrls);
            return noAuthMenuUrls;
        }
        //缓存
        redis.setex(key, 60 * 30, noAuthMenuUrls);
        return noAuthMenuUrls;
    }

    @Override
    public List<AdminRole> queryRoleByRoleTypeAndUserId(Integer type) {
        return getBaseMapper().queryRoleByRoleTypeAndUserId(type, UserUtil.getUserId());
    }

    /**
     * 跟进角色ID查询下属员工
     *
     * @param roleId 角色ID
     * @return userIds
     */
    @Override
    public List<Long> queryUserIdByRoleId(Integer roleId) {
        LambdaQueryChainWrapper<AdminUserRole> queryChainWrapper = adminUserRoleService.lambdaQuery().select(AdminUserRole::getUserId).eq(AdminUserRole::getRoleId, roleId);
        return queryChainWrapper.list().stream().map(AdminUserRole::getUserId).collect(Collectors.toList());
    }
}
