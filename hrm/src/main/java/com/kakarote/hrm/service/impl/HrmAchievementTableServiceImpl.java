package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.constant.IsEnum;
import com.kakarote.hrm.entity.BO.SetAchievementTableBO;
import com.kakarote.hrm.entity.PO.HrmAchievementAppraisal;
import com.kakarote.hrm.entity.PO.HrmAchievementSeg;
import com.kakarote.hrm.entity.PO.HrmAchievementSegItem;
import com.kakarote.hrm.entity.PO.HrmAchievementTable;
import com.kakarote.hrm.entity.VO.AchievementTableVO;
import com.kakarote.hrm.mapper.HrmAchievementTableMapper;
import com.kakarote.hrm.service.IHrmAchievementAppraisalService;
import com.kakarote.hrm.service.IHrmAchievementSegItemService;
import com.kakarote.hrm.service.IHrmAchievementSegService;
import com.kakarote.hrm.service.IHrmAchievementTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 绩效考核表模板 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmAchievementTableServiceImpl extends BaseServiceImpl<HrmAchievementTableMapper, HrmAchievementTable> implements IHrmAchievementTableService {

    @Autowired
    private IHrmAchievementSegService achievementSegService;

    @Autowired
    private IHrmAchievementSegItemService achievementSegItemService;

    @Autowired
    private IHrmAchievementAppraisalService achievementAppraisalService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HrmAchievementTable setAchievementTable(SetAchievementTableBO setAchievementTableBO) {
        HrmAchievementTable hrmAchievementTable = BeanUtil.copyProperties(setAchievementTableBO, HrmAchievementTable.class);
        Integer count = achievementAppraisalService.lambdaQuery().eq(HrmAchievementAppraisal::getTableId, hrmAchievementTable.getTableId()).count();
        if (count > 0){
            //如果有考核使用,编辑等于新建,需要把老的标记为删除
            lambdaUpdate().set(HrmAchievementTable::getStatus, 0).eq(HrmAchievementTable::getTableId, hrmAchievementTable.getTableId()).update();
            hrmAchievementTable.setTableId(null);
            save(hrmAchievementTable);
        }else {
            //如果没有考核管理直接更新并删除模板考核项
            List<Integer> segIdList = achievementSegService.lambdaQuery().select(HrmAchievementSeg::getSegId)
                    .eq(HrmAchievementSeg::getTableId, hrmAchievementTable.getTableId()).list()
                    .stream().map(HrmAchievementSeg::getSegId).collect(Collectors.toList());
            achievementSegItemService.lambdaUpdate().in(HrmAchievementSegItem::getSegId,segIdList).remove();
            achievementSegService.removeByIds(segIdList);
            updateById(hrmAchievementTable);
        }
        Integer tableId = hrmAchievementTable.getTableId();
        List<HrmAchievementSeg> fixedSegList = setAchievementTableBO.getFixedSegList();
        List<HrmAchievementSegItem> items = new ArrayList<>();
        if (CollUtil.isNotEmpty(fixedSegList)) {
            for (int i = 0; i < fixedSegList.size(); i++) {
                HrmAchievementSeg seg = fixedSegList.get(i);
                seg.setSort(i + 1);
                transferSeg(tableId, items, seg, IsEnum.YES);
            }
        }

        List<HrmAchievementSeg> noFixedSegList = setAchievementTableBO.getNoFixedSegList();
        if (CollUtil.isNotEmpty(noFixedSegList)) {
            for (int i = 0; i < noFixedSegList.size(); i++) {
                HrmAchievementSeg seg = noFixedSegList.get(i);
                seg.setSort(i + 1);
                transferSeg(tableId, items, seg, IsEnum.NO);
            }
        }
        achievementSegItemService.saveBatch(items);
        return hrmAchievementTable;
    }

    /**
     * 转换考核模板项
     */
    private void transferSeg(Integer tableId, List<HrmAchievementSegItem> items, HrmAchievementSeg seg, IsEnum isEnum) {
        seg.setSegId(null);
        seg.setIsFixed(isEnum.getValue());
        seg.setTableId(tableId);
        achievementSegService.save(seg);
        List<HrmAchievementSegItem> fixedItems = seg.getItems();
        if (CollUtil.isNotEmpty(fixedItems)) {
            for (int i = 0; i < fixedItems.size(); i++) {
                HrmAchievementSegItem item = fixedItems.get(i);
                item.setSegId(seg.getSegId());
                item.setSort(i + 1);
                item.setItemId(null);
            }
        }
        items.addAll(fixedItems);
    }

    @Override
    public AchievementTableVO queryAchievementTableById(Integer tableId) {
        HrmAchievementTable achievementTable = getById(tableId);
        if (achievementTable == null) {
            return null;
        }
        List<HrmAchievementSeg> achievementSegs = achievementSegService.lambdaQuery().eq(HrmAchievementSeg::getTableId, achievementTable.getTableId())
                .orderByAsc(HrmAchievementSeg::getSort).list();
        achievementSegs.forEach(seg -> {
            List<HrmAchievementSegItem> items = achievementSegItemService.lambdaQuery().eq(HrmAchievementSegItem::getSegId, seg.getSegId())
                    .orderByAsc(HrmAchievementSegItem::getSort).list();
            seg.setItems(items);
        });
        Map<Integer, List<HrmAchievementSeg>> collect = achievementSegs.stream().collect(Collectors.groupingBy(HrmAchievementSeg::getIsFixed));
        AchievementTableVO achievementTableVO = BeanUtil.copyProperties(achievementTable, AchievementTableVO.class);
        achievementTableVO.setFixedSegList(collect.get(IsEnum.YES.getValue()));
        achievementTableVO.setNoFixedSegList(collect.get(IsEnum.NO.getValue()));
        return achievementTableVO;
    }

    @Override
    public List<HrmAchievementTable> queryAchievementTableList() {
        return lambdaQuery().select(HrmAchievementTable::getTableId, HrmAchievementTable::getTableName, HrmAchievementTable::getType).eq(HrmAchievementTable::getStatus,1).list();
    }
}
