package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.constant.CrmActivityEnum;
import com.kakarote.crm.entity.BO.CrmActivityBO;
import com.kakarote.crm.entity.PO.CrmActivity;
import com.kakarote.crm.entity.VO.CrmActivityVO;

import java.util.List;

/**
 * <p>
 * crm活动表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
public interface ICrmActivityService extends BaseService<CrmActivity> {

    /**
     * 删除活动记录
     * @param crmActivityEnum 记录类型
     * @param ids ids
     */
    public void deleteActivityRecord(CrmActivityEnum crmActivityEnum, List<Integer> ids);

    /**
     * 新增活动记录
     * @param type type
     * @param activityEnum 类型
     * @param activityTypeId  类型ID
     * @param businessChange 商机变动
     */
    public void addActivity(Integer type, CrmActivityEnum activityEnum, Integer activityTypeId, String businessChange);

    /**
     * 新增活动记录
     * @param type type
     * @param activityEnum 类型
     * @param activityTypeId  类型ID
     */
    public void addActivity(Integer type, CrmActivityEnum activityEnum, Integer activityTypeId);

    /**
     * 查询活动记录列表
     * @param crmActivity activity
     * @return data
     */
    public CrmActivityVO getCrmActivityPageList(CrmActivityBO crmActivity);

    /**
     * 添加活动记录
     * @param crmActivity crmActivity
     */
    public void addCrmActivityRecord(CrmActivity crmActivity);

    /**
     * 查询文件batchId
     * @param id id
     * @param activityType 类型
     * @return data
     */
    public List<String> queryFileBatchId(Integer id,Integer activityType);

    public void buildActivityRelation(CrmActivity record);

    void deleteCrmActivityRecord(Integer activityId);

    CrmActivity updateActivityRecord(CrmActivity crmActivity);

    /**
     * 外勤签到
     */
    public void outworkSign(CrmActivity crmActivity);

    /**
     * app外勤统计
     */
    public BasePage<JSONObject> queryOutworkStats(PageEntity entity, String startTime, String endTime);

    /**
     * app外勤详情
     */
    public BasePage<CrmActivity> queryOutworkList(PageEntity entity, String startTime, String endTime, Long userId);

    /**
     * app 查询签到照片上传设置
     */
    public Integer queryPictureSetting();

    /**
     * app 查询签到照片上传设置
     */
    public void setPictureSetting(Integer status);

    /**
     * 删除外勤签到
     */
    public void deleteOutworkSign(Integer activityId);
}
