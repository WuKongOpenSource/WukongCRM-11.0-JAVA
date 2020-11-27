package com.kakarote.bi.service;

import com.kakarote.bi.entity.BO.AchievementBO;
import com.kakarote.bi.entity.PO.CrmAchievement;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 业绩目标 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-22
 */
public interface ICrmAchievementService extends BaseService<CrmAchievement> {

    /**
     * 查询业绩目标
     * @param achievementBO bo
     * @return data
     */
    public List<CrmAchievement> queryAchievementList(AchievementBO achievementBO);

    /**
     * 保存业绩目标
     * @param achievement achievement
     */
    public void addAchievement(CrmAchievement achievement);


    /**
     * 验证业绩目标数据
     * @date 2020/11/19 14:39
     * @param crmAchievements
     * @return void
     **/
    public void verifyCrmAchievementData(List<CrmAchievement> crmAchievements);

}
