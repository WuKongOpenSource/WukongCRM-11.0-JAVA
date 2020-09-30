package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.PO.CrmRoleField;

import java.util.List;

/**
 * <p>
 * 角色字段授权表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-22
 */
public interface ICrmRoleFieldService extends BaseService<CrmRoleField> {

    /**
     * 查询用户所属字段权限
     * @param label 类型
     * @param authLevel 权限等级
     * @return data
     */
    public List<CrmRoleField> queryUserFieldAuth(Integer label,Integer authLevel);

    /**
     * 查询没有权限的字段
     * @param label label
     * @return data
     */
    public List<String> queryNoAuthField(Integer label);

    /**
     * 查询字段授权设置
     * @param roleId 角色ID
     * @param label 类型
     */
    public List<CrmRoleField> queryRoleField(Integer roleId, Integer label);

    /**
     * 保存字段设置
     * @param fields 字段列表
     */
    public void saveRoleField(List<CrmRoleField> fields);

    /**
     * 内部调用，保存自定义字段
     * @param crmField 自定义字段
     */
    public void saveRoleField(CrmField crmField);

    /**
     * 内部调用，删除自定义字段
     * @param fieldId field
     */
    public void deleteRoleField(Integer fieldId);
}
