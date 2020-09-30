package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmFieldSort;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字段排序表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
public interface CrmFieldSortMapper extends BaseMapper<CrmFieldSort> {

    /**
     * 查询模块字段列表
     *
     * @param label label
     * @return data
     */
    public List<CrmFieldSortVO> queryListHead(@Param("label") Integer label,@Param("userId") Long userId);
}
