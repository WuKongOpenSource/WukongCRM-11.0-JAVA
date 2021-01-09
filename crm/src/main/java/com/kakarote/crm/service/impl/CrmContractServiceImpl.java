package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.examine.entity.ExamineRecordReturnVO;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import com.kakarote.core.feign.examine.service.ExamineService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BiTimeUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.*;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmContractMapper;
import com.kakarote.crm.mapper.CrmReceivablesPlanMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
@Slf4j
public class CrmContractServiceImpl extends BaseServiceImpl<CrmContractMapper, CrmContract> implements ICrmContractService, CrmPageService {

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmContractDataService crmContractDataService;

    @Autowired
    private ICrmExamineService crmExamineService;

    @Autowired
    private ICrmExamineLogService examineLogService;

    @Autowired
    private ICrmExamineRecordService examineRecordService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ICrmNumberSettingService crmNumberSettingService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    @Autowired
    private ICrmContractProductService crmContractProductService;

    @Autowired
    private ICrmBusinessProductService crmBusinessProductService;

    @Autowired
    private ICrmReceivablesPlanService crmReceivablesPlanService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private ICrmReceivablesService crmReceivablesService;

    @Autowired
    private ExamineService examineService;

    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"name", "num", "customerName"};
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    @Override
    public CrmEnum getLabel() {
        return CrmEnum.CONTRACT;
    }

    /**
     * 查询所有字段
     *
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        List<CrmModelFiledVO> filedList = crmFieldService.queryField(getLabel().getType());
        filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("roUserId", FieldEnum.CHECKBOX, 1));
        filedList.add(new CrmModelFiledVO("rwUserId", FieldEnum.CHECKBOX, 1));
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("companyUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("checkStatus", FieldEnum.TEXT, 1));
        filedList.add(new CrmModelFiledVO("contractId", FieldEnum.TEXT, 1));
        filedList.add(new CrmModelFiledVO("receivedMoney", FieldEnum.TEXT, 1));
        filedList.add(new CrmModelFiledVO("unreceivedMoney", FieldEnum.TEXT, 1));
        return filedList;
    }

    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryField(Integer id) {
        CrmModel crmModel = queryById(id);
        if (id != null) {
            List<JSONObject> customerList = new ArrayList<>();
            if (crmModel.get("customerId")!=null){
                JSONObject customer = new JSONObject();
                customerList.add(customer.fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName")));
            }
            crmModel.put("customerId", customerList);
            if (crmModel.get("businessId") != null){
                crmModel.put("businessId", Collections.singletonList(new JSONObject().fluentPut("businessId", crmModel.get("businessId")).fluentPut("businessName", crmModel.get("businessName"))));
            }else {
                crmModel.put("businessId", new ArrayList<>());
            }
            if (crmModel.get("contactsId") != null){
                crmModel.put("contactsId", Collections.singletonList(new JSONObject().fluentPut("contactsId", crmModel.get("contactsId")).fluentPut("name", crmModel.get("contactsName"))));
            }else {
                crmModel.put("contactsId", new ArrayList<>());
            }
        }
        List<CrmModelFiledVO> vos = crmFieldService.queryField(crmModel);
        JSONObject object = new JSONObject();
        object.fluentPut("discountRate", crmModel.get("discountRate")).fluentPut("product", crmContractProductService.queryList(id)).fluentPut("totalPrice", crmModel.get("totalPrice"));
        vos.add(new CrmModelFiledVO().setFieldName("product").setName("产品").setValue(object).setFormType("product").setSetting(new ArrayList<>()).setIsNull(0).setFieldType(1));
        return vos;
    }

    /**
     * 分页查询
     *
     * @param search
     * @return
     */
    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        BasePage<Map<String, Object>> basePage = queryList(search);
        for (Map<String, Object> map : basePage.getList()) {
            Double contractMoney =StrUtil.isNotEmpty(map.get("money").toString()) ? Double.parseDouble(map.get("money").toString()):0D;
            BigDecimal receivedProgress = new BigDecimal(100);
            if (!contractMoney.equals(0D)){
                receivedProgress = new BigDecimal(map.get("receivedMoney")!=null?Double.parseDouble(map.get("receivedMoney").toString()):0D).divide(new BigDecimal(contractMoney), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            }
            map.put("receivedProgress",receivedProgress);
        }
        SearchRequest searchRequest = new SearchRequest(getIndex());
        searchRequest.types(getDocType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = createQueryBuilder(search);
        queryBuilder.must(QueryBuilders.termQuery("checkStatus", 1));
        sourceBuilder.query(queryBuilder);
        sourceBuilder.aggregation(AggregationBuilders.sum("contractMoney").field("money"))
                .aggregation(AggregationBuilders.sum("receivedMoney").field("receivedMoney"))
                .aggregation(AggregationBuilders.sum("unreceivedMoney").field("unreceivedMoney"));
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchCount = elasticsearchRestTemplate.getClient().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchCount.getAggregations();
            Map<String, Object> countMap = new HashMap<>();
            ParsedSum contractMoney = aggregations.get("contractMoney");
            ParsedSum receivedMoney = aggregations.get("receivedMoney");
            ParsedSum unreceivedMoney = aggregations.get("unreceivedMoney");
            countMap.put("contractMoney", contractMoney.getValue());
            countMap.put("receivedMoney", receivedMoney.getValue());
            countMap.put("unReceivedMoney", unreceivedMoney.getValue());
            basePage.setExtraData(new JSONObject().fluentPut("money", countMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basePage;
    }

    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    @Override
    public CrmModel queryById(Integer id) {
        CrmModel crmModel;
        if (id != null) {
            crmModel = getBaseMapper().queryById(id, UserUtil.getUserId());
            crmModel.setLabel(CrmEnum.CONTRACT.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            List<String> nameList = StrUtil.splitTrim((String) crmModel.get("companyUserId"), Const.SEPARATOR);
            String name = nameList.stream().map(str -> UserCacheUtil.getUserName(Long.valueOf(str))).collect(Collectors.joining(Const.SEPARATOR));
            crmModel.put("companyUserName", name);
            crmModel.put("createUserName", UserCacheUtil.getUserName((Long) crmModel.get("createUserId")));
            crmContractDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
            Double contractMoney =crmModel.get("money")!=null?Double.parseDouble(crmModel.get("money").toString()):0D;
            BigDecimal receivedProgress = new BigDecimal(100);
            if (!contractMoney.equals(0D)){
                receivedProgress = new BigDecimal(crmModel.get("receivedMoney")!=null?Double.parseDouble(crmModel.get("receivedMoney").toString()):0D).divide(new BigDecimal(contractMoney), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            }
            crmModel.put("receivedProgress",receivedProgress);
        } else {
            crmModel = new CrmModel(CrmEnum.CONTRACT.getType());
        }
        return crmModel;
    }

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 保存或新增信息
     *
     * @param crmModel model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(CrmContractSaveBO crmModel) {
        CrmContract crmContract = BeanUtil.copyProperties(crmModel.getEntity(), CrmContract.class);
        String batchId = StrUtil.isNotEmpty(crmContract.getBatchId()) ? crmContract.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_contract_data"));
        crmContractDataService.saveData(crmModel.getField(), batchId);
        if (crmContract.getStartTime() != null && crmContract.getEndTime() != null && crmContract.getStartTime().compareTo(crmContract.getEndTime()) > 0) {
            throw new CrmException(CrmCodeEnum.CRM_CONTRACT_DATE_ERROR);
        }
        if (crmContract.getDiscountRate() == null) {
            crmContract.setDiscountRate(new BigDecimal("0"));
        }
        crmContract.setUnreceivedMoney(crmContract.getMoney());
        crmContract.setReceivedMoney(new BigDecimal("0"));
//        Integer examineCount = crmExamineService.queryCount(getLabel());
        ExamineRecordSaveBO examineRecordSaveBO = crmModel.getExamineFlowData();
        ExamineRecordReturnVO examineData = null;
        if (crmContract.getContractId() == null) {
            List<AdminConfig> configList = adminService.queryConfigByName("numberSetting").getData();
            AdminConfig adminConfig = configList.stream().filter(config -> Objects.equals(getLabel().getType().toString(), config.getValue())).collect(Collectors.toList()).get(0);
            if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmContract.getNum())) {
                String result = crmNumberSettingService.generateNumber(adminConfig, crmContract.getOrderDate());
                crmContract.setNum(result);
            }
            Integer contract = lambdaQuery().eq(CrmContract::getNum, crmContract.getNum()).ne(CrmContract::getCheckStatus, 7).count();
            if (contract != 0) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_NUM_ERROR);
            }
            crmContract.setCreateUserId(UserUtil.getUserId());
            crmContract.setBatchId(batchId);
            crmContract.setCreateTime(DateUtil.date());
            crmContract.setUpdateTime(DateUtil.date());
            crmContract.setRoUserId(",");
            crmContract.setRwUserId(",");
            crmContract.setOwnerUserId(UserUtil.getUserId());
            save(crmContract);
            if (crmContract.getCheckStatus() != null && crmContract.getCheckStatus() == 5) {
                crmContract.setCheckStatus(5);
            }else {
                if (examineRecordSaveBO != null) {
                    this.supplementFieldInfo(1, crmContract.getContractId(), null, examineRecordSaveBO);
                    examineRecordSaveBO.setTitle(crmContract.getName());
                    examineData = examineService.addExamineRecord(examineRecordSaveBO).getData();
                    crmContract.setExamineRecordId(examineData.getRecordId());
                    crmContract.setCheckStatus(examineData.getExamineStatus());
                } else {
                    crmContract.setCheckStatus(1);
                }
            }
            if (crmContract.getCheckStatus() == 1) {
                CrmCustomer customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).getById(crmContract.getCustomerId());
                customer.setDealStatus(1);
                Date dealTime = crmContract.getOrderDate() != null ? crmContract.getOrderDate() : new Date();
                customer.setDealTime(dealTime);
                ApplicationContextHolder.getBean(ICrmCustomerService.class).updateById(customer);
                Map<String, Object> map = new HashMap<>();
                map.put("dealTime", DateUtil.formatDateTime(dealTime));
                map.put("dealStatus", 1);
                ElasticUtil.updateField(elasticsearchRestTemplate, map, customer.getCustomerId(), CrmEnum.CUSTOMER.getIndex());
            }
            updateById(crmContract);
            crmActivityService.addActivity(2, CrmActivityEnum.CONTRACT, crmContract.getContractId());
            actionRecordUtil.addRecord(crmContract.getContractId(), CrmEnum.CONTRACT, crmContract.getName());
        } else {
            CrmContract contract = getById(crmContract.getContractId());
            if (contract.getCheckStatus() == 8) {
                contract.setCheckStatus(5);
            }
            if (contract.getCheckStatus() == 1) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EXAMINE_PASS_HINT_ERROR);
            }
