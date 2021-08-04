package com.kakarote.hrm.mapper;

import cn.hutool.core.lang.Dict;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmActionRecord;
import com.kakarote.hrm.entity.VO.HrmModelFiledVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * hrm员工操作记录表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmActionRecordMapper extends BaseMapper<HrmActionRecord> {
    /**
     *
     * @param kv 字典值
     * @return
     */
    List<HrmModelFiledVO> queryFieldValue(@Param("data") Dict kv);
}
