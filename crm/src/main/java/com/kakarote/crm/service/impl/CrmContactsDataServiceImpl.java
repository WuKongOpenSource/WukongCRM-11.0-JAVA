package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmContactsData;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmContactsDataMapper;
import com.kakarote.crm.service.ICrmContactsDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 联系人扩展字段数据表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class CrmContactsDataServiceImpl extends BaseServiceImpl<CrmContactsDataMapper, CrmContactsData> implements ICrmContactsDataService {

    /**
     * 设置用户数据
     *
     * @param crmModel crmModel
     */
    @Override
    public void setDataByBatchId(CrmModel crmModel) {
        List<CrmContactsData> contactsDataList = query().eq("batch_id", crmModel.getBatchId()).list();
        contactsDataList.forEach(obj -> {
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
        List<CrmContactsData> contactsDataList = new ArrayList<>();
        remove(new LambdaQueryWrapper<CrmContactsData>().eq(CrmContactsData::getBatchId, batchId));
        Date date = new Date();
        for (CrmModelFiledVO obj : array) {
            CrmContactsData contactsData = BeanUtil.copyProperties(obj, CrmContactsData.class);
            contactsData.setName(obj.getFieldName());
            contactsData.setCreateTime(date);
            contactsData.setBatchId(batchId);
            contactsDataList.add(contactsData);
        }
        saveBatch(contactsDataList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 通过batchId删除数据
     *
     * @param batchId data
     */
    @Override
    public void deleteByBatchId(List<String> batchId) {
        LambdaQueryWrapper<CrmContactsData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmContactsData::getBatchId,batchId);
        remove(wrapper);
    }
}
