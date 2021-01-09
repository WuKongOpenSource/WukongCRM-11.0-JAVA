package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmActivityEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.entity.BO.CrmBusinessStatusBO;
import com.kakarote.crm.entity.PO.CrmBusiness;
import com.kakarote.crm.entity.PO.CrmBusinessChange;
import com.kakarote.crm.entity.PO.CrmBusinessStatus;
import com.kakarote.crm.entity.PO.CrmBusinessType;
import com.kakarote.crm.entity.VO.CrmBusinessStatusVO;
import com.kakarote.crm.entity.VO.CrmListBusinessStatusVO;
import com.kakarote.crm.mapper.CrmBusinessTypeMapper;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 商机状态组类别 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
public class CrmBusinessTypeServiceImpl extends BaseServiceImpl<CrmBusinessTypeMapper, CrmBusinessType> implements ICrmBusinessTypeService {

    @Autowired
    private ICrmBusinessStatusService crmBusinessStatusService;

    @Autowired
    private ICrmBusinessService crmBusinessService;

    @Autowired
    private ICrmBusinessChangeService crmBusinessChangeService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private AdminService adminService;

    /**
     * 查询商机状态组列表
     *
     * @return data
     */
    @Override
    public List<CrmBusinessType> queryBusinessStatusOptions() {
        List<CrmBusinessType> list = getBaseMapper().queryBusinessStatusOptions(UserUtil.getUser().getDeptId(), UserUtil.isAdmin());
        list.forEach(businessType -> {
            List<CrmBusinessStatus> statusList = crmBusinessStatusService.lambdaQuery().eq(CrmBusinessStatus::getTypeId, businessType.getTypeId()).list();
            businessType.setStatusList(statusList);
        });
        return list;
    }

    /**
     * 商机阶段推进
     *
     * @param businessStatusBO data
     */
    @Override
    public void boostBusinessStatus(CrmBusinessStatusBO businessStatusBO) {
        CrmBusiness byId = crmBusinessService.getById(businessStatusBO.getBusinessId());
        if (byId.getStatus() != 3) {
            String statusName = "";
            if (businessStatusBO.getIsEnd() == null) {
                CrmBusinessChange change = new CrmBusinessChange();
                change.setBusinessId(businessStatusBO.getBusinessId());
                change.setStatusId(businessStatusBO.getStatusId());
                change.setCreateTime(DateUtil.date());
                change.setCreateUserId(UserUtil.getUserId());
                crmBusinessChangeService.save(change);
                byId.setStatusId(businessStatusBO.getStatusId());
                crmBusinessService.updateById(byId);
                CrmBusinessStatus one = crmBusinessStatusService.lambdaQuery().select(CrmBusinessStatus::getName)
                        .eq(CrmBusinessStatus::getStatusId, businessStatusBO.getStatusId()).one();
                statusName = one.getName();
            } else {
                if (businessStatusBO.getIsEnd() == 1) {
                    statusName = "赢单";
                } else if (businessStatusBO.getIsEnd() == 2) {
                    statusName = "输单";
                } else if (businessStatusBO.getIsEnd() == 3) {
                    statusName = "无效";
                }
                byId.setIsEnd(businessStatusBO.getIsEnd());
                byId.setStatusRemark(businessStatusBO.getStatusRemark());
                crmBusinessService.updateById(byId);
            }
            crmActivityService.addActivity(3, CrmActivityEnum.BUSINESS, businessStatusBO.getBusinessId(), statusName);
            Map<String, Object> map = new HashMap<>();
            if (businessStatusBO.getIsEnd() == null) {
                map.put("statusId", businessStatusBO.getStatusId());
            } else {
                map.put("isEnd", businessStatusBO.getIsEnd());
                map.put("statusId", null);
            }
            map.put("statusName", statusName);
            ApplicationContextHolder.getBean(CrmBusinessServiceImpl.class).updateField(map, Collections.singletonList(byId.getBusinessId()));
        }

    }


    /**
     * 查询商机阶段状态(列表展示)
     */
    @Override
    public CrmListBusinessStatusVO queryListBusinessStatus(Integer typeId, Integer statusId, Integer isEnd) {
        if (isEnd == null){
            isEnd = 0;
        }
        CrmListBusinessStatusVO crmListBusinessStatusVO = new CrmListBusinessStatusVO();
        crmListBusinessStatusVO.setIsEnd(isEnd);
        if (isEnd == 0){
            List<CrmBusinessStatus> list = crmBusinessStatusService.lambdaQuery().select(CrmBusinessStatus::getStatusId, CrmBusinessStatus::getName, CrmBusinessStatus::getOrderNum)
                    .eq(CrmBusinessStatus::getTypeId, typeId).list();
            Optional<CrmBusinessStatus> optional = list.stream().filter(businessStatus -> businessStatus.getStatusId().equals(statusId)).findAny();
            if (optional.isPresent()) {
                CrmBusinessStatus crmBusinessStatus = optional.get();
                crmListBusinessStatusVO.setStatusName(crmBusinessStatus.getName());
                crmListBusinessStatusVO.setCurrentProgress(crmBusinessStatus.getOrderNum());
                crmListBusinessStatusVO.setTotalProgress(list.size() + 1);
            }
        }
        return crmListBusinessStatusVO;
    }

