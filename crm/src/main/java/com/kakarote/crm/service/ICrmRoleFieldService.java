package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.PO.CrmRoleField;

import java.util.List;
import java.util.Map;

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
     * 掩码替换值工具类
     * @param crmEnum crm类型
     * @param list 数据列表
     * @param maskType 掩码级别 0 都不隐藏 1 列表隐藏详情不隐藏 2 都隐藏
     */
    public void replaceMaskFieldValue(CrmEnum crmEnum, List<? extends Map<String, Object>> list, Integer maskType);

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

    /**
     * 格式化字段值
     * @param type 字段类型
     * @param value 字段值
     * @return 格式化后的值
     */
    public Object parseValue(Integer type, Object value);


}
