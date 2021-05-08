package com.kakarote.crm.common;

import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmCustomerPool;
import com.kakarote.crm.mapper.CrmAuthMapper;
import com.kakarote.crm.service.ICrmCustomerPoolService;
import com.kakarote.crm.service.ICrmCustomerService;

import java.util.*;

/**
 * 权限相关封装
 *
 * @author zhangzhiwei
 */
public class AuthUtil {

    public static boolean isCrmAuth(CrmEnum crmEnum, Integer id,CrmAuthEnum crmAuthEnum) {
        String name = crmEnum.getTable();
        String conditions = name + "_id" + " = " + id + getCrmAuthSql(crmEnum, 1,crmAuthEnum);
        Integer integer = ApplicationContextHolder.getBean(CrmAuthMapper.class).queryAuthNum("wk_crm_" + name, conditions);
        return integer == 0;
    }

    /**
     * 团队成员是否有操作权限
     */
    public static boolean isRwAuth(Integer id, CrmEnum crmEnum,CrmAuthEnum crmAuthEnum) {
        String name = crmEnum.getTable();
        String conditions = name + "_id" + " = " + id + getCrmAuthSql(crmEnum, 0,crmAuthEnum);
        Integer integer = ApplicationContextHolder.getBean(CrmAuthMapper.class).queryAuthNum("wk_crm_" + name, conditions);
        return integer == 0;
    }

    /**
     * 是否具有转移客户负责人权限
     */
    public static boolean isChangeOwnerUserAuth(Integer id, CrmEnum crmEnum,CrmAuthEnum crmAuthEnum) {
        String name = crmEnum.getTable();
        String conditions = name + "_id" + " = " + id + getCrmAuthSql(crmEnum, 3,crmAuthEnum);
        Integer integer = ApplicationContextHolder.getBean(CrmAuthMapper.class).queryAuthNum("wk_crm_" + name, conditions);
        return integer == 0;
    }

