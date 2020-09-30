package com.kakarote.bi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.bi.entity.BO.AchievementBO;
import com.kakarote.bi.entity.PO.CrmAchievement;
import com.kakarote.bi.mapper.CrmAchievementMapper;
import com.kakarote.bi.service.ICrmAchievementService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 业绩目标 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-22
 */
@Service
public class CrmAchievementServiceImpl extends BaseServiceImpl<CrmAchievementMapper, CrmAchievement> implements ICrmAchievementService {

    @Autowired
    private AdminService adminService;

    /**
     * 查询业绩目标
     *
     * @param achievement bo
     * @return data
     */
    @Override
    public List<CrmAchievement> queryAchievementList(AchievementBO achievement) {
        if (achievement.getType() == null) {
            achievement.setType(2);
        }
        LambdaQueryWrapper<CrmAchievement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmAchievement::getYear, achievement.getYear());
        wrapper.eq(CrmAchievement::getType, achievement.getType());
        wrapper.eq(CrmAchievement::getStatus, achievement.getStatus());
        if (achievement.getType() == 3) {
            if (achievement.getUserId() == null) {
                List<Integer> data = adminService.queryChildDeptId(achievement.getDeptId()).getData();
                data.add(achievement.getDeptId());
                List<Long> ids = adminService.queryUserByDeptIds(data).getData();
                if (CollUtil.isEmpty(ids)){
                    return new ArrayList<>();
                }
                wrapper.in(CrmAchievement::getObjId, ids);
            } else {
                wrapper.eq(CrmAchievement::getObjId, achievement.getUserId());
            }
        } else {
            List<Integer> data = adminService.queryChildDeptId(achievement.getDeptId()).getData();
            data.add(achievement.getDeptId());
            wrapper.in(CrmAchievement::getObjId, data);
        }
        List<CrmAchievement> list = list(wrapper);
        list.forEach(crmAchievement -> {
            if (achievement.getType() == 3) {
                crmAchievement.setObjName(UserCacheUtil.getUserName(crmAchievement.getObjId().longValue()));
            } else {
                crmAchievement.setObjName(UserCacheUtil.getDeptName(crmAchievement.getObjId()));
            }
        });
        return list;
    }

    /**
     * 保存业绩目标
     *
     * @param achievement achievement
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAchievement(CrmAchievement achievement) {
        if (achievement.getObjIds().size() == 0) {
            return;
        }
        List<CrmAchievement> crmAchievements = new ArrayList<>();
        LambdaQueryWrapper<CrmAchievement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmAchievement::getType, achievement.getType());
        wrapper.eq(CrmAchievement::getYear, achievement.getYear());
        wrapper.eq(CrmAchievement::getStatus, achievement.getStatus());
        wrapper.in(CrmAchievement::getObjId, achievement.getObjIds());
        remove(wrapper);
        achievement.getObjIds().forEach(obj -> {
            CrmAchievement crmAchievement = BeanUtil.copyProperties(achievement, CrmAchievement.class);
            crmAchievement.setAchievementId(null);
            crmAchievement.setObjIds(null);
            crmAchievement.setObjId(obj);
            crmAchievements.add(crmAchievement);
        });
        saveBatch(crmAchievements);
    }
}
