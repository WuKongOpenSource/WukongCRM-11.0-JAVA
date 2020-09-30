package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmContractData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmContractDataMapper;
import com.kakarote.crm.service.ICrmContractDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 合同扩展字段数据表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
public class CrmContractDataServiceImpl extends BaseServiceImpl<CrmContractDataMapper, CrmContractData> implements ICrmContractDataService {

    /**
     * 设置用户数据
     *
     * @param crmModel crmModel
     */
    @Override
    public void setDataByBatchId(CrmModel crmModel) {
        List<CrmContractData> contractDataList = query().eq("batch_id", crmModel.getBatchId()).list();
        contractDataList.forEach(obj -> {
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
        List<CrmContractData> contractDataList = new ArrayList<>();
        remove(new LambdaQueryWrapper<CrmContractData>().eq(CrmContractData::getBatchId, batchId));
        Date date = new Date();
        for (CrmModelFiledVO obj : array) {
            CrmContractData crmContractData = BeanUtil.copyProperties(obj, CrmContractData.class);
            crmContractData.setName(obj.getFieldName());
            crmContractData.setCreateTime(date);
            crmContractData.setBatchId(batchId);
            contractDataList.add(crmContractData);
        }
        saveBatch(contractDataList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 通过batchId删除数据
     *
     * @param batchId data
     */
    @Override
    public void deleteByBatchId(List<String> batchId) {
        LambdaQueryWrapper<CrmContractData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmContractData::getBatchId,batchId);
        remove(wrapper);
    }
}
