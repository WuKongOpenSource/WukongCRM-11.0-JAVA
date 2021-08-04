package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.ExcelParseUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.CrmBackLogEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmReceivablesPlanBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmReceivablesPlanMapper;
import com.kakarote.crm.service.*;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 回款计划表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Service
public class CrmReceivablesPlanServiceImpl extends BaseServiceImpl<CrmReceivablesPlanMapper, CrmReceivablesPlan> implements ICrmReceivablesPlanService,CrmPageService {

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmReceivablesService crmReceivablesService;

    @Autowired
    private ICrmReceivablesPlanDataService receivablesPlanDataService;

    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private FieldService fieldService;

    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        CrmSearchBO search1 = ObjectUtil.cloneByStream(search);
        BasePage<Map<String, Object>> basePage = queryList(search, false);
        SearchRequest searchRequest = new SearchRequest(getIndex());
        searchRequest.types(getDocType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = createQueryBuilder(search1);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.aggregation(AggregationBuilders.sum("realReceivedMoney").field("realReceivedMoney"));
        sourceBuilder.aggregation(AggregationBuilders.sum("unreceivedMoney").field("unreceivedMoney"));
        sourceBuilder.aggregation(AggregationBuilders.sum("planReceivedMoney").field("money"));
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchCount = getRestTemplate().getClient().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchCount.getAggregations();
            Map<String, Object> countMap = new HashMap<>();
            ParsedSum realReceivedMoney = aggregations.get("realReceivedMoney");
            ParsedSum unreceivedMoney = aggregations.get("unreceivedMoney");
            ParsedSum planReceivedMoney = aggregations.get("planReceivedMoney");
            countMap.put("realReceivedMoney", realReceivedMoney.getValue());
            countMap.put("unreceivedMoney", unreceivedMoney.getValue());
            countMap.put("planReceivedMoney", planReceivedMoney.getValue());
            JSONObject jsonObject = new JSONObject().fluentPut("money",countMap);
            basePage.setExtraData(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basePage;
    }

    @Override
    public CrmModel queryById(Integer id) {
        CrmModel crmModel;
        if (id != null) {
            crmModel = getBaseMapper().queryById(id);
            crmModel.setLabel(getLabel().getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            receivablesPlanDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
        } else {
            crmModel = new CrmModel(getLabel().getType());
        }
        return crmModel;
    }


    @Override
    public void exportExcel(HttpServletResponse response, CrmSearchBO search) {
        List<Map<String, Object>> dataList = queryList(search,true).getList();
        List<CrmFieldSortVO> headList = crmFieldService.queryListHead(getLabel().getType());
        ExcelParseUtil.exportExcel(dataList, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {
                for (String fieldName : headMap.keySet()) {
                    record.put(fieldName, ActionRecordUtil.parseValue(record.get(fieldName),headMap.get(fieldName),false));
                }
                if (ObjectUtil.isEmpty(record.get("receivedStatus"))) {
                    return;
                }
                String receivedStatus;
                switch (TypeUtils.castToInt(record.get("receivedStatus"))) {
                    case 1:
                        receivedStatus = "回款完成";
                        break;
                    case 2:
                        receivedStatus = "部分回款";
                        break;
                    case 3:
                        receivedStatus = "作废";
                        break;
                    case 4:
                        receivedStatus = "逾期";
                        break;
                    case 5:
                        receivedStatus = "待生效";
                        break;
                    default:
                        receivedStatus = "待审核";
                }
                record.put("receivedStatus", receivedStatus);
            }
            @Override
            public String getExcelName() {
                return "回款计划";
            }
        },headList,response);
    }

    /**
     * 批量保存回款计划
     * @param receivablesPlans 回款计划列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<CrmReceivablesPlanBO> receivablesPlans){
        List<String> batchIdList = new ArrayList<>();
        CrmReceivablesPlanBO receivablesPlanBO = receivablesPlans.get(0);
        Integer contractId = receivablesPlanBO.getContractId();
        CrmContract crmContract = crmContractService.getById(contractId);
        if (crmContract == null || !Arrays.asList(0,1,3,10).contains(crmContract.getCheckStatus())) {
            throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_PLAN_ADD_ERROR);
        }
        CrmReceivablesPlan receivablesPlanNum = lambdaQuery().eq(CrmReceivablesPlan::getContractId, receivablesPlanBO.getContractId()).orderByDesc(CrmReceivablesPlan::getNum).last("limit 1").one();
        int num;
        if (receivablesPlanNum == null) {
            num = 0;
        } else {
            num = Integer.parseInt(receivablesPlanNum.getNum());
        }
        Integer receivedStatus = getReceivedStatus(crmContract.getCheckStatus());
        UserInfo userInfo = UserUtil.getUser();
        String customerName = crmCustomerService.getCustomerName(receivablesPlanBO.getCustomerId());
        List<CrmReceivablesPlan> receivablesPlanList = new ArrayList<>();
        for (CrmReceivablesPlanBO bo : receivablesPlans) {
            String uuid = IdUtil.simpleUUID();
            batchIdList.add(uuid);
            CrmReceivablesPlan receivablesPlan = BeanUtil.copyProperties(bo,CrmReceivablesPlan.class);
            receivablesPlan.setBatchId(uuid);
            receivablesPlan.setCreateTime(DateUtil.date());
            receivablesPlan.setCreateUserId(userInfo.getUserId());
            receivablesPlan.setOwnerUserId(userInfo.getUserId());
            receivablesPlan.setUpdateTime(new Date());
            receivablesPlan.setNum(String.valueOf(++num));
            receivablesPlan.setUnreceivedMoney(receivablesPlan.getMoney());
            receivablesPlan.setReceivedStatus(receivedStatus);
            receivablesPlan.setRealReceivedMoney(BigDecimal.ZERO);
            receivablesPlanList.add(receivablesPlan);
        }
        saveBatch(receivablesPlanList,100);
        receivablesPlanList  = lambdaQuery().in(CrmReceivablesPlan::getBatchId,batchIdList).list();
        BulkRequest bulkRequest = new BulkRequest();
        for (CrmReceivablesPlan receivablesPlan : receivablesPlanList) {
            Map<String,Object> map = new HashMap<>();
            IndexRequest request = new IndexRequest(getIndex(),getDocType(),receivablesPlan.getReceivablesPlanId().toString());
            map.put("contractId",receivablesPlan.getContractId());
            map.put("contractNum",crmContract.getNum());
            map.put("contractMoney",crmContract.getMoney());
            map.put("customerId",receivablesPlan.getCustomerId());
            map.put("customerName",customerName);
            map.put("money",receivablesPlan.getMoney());
            map.put("returnDate",DateUtil.formatDate(receivablesPlan.getReturnDate()));
            map.put("returnType",receivablesPlan.getReturnType());
            map.put("remind",receivablesPlan.getRemind());
            map.put("num",receivablesPlan.getNum());
            map.put("remark",receivablesPlan.getRemark());
            map.put("ownerUserId", userInfo.getUserId());
            map.put("ownerUserName", userInfo.getRealname());
            map.put("createUserId", userInfo.getUserId());
            map.put("createUserName",userInfo.getRealname());
            map.put("createTime",DateUtil.formatDateTime(receivablesPlan.getCreateTime()));
            map.put("updateTime",DateUtil.formatDateTime(receivablesPlan.getUpdateTime()));
            SimpleUser simpleUser = UserCacheUtil.getSimpleUser(userInfo.getUserId());
            map.put("ownerDeptId",simpleUser.getDeptId());
            map.put("ownerDeptName",simpleUser.getDeptName());
            map.put("unreceivedMoney",receivablesPlan.getUnreceivedMoney());
            map.put("realReceivedMoney",0);
            map.put("receivedStatus",receivablesPlan.getReceivedStatus());
            request.source(map);
            bulkRequest.add(request);
        }
        try {
            ElasticsearchRestTemplate restTemplate = getRestTemplate();
            restTemplate.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            restTemplate.refresh(getIndex());
        } catch (Exception ex){
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }

    }

    @Override
    public void deleteByContractId(Integer contractId) {
        LambdaQueryWrapper<CrmReceivablesPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CrmReceivablesPlan::getReceivablesPlanId);
        queryWrapper.eq(CrmReceivablesPlan::getContractId,contractId);
        List<Integer> ids = listObjs(queryWrapper, TypeUtils::castToInt);
        deleteByIds(ids);
    }

    /**
     * 修改回款计划状态
     * @param crmEnum crmEnum
     * @param object 对应的PO对象
     * @param examineStatus 审批状态
     */
    @Override
    public void updateReceivedStatus(CrmEnum crmEnum,Object object,Integer examineStatus) {
        if(crmEnum == CrmEnum.CONTRACT) {
            CrmContract contract = BeanUtil.copyProperties(object,CrmContract.class);
            LambdaQueryWrapper<CrmReceivablesPlan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(CrmReceivablesPlan::getReceivablesPlanId);
            queryWrapper.eq(CrmReceivablesPlan::getContractId,contract.getContractId());
            List<Integer> ids = listObjs(queryWrapper, TypeUtils::castToInt);
            if(ids.size() > 0) {
                if (Objects.equals(1, examineStatus) || Objects.equals(10, examineStatus)) {
                    lambdaUpdate().set(CrmReceivablesPlan::getReceivedStatus, 0).in(CrmReceivablesPlan::getReceivablesPlanId, ids).update();
                    ElasticUtil.updateField(getRestTemplate(), "receivedStatus", 0, ids, getIndex());
                } else if (Objects.equals(2, examineStatus)) {
                    lambdaUpdate().set(CrmReceivablesPlan::getReceivedStatus, 3).in(CrmReceivablesPlan::getReceivablesPlanId, ids).update();
                    ElasticUtil.updateField(getRestTemplate(), "receivedStatus", 3, ids, getIndex());
                }
            }
        } else if(crmEnum == CrmEnum.RECEIVABLES) {
            CrmReceivables receivables = BeanUtil.copyProperties(object,CrmReceivables.class);
            if((Objects.equals(1,examineStatus) || Objects.equals(10,examineStatus) ) && receivables.getReceivablesPlanId() != null) {
                List<Integer> statuss =  new ArrayList<>();
                statuss.add(1);
                statuss.add(10);
                List<CrmReceivables> receivablesList = crmReceivablesService.lambdaQuery().eq(CrmReceivables::getReceivablesPlanId,receivables.getReceivablesPlanId()).in(CrmReceivables::getCheckStatus,statuss).list();
                if (receivablesList.size() > 0){
                    BigDecimal crmReceivablesMoney = BigDecimal.ZERO;
                    for (CrmReceivables crmReceivables: receivablesList) {
                        crmReceivablesMoney = crmReceivablesMoney.add(crmReceivables.getMoney());
                    }
                    CrmReceivablesPlan crmReceivablesPlan = getById(receivables.getReceivablesPlanId());
                    if (crmReceivablesMoney.compareTo(BigDecimal.ZERO) == 1){
                        Map<String, Object> map = new HashMap<>();
                        if (crmReceivablesPlan.getMoney().compareTo(crmReceivablesMoney) == 1){
                            crmReceivablesPlan.setReceivedStatus(2);
                            map.put("receivedStatus",2);
                        }else {
                            crmReceivablesPlan.setReceivedStatus(1);
                            map.put("receivedStatus",1);
                        }
                        crmReceivablesPlan.setRealReceivedMoney(crmReceivablesMoney);
                        crmReceivablesPlan.setUnreceivedMoney(crmReceivablesPlan.getMoney().subtract(crmReceivablesMoney));
                        crmReceivablesPlan.setRealReturnDate(DateUtil.date());
                        updateById(crmReceivablesPlan);
                        map.put("realReceivedMoney", crmReceivablesMoney);
                        map.put("unreceivedMoney", crmReceivablesPlan.getMoney().subtract(crmReceivablesMoney));
                        map.put("realReturnDate", DateUtil.date().toString());
                        ElasticUtil.updateField(getRestTemplate(), map, receivables.getReceivablesPlanId(), getIndex());
                    }
                }
            }
        }
    }

    @Override
    public void updateReceivedStatus() {
        List<CrmReceivablesPlan> plans = getBaseMapper().queryReceivablesPlans();
        for (CrmReceivablesPlan receivablesPlan: plans) {
            receivablesPlan.setReceivedStatus(4);
            updateById(receivablesPlan);
            Map<String, Object> map = new HashMap<>();
            map.put("receivedStatus",4);
            ElasticUtil.updateField(getRestTemplate(), map, receivablesPlan.getReceivablesPlanId(), getIndex());
        }
    }

    /**
     * 获取回款计划状态
     * @param checkStatus 当前合同审核状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废
     * @return 回款计划状态  0 待回款 1 回款完成 2 部分回款 3 作废 4 逾期 5 待生效
     */
    private Integer getReceivedStatus(Integer checkStatus) {
        if(checkStatus == null) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        switch (checkStatus) {
            case 0:
            case 3:
                return 5;
            case 1:
            case 10:
                return 0;
            default:return 3;
        }
    }

    /**
     * 保存或修改
     *
     * @param crmModel data
     */
    @Override
    public void addOrUpdate(CrmBusinessSaveBO crmModel) {
        CrmReceivablesPlan crmReceivablesPlan = BeanUtil.copyProperties(crmModel.getEntity(), CrmReceivablesPlan.class);
        CrmContract crmContract = crmContractService.getById(crmReceivablesPlan.getContractId());
        if (crmContract == null || !Arrays.asList(0,1,3,10).contains(crmContract.getCheckStatus())) {
            throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_PLAN_ADD_ERROR);
        }
        Integer receivedStatus = getReceivedStatus(crmContract.getCheckStatus());
        String batchId = StrUtil.isNotEmpty(crmReceivablesPlan.getBatchId()) ? crmReceivablesPlan.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_receivables_plan_data"));
        receivablesPlanDataService.saveData(crmModel.getField(), batchId);
        if (null == crmReceivablesPlan.getReceivablesPlanId()) {
            crmReceivablesPlan.setCreateTime(DateUtil.date());
            crmReceivablesPlan.setCreateUserId(UserUtil.getUserId());
            crmReceivablesPlan.setOwnerUserId(UserUtil.getUserId());
            crmReceivablesPlan.setUpdateTime(new Date());
            crmReceivablesPlan.setBatchId(batchId);
            crmReceivablesPlan.setUnreceivedMoney(crmReceivablesPlan.getMoney());
            crmReceivablesPlan.setRealReceivedMoney(BigDecimal.ZERO);
            crmReceivablesPlan.setReceivedStatus(receivedStatus);
            CrmReceivablesPlan receivablesPlan = lambdaQuery().eq(CrmReceivablesPlan::getContractId, crmReceivablesPlan.getContractId()).orderByDesc(CrmReceivablesPlan::getNum).last("limit 1").one();
            if (receivablesPlan == null) {
                crmReceivablesPlan.setNum("1");
            } else {
                crmReceivablesPlan.setNum(Integer.valueOf(receivablesPlan.getNum()) + 1 + "");
            }
            save(crmReceivablesPlan);
            actionRecordUtil.addRecord(crmReceivablesPlan.getReceivablesPlanId(), CrmEnum.RECEIVABLES_PLAN, crmReceivablesPlan.getNum());
        } else {
            Integer number = crmReceivablesService.lambdaQuery().eq(CrmReceivables::getReceivablesPlanId, crmReceivablesPlan.getReceivablesPlanId()).count();
            if (number > 0) {
                throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_PLAN_ERROR);
            }
            if (crmReceivablesPlan.getContractId() != null) {
                crmBackLogDealService.deleteByType(crmContract.getOwnerUserId(), CrmEnum.RECEIVABLES_PLAN, CrmBackLogEnum.REMIND_RECEIVABLES_PLAN, crmReceivablesPlan.getReceivablesPlanId());
            }
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(getById(crmReceivablesPlan.getReceivablesPlanId())), BeanUtil.beanToMap(crmReceivablesPlan), CrmEnum.RECEIVABLES_PLAN, crmReceivablesPlan.getNum(), crmReceivablesPlan.getReceivablesPlanId());
            if (crmReceivablesPlan.getReceivedStatus() == null || crmReceivablesPlan.getReceivedStatus() == 3){
                crmReceivablesPlan.setReceivedStatus(0);
            }
            crmReceivablesPlan.setUnreceivedMoney(crmReceivablesPlan.getMoney());
            crmReceivablesPlan.setUpdateTime(DateUtil.date());
            updateById(crmReceivablesPlan);
            crmReceivablesPlan = getById(crmReceivablesPlan.getReceivablesPlanId());
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmReceivablesPlan));
        savePage(crmModel, crmReceivablesPlan.getReceivablesPlanId(),false);
    }

    @Override
    public List<CrmModelFiledVO> information(Integer receivablesPlanId) {
        return queryField(receivablesPlanId,true);
    }

    /**
     * 删除ids
     *
     * @param ids ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        if (ids.size() > 0) {
            removeByIds(ids);
            deletePage(ids);
            crmReceivablesService.lambdaUpdate().in(CrmReceivables::getReceivablesPlanId, ids).set(CrmReceivables::getReceivablesPlanId, null).update();
        }
    }

    /**
     * 修改基本信息
     * @param updateInformationBO data
     */
    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer receivablesPlanId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmReceivablesPlan oldReceivablesPlan = getById(updateInformationBO.getId());
            Map<String, Object> oldReceivablesPlanMap = BeanUtil.beanToMap(oldReceivablesPlan);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmReceivablesPlanMap = new HashMap<>(oldReceivablesPlanMap);
                crmReceivablesPlanMap.put(record.getString("fieldName"), record.get("value"));
                CrmReceivablesPlan crmReceivablesPlan = BeanUtil.mapToBean(crmReceivablesPlanMap, CrmReceivablesPlan.class, true);
                actionRecordUtil.updateRecord(oldReceivablesPlanMap, crmReceivablesPlanMap, CrmEnum.RECEIVABLES_PLAN, crmReceivablesPlan.getNum(), crmReceivablesPlan.getReceivablesPlanId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("receivables_plan_id",updateInformationBO.getId()).update();
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                CrmReceivablesPlanData receivablesPlanData = receivablesPlanDataService.lambdaQuery().select(CrmReceivablesPlanData::getValue,CrmReceivablesPlanData::getId).eq(CrmReceivablesPlanData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmReceivablesPlanData::getBatchId, batchId).one();
                String value = receivablesPlanData != null ? receivablesPlanData.getValue() : null;
                actionRecordUtil.publicContentRecord(CrmEnum.RECEIVABLES_PLAN, BehaviorEnum.UPDATE, receivablesPlanId, oldReceivablesPlan.getNum(), record,value);
                String newValue = fieldService.convertObjectValueToString(record.getInteger("type"),record.get("value"),record.getString("value"));
                CrmReceivablesPlanData crmReceivablesPlanData = new CrmReceivablesPlanData();
                crmReceivablesPlanData.setId(receivablesPlanData != null ? receivablesPlanData.getId() : null);
                crmReceivablesPlanData.setFieldId(record.getInteger("fieldId"));
                crmReceivablesPlanData.setName(record.getString("fieldName"));
                crmReceivablesPlanData.setValue(newValue);
                crmReceivablesPlanData.setCreateTime(new Date());
                crmReceivablesPlanData.setBatchId(batchId);
                receivablesPlanDataService.saveOrUpdate(crmReceivablesPlanData);
            }
            updateField(record,receivablesPlanId);
        });
        this.lambdaUpdate().set(CrmReceivablesPlan::getUpdateTime,new Date()).eq(CrmReceivablesPlan::getReceivablesPlanId,receivablesPlanId).update();
    }

    @Override
    public List<CrmModelFiledVO> queryField(Integer id){
        return queryField(id,false);
    }

    @Override
    public List<List<CrmModelFiledVO>> queryFormPositionField(Integer id) {
        CrmModel crmModel = queryById(id);
        if (id != null){
            List<JSONObject> customerList = new ArrayList<>();
            JSONObject customer = new JSONObject();
            customerList.add(customer.fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName")));
            crmModel.put("customerId", customerList);
            crmModel.put("contractId", Collections.singletonList(new JSONObject().fluentPut("contractId",crmModel.get("contractId")).fluentPut("contractNum",crmModel.get("contractNum"))));
        }
        return crmFieldService.queryFormPositionFieldVO(crmModel);
    }

    /**
     * 查询新增所需字段
     *
     * @param id id
     */
    private List<CrmModelFiledVO> queryField(Integer id,boolean appendInformation) {
        CrmModel crmModel = queryById(id);
        if (id != null) {
            List<JSONObject> customerList = new ArrayList<>();
            if (crmModel.get("customerId") != null) {
                JSONObject customer = new JSONObject();
                customerList.add(customer.fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName")));
            }
            crmModel.put("customerId", customerList);
            crmModel.put("contractId", Collections.singletonList(new JSONObject().fluentPut("contractId",crmModel.get("contractId")).fluentPut("contractNum",crmModel.get("contractNum"))));
        }
        List<CrmModelFiledVO> filedVOS = crmFieldService.queryField(crmModel);
       // CrmField crmField = crmFieldService.lambdaQuery().eq(CrmField::getLabel, CrmEnum.RECEIVABLES.getType()).eq(CrmField::getFieldName, "return_type").one();
       // filedVOS.add(BeanUtil.copyProperties(crmField, CrmModelFiledVO.class));
        if (appendInformation) {
            List<CrmModelFiledVO> modelFiledVOS = appendInformation(crmModel);
            filedVOS.addAll(modelFiledVOS);
        }
        return filedVOS;
    }

    /**
     * 根据客户ID查询未被使用回款计划
     *
     * @param crmReceivablesPlanBO param
     * @return data
     */
    @Override
    public List<CrmReceivablesPlan> queryByContractAndCustomer(CrmReceivablesPlanBO crmReceivablesPlanBO) {
        return lambdaQuery().isNull(CrmReceivablesPlan::getReceivablesId)
                .eq(CrmReceivablesPlan::getContractId, crmReceivablesPlanBO.getContractId())
                .eq(CrmReceivablesPlan::getCustomerId, crmReceivablesPlanBO.getCustomerId())
                .list();
    }

    @Override
    public String getReceivablesPlanNum(Integer receivablesPlanId) {
        return lambdaQuery().select(CrmReceivablesPlan::getNum).eq(CrmReceivablesPlan::getReceivablesPlanId,receivablesPlanId).oneOpt()
                .map(CrmReceivablesPlan::getNum).orElse("");
    }

    /**
     * 查询文件列表
     *
     * @param receivablesPlanId id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer receivablesPlanId) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmReceivablesPlan crmReceivablesPlan = getById(receivablesPlanId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmReceivablesPlan.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmReceivablesPlanData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmReceivablesPlanData::getValue);
            wrapper.eq(CrmReceivablesPlanData::getBatchId, crmReceivablesPlan.getBatchId());
            wrapper.in(CrmReceivablesPlanData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(receivablesPlanDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("回款计划详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        return fileEntityList;
    }

    @Override
    public String[] appendSearch() {
        return new String[]{"customerName","contractNum"};
    }

    @Override
    public void setOtherField(Map<String, Object> map) {
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName", ownerUserName);
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName",createUserName);
        CrmContract contract = crmContractService.getById((Serializable) map.get("contractId"));
        String customerName = crmCustomerService.getCustomerName((Integer) map.get("customerId"));
        map.put("customerName", customerName);
        if (contract != null) {
            map.put("contractNum", contract.getNum());
            map.put("contractMoney", contract.getMoney());
        }
    }

    @Override
    public CrmEnum getLabel() {
        return CrmEnum.RECEIVABLES_PLAN;
    }

    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        List<CrmModelFiledVO> filedList = crmFieldService.queryField(getLabel().getType());
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
        return filedList;
    }
}
