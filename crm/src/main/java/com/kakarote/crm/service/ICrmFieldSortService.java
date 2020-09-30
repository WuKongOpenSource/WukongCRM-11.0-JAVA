package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmFieldSort;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;

import java.util.List;

/**
 * <p>
 * 字段排序表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
public interface ICrmFieldSortService extends BaseService<CrmFieldSort> {

    /**
     * 查询模块字段列表
     * @param label label
     * @return data
     */
    public List<CrmFieldSortVO> queryListHead(Integer label);
}
