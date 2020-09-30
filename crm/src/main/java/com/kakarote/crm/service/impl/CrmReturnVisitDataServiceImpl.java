package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmReturnVisitData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmReturnVisitDataMapper;
import com.kakarote.crm.service.ICrmReturnVisitDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@Service
public class CrmReturnVisitDataServiceImpl extends BaseServiceImpl<CrmReturnVisitDataMapper, CrmReturnVisitData> implements ICrmReturnVisitDataService {

    /**
     * 保存自定义字段数据
     * @param array data
     * @param batchId batchId
     */
    @Override
    public void saveData(List<CrmModelFiledVO> array, String batchId) {
        List<CrmReturnVisitData> crmReturnVisitDataList = new ArrayList<>();
        remove(new LambdaQueryWrapper<CrmReturnVisitData>().eq(CrmReturnVisitData::getBatchId, batchId));
        Date date = new Date();
        for (CrmModelFiledVO obj : array) {
            CrmReturnVisitData crmCustomerData = BeanUtil.copyProperties(obj, CrmReturnVisitData.class);
            crmCustomerData.setName(obj.getFieldName());
            crmCustomerData.setCreateTime(date);
            crmCustomerData.setBatchId(batchId);
            crmReturnVisitDataList.add(crmCustomerData);
        }
        saveBatch(crmReturnVisitDataList, Const.BATCH_SAVE_SIZE);
    }

    @Override
    public void setDataByBatchId(CrmModel crmModel) {
        List<CrmReturnVisitData> crmReturnVisitDataList = query().eq("batch_id", crmModel.getBatchId()).list();
        crmReturnVisitDataList.forEach(obj -> {
            crmModel.put(obj.getName(), obj.getValue());
        });
    }

    @Override
    public void deleteByBatchId(List<String> batchList) {

    }
}
