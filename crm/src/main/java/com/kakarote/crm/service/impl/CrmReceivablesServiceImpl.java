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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.examine.entity.ExamineRecordReturnVO;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import com.kakarote.core.feign.examine.service.ExamineService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmActivityEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.BO.CrmContractSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmReceivablesMapper;
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
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 回款表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Service
@Slf4j
public class CrmReceivablesServiceImpl extends BaseServiceImpl<CrmReceivablesMapper, CrmReceivables> implements ICrmReceivablesService, CrmPageService {

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmReceivablesDataService crmReceivablesDataService;

    @Autowired
    private ICrmContractService crmContractService;

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
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmReceivablesPlanService crmReceivablesPlanService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ExamineService examineService;


    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"number", "customerName"};
    }

    @Override
    public CrmEnum getLabel() {
        return CrmEnum.RECEIVABLES;
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
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("checkStatus", FieldEnum.TEXT, 1));
        filedList.add(new CrmModelFiledVO("planId", FieldEnum.TEXT, 1));
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
        if (id != null){
            List<JSONObject> customerList = new ArrayList<>();
            JSONObject customer = new JSONObject();
            customerList.add(customer.fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName")));
            crmModel.put("customerId", customerList);
            crmModel.put("contractId", Collections.singletonList(new JSONObject().fluentPut("contractId",crmModel.get("contractId")).fluentPut("contractNum",crmModel.get("contractNum"))));
        }
        return crmFieldService.queryField(crmModel);
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
        SearchRequest searchRequest = new SearchRequest(getIndex());
        searchRequest.types(getDocType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = createQueryBuilder(search);
        queryBuilder.must(QueryBuilders.termQuery("checkStatus", 1));
        sourceBuilder.query(queryBuilder);
        sourceBuilder.aggregation(AggregationBuilders.sum("receivablesMoney").field("money"));
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchCount = elasticsearchRestTemplate.getClient().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchCount.getAggregations();
            Map<String, Object> countMap = new HashMap<>();
            ParsedSum receivablesMoney = aggregations.get("receivablesMoney");
            countMap.put("receivablesMoney",receivablesMoney.getValue());
            basePage.setExtraData(new JSONObject().fluentPut("money",countMap));
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
            crmModel.setLabel(CrmEnum.RECEIVABLES.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            crmModel.put("createUserName", UserCacheUtil.getUserName((Long) crmModel.get("createUserId")));
            crmReceivablesDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
        } else {
            crmModel = new CrmModel(CrmEnum.RECEIVABLES.getType());
        }
        return crmModel;
    }

    /**
     * 保存或新增信息
     *
     * @param crmModel model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(CrmContractSaveBO crmModel) {
        CrmReceivables crmReceivables = BeanUtil.copyProperties(crmModel.getEntity(), CrmReceivables.class);
        CrmContract crmContract = crmContractService.getById(crmReceivables.getContractId());
        if (crmContract == null || !crmContract.getCheckStatus().equals(1)) {
            throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_ADD_ERROR);
        }
        crmReceivables.setCreateUserId(UserUtil.getUserId());
        String batchId = StrUtil.isNotEmpty(crmReceivables.getBatchId()) ? crmReceivables.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_business_data"));
        crmReceivablesDataService.saveData(crmModel.getField(), batchId);
        ExamineRecordSaveBO examineRecordSaveBO = crmModel.getExamineFlowData();
        ExamineRecordReturnVO examineData = null;
        if (crmReceivables.getReceivablesId() == null) {
            List<AdminConfig> configList = adminService.queryConfigByName("numberSetting").getData();
            AdminConfig adminConfig = configList.stream().filter(config -> Objects.equals(getLabel().getType().toString(), config.getValue())).collect(Collectors.toList()).get(0);
            if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmReceivables.getNumber())) {
                String result = crmNumberSettingService.generateNumber(adminConfig, null);
                crmReceivables.setNumber(result);
            }
            Integer count = lambdaQuery().eq(CrmReceivables::getNumber, crmReceivables.getNumber()).ne(CrmReceivables::getCheckStatus, 7).count();
            if (count != null && count > 0) {
                throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_NUM_ERROR);
            }
            crmReceivables.setCreateTime(DateUtil.date());
            crmReceivables.setUpdateTime(DateUtil.date());
            crmReceivables.setBatchId(batchId);
            crmReceivables.setOwnerUserId(UserUtil.getUserId());
            save(crmReceivables);
            if (crmReceivables.getCheckStatus() == null || crmReceivables.getCheckStatus() != 5) {
                if (examineRecordSaveBO != null) {
                    this.supplementFieldInfo(2, crmReceivables.getReceivablesId(), null, examineRecordSaveBO);
                    examineRecordSaveBO.setTitle(crmReceivables.getNumber());
                    examineData = examineService.addExamineRecord(examineRecordSaveBO).getData();
                    crmReceivables.setExamineRecordId(examineData.getRecordId());
                    crmReceivables.setCheckStatus(examineData.getExamineStatus());

                } else {
                    crmReceivables.setCheckStatus(1);
                }
            }
            updateById(crmReceivables);
            if (crmReceivables.getCheckStatus() == 1) {
                examineRecordService.updateContractMoney(crmReceivables.getReceivablesId());
            }
            CrmReceivablesPlan crmReceivablesPlan = crmReceivablesPlanService.getById(crmReceivables.getPlanId());
            if (crmReceivablesPlan != null) {
                crmReceivablesPlan.setReceivablesId(crmReceivables.getReceivablesId());
                crmReceivablesPlan.setUpdateTime(DateUtil.date());
                crmReceivablesPlanService.updateById(crmReceivablesPlan);
            }
            crmActivityService.addActivity(2, CrmActivityEnum.RECEIVABLES, crmReceivables.getReceivablesId());
            actionRecordUtil.addRecord(crmReceivables.getReceivablesId(), CrmEnum.RECEIVABLES, crmReceivables.getNumber());
        } else {
            CrmReceivables receivables = getById(crmReceivables.getReceivablesId());
            if (!receivables.getCreateUserId().equals(UserUtil.getUserId()) && examineRecordSaveBO != null) {
                throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_EDIT_ERROR);
            }
            if (receivables.getCheckStatus() == 1){
                throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_EXAMINE_PASS_ERROR);
            }
            if (receivables.getCheckStatus() != 4 && receivables.getCheckStatus() != 2 && receivables.getCheckStatus() != 5) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EDIT_ERROR);
            }
            if (crmReceivables.getCheckStatus() == null || crmReceivables.getCheckStatus() != 5) {
                if (examineRecordSaveBO != null) {
                    this.supplementFieldInfo(2, receivables.getReceivablesId(), receivables.getExamineRecordId(), examineRecordSaveBO);
                    examineRecordSaveBO.setTitle(crmReceivables.getNumber());
                    examineData = examineService.addExamineRecord(examineRecordSaveBO).getData();
                    crmReceivables.setExamineRecordId(examineData.getRecordId());
                    crmReceivables.setCheckStatus(examineData.getExamineStatus());
                } else {
                    crmReceivables.setCheckStatus(1);
                    if (receivables.getCheckStatus() != 1) {
                        examineRecordService.updateContractMoney(crmReceivables.getReceivablesId());
                    }
                }
            }
            crmReceivables.setUpdateTime(DateUtil.date());
            crmReceivablesPlanService.update().eq("receivables_id", receivables.getReceivablesId()).set("receivables_id", null).update();
            CrmReceivablesPlan crmReceivablesPlan = crmReceivablesPlanService.getById(crmReceivables.getPlanId());
            if (crmReceivablesPlan != null) {
                crmReceivablesPlan.setReceivablesId(crmReceivables.getReceivablesId());
                crmReceivablesPlan.setUpdateTime(DateUtil.date());
                crmReceivablesPlanService.updateById(crmReceivablesPlan);
            }
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(receivables), BeanUtil.beanToMap(crmReceivables), CrmEnum.CONTRACT, crmContract.getName(), crmContract.getContractId());
            updateById(crmReceivables);
            crmReceivables = getById(crmReceivables.getReceivablesId());
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmReceivables));
        savePage(crmModel, crmReceivables.getReceivablesId(),false);
    }

    @Override
    public void setOtherField(Map<String, Object> map) {
        String customerName = crmCustomerService.getCustomerName((Integer) map.get("customerId"));
        map.put("customerName", customerName);
        CrmContract contract = crmContractService.getById((Serializable) map.get("contractId"));
        if (contract != null) {
            map.put("contractNum", contract.getNum());
            map.put("contractMoney", contract.getMoney());
        }
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName", ownerUserName);
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName", createUserName);
        Integer planId = (Integer) map.get("planId");
        if (planId != null) {
            map.put("planNum", crmReceivablesPlanService.query().select("num").eq("plan_id", planId).one().getNum());
        }
    }

    @Override
    public Dict getSearchTransferMap() {
        return Dict.create()
                .set("customerId", "customerName").set("contractId", "contractNum").set("planId","planNum");
    }

    /**
     * 删除回款数据
     *
     * @param ids ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        Integer count = crmReceivablesPlanService.lambdaQuery().in(CrmReceivablesPlan::getReceivablesId, ids).count();
        if (count != 0) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_JOIN_ERROR);
        }
        for (Integer id : ids) {
            CrmReceivables receivables = getById(id);
            boolean bol = (receivables.getCheckStatus() != 4 && receivables.getCheckStatus() != 5) && !UserUtil.isAdmin();
            if (bol) {
                throw new CrmException(CrmCodeEnum.CAN_ONLY_DELETE_WITHDRAWN_AND_SUBMITTED_EXAMINE);
            }
            if (receivables.getCheckStatus() == 1) {
                examineRecordService.updateUnreceivedMoney(id);
                //还原合同金额
            }
            //删除跟进记录
            crmActivityService.deleteActivityRecord(CrmActivityEnum.RECEIVABLES, Collections.singletonList(id));
            //删除字段操作记录
            crmActionRecordService.deleteActionRecord(CrmEnum.RECEIVABLES, Collections.singletonList(receivables.getReceivablesId()));

            //删除自定义字段
            crmReceivablesDataService.deleteByBatchId(Collections.singletonList(receivables.getBatchId()));
            //删除文件
            adminFileService.delete(Collections.singletonList(receivables.getBatchId()));
            if (ObjectUtil.isNotEmpty(receivables.getExamineRecordId())) {
                examineService.deleteExamineRecord(receivables.getExamineRecordId());
            }
            receivables.setCheckStatus(7);
            updateById(receivables);
        }
        deletePage(ids);
    }

    /**
     * 修改回款负责人
     *
     * @param receivablesIds 回款id列表
     * @param newOwnerUserId 新负责人ID
     */
    @Override
    public void changeOwnerUser(List<Integer> receivablesIds, Long newOwnerUserId) {
        LambdaUpdateWrapper<CrmReceivables> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(CrmReceivables::getReceivablesId, receivablesIds);
        wrapper.set(CrmReceivables::getOwnerUserId, newOwnerUserId);
        update(wrapper);
        for (Integer id : receivablesIds) {
            actionRecordUtil.addConversionRecord(id,CrmEnum.RECEIVABLES,newOwnerUserId,getById(id).getNumber());
        }
        //修改es
        updateField("ownerUserId", newOwnerUserId, receivablesIds);
    }

    /**
     * 导出
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
            writer.merge(headList.size() - 1, "回款信息");
            if (dataList.size() == 0) {
                Map<String, Object> record = new HashMap<>();
                headList.forEach(head -> record.put(head.getFieldName(), ""));
                dataList.add(record);
            }
            dataList.forEach(record -> {
                if (record.get("checkStatus") == null || "".equals(record.get("checkStatus"))) {
                    return;
                }
                String checkStatus;
                //0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交
                switch ((Integer) record.get("checkStatus")) {
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
                    default:
                        checkStatus = "待审核";
                }
                record.put("checkStatus", checkStatus);
            });
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
            response.setHeader("Content-Disposition", "attachment;filename=receivables.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出回款错误：", e);
        }
    }

    /**
     * 查询文件数量
     *
     * @param id id
     * @return data
     */
    @Override
    public CrmInfoNumVO num(Integer id) {
        List<String> batchIdList = new ArrayList<>();
        CrmReceivables crmReceivables = getById(id);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmReceivablesData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmReceivablesData::getValue);
            wrapper.eq(CrmReceivablesData::getBatchId, crmReceivables.getBatchId());
            wrapper.in(CrmReceivablesData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            batchIdList.addAll(crmReceivablesDataService.listObjs(wrapper, Object::toString));
        }
        CrmInfoNumVO numVO = new CrmInfoNumVO();
        numVO.setFileCount(fileService.queryNum(batchIdList).getData());
        return numVO;
    }

    /**
     * 查询文件列表
     *
     * @param id id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer id) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmReceivables crmReceivables = getById(id);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmReceivables.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmReceivablesData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmReceivablesData::getValue);
            wrapper.eq(CrmReceivablesData::getBatchId, crmReceivables.getBatchId());
            wrapper.in(CrmReceivablesData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(crmReceivablesDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("回款详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        return fileEntityList;
    }

    @Override
    public List<CrmModelFiledVO> information(Integer contractId) {
        CrmModel crmModel = queryById(contractId);
        List<String> keyList = Arrays.asList("number", "returnTime", "money", "remark", "returnType");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间", "更新时间");
        List<CrmModelFiledVO> crmModelFiledVOS = queryInformation(crmModel, keyList);
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("checkStatus").setName("审核状态").setValue(crmModel.get("checkStatus")).setFormType("check_status").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("contractId").setName("合同编号").setValue(new JSONObject().fluentPut("contractId", crmModel.get("contractId")).fluentPut("contractNum", crmModel.get("contractNum"))).setFormType("contract").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("contractId").setName("合同名称").setValue(new JSONObject().fluentPut("contractId", crmModel.get("contractId")).fluentPut("contractNum", crmModel.get("contractName"))).setFormType("contract").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("customerId").setName("客户名称").setValue(new JSONObject().fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName"))).setFormType("customer").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("planId").setName("期数").setValue(crmModel.get("planNum")).setFormType("planNum").setFieldType(FieldEnum.NUMBER.getType()));
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


    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer receivablesId = updateInformationBO.getId();
        Integer examineCount = crmExamineService.queryCount(getLabel());
        CrmReceivables receivables = getById(receivablesId);
        if (!receivables.getCreateUserId().equals(UserUtil.getUserId()) && examineCount > 0) {
            throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_EDIT_ERROR);
        }
        if (receivables.getCheckStatus() == 1){
            throw new CrmException(CrmCodeEnum.CRM_RECEIVABLES_EXAMINE_PASS_ERROR);
        }
        if (receivables.getCheckStatus() != 4 && receivables.getCheckStatus() != 2 && receivables.getCheckStatus() != 5) {
            throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EDIT_ERROR);
        }
        updateInformationBO.getList().forEach(record -> {
            CrmReceivables oldReceivables = getById(updateInformationBO.getId());
            Map<String, Object> oldReceivablesMap = BeanUtil.beanToMap(oldReceivables);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmReceivablesMap = new HashMap<>(oldReceivablesMap);
                crmReceivablesMap.put(record.getString("fieldName"), record.get("value"));
                CrmReceivables crmReceivables = BeanUtil.mapToBean(crmReceivablesMap, CrmReceivables.class, true);
                actionRecordUtil.updateRecord(oldReceivablesMap, crmReceivablesMap, CrmEnum.RECEIVABLES, crmReceivables.getNumber(), crmReceivables.getReceivablesId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("receivables_id",updateInformationBO.getId()).update();
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {

                CrmReceivablesData receivablesData = crmReceivablesDataService.lambdaQuery().select(CrmReceivablesData::getValue).eq(CrmReceivablesData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmReceivablesData::getBatchId, batchId).one();
                String value = receivablesData != null ? receivablesData.getValue() : null;
                String detail = actionRecordUtil.getDetailByFormTypeAndValue(record,value);
                actionRecordUtil.publicContentRecord(CrmEnum.RECEIVABLES, BehaviorEnum.UPDATE, receivablesId, oldReceivables.getNumber(), detail);
                boolean bol = crmReceivablesDataService.lambdaUpdate()
                        .set(CrmReceivablesData::getName,record.getString("fieldName"))
                        .set(CrmReceivablesData::getValue, record.getString("value"))
                        .eq(CrmReceivablesData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmReceivablesData::getBatchId, batchId).update();
                if (!bol) {
                    CrmReceivablesData crmReceivabelsData = new CrmReceivablesData();
                    crmReceivabelsData.setFieldId(record.getInteger("fieldId"));
                    crmReceivabelsData.setName(record.getString("fieldName"));
                    crmReceivabelsData.setValue(record.getString("value"));
                    crmReceivabelsData.setCreateTime(new Date());
                    crmReceivabelsData.setBatchId(batchId);
                    crmReceivablesDataService.save(crmReceivabelsData);
                }
            }
            updateField(record, receivablesId);
        });
    }

    @Override
    public BasePage<JSONObject> queryListByContractId(BasePage<JSONObject> page,Integer contractId, String conditions) {
        BasePage<JSONObject> jsonObjects = getBaseMapper().queryListByContractId(page,contractId, conditions);
        for (JSONObject jsonObject : jsonObjects.getList()) {
            Long ownerUserId = jsonObject.getLong("ownerUserId");
            String ownerUserName = UserCacheUtil.getUserName(ownerUserId);
            jsonObject.put("ownerUserName",ownerUserName);
        }
        return jsonObjects;
    }
}