//            if (contract.getCheckStatus() == 0){
//                contract.setCheckStatus(4);
//            }
            if (contract.getCheckStatus() != 4 && contract.getCheckStatus() != 2 && contract.getCheckStatus() != 5) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EDIT_ERROR);
            }
            if (crmContract.getCheckStatus() != null && crmContract.getCheckStatus() == 5) {
                crmContract.setCheckStatus(5);
            }else {
                if (examineRecordSaveBO != null) {
                    this.supplementFieldInfo(1, crmContract.getContractId(), contract.getExamineRecordId(), examineRecordSaveBO);
                    examineRecordSaveBO.setTitle(crmContract.getName());
                    examineData = examineService.addExamineRecord(examineRecordSaveBO).getData();
                    crmContract.setExamineRecordId(examineData.getRecordId());
                    crmContract.setCheckStatus(examineData.getExamineStatus());
                } else {
                    crmContract.setCheckStatus(1);
                }
            }
            crmBackLogDealService.deleteByType(crmContract.getOwnerUserId(), CrmEnum.CONTRACT, CrmBackLogEnum.END_CONTRACT, crmContract.getContractId());
            crmBackLogDealService.deleteByType(crmContract.getOwnerUserId(), CrmEnum.CONTRACT, CrmBackLogEnum.REMIND_RETURN_VISIT_CONTRACT, crmContract.getContractId());
            crmContract.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(contract), BeanUtil.beanToMap(crmContract), CrmEnum.CONTRACT, crmContract.getName(), crmContract.getContractId());
            updateById(crmContract);
            crmContract = getById(crmContract.getContractId());
            ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(), "contract", crmContract.getContractId().toString(), crmContract.getName());
        }

        //判断当前是否提交审核，提交审核需要生成一条系统通知
        if (0 == crmContract.getCheckStatus()) {
            if (examineData != null) {
                actionRecordUtil.addCrmExamineActionRecord(CrmEnum.CONTRACT, examineData.getRecordId(), BehaviorEnum.SUBMIT_EXAMINE, crmContract.getNum());
            }
        }
        List<CrmContractProduct> contractProductList = crmModel.getProduct();
        crmContractProductService.deleteByContractId(crmContract.getContractId());
        if (crmContract.getBusinessId() != null) {
            crmBusinessProductService.deleteByBusinessId(crmContract.getBusinessId());
        }
        if (contractProductList != null) {
            for (CrmContractProduct crmContractProduct : contractProductList) {
                crmContractProduct.setContractId(crmContract.getContractId());
                crmContractProductService.save(crmContractProduct);
                if (crmContract.getBusinessId() != null) {
                    CrmBusinessProduct crmBusinessProduct = BeanUtil.copyProperties(crmContractProduct, CrmBusinessProduct.class);
                    crmBusinessProduct.setRId(null);
                    crmBusinessProduct.setBusinessId(crmContract.getBusinessId());
                    crmBusinessProductService.save(crmBusinessProduct);
                }
            }
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmContract));
        savePage(crmModel, crmContract.getContractId(), false);
    }

    @Autowired
    private ICrmBusinessService crmBusinessService;

    @Autowired
    private ICrmContactsService crmContactsService;

    @Override
    public void setOtherField(Map<String, Object> map) {
        String customerName = crmCustomerService.getCustomerName((Integer) map.get("customerId"));
        map.put("customerName", customerName);
        if (map.containsKey("businessId") && ObjectUtil.isNotEmpty(map.get("businessId"))) {
            String businessName = crmBusinessService.getBusinessName((Integer) map.get("businessId"));
            map.put("businessName", businessName);
        } else {
            map.put("businessName", "");
        }
        if (map.containsKey("contactsId") && ObjectUtil.isNotEmpty(map.get("contactsId"))) {
            String contactsName = crmContactsService.getContactsName((Integer) map.get("contactsId"));
            map.put("contactsName", contactsName);
        } else {
            map.put("contactsName", "");
        }
        if (map.containsKey("companyUserId") && ObjectUtil.isNotEmpty(map.get("companyUserId"))) {
            String companyUserName = adminService.queryUserByIds(TagUtil.toLongSet(map.get("companyUserId").toString())).getData()
                    .stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
            map.put("companyUserName", companyUserName);
        } else {
            map.put("companyUserName", "");
        }
        if (map.containsKey("ownerUserId") && ObjectUtil.isNotEmpty(map.get("ownerUserId"))) {
            String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
            map.put("ownerUserName", ownerUserName);
        } else {
            map.put("ownerUserName", "");
        }
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName", createUserName);
    }

    @Override
    public Dict getSearchTransferMap() {
        return Dict.create().set("customerId", "customerName").set("businessId", "businessName").set("contactsId", "contactsName");
    }

    /**
     * 删除合同数据
     *
     * @param ids ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        //合同
        Integer number = ApplicationContextHolder.getBean(ICrmReceivablesService.class).lambdaQuery().in(CrmReceivables::getContractId, ids).ne(CrmReceivables::getCheckStatus, 7).count();
        if (number > 0) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_JOIN_ERROR);
        }
        ids.forEach(id -> {
            CrmContract contract = getById(id);
            boolean bol = (contract.getCheckStatus() != 4 && contract.getCheckStatus() != 5) && (!UserUtil.isAdmin());
            if (bol) {
                throw new CrmException(CrmCodeEnum.CAN_ONLY_DELETE_WITHDRAWN_AND_SUBMITTED_EXAMINE);
            }
            //删除合同产品关联
            crmContractProductService.deleteByContractId(contract.getContractId());
            //删除跟进记录
            crmActivityService.deleteActivityRecord(CrmActivityEnum.CONTRACT, Collections.singletonList(id));
            //删除字段操作记录
            crmActionRecordService.deleteActionRecord(CrmEnum.CONTRACT, Collections.singletonList(contract.getContractId()));
            //删除自定义字段
            crmContractDataService.deleteByBatchId(Collections.singletonList(contract.getBatchId()));
            //删除文件
            adminFileService.delete(Collections.singletonList(contract.getBatchId()));
            if (ObjectUtil.isNotEmpty(contract.getExamineRecordId())) {
                examineService.deleteExamineRecord(contract.getExamineRecordId());
            }
            contract.setCheckStatus(7);
            updateById(contract);
        });
        deletePage(ids);
    }

    /**
     * 修改合同负责人
     *
     * @param changOwnerUserBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeOwnerUser(CrmBusinessChangOwnerUserBO changOwnerUserBO) {
        if (changOwnerUserBO.getIds().size() == 0) {
            return;
        }
        String ownerUserName = UserCacheUtil.getUserName(changOwnerUserBO.getOwnerUserId());
        changOwnerUserBO.getIds().forEach(id -> {
            if (AuthUtil.isRwAuth(id, CrmEnum.CONTRACT)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            CrmContract contract = getById(id);
            if (contract.getCheckStatus() == 8) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_TRANSFER_ERROR);
            }
            String memberId = "," + changOwnerUserBO.getOwnerUserId() + ",";
            getBaseMapper().deleteMember(memberId, id);
            contract = getById(id);
            if (2 == changOwnerUserBO.getTransferType() && !changOwnerUserBO.getOwnerUserId().equals(contract.getOwnerUserId())) {
                if (1 == changOwnerUserBO.getPower()) {
                    contract.setRoUserId(contract.getRoUserId() + contract.getOwnerUserId() + Const.SEPARATOR);
                }
                if (2 == changOwnerUserBO.getPower()) {
                    contract.setRwUserId(contract.getRwUserId() + contract.getOwnerUserId() + Const.SEPARATOR);
                }
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_CONTRACT_USER, contract.getContractId(), contract.getName(), contract.getOwnerUserId());
            }
            contract.setOwnerUserId(changOwnerUserBO.getOwnerUserId());
            updateById(contract);
            actionRecordUtil.addConversionRecord(id, CrmEnum.CONTRACT, changOwnerUserBO.getOwnerUserId(), contract.getName());
            //修改es
            Map<String, Object> map = new HashMap<>();
            map.put("ownerUserId", changOwnerUserBO.getOwnerUserId());
            map.put("ownerUserName", ownerUserName);
            map.put("rwUserId", contract.getRwUserId());
            map.put("roUserId", contract.getRoUserId());
            updateField(map, Collections.singletonList(id));
        });

    }


    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    @Override
    public void exportExcel(HttpServletResponse response, CrmSearchBO search) {
        List<Map<String, Object>> dataList = queryPageList(search).getList();
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<CrmFieldSortVO> headList = crmFieldService.queryListHead(getLabel().getType());
            headList.forEach(head -> writer.addHeaderAlias(head.getFieldName(), head.getName()));
            writer.merge(headList.size() - 1, "合同信息");
            if (dataList.size() == 0) {
                Map<String, Object> record = new HashMap<>();
                headList.forEach(head -> record.put(head.getFieldName(), ""));
                dataList.add(record);
            }
            for (Map<String, Object> map : dataList) {
                if(map.get("checkStatus")==null||"".equals(map.get("checkStatus"))){
                    continue;
                }
                String checkStatus;
                //0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废
                switch ((Integer) map.get("checkStatus")) {
                    case 1:
                        checkStatus = "通过";
                        break;
                    case 2:
                        checkStatus = "拒绝";
                        break;
                    case 3:
                        checkStatus = "审核中";
                        break;
                    case 4:
                        checkStatus = "撤回";
                        break;
                    case 5:
                        checkStatus = "未提交";
                        break;
                    case 7:
                        checkStatus = "已删除";
                        break;
                    case 8:
                        checkStatus = "作废";
                        break;
                    default:
                        checkStatus = "待审核";
                }
                map.put("checkStatus", checkStatus);
            }
            writer.setOnlyAlias(true);
            writer.write(dataList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=contract.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出合同错误：", e);
        }
    }


    /**
     * 获取团队成员
     *
     * @param contractId 合同ID
     * @return data
     */
    @Override
    public List<CrmMembersSelectVO> getMembers(Integer contractId) {
        CrmContract crmContract = getById(contractId);
        List<CrmMembersSelectVO> recordList = new ArrayList<>();
        if (crmContract.getOwnerUserId() != null) {
            UserInfo userInfo = UserCacheUtil.getUserInfo(crmContract.getOwnerUserId());
            CrmMembersSelectVO crmMembersVO = new CrmMembersSelectVO();
            crmMembersVO.setUserId(userInfo.getUserId());
            crmMembersVO.setRealname(UserCacheUtil.getUserName(userInfo.getUserId()));
            crmMembersVO.setDeptName(UserCacheUtil.getDeptName(userInfo.getDeptId()));
            crmMembersVO.setPower("负责人权限");
            crmMembersVO.setGroupRole("负责人");
            crmMembersVO.setPost(userInfo.getPost());
            recordList.add(crmMembersVO);
        }
        String roUserId = crmContract.getRoUserId();
        String rwUserId = crmContract.getRwUserId();
        String memberIds = roUserId + rwUserId.substring(1);
        if (Const.SEPARATOR.equals(memberIds)) {
            return recordList;
        }
        String[] memberIdsArr = memberIds.substring(1, memberIds.length() - 1).split(",");
        Set<String> memberIdsSet = new HashSet<>(Arrays.asList(memberIdsArr));
        for (String memberId : memberIdsSet) {
            UserInfo userInfo = UserCacheUtil.getUserInfo(Long.valueOf(memberId));
            CrmMembersSelectVO crmMembersVO = new CrmMembersSelectVO();
            crmMembersVO.setUserId(userInfo.getUserId());
            crmMembersVO.setRealname(UserCacheUtil.getUserName(userInfo.getUserId()));
            crmMembersVO.setDeptName(UserCacheUtil.getDeptName(userInfo.getDeptId()));
            crmMembersVO.setGroupRole("普通成员");
            crmMembersVO.setPost(userInfo.getPost());
            if (roUserId.contains(memberId)) {
                crmMembersVO.setPower("只读");
            } else if (rwUserId.contains(memberId)) {
                crmMembersVO.setPower("读写");
            }
            recordList.add(crmMembersVO);
        }
        return recordList;
    }

    /**
     * 添加团队成员
     *
     * @param crmMemberSaveBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMember(CrmMemberSaveBO crmMemberSaveBO) {
        for (Integer id : crmMemberSaveBO.getIds()) {
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CONTRACT, id)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            CrmContract oldContract = lambdaQuery().eq(CrmContract::getContractId,id).ne(CrmContract::getCheckStatus,7).one();
            if(oldContract == null){
                continue;
            }
            Long ownerUserId = oldContract.getOwnerUserId();
            for (Long memberId : crmMemberSaveBO.getMemberIds()) {
                if (ownerUserId.equals(memberId)) {
                    throw new CrmException(CrmCodeEnum.CRM_MEMBER_ADD_ERROR);
                }
                oldContract.setRoUserId(oldContract.getRoUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
                oldContract.setRwUserId(oldContract.getRwUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
                String name = lambdaQuery().select(CrmContract::getName).eq(CrmContract::getContractId, id).one().getName();
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_CONTRACT_USER, oldContract.getContractId(), oldContract.getName(), memberId);
                actionRecordUtil.addMemberActionRecord(CrmEnum.BUSINESS, id, memberId, name);
            }
            if (1 == crmMemberSaveBO.getPower()) {
                oldContract.setRoUserId(oldContract.getRoUserId() + StrUtil.join(Const.SEPARATOR, crmMemberSaveBO.getMemberIds()) + Const.SEPARATOR);
            } else if (2 == crmMemberSaveBO.getPower()) {
                oldContract.setRwUserId(oldContract.getRwUserId() + StrUtil.join(Const.SEPARATOR, crmMemberSaveBO.getMemberIds()) + Const.SEPARATOR);
            }
            updateById(oldContract);
            Map<String, Object> map = new HashMap<>();
            map.put("roUserId", oldContract.getRoUserId());
            map.put("rwUserId", oldContract.getRwUserId());
            updateField(map, Collections.singletonList(id));
        }
    }

    /**
     * 删除团队成员
     *
     * @param crmMemberSaveBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(CrmMemberSaveBO crmMemberSaveBO) {
        for (Integer id : crmMemberSaveBO.getIds()) {
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CONTRACT, id)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            deleteMembers(id, crmMemberSaveBO.getMemberIds());
        }
    }

    private void deleteMembers(Integer id, List<Long> memberIds) {
        CrmContract oldContract = getById(id);
        Long ownerUserId = oldContract.getOwnerUserId();
        for (Long memberId : memberIds) {
            if (ownerUserId.equals(memberId)) {
                throw new CrmException(CrmCodeEnum.CRM_MEMBER_DELETE_ERROR);
            }
            oldContract.setRoUserId(oldContract.getRoUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
            oldContract.setRwUserId(oldContract.getRwUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
            if (!memberId.equals(UserUtil.getUserId())) {
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_CONTRACT_REMOVE_TEAM,oldContract.getContractId(),oldContract.getName(),memberId);
                actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CONTRACT, id, memberId, false, oldContract.getName());
            } else {
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_CONTRACT_TEAM_EXIT,oldContract.getContractId(),oldContract.getName(),oldContract.getOwnerUserId());
                actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CONTRACT, id, memberId, true, oldContract.getName());
            }
        }
        updateById(oldContract);
        Map<String, Object> map = new HashMap<>();
        map.put("roUserId", oldContract.getRoUserId());
        map.put("rwUserId", oldContract.getRwUserId());
        updateField(map, Collections.singletonList(id));
    }

    /**
     * 退出团队
     *
     * @param contractId 合同ID
     */
    @Override
    public void exitTeam(Integer contractId) {
        deleteMembers(contractId, Collections.singletonList(UserUtil.getUserId()));
    }

    @Override
    public List<CrmModelFiledVO> information(Integer contractId) {
        CrmModel crmModel = queryById(contractId);
        List<String> keyList = Arrays.asList("num", "name", "orderDate", "money", "startTime", "endTime", "remark");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间", "更新时间", "下次联系时间", "最后跟进时间", "已收款金额", "未收款金额");
        List<CrmModelFiledVO> crmModelFiledVOS = queryInformation(crmModel, keyList);
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("checkStatus").setName("审核状态").setValue(crmModel.get("checkStatus")).setFormType("check_status").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("businessId").setName("商机名称").setValue(new JSONObject().fluentPut("businessId", crmModel.get("businessId")).fluentPut("businessName", crmModel.get("businessName"))).setFormType("business").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("contactsId").setName("客户签约人").setValue(new JSONObject().fluentPut("contactsId", crmModel.get("contactsId")).fluentPut("contactsName", crmModel.get("contactsName"))).setFormType("contacts").setFieldType(1));
        List<Long> ids = StrUtil.splitTrim((String) crmModel.get("companyUserId"), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList());
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("companyUserId").setName("公司签约人").setValue(ids.size() > 0 ? adminService.queryUserByIds(ids).getData() : new ArrayList<>()).setFormType("user").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("customerId").setName("客户名称").setValue(new JSONObject().fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName"))).setFormType("customer").setFieldType(1));
        if (crmModel.get("receivedMoney") != null){
            crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("receivedMoney").setName("已收款金额").setValue(new BigDecimal(crmModel.get("receivedMoney").toString())).setFormType(FieldEnum.NUMBER.getFormType()).setFieldType(1));
        }
        if (crmModel.get("unreceivedMoney") != null){
            crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("unreceivedMoney").setName("未收款金额").setValue(new BigDecimal(crmModel.get("unreceivedMoney").toString())).setFormType(FieldEnum.NUMBER.getFormType()).setFieldType(1));
        }
        List<CrmModelFiledVO> filedVOS = crmModelFiledVOS.stream().sorted(Comparator.comparingInt(r -> -r.getFieldType())).peek(r -> {
            r.setFieldType(null);
            r.setSetting(null);
            r.setType(null);
            if (systemFieldList.contains(r.getName())) {
                r.setSysInformation(1);
            } else {
                r.setSysInformation(0);
            }
        }).collect(Collectors.toList());
        ICrmRoleFieldService crmRoleFieldService = ApplicationContextHolder.getBean(ICrmRoleFieldService.class);
        List<CrmRoleField> roleFieldList = crmRoleFieldService.queryUserFieldAuth(crmModel.getLabel(), 1);
        Map<String, Integer> levelMap = new HashMap<>();
        roleFieldList.forEach(crmRoleField -> {
            levelMap.put(StrUtil.toCamelCase(crmRoleField.getFieldName()), crmRoleField.getAuthLevel());
        });
        filedVOS.removeIf(field -> (!UserUtil.isAdmin() && Objects.equals(1, levelMap.get(field.getFieldName()))));
        return filedVOS;
    }

    /**
     * 查询产品通过合同ID
     *
     * @param crmRelationPageBO 合同ID
     * @return data
     */
    @Override
    public JSONObject queryProductListByContractId(CrmRelationPageBO crmRelationPageBO) {
        JSONObject record = getBaseMapper().querySubtotalByContractId(crmRelationPageBO.getContractId());
        if (record.getString("money") == null) {
            record.put("money", 0);
        }
        BasePage<JSONObject> page = getBaseMapper().queryProductPageList(crmRelationPageBO.parse(), crmRelationPageBO.getContractId());
        for (JSONObject jsonObject : page.getList()) {
            Integer status = jsonObject.getInteger("status");
            if (status == 1) {
                jsonObject.put("是否上下架", "是");
            } else {
                jsonObject.put("是否上下架", "否");
            }
        }
        record.put("pageNumber", page.getPageNumber());
        record.put("pageSize", page.getPageSize());
        record.put("totalPage", page.getTotalPage());
        record.put("totalRow", page.getTotalRow());
        record.put("list", page.getList());
        return record;
    }

    /**
     * 查询文件数量
     *
     * @param contractId id
     * @return data
     */
    @Override
    public CrmInfoNumVO num(Integer contractId) {
        CrmContract crmContract = getById(contractId);
        List<CrmField> crmFields = crmFieldService.queryFileField();
        List<String> batchIdList = new ArrayList<>();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmContractData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmContractData::getValue);
            wrapper.eq(CrmContractData::getBatchId, crmContract.getBatchId());
            wrapper.in(CrmContractData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            batchIdList.addAll(crmContractDataService.listObjs(wrapper, Object::toString));
        }
        batchIdList.add(crmContract.getBatchId());
        batchIdList.addAll(crmActivityService.queryFileBatchId(crmContract.getContractId(), getLabel().getType()));
        String receivableCon = AuthUtil.getCrmAuthSql(CrmEnum.RECEIVABLES, 1);
        String returnVisitCon = AuthUtil.getCrmAuthSql(CrmEnum.RETURN_VISIT, 1);
        String invoiceCon = AuthUtil.getCrmAuthSql(CrmEnum.INVOICE, 1);
        Map<String, Object> map = new HashMap<>();
        map.put("contractId", contractId);
        map.put("receivableCon", receivableCon);
        map.put("returnVisitCon", returnVisitCon);
        map.put("invoiceCon", invoiceCon);
        CrmInfoNumVO infoNumVO = getBaseMapper().queryNum(map);
        infoNumVO.setFileCount(adminFileService.queryNum(batchIdList).getData());
        Set<String> member = new HashSet<>();
        member.addAll(StrUtil.splitTrim(crmContract.getRoUserId(), Const.SEPARATOR));
        member.addAll(StrUtil.splitTrim(crmContract.getRwUserId(), Const.SEPARATOR));
        if (crmContract.getOwnerUserId() != null) {
            member.add(crmContract.getOwnerUserId().toString());
        }
        infoNumVO.setMemberCount(member.size());
        return infoNumVO;
    }

    /**
     * 查询文件列表
     *
     * @param contractId id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer contractId) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmContract crmContract = getById(contractId);
        adminFileService.queryFileList(crmContract.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmContractData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmContractData::getValue);
            wrapper.eq(CrmContractData::getBatchId, crmContract.getBatchId());
            wrapper.in(CrmContractData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = adminFileService.queryFileList(crmContractDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("合同详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        List<String> stringList = crmActivityService.queryFileBatchId(crmContract.getContractId(), getLabel().getType());
        if (stringList.size() > 0) {
            List<FileEntity> data = adminFileService.queryFileList(stringList).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("跟进记录");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        return fileEntityList;
    }

    @Override
    public BasePage<CrmReceivablesPlan> queryReceivablesPlanListByContractId(CrmRelationPageBO crmRelationPageBO) {
        CrmReceivablesPlanMapper mapper = (CrmReceivablesPlanMapper) crmReceivablesPlanService.getBaseMapper();
        return mapper.queryReceivablesPlanListByContractId(crmRelationPageBO.parse(), crmRelationPageBO.getContractId());
    }

    @Override
    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<CrmContract> list = query().in("contract_id", ids).list();
        return list.stream().map(crmContract -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmContract.getContractId());
            simpleCrmEntity.setName(crmContract.getName());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public String getContractName(int contractId) {
        return lambdaQuery().select(CrmContract::getName).eq(CrmContract::getContractId, contractId).oneOpt()
                .map(CrmContract::getName).orElse("");
    }


    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer contractId = updateInformationBO.getId();
        CrmContract contract = getById(contractId);
        if (contract.getCheckStatus() == 8) {
            throw new CrmException(CrmCodeEnum.CRM_CONTRACT_CANCELLATION_ERROR);
        }
        if (contract.getCheckStatus() == 1) {
            throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EXAMINE_PASS_ERROR);
        }
        if (contract.getCheckStatus() != 4 && contract.getCheckStatus() != 2 && contract.getCheckStatus() != 5) {
            throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EDIT_ERROR);
        }
        updateInformationBO.getList().forEach(record -> {
            CrmContract oldContract = getById(updateInformationBO.getId());
            Map<String, Object> oldContractMap = BeanUtil.beanToMap(oldContract);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmContractMap = new HashMap<>(oldContractMap);
                crmContractMap.put(record.getString("fieldName"), record.get("value"));
                CrmContract crmContract = BeanUtil.mapToBean(crmContractMap, CrmContract.class, true);
                actionRecordUtil.updateRecord(oldContractMap, crmContractMap, CrmEnum.CONTRACT, crmContract.getName(), crmContract.getContractId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("contract_id",updateInformationBO.getId()).update();
                if ("name".equals(record.getString("fieldName"))) {
                    ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(), "contract", crmContract.getContractId().toString(), crmContract.getName());
                }
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                CrmContractData contractData = crmContractDataService.lambdaQuery()
                        .select(CrmContractData::getValue)
                        .eq(CrmContractData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmContractData::getBatchId, batchId).last("limit 1").one();
                String value = contractData != null ? contractData.getValue() : null;
                String detail = actionRecordUtil.getDetailByFormTypeAndValue(record,value);
                actionRecordUtil.publicContentRecord(CrmEnum.CONTRACT, BehaviorEnum.UPDATE, contractId, oldContract.getName(), detail);
                boolean bol = crmContractDataService.lambdaUpdate()
                        .set(CrmContractData::getName,record.getString("fieldName"))
                        .set(CrmContractData::getValue, record.getString("value"))
                        .eq(CrmContractData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmContractData::getBatchId, batchId).update();
                if (!bol) {
                    CrmContractData crmContractData = new CrmContractData();
                    crmContractData.setFieldId(record.getInteger("fieldId"));
                    crmContractData.setName(record.getString("fieldName"));
                    crmContractData.setValue(record.getString("value"));
                    crmContractData.setCreateTime(new Date());
                    crmContractData.setBatchId(batchId);
                    crmContractDataService.save(crmContractData);
                }
            }
            updateField(record, contractId);
        });
    }


    @Override
    public BasePage<JSONObject> queryListByContractId(CrmRelationPageBO crmRelationPageBO) {
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.RECEIVABLES, "rec", 1);
        return crmReceivablesService.queryListByContractId(crmRelationPageBO.parse(), crmRelationPageBO.getContractId(), conditions);
    }

    @Override
    public List<CrmReceivablesPlan> queryReceivablesPlansByContractId(Integer contractId, Integer receivablesId) {
        List<CrmReceivablesPlan> recordList = getBaseMapper().queryReceivablesPlansByContractId(contractId);
        if (receivablesId != null) {
            CrmReceivablesPlan receivables = getBaseMapper().queryReceivablesPlansByReceivablesId(receivablesId);
            if (receivables != null) {
                recordList.add(receivables);
            }
        }
        return recordList;
    }

    @Override
    public BasePage<JSONObject> queryReturnVisit(CrmRelationPageBO crmRelationPageBO) {
        List<CrmField> nameList = crmFieldService.lambdaQuery().select(CrmField::getFieldId, CrmField::getFieldName).eq(CrmField::getLabel, CrmEnum.RETURN_VISIT.getType())
                .eq(CrmField::getIsHidden, 0).ne(CrmField::getFieldType, 1).list();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.RECEIVABLES, "a", 1);
        BasePage<JSONObject> basePage = getBaseMapper().queryReturnVisit(crmRelationPageBO.parse(), crmRelationPageBO.getContractId(), conditions, nameList);
        for (JSONObject jsonObject : basePage.getList()) {
            String ownerUserName = UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId"));
            jsonObject.put("ownerUserName", ownerUserName);
        }
        return basePage;
    }

    @Override
    public void contractDiscard(Integer contractId) {
        CrmContract contract = getById(contractId);
        actionRecordUtil.addObjectActionRecord(CrmEnum.CONTRACT, contractId, BehaviorEnum.CANCEL_EXAMINE, contract.getName());
        lambdaUpdate().set(CrmContract::getCheckStatus, 8).set(CrmContract::getUpdateTime, new Date()).eq(CrmContract::getContractId, contractId).update();
        Map<String, Object> map = new HashMap<>();
        map.put("checkStatus", 8);
        map.put("updateTime", DateUtil.formatDateTime(new Date()));
        updateField(map, Collections.singletonList(contractId));
    }

    @Override
    public List<String> endContract(CrmEventBO crmEventBO) {
        return getBaseMapper().endContract(crmEventBO);
    }

    @Override
    public List<String> receiveContract(CrmEventBO crmEventBO) {
        return getBaseMapper().receiveContract(crmEventBO);
    }

    @Override
    public BasePage<Map<String, Object>> eventContractPageList(QueryEventCrmPageBO eventCrmPageBO) {
        Long userId = eventCrmPageBO.getUserId();
        Long time = eventCrmPageBO.getTime();
        Integer type = eventCrmPageBO.getType();
        if (userId == null) {
            userId = UserUtil.getUserId();
        }
        List<Integer> contractIds;
        List<JSONObject> records = null;
        if (type == 1) {
            AdminConfig adminConfig = adminService.queryFirstConfigByName("expiringContractDays").getData();
            if (adminConfig.getStatus() == 0 || ObjectUtil.isNull(adminConfig)) {
                contractIds = new ArrayList<>();
            } else {
                contractIds = getBaseMapper().endContractList(userId, new Date(time), Integer.valueOf(adminConfig.getValue()));
            }
        } else {
            records = getBaseMapper().receiveContractList(userId, new Date(time));
            contractIds = records.stream().map(record -> record.getInteger("contractId")).collect(Collectors.toList());
        }
        if (contractIds.size() == 0) {
            return new BasePage<>();
        }
        List<String> collect = contractIds.stream().map(Object::toString).collect(Collectors.toList());
        CrmSearchBO crmSearchBO = new CrmSearchBO();
        crmSearchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "id", CrmSearchBO.FieldSearchEnum.ID, collect)));
        crmSearchBO.setLabel(CrmEnum.CONTRACT.getType());
        crmSearchBO.setPage(eventCrmPageBO.getPage());
        crmSearchBO.setLimit(eventCrmPageBO.getLimit());
        if (type == 2) {
            BasePage<Map<String, Object>> page = queryPageList(crmSearchBO);
            for (JSONObject record : records) {
                for (Map<String, Object> map : page.getList()) {
                    if (map.get("contractId").equals(record.getInteger("contractId"))) {
                        map.putAll(record.getInnerMap());
                    }
                }
            }
            return page;
        } else {
            return queryPageList(crmSearchBO);
        }
    }

    /**
     * 根据产品ID查询合同
     *
     * @param biParams 参数
     * @return data
     */
    @Override
    public BasePage<Map<String, Object>> queryListByProductId(BiParams biParams) {
        List<CrmContractProduct> products = crmContractProductService
                .lambdaQuery()
                 .select(CrmContractProduct::getContractId)
                .eq(CrmContractProduct::getProductId, biParams.getTypeId()).list();
        Integer menuId = 118;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity timeEntity = BiTimeUtil.analyzeType(biParams);

        CrmSearchBO searchBo = new CrmSearchBO();
        searchBo.setPage(biParams.getPage());
        searchBo.setLimit(biParams.getLimit());
        searchBo.setLabel(getLabel().getType());
        searchBo.setSearch(biParams.getSearch());
        searchBo.setOrder(biParams.getOrder());
        searchBo.setSortField(biParams.getSortField());
        List<String> stringList = products.stream().map(crmContractProduct -> crmContractProduct.getContractId().toString()).collect(Collectors.toList());
        List<CrmSearchBO.Search> searchList = searchBo.getSearchList();
        searchList.add(new CrmSearchBO.Search("contractId",null,CrmSearchBO.FieldSearchEnum.ID,stringList));
        List<String> userIds = timeEntity.getUserIds().stream().map(Object::toString).collect(Collectors.toList());
        searchList.add(new CrmSearchBO.Search("ownerUserId","single_user",CrmSearchBO.FieldSearchEnum.IS,userIds));
        searchList.add(new CrmSearchBO.Search("orderDate", "date", CrmSearchBO.FieldSearchEnum.DATE, Arrays.asList(DateUtil.formatDate(timeEntity.getBeginDate()), DateUtil.formatDate(DateUtil.offsetDay(timeEntity.getEndDate(),1)))));
        return queryPageList(searchBo);
    }
}
