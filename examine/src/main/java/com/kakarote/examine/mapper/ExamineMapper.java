package com.kakarote.examine.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.examine.entity.PO.Examine;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
public interface ExamineMapper extends BaseMapper<Examine> {

    BasePage<Examine> selectPartExaminePage(BasePage<Object> parse, @Param("label") Integer label, @Param("isAdmin") boolean isAdmin,
                                            @Param("isPart") boolean isPart,@Param("userId") Long userId,@Param("deptId") Integer deptId);

}