    /**
     * 客户接口判断公海不需要拦截
     *
     * @param customerId 客户ID
     * @return data
     */
    public static boolean isPoolAuth(Integer customerId,CrmAuthEnum crmAuthEnum) {
        CrmCustomer customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).getById(customerId);
        if (customer == null || customer.getOwnerUserId() == null) {
            return false;
        } else {
            return AuthUtil.isCrmAuth(CrmEnum.CUSTOMER, customerId,crmAuthEnum);
        }
    }

    /**
     * 根据当前用户的本人及下属userId取交集
     *
     * @param userIds user列表
     * @return data
     */
    public static List<Long> filterUserId(List<Long> userIds) {
        if (UserUtil.isAdmin()) {
            return userIds;
        }
        Long userId = UserUtil.getUserId();
        List<Long> subUserIdList = ApplicationContextHolder.getBean(AdminService.class).queryChildUserId(userId).getData();
        subUserIdList.add(userId);
        subUserIdList.retainAll(userIds);
        return subUserIdList;
    }

    /**
     * 根据当前用户的部门及下属部门取交集
     *
     * @param deptIds dept列表
     * @return data
     */
    public static List<Integer> filterDeptId(List<Integer> deptIds) {
        if (UserUtil.isAdmin()) {
            return deptIds;
        }
        Integer deptId = UserUtil.getUser().getDeptId();
        List<Integer> subDeptIdList = ApplicationContextHolder.getBean(AdminService.class).queryChildDeptId(deptId).getData();
        subDeptIdList.add(deptId);
        subDeptIdList.retainAll(deptIds);
        return subDeptIdList;
    }


    /**
     * 拼客户管理数据权限sql
     *
     * @param crmEnum 类型
     * @param readOnly 团队成员参数 0 要求读写权限 1 属于团队成员即可 3 禁止团队成员访问
     * @return sql
     */
    public static String getCrmAuthSql(CrmEnum crmEnum, String alias, Integer readOnly,CrmAuthEnum crmAuthEnum) {
        if (UserUtil.isAdmin() || crmEnum.equals(CrmEnum.PRODUCT) || crmEnum.equals(CrmEnum.CUSTOMER_POOL)) {
            return "";
        }
        StringBuilder conditions = new StringBuilder();
        List<Long> longs = queryAuthUserList(crmEnum, crmAuthEnum);
        if (longs != null && longs.size() > 0) {
            if (crmEnum.equals(CrmEnum.MARKETING)) {
                conditions.append(" and (");
                longs.forEach(id -> conditions.append(" {alias}owner_user_id like CONCAT('%,','").append(id).append("',',%') or ").append("  {alias}relation_user_id like CONCAT('%,','").append(id).append("',',%') or"));
                conditions.delete(conditions.length() - 2, conditions.length());
            } else {
                conditions.append(" and ({alias}owner_user_id in (").append(StrUtil.join(",", longs)).append(")");
                /* 坚持对应团队负责人权限 */
                boolean contains = Arrays.asList(CrmEnum.CUSTOMER, CrmEnum.CONTACTS, CrmEnum.BUSINESS, CrmEnum.RECEIVABLES, CrmEnum.CONTRACT).contains(crmEnum);
                if (contains && CrmAuthEnum.DELETE != crmAuthEnum && !Objects.equals(3,readOnly)) {
                    conditions.append("or {alias}").append(crmEnum.getTable()).append("_id");
                    conditions.append(" in (");
                    conditions.append("SELECT type_id FROM wk_crm_team_members where type = '")
                            .append(crmEnum.getType())
                            .append("' and user_id = '").append(UserUtil.getUserId()).append("'");
                    if(Objects.equals(0,readOnly)){
                        conditions.append(" and power ='2'");
                    }
                    conditions.append(")");
                }
            }
            conditions.append(")");
        }
        Map<String, String> map = new HashMap<>();
        if (StrUtil.isEmpty(alias)) {
            map.put("alias", "");
        } else {
            map.put("alias", alias + ".");
        }
        return StrUtil.format(conditions.toString(), map);
    }

    public static String getCrmAuthSql(CrmEnum crmEnum, Integer readOnly,CrmAuthEnum crmAuthEnum) {
        return getCrmAuthSql(crmEnum, "", readOnly,crmAuthEnum);
    }

    /**
     * 根据数据权限查询权限范围内员工
     */
    public static List<Long> getUserIdByAuth(Integer menuId) {
        return ApplicationContextHolder.getBean(AdminService.class).queryUserByAuth(UserUtil.getUserId(), menuId).getData();
    }

    /**
     * 公海管理权限判断
     */
    public static boolean isPoolAdmin(Integer poolId) {
        if (!UserUtil.isAdmin()) {
            CrmCustomerPool customerPool = ApplicationContextHolder.getBean(ICrmCustomerPoolService.class).getById(poolId);
            return customerPool == null || !StrUtil.splitTrim(customerPool.getAdminUserId(), Const.SEPARATOR).contains(UserUtil.getUserId().toString());
        }
        return false;
    }


    /**
     * 查询当前用户可查询的用户列表
     * @param crmEnum     crm类型
     * @param crmAuthEnum 数据操作权限类型
     * @return 用户列表
     */
    public static List<Long> queryAuthUserList(CrmEnum crmEnum, CrmAuthEnum crmAuthEnum) {
        Long userId = UserUtil.getUserId();
        Integer menuId = crmAuthEnum.getMenuId(crmEnum);
        String key = CrmCacheKey.CRM__AUTH_USER_CACHE_KEY + menuId.toString() + ":User:" + userId.toString();
        Redis redis = BaseUtil.getRedis();
        List<Long> userIds = redis.get(key);
        if (userIds != null && userIds.size() > 0) {
            return userIds;
        }
        userIds = ApplicationContextHolder.getBean(AdminService.class).queryUserByAuth(UserUtil.getUserId(), menuId).getData();
        redis.setex(key, 60 * 30, userIds);
        return userIds;
    }

    /**
     * 查询当前用户可查询的用户列表与数据的交集
     * @param crmEnum     crm类型
     * @param crmAuthEnum 数据操作权限类型
     * @return 用户列表
     */
    public static List<Long> filterUserIdList(CrmEnum crmEnum, CrmAuthEnum crmAuthEnum,List<Long> allUserIdList) {
        List<Long> authUserList = queryAuthUserList(crmEnum, crmAuthEnum);
        authUserList.retainAll(allUserIdList);
        return authUserList;
    }

    public static boolean isReadFollowRecord(Integer crmType) {
        int menuId;
        switch (crmType) {
            case 1:
                menuId = 20;
                break;
            case 2:
                menuId = 29;
                break;
            case 3:
                menuId = 43;
                break;
            case 5:
                menuId = 49;
                break;
            case 6:
                menuId = 56;
                break;
            default:
                return false;
        }
        int followRecordReadMenuId = 441;
        return ApplicationContextHolder.getBean(CrmAuthMapper.class).queryReadFollowRecord(menuId, followRecordReadMenuId, UserUtil.getUserId()) > 0;
    }
}
