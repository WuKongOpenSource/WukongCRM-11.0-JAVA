package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.BO.CrmBusinessStatusBO;
import com.kakarote.crm.entity.PO.CrmBusinessType;
import com.kakarote.crm.entity.VO.CrmBusinessStatusVO;
import com.kakarote.crm.entity.VO.CrmListBusinessStatusVO;

import java.util.List;

/**
 * <p>
 * 商机状态组类别 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface ICrmBusinessTypeService extends BaseService<CrmBusinessType> {

    /**
     * 查询商机状态组列表
     *
     * @return data
     */
    public List<CrmBusinessType> queryBusinessStatusOptions();

    /**
     * 商机阶段推进
     *
     * @param businessStatusBO data
     */
    public void boostBusinessStatus(CrmBusinessStatusBO businessStatusBO);

    CrmListBusinessStatusVO queryListBusinessStatus(Integer typeId, Integer statusId, Integer isEnd);
    /**
     * 查询商机状态
     *
     * @param businessId 商机ID
     * @return data
     */
    public CrmBusinessStatusVO queryBusinessStatus(Integer businessId);

    String getBusinessTypeName(int businessTypeId);

    /**
     * 分页查询商机类型列表
     *
     * @param entity entity
     * @return data
     */
    public BasePage<CrmBusinessType> queryBusinessTypeList(PageEntity entity);

    /**
     * 根据ID获取商机状态组
     *
     * @param typeId typeId
     * @return data
     */
    public CrmBusinessType getBusinessType(Integer typeId);

    /**
     * 保存商机状态组
     *
     * @param object obj
     */
    public void addBusinessType(JSONObject object);

    /**
     * 删除商机状态组
     *
     * @param typeId typeId
     */
    public void deleteById(Integer typeId);

    /**
     * 商机状态组停启用
     *
     * @param typeId typeId
     * @param status 状态
     */
    public void updateStatus(Integer typeId, Integer status);
}
