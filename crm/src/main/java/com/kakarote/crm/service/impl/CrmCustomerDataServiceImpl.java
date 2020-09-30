package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmCustomerData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmCustomerDataMapper;
import com.kakarote.crm.service.ICrmCustomerDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户扩展字段数据表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Service
public class CrmCustomerDataServiceImpl extends BaseServiceImpl<CrmCustomerDataMapper, CrmCustomerData> implements ICrmCustomerDataService {
    /**
     * 设置用户数据
     *
     * @param crmModel crmModel
     */
    @Override
    public void setDataByBatchId(CrmModel crmModel) {
        List<CrmCustomerData> customerDataList = query().eq("batch_id", crmModel.getBatchId()).list();
        customerDataList.forEach(obj -> {
            crmModel.put(obj.getName(), obj.getValue());
        });
    }

    /**
     * 保存自定义字段数据
     *
     * @param array data
     */
    @Override
    public void saveData(List<CrmModelFiledVO> array, String batchId) {
        List<CrmCustomerData> customerDataList = new ArrayList<>();
        remove(new LambdaQueryWrapper<CrmCustomerData>().eq(CrmCustomerData::getBatchId, batchId));
        Date date = new Date();
        for (CrmModelFiledVO obj : array) {
            CrmCustomerData customerData = BeanUtil.copyProperties(obj, CrmCustomerData.class);
            customerData.setName(obj.getFieldName());
            customerData.setCreateTime(date);
            customerData.setBatchId(batchId);
            customerDataList.add(customerData);
        }
        saveBatch(customerDataList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 通过batchId删除数据
     *
     * @param batchId data
     */
    @Override
    public void deleteByBatchId(List<String> batchId) {
        LambdaQueryWrapper<CrmCustomerData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmCustomerData::getBatchId, batchId);
        remove(wrapper);
    }
}
