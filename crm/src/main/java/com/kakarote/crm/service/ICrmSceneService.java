package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmSceneConfigBO;
import com.kakarote.crm.entity.PO.CrmScene;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;

/**
 * <p>
 * 场景 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-06
 */
public interface ICrmSceneService extends BaseService<CrmScene> {

    /**
     * 查询场景
     *
     * @param crmEnum 类型
     * @return data
     */
    public List<CrmScene> queryScene(CrmEnum crmEnum);

    /**
     * 查询场景处字段
     * @param label label
     * @return data
     */
    public List<CrmModelFiledVO> queryField(Integer label);

    /**
     * 新增场景
     * @param crmScene data
     */
    public void addScene(CrmScene crmScene);

    /**
     * 修改场景
     * @param crmScene data
     */
    public void updateScene(CrmScene crmScene);

    /**
     * 保存默认场景
     * @param sceneId sceneId
     */
    public void setDefaultScene(Integer sceneId);

    /**
     * 删除场景
     * @param sceneId sceneId
     */
    public void deleteScene(Integer sceneId);

    /**
     * 查询场景设置
     * @param type type
     * @return data
     */
    public JSONObject querySceneConfig(Integer type);

    /**
     * 设置场景
     */
    public void sceneConfig(CrmSceneConfigBO config);
}
