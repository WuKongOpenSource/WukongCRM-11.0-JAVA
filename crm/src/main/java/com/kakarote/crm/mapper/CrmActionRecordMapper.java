package com.kakarote.crm.mapper;

import cn.hutool.core.lang.Dict;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmActionRecord;
import com.kakarote.crm.entity.VO.CrmActionRecordVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字段操作记录表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
public interface CrmActionRecordMapper extends BaseMapper<CrmActionRecord> {

    /**
     * 查询字段操作记录列表
     * @param actionId 类型
     * @param types type
     * @return data
     */
    public List<CrmActionRecordVO> queryRecordList(@Param("actionId") Integer actionId, @Param("types") Integer types);

    List<CrmModelFiledVO> queryFieldValue(@Param("data") Dict kv);
}
