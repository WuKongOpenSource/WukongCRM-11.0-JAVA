package com.kakarote.crm.common;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
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

    public static Map<String, String> getCrmTablePara(CrmEnum Enum) {
        Map<String, String> tableParaMap = new HashMap<>();
        switch (Enum) {
            case CUSTOMER:
            case CUSTOMER_POOL:
                tableParaMap.put("tableName", "wk_crm_customer");
                tableParaMap.put("tableId", "customer_id");
                break;
            case LEADS:
                tableParaMap.put("tableName", "wk_crm_leads");
                tableParaMap.put("tableId", "leads_id");
                break;
            case CONTRACT:
                tableParaMap.put("tableName", "wk_crm_contract");
                tableParaMap.put("tableId", "contract_id");
                break;
            case CONTACTS:
                tableParaMap.put("tableName", "wk_crm_contacts");
                tableParaMap.put("tableId", "contacts_id");
                break;
            case BUSINESS:
                tableParaMap.put("tableName", "wk_crm_business");
                tableParaMap.put("tableId", "business_id");
                break;
            case RECEIVABLES:
                tableParaMap.put("tableName", "wk_crm_receivables");
                tableParaMap.put("tableId", "receivables_id");
                break;
            case PRODUCT:
                tableParaMap.put("tableName", "wk_crm_product");
                tableParaMap.put("tableId", "product_id");
                break;
            case MARKETING:
                tableParaMap.put("tableName", "wk_crm_marketing");
                tableParaMap.put("tableId", "marketing_id");
                break;
            default:
                return tableParaMap;
        }
        return tableParaMap;
    }

    public static boolean isCrmAuth(CrmEnum crmEnum, Integer id) {
        String name = crmEnum.getTable();
        String conditions = name + "_id" + " = " + id +
                getCrmAuthSql(crmEnum, 1);
        Integer integer = ApplicationContextHolder.getBean(CrmAuthMapper.class).queryAuthNum("wk_crm_" + name, conditions);
        return integer == 0;
    }

    public static boolean isCrmOperateAuth(CrmEnum crmEnum, Integer id) {
        String name = crmEnum.getTable();
        String conditions = name + "_id" + " = " + id +
                getCrmAuthSql(crmEnum, 0);
        Integer integer = ApplicationContextHolder.getBean(CrmAuthMapper.class).queryAuthNum("wk_crm_" + name, conditions);

        return integer == 0;
    }

    public static boolean oaAuth(JSONObject record) {
        boolean auth = false;
        if (record.getString("business_ids") != null) {
            auth = isCrmAuth(CrmEnum.BUSINESS, Integer.valueOf(record.getString("business_ids")));
        } else if (record.getString("contacts_ids") != null) {
            auth = isCrmAuth(CrmEnum.CONTACTS, Integer.valueOf(record.getString("contacts_ids")));
        } else if (record.getString("contract_ids") != null) {
            auth = isCrmAuth(CrmEnum.CONTRACT, Integer.valueOf(record.getString("contract_ids")));
        } else if (record.getString("customer_ids") != null) {
            auth = isCrmAuth(CrmEnum.CUSTOMER, Integer.valueOf(record.getString("customer_ids")));
        }
        return auth;
    }

    /**
     * 团队成员是否有操作权限
     */
    public static boolean isRwAuth(Integer id, CrmEnum crmEnum) {
        String name = crmEnum.getTable();
        String conditions = name + "_id" + " = " + id +
                getCrmAuthSql(crmEnum, 0);
        Integer integer = ApplicationContextHolder.getBean(CrmAuthMapper.class).queryAuthNum("wk_crm_" + name, conditions);

        return integer == 0;
    }

    /**
     * 客户接口判断公海不需要拦截
     *
     * @param customerId 客户ID
     * @return data
     */
    public static boolean isPoolAuth(Integer customerId) {
        CrmCustomer customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).getById(customerId);
        if (customer == null || customer.getOwnerUserId() == null) {
            return false;
        } else {
            return AuthUtil.isCrmAuth(CrmEnum.CUSTOMER, customerId);
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
        userIds.retainAll(subUserIdList);
        return userIds;
    }

    public static List<Integer> filterDeptId(List<Integer> deptIds) {
        if (UserUtil.isAdmin()) {
            return deptIds;
        }
        Integer deptId = UserUtil.getUser().getDeptId();
        List<Integer> subDeptIdList = ApplicationContextHolder.getBean(AdminService.class).queryChildDeptId(deptId).getData();
        subDeptIdList.add(deptId);
        deptIds.retainAll(subDeptIdList);
        return deptIds;
    }

    public static boolean isSubDept(Integer deptId) {
        List<Integer> deptIdList = ApplicationContextHolder.getBean(AdminService.class).queryChildDeptId(UserUtil.getUser().getDeptId()).getData();
        return deptId.equals(UserUtil.getUser().getDeptId()) || deptIdList.contains(deptId);
    }

    public static boolean isSubUser(Long userId) {
        if (!UserUtil.isAdmin()) {
            List<Long> userIdList = ApplicationContextHolder.getBean(AdminService.class).queryChildUserId(userId).getData();
            return userId.equals(UserUtil.getUserId()) || userIdList.contains(userId);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static List<String> queryAuth(Map<String, Object> map, String key) {
        List<String> permissions = new ArrayList<>();
        map.keySet().forEach(str -> {
            if (map.get(str) instanceof Map) {
                permissions.addAll(queryAuth((Map<String, Object>) map.get(str), key + str + ":"));
            } else {
                permissions.add(key + str);
            }
        });
        return permissions;
    }

    /**
     * 拼客户管理数据权限sql
     *
     * @param crmEnum
     * @return
     */
    public static String getCrmAuthSql(CrmEnum crmEnum, String alias, Integer readOnly) {
        UserInfo user = UserUtil.getUser();
        Long userId = user.getUserId();
        if (UserUtil.isAdmin() || crmEnum.equals(CrmEnum.PRODUCT) || crmEnum.equals(CrmEnum.CUSTOMER_POOL)) {
            return "";
        }
        AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);
        String tableName = "wk_crm_" + crmEnum.getTable();
        StringBuilder conditions = new StringBuilder();
        List<Long> longs = adminService.queryUserByAuth(userId, StrUtil.subAfter(tableName, "wk_crm_", false)).getData();
        if (longs != null && longs.size() > 0) {
            if (crmEnum.equals(CrmEnum.MARKETING)) {
                conditions.append(" and (");
                longs.forEach(id -> conditions.append(" {alias}owner_user_id like CONCAT('%,','").append(id).append("',',%') or ").append("  {alias}relation_user_id like CONCAT('%,','").append(id).append("',',%') or"));
                conditions.delete(conditions.length() - 2, conditions.length());
            } else {
                conditions.append(" and ({alias}owner_user_id in (").append(StrUtil.join(",", longs)).append(")");
                if (crmEnum.equals(CrmEnum.CUSTOMER) || crmEnum.equals(CrmEnum.BUSINESS) || crmEnum.equals(CrmEnum.CONTRACT)) {
                    if (readOnly != null && 0 == readOnly) {
                        conditions.append(" or {alias}rw_user_id like CONCAT('%,','").append(userId).append("',',%')");
                    } else {
                        conditions.append(" or {alias}ro_user_id like CONCAT('%,','").append(userId).append("',',%')").append(" or {alias}rw_user_id like CONCAT('%,','").append(userId).append("',',%')");
                    }
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

    public static String getCrmAuthSql(CrmEnum crmEnum, Integer readOnly) {
        return getCrmAuthSql(crmEnum, "", readOnly);
    }

    /**
     * 根据数据权限查询权限范围内员工
     */
    public static List<Long> getUserIdByAuth(Integer menuId) {
        List<Long> authUserIdList = new ArrayList<>();
        AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);
        List<Long> allUserIdList = adminService.queryUserList().getData();
        if (UserUtil.isAdmin()) {
            authUserIdList = allUserIdList;
        } else {
            Integer dataType = adminService.queryDataType(UserUtil.getUserId(), menuId).getData();
            authUserIdList = getAuthUserIdByDataType(authUserIdList, allUserIdList, dataType);
        }
        return authUserIdList;
    }

    private static List<Long> getAuthUserIdByDataType(List<Long> authUserIdList, List<Long> allUserIdList, Integer dataType) {
        AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);
        if (dataType == null) {
            authUserIdList.clear();
        } else if (dataType == 1) {
            authUserIdList.add(UserUtil.getUserId());
        } else if (dataType == 2) {
            authUserIdList.addAll(adminService.queryChildUserId(UserUtil.getUserId()).getData());
            authUserIdList.add(UserUtil.getUserId());
        } else if (dataType == 3) {
            List<Long> longList = adminService.queryUserByDeptIds(Collections.singletonList(UserUtil.getUser().getDeptId())).getData();
            authUserIdList.addAll(longList);
        } else if (dataType == 4) {
            Integer deptId = UserUtil.getUser().getDeptId();
            List<Integer> deptIds = adminService.queryChildDeptId(deptId).getData();
            deptIds.add(deptId);
            List<Long> longList = adminService.queryUserByDeptIds(deptIds).getData();
            authUserIdList.addAll(longList);
        } else if (dataType == 5) {
            authUserIdList = allUserIdList;
        }
        return authUserIdList;
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

    public static Map<String, Integer> getDataTypeByUserId() {
        Long userId = UserUtil.getUserId();
        Map<String, Integer> map = BaseUtil.getRedis().get("role:dataType:" + userId);
        if (CollectionUtil.isEmpty(map)) {
            map = new HashMap<>();
            if (!UserUtil.isAdmin()) {
                AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);
                Integer leadsMenuId = 20;
                Integer leadsDataType = adminService.queryDataType(userId, leadsMenuId).getData();
                map.put("leads", leadsDataType);
                Integer customerMenuId = 29;
                Integer customerDataType = adminService.queryDataType(userId, customerMenuId).getData();
                map.put("customer", customerDataType);
                Integer contactsMenuId = 43;
                Integer contactsDataType = adminService.queryDataType(userId, contactsMenuId).getData();
                map.put("contacts", contactsDataType);
                Integer businessMenuId = 49;
                Integer businessDataType = adminService.queryDataType(userId, businessMenuId).getData();
                map.put("business", businessDataType);
                Integer contractMenuId = 56;
                Integer contractDataType = adminService.queryDataType(userId, contractMenuId).getData();
                map.put("contract", contractDataType);
                Integer receivablesMenuId = 63;
                Integer receivablesDataType = adminService.queryDataType(userId, receivablesMenuId).getData();
                map.put("receivables", receivablesDataType);
                Integer productMenuId = 68;
                Integer productDataType = adminService.queryDataType(userId, productMenuId).getData();
                map.put("product", productDataType);
                Integer recordMenuId = 441;
                Integer recordDataType = adminService.queryDataType(userId, recordMenuId).getData();
                map.put("record", recordDataType);
            } else {
                map.put("leads", 5);
                map.put("customer", 5);
                map.put("contacts", 5);
                map.put("business", 5);
                map.put("contract", 5);
                map.put("receivables", 5);
                map.put("product", 5);
                map.put("record", 5);
            }
            BaseUtil.getRedis().setex("role:dataType:" + userId.toString(), 60 * 30, map);
        }
        return map;
    }

    public static List<Long> filterUserIdListByDataType(Integer dataType, List<Long> allUserIdList) {
        List<Long> authUserIdList;
        if (UserUtil.isAdmin()){
            authUserIdList = allUserIdList;
        }else {
            authUserIdList = getAuthUserIdByDataType(new ArrayList<>(), allUserIdList, dataType);
        }
        ArrayList<Long> longs = new ArrayList<>(allUserIdList);
        longs.retainAll(authUserIdList);
        return longs;
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
