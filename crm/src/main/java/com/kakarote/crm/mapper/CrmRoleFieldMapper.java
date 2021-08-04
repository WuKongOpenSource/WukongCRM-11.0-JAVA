package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmRoleField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色字段授权表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-22
 */
public interface CrmRoleFieldMapper extends BaseMapper<CrmRoleField> {

    /**
     * 查询字段授权列表
     *
     * @return data
     */
    public List<CrmRoleField> queryRoleFieldList(@Param("roleId") Integer roleId, @Param("label") Integer label);

    public List<CrmRoleField> queryMaskFieldList(@Param("maskType") Integer maskType, @Param("label") Integer label);
}
