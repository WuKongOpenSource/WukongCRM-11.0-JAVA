package com.kakarote.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.entity.PO.CrmInstrumentSort;
import com.kakarote.crm.mapper.CrmInstrumentSortMapper;
import com.kakarote.crm.service.ICrmInstrumentSortService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 仪表盘排序表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-04
 */
@Service
public class CrmInstrumentSortServiceImpl extends BaseServiceImpl<CrmInstrumentSortMapper, CrmInstrumentSort> implements ICrmInstrumentSortService {

    /**
     * 查询模块排序
     *
     * @return data
     */
    @Override
    public JSONObject queryModelSort() {
        List<CrmInstrumentSort> list = lambdaQuery().select(CrmInstrumentSort::getModelId, CrmInstrumentSort::getIsHidden, CrmInstrumentSort::getList)
                .eq(CrmInstrumentSort::getUserId, UserUtil.getUserId()).orderByAsc(CrmInstrumentSort::getSort).list();
        if (list.size() == 0) {
            list.add(new CrmInstrumentSort(1, 0, 1));
            list.add(new CrmInstrumentSort(5, 0, 1));
            list.add(new CrmInstrumentSort(7, 0, 1));
            list.add(new CrmInstrumentSort(2, 0, 2));
            list.add(new CrmInstrumentSort(4, 0, 2));
            list.add(new CrmInstrumentSort(6, 0, 2));
        }
        Map<Integer, List<CrmInstrumentSort>> collect = list.stream().collect(Collectors.groupingBy(CrmInstrumentSort::getList));
        return new JSONObject().fluentPut("left", collect.get(1)).fluentPut("right", collect.get(2));
    }

    /**
     * 设置模块排序
     *
     * @param object obj
     */
    @Override
    public void setModelSort(JSONObject object) {
        List<CrmInstrumentSort> leftList = object.getJSONArray("left").toJavaList(CrmInstrumentSort.class);
        LambdaQueryWrapper<CrmInstrumentSort> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmInstrumentSort::getUserId, UserUtil.getUserId());
        remove(wrapper);
        List<CrmInstrumentSort> sortList = new ArrayList<>();
        for (int i = 0; i < leftList.size(); i++) {
            CrmInstrumentSort instrumentSort = leftList.get(i);
            instrumentSort.setList(1);
            instrumentSort.setUserId(UserUtil.getUserId());
            instrumentSort.setSort(i);
            sortList.add(instrumentSort);
        }
        List<CrmInstrumentSort> rightList = object.getJSONArray("right").toJavaList(CrmInstrumentSort.class);
        for (int i = 0; i < rightList.size(); i++) {
            CrmInstrumentSort crmInstrumentSort = rightList.get(i);
            crmInstrumentSort.setList(2);
            crmInstrumentSort.setUserId(UserUtil.getUserId());
            crmInstrumentSort.setSort(i);
            sortList.add(crmInstrumentSort);
        }
        saveBatch(sortList, Const.BATCH_SAVE_SIZE);
    }
}
