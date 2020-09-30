package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmBusinessData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmBusinessDataMapper;
import com.kakarote.crm.service.ICrmBusinessDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商机扩展字段数据表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
public class CrmBusinessDataServiceImpl extends BaseServiceImpl<CrmBusinessDataMapper, CrmBusinessData> implements ICrmBusinessDataService {
    /**
     * 设置用户数据
     *
     * @param crmModel crmModel
     */
    @Override
    public void setDataByBatchId(CrmModel crmModel) {
        List<CrmBusinessData> businessDataList = query().eq("batch_id", crmModel.getBatchId()).list();
        businessDataList.forEach(obj -> {
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
        List<CrmBusinessData> businessDataList = new ArrayList<>();
        remove(new LambdaQueryWrapper<CrmBusinessData>().eq(CrmBusinessData::getBatchId, batchId));
        Date date = new Date();
        for (CrmModelFiledVO obj : array) {
            CrmBusinessData businessData = BeanUtil.copyProperties(obj, CrmBusinessData.class);
            businessData.setName(obj.getFieldName());
            businessData.setCreateTime(date);
            businessData.setBatchId(batchId);
            businessDataList.add(businessData);
        }
        saveBatch(businessDataList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 通过batchId删除数据
     *
     * @param batchId data
     */
    @Override
    public void deleteByBatchId(List<String> batchId) {
        LambdaQueryWrapper<CrmBusinessData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmBusinessData::getBatchId,batchId);
        remove(wrapper);
    }
}
