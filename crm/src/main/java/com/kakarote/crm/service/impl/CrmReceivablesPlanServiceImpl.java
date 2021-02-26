package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmBackLogEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmReceivablesPlanBO;
import com.kakarote.crm.entity.PO.CrmContract;
import com.kakarote.crm.entity.PO.CrmReceivables;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmReceivablesPlanMapper;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 回款计划表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Service
public class CrmReceivablesPlanServiceImpl extends BaseServiceImpl<CrmReceivablesPlanMapper, CrmReceivablesPlan> implements ICrmReceivablesPlanService {

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmReceivablesService crmReceivablesService;

    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    /**
     * 保存或修改
     *
     * @param crmModel data
     */
    @Override
    public void saveAndUpdate(CrmBusinessSaveBO crmModel) {
        CrmReceivablesPlan crmReceivablesPlan = BeanUtil.copyProperties(crmModel.getEntity(), CrmReceivablesPlan.class);
        CrmContract crmContract = crmContractService.getById(crmReceivablesPlan.getContractId());
        if (crmContract == null || !crmContract.getCheckStatus().equals(1)) {
            throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_ADD_ERROR);
        }
        String batchId = StrUtil.isNotEmpty(crmReceivablesPlan.getFileBatch()) ? crmReceivablesPlan.getFileBatch() : IdUtil.simpleUUID();
        if (null == crmReceivablesPlan.getPlanId()) {
            crmReceivablesPlan.setCreateTime(DateUtil.date());
            crmReceivablesPlan.setCreateUserId(UserUtil.getUserId());
            crmReceivablesPlan.setFileBatch(batchId);
            CrmReceivablesPlan receivablesPlan = lambdaQuery().eq(CrmReceivablesPlan::getContractId, crmReceivablesPlan.getContractId()).orderByDesc(CrmReceivablesPlan::getNum).last("limit 1").one();
            if (receivablesPlan == null) {
                crmReceivablesPlan.setNum("1");
            } else {
                crmReceivablesPlan.setNum(Integer.valueOf(receivablesPlan.getNum()) + 1 + "");
            }
            save(crmReceivablesPlan);
        } else {

            Integer number = crmReceivablesService.lambdaQuery().eq(CrmReceivables::getPlanId, crmReceivablesPlan.getPlanId()).count();
            if (number > 0) {
                throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_PLAN_ERROR);
            }
            if (crmReceivablesPlan.getContractId() != null) {
                crmBackLogDealService.deleteByType(crmContract.getOwnerUserId(), CrmEnum.RECEIVABLES_PLAN, CrmBackLogEnum.REMIND_RECEIVABLES_PLAN, crmReceivablesPlan.getPlanId());
            }
            crmReceivablesPlan.setUpdateTime(DateUtil.date());
            updateById(crmReceivablesPlan);
        }
    }

    /**
     * 删除ids
     *
     * @param ids ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        removeByIds(ids);
        crmReceivablesService.lambdaUpdate().in(CrmReceivables::getPlanId,ids).set(CrmReceivables::getPlanId,null).update();
    }

    /**
     * 查询新增所需字段
     *
     * @param id id
     */
    @Override
    public List<CrmModelFiledVO> queryField(Integer id) {
        Map<String, Object> map;
        if (id != null) {
            map = getBaseMapper().queryUpdateField(id);
        } else {
            map = new HashMap<>();
        }
        List<CrmModelFiledVO> fieldList = new ArrayList<>();
        fieldList.add(new CrmModelFiledVO("customer_id", FieldEnum.CUSTOMER,1).setName("客户名称").setIsNull(1));
        fieldList.add(new CrmModelFiledVO("contract_id", FieldEnum.CONTRACT,1).setName("合同编号").setIsNull(1));
        fieldList.add(new CrmModelFiledVO("money", FieldEnum.NUMBER,1).setName("计划回款金额").setIsNull(1));
        fieldList.add(new CrmModelFiledVO("return_date", FieldEnum.DATE,1).setName("计划回款日期").setIsNull(1));
        fieldList.add(new CrmModelFiledVO("return_type", FieldEnum.SELECT,1).setName("计划回款方式").setIsNull(1));
        fieldList.add(new CrmModelFiledVO("remind", FieldEnum.NUMBER,1).setName("提前几天提醒").setIsNull(0));
        fieldList.add(new CrmModelFiledVO("remark", FieldEnum.TEXTAREA,1).setName("备注").setIsNull(0));
        fieldList.forEach(field -> {
            field.setAuthLevel(3);
            if ("returnType".equals(field.getFieldName())) {
                field.setValue(map.get(field.getFieldName()));
                field.setSetting(Arrays.asList("支票", "现金", "邮政汇款", "电汇", "网上转账", "支付宝", "微信支付", "其他"));
                return;
            }
            if (map.containsKey(field.getFieldName())) {
                if ("customerId".equals(field.getFieldName())) {
                    List<JSONObject> list = new ArrayList<>();
                    list.add(new JSONObject().fluentPut("customerId", map.get("customerId")).fluentPut("customerName", map.get("customerName")));
                    field.setValue(list);
                } else if ("contractId".equals(field.getFieldName())) {
                    List<JSONObject> list = new ArrayList<>();
                    list.add(new JSONObject().fluentPut("contractId", map.get("contractId")).fluentPut("num", map.get("num")));
                    field.setValue(list);
                } else {
                    field.setValue(map.get(field.getFieldName()));
                }
                field.setSetting(new ArrayList<>());
            } else {
                field.setValue("");
                field.setSetting(new ArrayList<>());
            }
        });
        return fieldList;
    }

    /**
     * 根据客户ID查询未被使用回款计划
     *
     * @param crmReceivablesPlanBO param
     * @return data
     */
    @Override
    public List<CrmReceivablesPlan> queryByContractAndCustomer(CrmReceivablesPlanBO crmReceivablesPlanBO) {
        List<CrmReceivablesPlan> list = lambdaQuery().isNull(CrmReceivablesPlan::getReceivablesId)
                .eq(CrmReceivablesPlan::getContractId, crmReceivablesPlanBO.getContractId())
                .eq(CrmReceivablesPlan::getCustomerId, crmReceivablesPlanBO.getCustomerId())
                .list();
        return list;
    }

    /**
     * 根据合同查询回款计划
     */
    @Override
    public void qureyListByContractId(CrmReceivablesPlanBO crmReceivablesPlanBO) {

    }

    @Override
    public void queryReceivablesPlansByContractId(CrmReceivablesPlanBO crmReceivablesPlanBO) {

    }

    @Override
    public String getReceivablesPlanNum(int planId) {
        return lambdaQuery().select(CrmReceivablesPlan::getNum).eq(CrmReceivablesPlan::getPlanId,planId).oneOpt()
                .map(CrmReceivablesPlan::getNum).orElse("");
    }
}
