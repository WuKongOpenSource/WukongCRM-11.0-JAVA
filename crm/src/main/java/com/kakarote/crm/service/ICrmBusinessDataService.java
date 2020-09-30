package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmBusinessData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;

/**
 * <p>
 * 商机扩展字段数据表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface ICrmBusinessDataService extends BaseService<CrmBusinessData> {
    /**
     * 设置用户数据
     * @param crmModel crmModel
     */
    public void setDataByBatchId(CrmModel crmModel);

    /**
     * 保存自定义字段数据
     * @param array data
     * @param batchId batchId
     */
    public void saveData(List<CrmModelFiledVO> array, String batchId);

    /**
     * 通过batchId删除数据
     * @param batchId data
     */
    public void deleteByBatchId(List<String> batchId);
}
