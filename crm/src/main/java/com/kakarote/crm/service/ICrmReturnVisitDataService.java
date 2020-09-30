package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmReturnVisitData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
public interface ICrmReturnVisitDataService extends BaseService<CrmReturnVisitData> {
    /**
     * 保存自定义字段数据
     * @param array array
     * @param batchId batchId
     */
    void saveData(List<CrmModelFiledVO> array, String batchId);

    void setDataByBatchId(CrmModel crmModel);

    void deleteByBatchId(List<String> batchList);
}
