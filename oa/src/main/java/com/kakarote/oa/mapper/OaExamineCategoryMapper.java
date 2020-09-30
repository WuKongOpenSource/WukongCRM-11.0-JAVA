package com.kakarote.oa.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 审批类型表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface OaExamineCategoryMapper extends BaseMapper<OaExamineCategory> {

    List<OaExamineCategory> queryAllExamineCategoryList(@Param("isAdmin") boolean isAdmin,@Param("userId") Long userId,@Param("deptId") Integer deptId);
}
