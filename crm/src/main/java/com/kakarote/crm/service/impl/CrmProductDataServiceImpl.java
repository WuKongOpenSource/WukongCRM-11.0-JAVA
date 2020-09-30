package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmProductData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmProductDataMapper;
import com.kakarote.crm.service.ICrmProductDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 产品自定义字段存值表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class CrmProductDataServiceImpl extends BaseServiceImpl<CrmProductDataMapper, CrmProductData> implements ICrmProductDataService {

    /**
     * 设置用户数据
     *
     * @param crmModel crmModel
     */
    @Override
    public void setDataByBatchId(CrmModel crmModel) {
        List<CrmProductData> productDataList = query().eq("batch_id", crmModel.getBatchId()).list();
        productDataList.forEach(obj -> {
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
        List<CrmProductData> productDataList = new ArrayList<>();
        remove(new LambdaQueryWrapper<CrmProductData>().eq(CrmProductData::getBatchId, batchId));
        Date date = new Date();
        for (CrmModelFiledVO obj : array) {
            CrmProductData productData = BeanUtil.copyProperties(obj, CrmProductData.class);
            productData.setName(obj.getFieldName());
            productData.setCreateTime(date);
            productData.setBatchId(batchId);
            productDataList.add(productData);
        }
        saveBatch(productDataList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 通过batchId删除数据
     *
     * @param batchId data
     */
    @Override
    public void deleteByBatchId(List<String> batchId) {
        lambdaUpdate().in(CrmProductData::getBatchId,batchId).remove();
    }

}