    /**
     * 查询商机状态
     *
     * @param businessId 商机ID
     * @return data
     */
    @Override
    public CrmBusinessStatusVO queryBusinessStatus(Integer businessId) {
        CrmBusiness business = crmBusinessService.getById(businessId);
        CrmBusinessStatusVO businessStatusVO = BeanUtil.copyProperties(business, CrmBusinessStatusVO.class);
        List<CrmBusinessStatus> list = crmBusinessStatusService.lambdaQuery()
                .select(CrmBusinessStatus::getStatusId, CrmBusinessStatus::getName, CrmBusinessStatus::getRate, CrmBusinessStatus::getOrderNum)
                .eq(CrmBusinessStatus::getTypeId, business.getTypeId()).list();
        businessStatusVO.setStatusList(list);
        return businessStatusVO;
    }

    @Override
    public String getBusinessTypeName(int businessTypeId) {
        return lambdaQuery().select(CrmBusinessType::getName).eq(CrmBusinessType::getTypeId, businessTypeId).oneOpt()
                .map(CrmBusinessType::getName).orElse("");
    }

    /**
     * 分页查询商机类型列表
     *
     * @param entity entity
     * @return data
     */
    @Override
    public BasePage<CrmBusinessType> queryBusinessTypeList(PageEntity entity) {
        BasePage<CrmBusinessType> page = lambdaQuery().ne(CrmBusinessType::getStatus, 2).page(entity.parse());
        page.getList().forEach(crmBusinessType -> {
            crmBusinessType.setCreateName(UserCacheUtil.getUserName(crmBusinessType.getCreateUserId()));
            List<String> ids = StrUtil.splitTrim(crmBusinessType.getDeptIds(), Const.SEPARATOR);
            if (ids.size() > 0) {
                crmBusinessType.setDeptList(adminService.queryDeptByIds(ids.stream().map(Integer::valueOf).collect(Collectors.toList())).getData());
            } else {
                crmBusinessType.setDeptList(new ArrayList<>());
            }
        });
        return page;
    }

    /**
     * 根据ID获取商机状态组
     *
     * @param typeId typeId
     * @return data
     */
    @Override
    public CrmBusinessType getBusinessType(Integer typeId) {
        CrmBusinessType crmBusinessType = getById(typeId);
        List<String> ids = StrUtil.splitTrim(crmBusinessType.getDeptIds(), Const.SEPARATOR);
        if (ids.size() > 0) {
            crmBusinessType.setDeptList(adminService.queryDeptByIds(ids.stream().map(Integer::valueOf).collect(Collectors.toList())).getData());
        } else {
            crmBusinessType.setDeptList(new ArrayList<>());
        }
        List<CrmBusinessStatus> list = crmBusinessStatusService.lambdaQuery().eq(CrmBusinessStatus::getTypeId, typeId).orderByAsc(CrmBusinessStatus::getOrderNum).list();
        crmBusinessType.setStatusList(list);
        return crmBusinessType;
    }

    /**
     * 保存商机状态组
     *
     * @param object obj
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBusinessType(JSONObject object) {
        CrmBusinessType crmBusinessType = object.getObject("crmBusinessType", CrmBusinessType.class);
        List<Integer> deptIds = object.getJSONArray("deptIds").toJavaList(Integer.class);
        List<CrmBusinessStatus> crmBusinessStatusList = object.getJSONArray("crmBusinessStatus").toJavaList(CrmBusinessStatus.class);
        boolean b = crmBusinessStatusList.stream()
                .map(CrmBusinessStatus::getRate)
                .anyMatch(rate -> Integer.parseInt(rate) > 100);
        if (b) {
            throw new CrmException(CrmCodeEnum.CRM_BUSINESS_TYPE_RATE_ERROR);
        }
        crmBusinessType.setDeptIds(StrUtil.join(Const.SEPARATOR, deptIds));
        if (crmBusinessType.getTypeId() == null) {
            crmBusinessType.setCreateTime(new Date());
            crmBusinessType.setCreateUserId(UserUtil.getUserId());
            save(crmBusinessType);
        } else {
            Integer count = crmBusinessService.lambdaQuery().eq(CrmBusiness::getTypeId, crmBusinessType.getTypeId()).count();
            if (count > 0) {
                throw new CrmException(CrmCodeEnum.CRM_BUSINESS_TYPE_OCCUPY_ERROR);
            }
            crmBusinessType.setUpdateTime(new Date());
            updateById(crmBusinessType);
            crmBusinessStatusService.removeByMap(new JSONObject().fluentPut("type_id", crmBusinessType.getTypeId()));
        }
        Integer typeId = crmBusinessType.getTypeId();
        for (int i = 0; i < crmBusinessStatusList.size(); i++) {
            CrmBusinessStatus crmBusinessStatus = crmBusinessStatusList.get(i);
            crmBusinessStatus.setStatusId(null);
            crmBusinessStatus.setTypeId(typeId);
            crmBusinessStatus.setOrderNum(i + 1);
        }
        crmBusinessStatusService.saveBatch(crmBusinessStatusList);

    }

    /**
     * 删除商机状态组
     *
     * @param typeId typeId
     */
    @Override
    public void deleteById(Integer typeId) {
        CrmBusinessType byId = getById(typeId);
        byId.setStatus(2);
        updateById(byId);
    }

    /**
     * 商机状态组停启用
     *
     * @param typeId typeId
     * @param status 状态
     */
    @Override
    public void updateStatus(Integer typeId, Integer status) {
        if (!Objects.equals(1, status) && !Objects.equals(0, status)) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        CrmBusinessType byId = getById(typeId);
        byId.setStatus(status);
        updateById(byId);
    }
}
