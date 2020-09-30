package com.kakarote.crm.service.impl;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmActionRecord;
import com.kakarote.crm.entity.VO.CrmActionRecordVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmActionRecordMapper;
import com.kakarote.crm.service.ICrmActionRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段操作记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
@Service
public class CrmActionRecordServiceImpl extends BaseServiceImpl<CrmActionRecordMapper, CrmActionRecord> implements ICrmActionRecordService {

    /**
     * 删除字段记录类型
     *
     * @param crmEnum 类型
     * @param ids     ids
     */
    @Override
    public void deleteActionRecord(CrmEnum crmEnum, List<Integer> ids) {
        LambdaQueryWrapper<CrmActionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmActionRecord::getTypes, crmEnum.getType());
        wrapper.in(CrmActionRecord::getActionId, ids);
        remove(wrapper);
    }

    /**
     * 查询自定义欢迎语
     *
     * @return data
     */
    @Override
    public List<String> queryRecordOptions() {
        AdminService bean = ApplicationContextHolder.getBean(AdminService.class);
        List<AdminConfig> option = bean.queryConfigByName("followRecordOption").getData();
        return option.stream().map(AdminConfig::getValue).collect(Collectors.toList());
    }

    @Override
    public List<CrmActionRecordVO> queryRecordList(Integer actionId, Integer crmTypes) {
        List<CrmActionRecordVO> recordList = getBaseMapper().queryRecordList(actionId, crmTypes);
        recordList.forEach(record -> {
            try {
                List<String> list = JSON.parseArray((String) record.getContent(), String.class);
                record.setContent(list);
            } catch (Exception e) {
                List<String> list = new ArrayList<>();
                list.add((String) record.getContent());
                record.setContent(list);
            }
        });
        return recordList;
    }

    @Override
    public List<CrmModelFiledVO> queryFieldValue(Dict kv) {
        return getBaseMapper().queryFieldValue(kv);
    }
}
