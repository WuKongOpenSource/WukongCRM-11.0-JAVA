package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmBusinessType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商机状态组类别 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface CrmBusinessTypeMapper extends BaseMapper<CrmBusinessType> {

    List<CrmBusinessType> queryBusinessStatusOptions(@Param("deptId") Integer deptId, @Param("isAdmin") boolean isAdmin);
}
