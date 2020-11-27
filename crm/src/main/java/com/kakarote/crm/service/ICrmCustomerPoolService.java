package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.PO.CrmCustomerPool;
import com.kakarote.crm.entity.PO.CrmCustomerPoolFieldSort;
import com.kakarote.crm.entity.VO.CrmCustomerPoolVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公海表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
public interface ICrmCustomerPoolService extends BaseService<CrmCustomerPool> {

    /**
     * 查询公海规则列表
     * @param pageEntity entity
     */
    public BasePage<CrmCustomerPoolVO> queryPoolSettingList(PageEntity pageEntity);

    /**
     * 公海选择列表
     * @return data
     */
    public List<CrmCustomerPool> queryPoolNameList();

    /**
     * 修改公海状态
     * @param poolId 公海ID
     * @param status 状态
     */
    public void changeStatus(Integer poolId, Integer status);

    /**
     *
     * @param prePoolId 原公海ID
     * @param postPoolId 转移的公海ID
     */
    public void transfer(Integer prePoolId, Integer postPoolId);

    /**
     * 根据ID查询公海信息
     * @param poolId 公海ID
     * @return data
     */
    public CrmCustomerPoolVO queryPoolById(Integer poolId);

    /**
     * 查询公海默认字段
     */
    public List<CrmModelFiledVO> queryPoolField();

    /**
     * 删除客户数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids,Integer poolId);

    /**
     * 获取客户级别选项
     * @return data
     */
    public List<String> queryCustomerLevel();

    /**
     * 设置公海规则
     * @param jsonObject obj
     */
    public void setCustomerPool(JSONObject jsonObject);

    /**
     * 查询公海字段配置
     * @param poolId 公海ID
     * @return data
     */
    public JSONObject queryPoolFieldConfig(Integer poolId);

    /**
     * 公海展示配置
     * @param object obj
     */
    public void poolFieldConfig(JSONObject object);

    /**
     * 删除公海
     */
    public void deleteCustomerPool(Integer poolId);

    /**
     * 查询公海客户列表
     */
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search);

    /**
     * 查询前台公海列表
     */
    public List<CrmCustomerPool> queryPoolNameListByAuth();

    /**
     * 查询前台公海字段
     */
    public List<CrmCustomerPoolFieldSort> queryPoolListHead(Integer poolId);

    /**
     * 查询公海权限
     * @param poolId 公海ID
     * @return auth
     */
    public JSONObject queryAuthByPoolId(Integer poolId);

    /**
     * 获取用户拥有最多权限的公海  如果没有默认取第一个
     * @param poolIdList 公海ID列表
     * @return auth
     */
    public JSONObject getOnePoolAuthByPoolIds(List<Integer> poolIdList);

    public List<Integer> queryPoolIdByUserId();

}
