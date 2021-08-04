package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmReceivablesPlanData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmReceivablesPlanDataMapper;
import com.kakarote.crm.service.ICrmReceivablesPlanDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 回款计划自定义字段存值表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
@Service
public class CrmReceivablesPlanDataServiceImpl extends BaseServiceImpl<CrmReceivablesPlanDataMapper, CrmReceivablesPlanData> implements ICrmReceivablesPlanDataService {

    @Autowired
    private FieldService fieldService;

    /**
     * 设置用户数据
     *
     * @param crmModel crmModel
     */
    @Override
    public void setDataByBatchId(CrmModel crmModel) {
        List<CrmReceivablesPlanData> leadsDataList = query().eq("batch_id", crmModel.getBatchId()).list();
        leadsDataList.forEach(obj -> {
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
        List<CrmReceivablesPlanData> receivablesPlanDataList = new ArrayList<>();
        remove(new LambdaQueryWrapper<CrmReceivablesPlanData>().eq(CrmReceivablesPlanData::getBatchId, batchId));
        Date date = new Date();
        for (CrmModelFiledVO obj : array) {
            CrmReceivablesPlanData receivablesPlanData = BeanUtil.copyProperties(obj, CrmReceivablesPlanData.class);
            receivablesPlanData.setValue(fieldService.convertObjectValueToString(obj.getType(),obj.getValue(),receivablesPlanData.getValue()));
            receivablesPlanData.setName(obj.getFieldName());
            receivablesPlanData.setCreateTime(date);
            receivablesPlanData.setBatchId(batchId);
            receivablesPlanDataList.add(receivablesPlanData);
        }
        saveBatch(receivablesPlanDataList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 通过batchId删除数据
     *
     * @param batchId data
     */
    @Override
    public void deleteByBatchId(List<String> batchId) {
        LambdaQueryWrapper<CrmReceivablesPlanData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmReceivablesPlanData::getBatchId,batchId);
        remove(wrapper);
    }

}
