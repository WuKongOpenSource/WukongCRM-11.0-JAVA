package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmActivityEnum;
import com.kakarote.crm.constant.CrmBackLogEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmLeadsMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 线索表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
@Service
@Slf4j
public class CrmLeadsServiceImpl extends BaseServiceImpl<CrmLeadsMapper, CrmLeads> implements ICrmLeadsService, CrmPageService {


    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmLeadsDataService crmLeadsDataService;

    @Autowired
    private ICrmLeadsUserStarService crmLeadsUserStarService;

    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ICrmCustomerSettingService crmCustomerSettingService;

    @Autowired
    private ICrmCustomerDataService crmCustomerDataService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private FieldService fieldService;

    @Resource
    private CrmPageService customerService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Autowired
    private ActionRecordUtil actionRecordUtil;


    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"leadsName", "telephone", "mobile"};
    }

    @Override
    public CrmEnum getLabel() {
        return CrmEnum.LEADS;
    }

    /**
     * 查询所有字段
     *
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        List<CrmModelFiledVO> filedList = crmFieldService.queryField(getLabel().getType());
        filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME,1));
        filedList.add(new CrmModelFiledVO("lastContent", FieldEnum.TEXTAREA,1));
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME,1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME,1));
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER,1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER,1));
        filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT,1));
        filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT,1));
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
        return queryField(id,false);
    }

    private List<CrmModelFiledVO> queryField(Integer id,boolean appendInformation) {
        CrmModel crmModel = queryById(id);
        List<CrmModelFiledVO> filedVOS = crmFieldService.queryField(crmModel);
        if(appendInformation){
            List<CrmModelFiledVO> modelFiledVOS = appendInformation(crmModel);
            filedVOS.addAll(modelFiledVOS);
        }
        return filedVOS;
    }

    @Override
    public List<List<CrmModelFiledVO>> queryFormPositionField(Integer id) {
        CrmModel crmModel = queryById(id);
        return crmFieldService.queryFormPositionFieldVO(crmModel);
    }

    /**
     * 分页查询
     *
     * @param search
     * @return
     */
    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        BasePage<Map<String, Object>> basePage = queryList(search,false);
        Long userId = UserUtil.getUserId();
        List<Integer> starIds = crmLeadsUserStarService.starList(userId);
        basePage.getList().forEach(map -> {
            map.put("star", starIds.contains((Integer) map.get("leadsId"))?1:0);
        });
        return basePage;
    }

    @Override
    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<CrmLeads> list = lambdaQuery().select(CrmLeads::getLeadsId,CrmLeads::getLeadsName).in(CrmLeads::getLeadsId, ids).list();
        return list.stream().map(crmLeads -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmLeads.getLeadsId());
            simpleCrmEntity.setName(crmLeads.getLeadsName());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
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
            crmModel.setLabel(CrmEnum.LEADS.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            crmLeadsDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
        } else {
            crmModel = new CrmModel(CrmEnum.LEADS.getType());
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
    public void addOrUpdate(CrmModelSaveBO crmModel,boolean isExcel) {
        CrmLeads crmLeads = BeanUtil.copyProperties(crmModel.getEntity(), CrmLeads.class);
        String batchId = StrUtil.isNotEmpty(crmLeads.getBatchId()) ? crmLeads.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_leads_data"));
        crmLeadsDataService.saveData(crmModel.getField(), batchId);
        if (StrUtil.isEmpty(crmLeads.getEmail())) {
            crmLeads.setEmail(null);
        }
        //修改下次联系时间,待办事项需要提醒,需要同步最后跟进时间
        if (crmLeads.getNextTime()!=null){
            crmLeads.setLastTime(DateUtil.date());
        }
        if (crmLeads.getLeadsId() != null) {
            crmLeads.setCustomerId(0);
            crmLeads.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(getById(crmLeads.getLeadsId())), BeanUtil.beanToMap(crmLeads), CrmEnum.LEADS, crmLeads.getLeadsName(), crmLeads.getLeadsId());
            updateById(crmLeads);
            //查询一次保存es,因为有些字段没有保存es会出现null
            crmLeads = getById(crmLeads.getLeadsId());
            crmBackLogDealService.deleteByType(crmLeads.getOwnerUserId(), CrmEnum.LEADS, CrmBackLogEnum.FOLLOW_LEADS, crmLeads.getLeadsId());
        } else {
            crmLeads.setCreateTime(DateUtil.date());
            crmLeads.setUpdateTime(DateUtil.date());
            crmLeads.setCreateUserId(UserUtil.getUserId());
            crmLeads.setIsTransform(0);
            if(!isExcel){
                crmLeads.setFollowup(0);
            }
            crmLeads.setCreateUserId(UserUtil.getUserId());
            if (crmLeads.getOwnerUserId() == null) {
                crmLeads.setOwnerUserId(UserUtil.getUserId());
            }
            crmLeads.setBatchId(batchId);
            save(crmLeads);
            actionRecordUtil.addRecord(crmLeads.getLeadsId(), CrmEnum.LEADS, crmLeads.getLeadsName());
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmLeads));
        savePage(crmModel, crmLeads.getLeadsId(),isExcel);
    }

    @Override
    public void setOtherField(Map<String, Object> map) {
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName",ownerUserName);
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName",createUserName);
    }


    /**
     * 删除线索数据
     *
     * @param ids ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        LambdaQueryWrapper<CrmLeads> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmLeads::getBatchId);
        wrapper.in(CrmLeads::getLeadsId, ids);
        List<String> batchIdList = listObjs(wrapper, Object::toString);
        //删除跟进记录
        crmActivityService.deleteActivityRecord(CrmActivityEnum.LEADS, ids);
        //删除字段操作记录
        crmActionRecordService.deleteActionRecord(CrmEnum.LEADS, ids);
        //删除自定义字段
        crmLeadsDataService.deleteByBatchId(batchIdList);
        //todo 删除文件,暂不处理
        removeByIds(ids);
        //删除es数据
        deletePage(ids);
    }

    /**
     * 修改线索负责人
     *
     * @param leadsIds       线索id列表
     * @param newOwnerUserId 新负责人ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeOwnerUser(List<Integer> leadsIds, Long newOwnerUserId) {
        LambdaUpdateWrapper<CrmLeads> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(CrmLeads::getLeadsId, leadsIds);
        wrapper.set(CrmLeads::getOwnerUserId, newOwnerUserId);
        wrapper.set(CrmLeads::getFollowup, 0);
        wrapper.set(CrmLeads::getIsReceive, 1);
        for (Integer leadsId : leadsIds) {
            CrmLeads crmLeads = getById(leadsId);
            BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + crmLeads.getOwnerUserId().toString());
            actionRecordUtil.addConversionRecord(leadsId,CrmEnum.LEADS,newOwnerUserId,crmLeads.getLeadsName());
        }
        update(wrapper);
        BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + newOwnerUserId.toString());
        //修改es
        String ownerUserName = UserCacheUtil.getUserName(newOwnerUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("ownerUserId", newOwnerUserId);
        map.put("ownerUserName", ownerUserName);
        map.put("followup", 0);
        map.put("isReceive", 1);
        updateField(map, leadsIds);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(List<Integer> leadsIds) {
        List<Integer> customerIds = new ArrayList<>();
        Map<Integer, CrmModelSaveBO> crmModelSaveBoMap = new HashMap<>(8);
        for (Integer leadsId : leadsIds) {
            CrmModel leadsMap = queryById(leadsId);
            CrmLeads crmLeads = BeanUtil.copyProperties(leadsMap,CrmLeads.class);
            if (crmLeads.getIsTransform() == 1) {
                throw new CrmException(CrmCodeEnum.CRM_LEADS_TRANSFER_ERROR);
            }
            CrmCustomer crmCustomer = new CrmCustomer();
            crmCustomer.setCustomerName(crmLeads.getLeadsName());
            crmCustomer.setNextTime(crmLeads.getNextTime());
            crmCustomer.setMobile(crmLeads.getMobile());
            crmCustomer.setTelephone(crmLeads.getTelephone());
            crmCustomer.setDealStatus(0);
            crmCustomer.setCreateUserId(UserUtil.getUserId());
            crmCustomer.setOwnerUserId(crmLeads.getOwnerUserId());
            crmCustomer.setCreateTime(new Date());
            crmCustomer.setUpdateTime(new Date());
            crmCustomer.setReceiveTime(new Date());
            crmCustomer.setDetailAddress(crmLeads.getAddress());
            crmCustomer.setLocation("");
            crmCustomer.setAddress("");
            crmCustomer.setLng("");
            crmCustomer.setLat("");
            crmCustomer.setRemark("");
            crmCustomer.setEmail(crmLeads.getEmail());
            crmCustomer.setStatus(1);
            crmCustomer.setLastContent(crmLeads.getLastContent());
            crmCustomer.setLastTime(crmLeads.getLastTime());
            String customerBatchId = IdUtil.simpleUUID();
            crmCustomer.setBatchId(customerBatchId);
            List<CrmField> leadsFields = crmFieldService.list(CrmEnum.LEADS.getType(), false);
            List<CrmField> customerFields = crmFieldService.list(CrmEnum.CUSTOMER.getType(), true);
            List<CrmCustomerData> customerDataList = new ArrayList<>();
            Map<String, Object> customerExtraMap = new HashMap<>();
            for (CrmField leadsField : leadsFields) {
                for (CrmField customerField : customerFields) {
                    Integer isUnique = customerField.getIsUnique();
                    boolean bol = ("客户来源".equals(customerField.getName()) && "线索来源".equals(leadsField.getName()))
                            || ("客户行业".equals(customerField.getName()) && "客户行业".equals(leadsField.getName()))
                            || ("客户级别".equals(customerField.getName()) && "客户级别".equals(leadsField.getName()));
                    if (bol) {
                        if (isUnique == 1 && crmFieldService.queryCustomerFieldDuplicateByNoFixed(customerField.getName(), leadsMap.get(leadsField.getName())) > 0) {
                            throw new CrmException(CrmCodeEnum.CRM_FIELD_EXISTED, customerField.getName());
                        }
                        CrmCustomerData crmCustomerData = new CrmCustomerData();
                        crmCustomerData.setValue((String) leadsMap.get(leadsField.getFieldName()));
                        crmCustomerData.setFieldId(customerField.getFieldId());
                        crmCustomerData.setName(customerField.getName());
                        crmCustomerData.setFieldName(customerField.getFieldName());
                        customerDataList.add(crmCustomerData);
                        continue;
                    }
                    if (leadsField.getRelevant() != null && customerField.getFieldId().equals(leadsField.getRelevant())) {
                        if (customerField.getFieldType().equals(1)) {
                            customerExtraMap.put(customerField.getFieldName(), leadsMap.get(StrUtil.toCamelCase(leadsField.getFieldName())));
                        } else {
                            CrmCustomerData crmCustomerData = new CrmCustomerData();
                            crmCustomerData.setValue((String) leadsMap.get(StrUtil.toCamelCase(leadsField.getFieldName())));
                            crmCustomerData.setFieldId(customerField.getFieldId());
                            crmCustomerData.setName(customerField.getName());
                            crmCustomerData.setFieldName(StrUtil.toCamelCase(customerField.getFieldName()));
                            customerDataList.add(crmCustomerData);
                        }
                    }

                }
            }
            BeanUtil.fillBeanWithMap(customerExtraMap, crmCustomer, true);
            crmCustomer.setBatchId(customerBatchId);
            for (CrmField customerField : customerFields) {
                Integer isUnique = customerField.getIsUnique();
                String name = customerField.getName();
                Map<String, Object> customerMap = BeanUtil.beanToMap(crmCustomer);
                for (String key : customerMap.keySet()) {
                    if (key.equals(StrUtil.toCamelCase(customerField.getFieldName()))) {
                        Object value = customerMap.get(key);
                        if (value != null && !"".equals(value.toString())) {
                            if (isUnique == 1 && crmFieldService.queryCustomerFieldDuplicateByFixed(customerField.getFieldName(), value) > 0) {
                                throw new CrmException(CrmCodeEnum.CRM_FIELD_EXISTED, name);
                            }
                        }
                    }
                }
            }
            if (!crmCustomerSettingService.queryCustomerSettingNum(1, crmCustomer.getOwnerUserId())) {
                throw new CrmException(CrmCodeEnum.THE_NUMBER_OF_CUSTOMERS_HAS_REACHED_THE_LIMIT);
            }
            crmCustomerService.save(crmCustomer);
            Integer customerId = crmCustomer.getCustomerId();
            customerIds.add(customerId);
            //保存自定义字段
            saveCustomerField(customerDataList, customerBatchId);
            CrmModelSaveBO crmModelSaveBO = new CrmModelSaveBO();
            crmModelSaveBO.setEntity(BeanUtil.beanToMap(crmCustomer));
            List<CrmModelFiledVO> collect = customerDataList.stream().map(field -> BeanUtil.copyProperties(field, CrmModelFiledVO.class)).collect(Collectors.toList());
            crmModelSaveBO.setField(collect);
            crmModelSaveBoMap.put(customerId,crmModelSaveBO);

            actionRecordUtil.addConversionCustomerRecord(crmCustomer.getCustomerId(), CrmEnum.CUSTOMER, crmCustomer.getCustomerName());
            lambdaUpdate().set(CrmLeads::getIsTransform, 1).set(CrmLeads::getUpdateTime, new Date()).set(CrmLeads::getCustomerId, crmCustomer.getCustomerId())
                    .eq(CrmLeads::getLeadsId, leadsId).update();

            //转移操作记录
            List<CrmActionRecord> crmActionRecordList = crmActionRecordService.lambdaQuery().eq(CrmActionRecord::getActionId, leadsId).eq(CrmActionRecord::getTypes, 1).list();
            crmActionRecordList.forEach(crmActionRecord -> {
                crmActionRecord.setId(null);
                crmActionRecord.setTypes(CrmEnum.CUSTOMER.getType());
                crmActionRecord.setActionId(crmCustomer.getCustomerId());
            });
            crmActionRecordService.saveBatch(crmActionRecordList, 500);

            //转移活动数据
            List<CrmActivity> crmActivityList = crmActivityService.lambdaQuery().eq(CrmActivity::getActivityType, 1).eq(CrmActivity::getType, 1).eq(CrmActivity::getActivityTypeId, leadsId).list();
            List<String> adminFileIdList = new ArrayList<>();
            if (crmActivityList.size() != 0) {
                crmActivityList.forEach(crmActivity -> {
                    List<FileEntity> leadsRecordFiles = adminFileService.queryFileList(crmActivity.getBatchId()).getData();
                    String customerRecordBatchId = IdUtil.simpleUUID();
                    List<String> fileIds = leadsRecordFiles.stream().map(FileEntity::getFileId).collect(Collectors.toList());
                    crmActivity.setBatchId(customerRecordBatchId);
                    crmActivity.setActivityId(null);
                    crmActivity.setActivityType(CrmEnum.CUSTOMER.getType());
                    crmActivity.setActivityTypeId(crmCustomer.getCustomerId());
                    adminFileService.saveBatchFileEntity(fileIds, customerRecordBatchId);
                });
                crmActivityService.saveBatch(crmActivityList, 100);
            }
            List<FileEntity> fileList = adminFileService.queryFileList(crmLeads.getBatchId()).getData();
            if (fileList.size() != 0) {
                fileList.forEach(adminFile -> {
                    adminFileIdList.add(adminFile.getFileId());
                });
            }
            adminFileService.saveBatchFileEntity(adminFileIdList, customerBatchId);
        }
        for (int i = 0; i < leadsIds.size(); i++) {
            Integer leadsId = leadsIds.get(i);
            Integer customerId = customerIds.get(i);
            UpdateRequest updateRequest = new UpdateRequest(CrmEnum.LEADS.getIndex(),"_doc",leadsId.toString());
            Map<String,Object> map = new HashMap<>();
            map.put("isTransform",1);
            map.put("updateTime",DateUtil.formatDateTime(new Date()));
            map.put("customerId",customerId);
            updateRequest.doc(map);
            try {
                elasticsearchRestTemplate.getClient().update(updateRequest, RequestOptions.DEFAULT);
            }catch (IOException e){
                log.error("es 更新异常!");
            }
            customerService.savePage(crmModelSaveBoMap.get(customerId), customerId,false);
        }

        elasticsearchRestTemplate.refresh(getIndex());
    }

    private void saveCustomerField(List<CrmCustomerData> customerDataList, String batchId) {
        if (CollUtil.isEmpty(customerDataList) || StrUtil.isEmpty(batchId)) {
            return;
        }
        crmCustomerDataService.lambdaUpdate().eq(CrmCustomerData::getBatchId, batchId)
                .remove();
        customerDataList.forEach(fieldv -> {
            fieldv.setId(null);
            fieldv.setName(fieldv.getFieldName());
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            crmCustomerDataService.save(fieldv);
        });
    }


    /**
     * 下载导入模板
     *
     * @param response 线索id
     */
    @Override
    public void downloadExcel(HttpServletResponse response) throws IOException {
        List<CrmModelFiledVO> crmModelFiledList = queryField(null);
        int k = 0;
        for (int i = 0; i < crmModelFiledList.size(); i++) {
            if("leadsName".equals(crmModelFiledList.get(i).getFieldName())){
                k=i;break;
            }
        }
        crmModelFiledList.add(k+1,new CrmModelFiledVO("ownerUserId",FieldEnum.TEXT,"负责人",1).setIsNull(1));
        removeFieldByType(crmModelFiledList);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("线索导入表");
        sheet.setDefaultRowHeight((short) 400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < crmModelFiledList.size(); i++) {
            CrmModelFiledVO crmModel = crmModelFiledList.get(i);
            if (FieldEnum.DATE.getType().equals(crmModel.getType())) {
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATE_PATTERN));
                sheet.setDefaultColumnStyle(i, dateStyle);
            } else if (FieldEnum.DATETIME.getType().equals(crmModel.getType())) {
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATETIME_PATTERN));
                sheet.setDefaultColumnStyle(i, dateStyle);
            } else {
                sheet.setDefaultColumnStyle(i, textStyle);
            }
            sheet.setColumnWidth(i, 20 * 256);
        }
        CellStyle cellStyle = wb.createCellStyle();
        HSSFRow titleRow = sheet.createRow(0);
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        titleRow.createCell(0).setCellValue("线索导入模板(*)为必填项");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, crmModelFiledList.size()-1);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < crmModelFiledList.size(); i++) {
                CrmModelFiledVO crmModel = crmModelFiledList.get(i);
                List<String> setting = crmModel.getSetting().stream().map(Object::toString).collect(Collectors.toList());
                // 在第一行第一个单元格，插入选项
                HSSFCell cell = row.createCell(i);
                // 普通写入操作
                cell.setCellValue(crmModel.getName() + (crmModel.getIsNull() == 1 ? "(*)" : ""));
                if (setting.size() != 0) {
                    String fieldName = "_" + crmModel.getFieldName();
                    HSSFSheet hidden = wb.createSheet(fieldName);
                    HSSFCell sheetCell;
                    for (int j = 0, length = setting.size(); j < length; j++) {
                        String name = setting.get(j);
                        HSSFRow sheetRow = hidden.createRow(j);
                        sheetCell = sheetRow.createCell(0);
                        sheetCell.setCellValue(name);
                    }
                    Name namedCell = wb.createName();
                    namedCell.setNameName(fieldName);
                    namedCell.setRefersToFormula(fieldName + "!$A$1:$A$" + setting.size());
                    CellRangeAddressList regions = new CellRangeAddressList(2, Integer.MAX_VALUE, i, i);
                    DVConstraint constraint = DVConstraint.createFormulaListConstraint(fieldName);
                    HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
                    wb.setSheetHidden(wb.getSheetIndex(hidden), true);
                    sheet.addValidationData(dataValidation);
                }
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=leads_import.xls");
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("获取导入模板异常", e);
        } finally {
            wb.close();
        }
    }

    /**
     * 导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    @Override
    public void exportExcel(HttpServletResponse response, CrmSearchBO search) {
        List<Map<String, Object>> dataList = queryList(search,true).getList();
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<CrmFieldSortVO> headList = crmFieldService.queryListHead(getLabel().getType());
            headList.removeIf(head -> FieldEnum.HANDWRITING_SIGN.getFormType().equals(head.getFormType()));
            headList.forEach(head -> writer.addHeaderAlias(head.getFieldName(), head.getName()));
            writer.merge(headList.size() - 1, "线索信息");
            if (dataList.size() == 0) {
                Map<String, Object> record = new HashMap<>();
                headList.forEach(head -> record.put(head.getFieldName(), ""));
                dataList.add(record);
            }
            for (Map<String, Object> record : dataList) {
                headList.forEach(field ->{
                    if (fieldService.equalsByType(field.getType())) {
                        record.put(field.getFieldName(),ActionRecordUtil.parseValue(record.get(field.getFieldName()),field.getType(),false));
                    }
                });
            }
            writer.setOnlyAlias(true);
            writer.write(dataList, true);
            writer.setRowHeight(0, 30);
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
            response.setHeader("Content-Disposition", "attachment;filename=leads.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出线索错误：", e);
        }
    }

    /**
     * 标星
     *
     * @param leads 线索id
     */
    @Override
    public void star(Integer leads) {
        LambdaQueryWrapper<CrmLeadsUserStar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmLeadsUserStar::getLeadsId, leads);
        wrapper.eq(CrmLeadsUserStar::getUserId, UserUtil.getUserId());
        CrmLeadsUserStar star = crmLeadsUserStarService.getOne(wrapper);
        if (star == null) {
            star = new CrmLeadsUserStar();
            star.setLeadsId(leads);
            star.setUserId(UserUtil.getUserId());
            crmLeadsUserStarService.save(star);
        } else {
            crmLeadsUserStarService.removeById(star.getId());
        }
    }

    @Override
    public List<CrmModelFiledVO> information(Integer leadsId) {
        return queryField(leadsId,true);
    }

    /**
     * 查询文件数量
     *
     * @param leadsId id
     * @return data
     */
    @Override
    public CrmInfoNumVO num(Integer leadsId) {
        List<String> batchIdList = new ArrayList<>();
        CrmLeads crmLeads = getById(leadsId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmLeadsData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmLeadsData::getValue);
            wrapper.eq(CrmLeadsData::getBatchId, crmLeads.getBatchId());
            wrapper.in(CrmLeadsData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            batchIdList.addAll(crmLeadsDataService.listObjs(wrapper, Object::toString));
        }
        batchIdList.add(crmLeads.getBatchId());
        batchIdList.addAll(crmActivityService.queryFileBatchId(crmLeads.getLeadsId(), getLabel().getType()));
        CrmInfoNumVO numVO = new CrmInfoNumVO();
        numVO.setFileCount(fileService.queryNum(batchIdList).getData());
        return numVO;
    }


    /**
     * 查询文件列表
     *
     * @param leadsId id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer leadsId) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmLeads crmLeads = getById(leadsId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmLeads.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmLeadsData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmLeadsData::getValue);
            wrapper.eq(CrmLeadsData::getBatchId, crmLeads.getBatchId());
            wrapper.in(CrmLeadsData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(crmLeadsDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("线索详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        List<String> stringList = crmActivityService.queryFileBatchId(crmLeads.getLeadsId(), getLabel().getType());
        if (stringList.size() > 0) {
            List<FileEntity> data = fileService.queryFileList(stringList).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("跟进记录");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        return fileEntityList;
    }

    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer leadsId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmLeads oldLeads = getById(updateInformationBO.getId());
            uniqueFieldIsAbnormal(record.getString("name"),record.getInteger("fieldId"),record.getString("value"),batchId);
            Map<String, Object> oldLeadsMap = BeanUtil.beanToMap(oldLeads);
            if (record.getInteger("fieldType") == 1){
                Map<String,Object> crmLeadsMap = new HashMap<>(oldLeadsMap);
                crmLeadsMap.put(record.getString("fieldName"),record.get("value"));
                CrmLeads crmLeads = BeanUtil.mapToBean(crmLeadsMap, CrmLeads.class, true);
                actionRecordUtil.updateRecord(oldLeadsMap, crmLeadsMap, CrmEnum.LEADS,crmLeads.getLeadsName(),crmLeads.getLeadsId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("leads_id",updateInformationBO.getId()).update();
            }else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2){
                CrmLeadsData leadsData = crmLeadsDataService.lambdaQuery().select(CrmLeadsData::getValue,CrmLeadsData::getId).eq(CrmLeadsData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmLeadsData::getBatchId, batchId).one();
                String value = leadsData != null ? leadsData.getValue() : null;
                actionRecordUtil.publicContentRecord(CrmEnum.LEADS, BehaviorEnum.UPDATE,leadsId,oldLeads.getLeadsName(),record,value);
                String newValue = fieldService.convertObjectValueToString(record.getInteger("type"),record.get("value"),record.getString("value"));
                CrmLeadsData crmLeadsData = new CrmLeadsData();
                crmLeadsData.setId(leadsData != null ? leadsData.getId() : null);
                crmLeadsData.setFieldId(record.getInteger("fieldId"));
                crmLeadsData.setName(record.getString("fieldName"));
                crmLeadsData.setValue(newValue);
                crmLeadsData.setCreateTime(new Date());
                crmLeadsData.setBatchId(batchId);
                crmLeadsDataService.saveOrUpdate(crmLeadsData);

            }
            updateField(record,leadsId);
        });
        this.lambdaUpdate().set(CrmLeads::getUpdateTime,new Date()).eq(CrmLeads::getLeadsId,leadsId).update();
    }

    @Override
    public List<String> eventLeads(CrmEventBO crmEventBO) {
        return getBaseMapper().eventLeads(crmEventBO);
    }

    @Override
    public BasePage<Map<String, Object>> eventLeadsPageList(QueryEventCrmPageBO eventCrmPageBO) {
        Long userId = eventCrmPageBO.getUserId();
        Long time = eventCrmPageBO.getTime();
        if (userId == null) {
            userId = UserUtil.getUserId();
        }
        List<Integer> leadsIds = getBaseMapper().eventLeadsList(userId, new Date(time));
        if (leadsIds.size() == 0) {
            return new BasePage<>();
        }
        List<String> collect = leadsIds.stream().map(Object::toString).collect(Collectors.toList());
        CrmSearchBO crmSearchBO = new CrmSearchBO();
        crmSearchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "text", CrmSearchBO.FieldSearchEnum.ID, collect)));
        crmSearchBO.setLabel(CrmEnum.LEADS.getType());
        crmSearchBO.setPage(eventCrmPageBO.getPage());
        crmSearchBO.setLimit(eventCrmPageBO.getLimit());
        return queryPageList(crmSearchBO);
    }
}
