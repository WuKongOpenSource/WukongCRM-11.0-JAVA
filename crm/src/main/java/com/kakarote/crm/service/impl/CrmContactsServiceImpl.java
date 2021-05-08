package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.CrmActivityEnum;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmContactsMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 联系人表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
@Slf4j
public class CrmContactsServiceImpl extends BaseServiceImpl<CrmContactsMapper, CrmContacts> implements ICrmContactsService, CrmPageService {

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmContactsDataService crmContactsDataService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmContactsBusinessService crmContactsBusinessService;

    @Autowired
    private ICrmContactsUserStarService crmContactsUserStarService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private ICrmBusinessService crmBusinessService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private FieldService fieldService;

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
        if (id != null){
            List<JSONObject> customerList = new ArrayList<>();
            if (crmModel.get("customerId")!=null){
                JSONObject customer = new JSONObject();
                customerList.add(customer.fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName")));
            }
            crmModel.put("customerId", customerList);
        }
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
        if (id != null){
            List<JSONObject> customerList = new ArrayList<>();
            if (crmModel.get("customerId")!=null){
                JSONObject customer = new JSONObject();
                customerList.add(customer.fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName")));
            }
            crmModel.put("customerId", customerList);
        }
        return crmFieldService.queryFormPositionFieldVO(crmModel);
    }

    /**
     * 分页查询
     *
     * @param search search
     * @return data
     */
    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        BasePage<Map<String, Object>> basePage = queryList(search,false);
        Long userId = UserUtil.getUserId();
        List<Integer> starIds = crmContactsUserStarService.starList(userId);
        basePage.getList().forEach(map -> {
            map.put("star", starIds.contains((Integer) map.get("contactsId"))?1:0);
        });
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
            crmModel.setLabel(CrmEnum.CONTACTS.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            crmContactsDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
        } else {
            crmModel = new CrmModel(CrmEnum.CONTACTS.getType());
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
    public void addOrUpdate(CrmContactsSaveBO crmModel,boolean isExcel) {
        CrmContacts crmContacts = BeanUtil.copyProperties(crmModel.getEntity(), CrmContacts.class);
        String batchId = StrUtil.isNotEmpty(crmContacts.getBatchId()) ? crmContacts.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_contacts_data"));
        crmContactsDataService.saveData(crmModel.getField(), batchId);
        if (StrUtil.isEmpty(crmContacts.getEmail())) {
            crmContacts.setEmail(null);
        }
        if (crmContacts.getContactsId() != null) {
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(getById(crmContacts.getContactsId())),BeanUtil.beanToMap(crmContacts),CrmEnum.CONTACTS,crmContacts.getName(), crmContacts.getContactsId());
            crmContacts.setUpdateTime(DateUtil.date());
            updateById(crmContacts);
            crmContacts = getById(crmContacts);
            ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(),"contacts",crmContacts.getContactsId().toString(),crmContacts.getName());
        } else {
            crmContacts.setCreateTime(DateUtil.date());
            crmContacts.setUpdateTime(DateUtil.date());
            crmContacts.setCreateUserId(UserUtil.getUserId());
            if (crmContacts.getOwnerUserId() == null) {
                crmContacts.setOwnerUserId(UserUtil.getUserId());
            }
            crmContacts.setBatchId(batchId);
            if (crmContacts.getCustomerId() == null){
                throw new CrmException(CrmCodeEnum.CRM_CONTACTS_DATA_ERROR);
            }
            save(crmContacts);
            if (crmModel.getBusinessId() != null) {
                crmContactsBusinessService.save(crmModel.getBusinessId(), crmContacts.getContactsId());
            }
            crmActivityService.addActivity(2, CrmActivityEnum.CONTACTS, crmContacts.getContactsId());
            actionRecordUtil.addRecord(crmContacts.getContactsId(),CrmEnum.CONTACTS,crmContacts.getName());
        }
        ICrmCustomerService bean = ApplicationContextHolder.getBean(ICrmCustomerService.class);
        CrmCustomer customer = bean.getById(crmContacts.getCustomerId());
        if (customer != null && customer.getContactsId() == null) {
            customer.setContactsId(crmContacts.getContactsId());
            bean.updateById(customer);
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmContacts));
        savePage(crmModel, crmContacts.getContactsId(),isExcel);
    }

    @Override
    public void setOtherField(Map<String, Object> map) {
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName",ownerUserName);
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName",createUserName);
        String customerName = crmCustomerService.getCustomerName((Integer) map.get("customerId"));
        map.put("customerName",customerName);
    }

    @Override
    public Dict getSearchTransferMap() {
        return Dict.create().set("customerId","customerName");
    }

    /**
     * 删除联系人数据
     *
     * @param ids ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        for (Integer id : ids) {
            Integer count = ApplicationContextHolder.getBean(ICrmContractService.class).lambdaQuery().eq(CrmContract::getContactsId, id).ne(CrmContract::getCheckStatus, 7).count();
            if (count > 0) {
                throw new CrmException(CrmCodeEnum.CRM_DATA_JOIN_ERROR);
            }
            CrmContacts contacts = getById(id);
            if (contacts != null) {
                //删除自定义字段
                crmContactsBusinessService.removeByContactsId(id);
                LambdaUpdateWrapper<CrmCustomer> wrapper = new LambdaUpdateWrapper<>();
                wrapper.set(CrmCustomer::getContactsId,null).eq(CrmCustomer::getContactsId, contacts.getContactsId());
                ApplicationContextHolder.getBean(ICrmCustomerService.class).update(wrapper);
                LambdaUpdateWrapper<CrmBusiness> businessLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                businessLambdaUpdateWrapper.set(CrmBusiness::getContactsId,null).eq(CrmBusiness::getContactsId, contacts.getContactsId());
                ApplicationContextHolder.getBean(ICrmBusinessService.class).update(businessLambdaUpdateWrapper);
                //删除跟进记录
                crmActivityService.deleteActivityRecord(CrmActivityEnum.CONTACTS, Collections.singletonList(contacts.getContactsId()));
                //删除字段操作记录
                crmActionRecordService.deleteActionRecord(CrmEnum.CONTACTS, Collections.singletonList(contacts.getContactsId()));
                //删除联系人商机关联
                ApplicationContextHolder.getBean(ICrmContactsBusinessService.class).removeByContactsId(id);
                //删除自定义字段
                crmContactsDataService.deleteByBatchId(Collections.singletonList(contacts.getBatchId()));
                removeById(id);
            }
        }
        deletePage(ids);
    }

    /**
     * 修改联系人负责人
     *
     * @param changeOwnerUserBO  负责人变更BO
     */
    @Override
    public void changeOwnerUser(CrmChangeOwnerUserBO changeOwnerUserBO) {
        Long newOwnerUserId = changeOwnerUserBO.getOwnerUserId();
        List<Integer> ids = changeOwnerUserBO.getIds();
        if (ids.size() == 0) {
            return;
        }
        for (Integer id : ids) {
            if (AuthUtil.isChangeOwnerUserAuth(id, CrmEnum.CONTACTS,CrmAuthEnum.EDIT)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            CrmContacts contacts = getById(id);
            actionRecordUtil.addConversionRecord(id,CrmEnum.CONTACTS,newOwnerUserId,contacts.getName());
            if (2 == changeOwnerUserBO.getTransferType() && !changeOwnerUserBO.getOwnerUserId().equals(contacts.getOwnerUserId())) {
                ApplicationContextHolder.getBean(ICrmTeamMembersService.class).addSingleMember(getLabel(),contacts.getContactsId(),contacts.getOwnerUserId(),changeOwnerUserBO.getPower(),changeOwnerUserBO.getExpiresTime(),contacts.getName());
            }
            ApplicationContextHolder.getBean(ICrmTeamMembersService.class).deleteMember(getLabel(),new CrmMemberSaveBO(id,newOwnerUserId));
        }
        LambdaUpdateWrapper<CrmContacts> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(CrmContacts::getContactsId, ids);
        wrapper.set(CrmContacts::getOwnerUserId, newOwnerUserId);
        update(wrapper);
        //修改es
        String ownerUserName = UserCacheUtil.getUserName(newOwnerUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("ownerUserId", newOwnerUserId);
        map.put("ownerUserName", ownerUserName);
        updateField(map, ids);
    }

    /**
     * 下载导入模板
     *
     * @param response 联系人id
     * @throws IOException exception
     */
    @Override
    public void downloadExcel(HttpServletResponse response) throws IOException {
        List<CrmModelFiledVO> crmModelFiledList = queryField(null);
        int k = 0;
        for (int i = 0; i < crmModelFiledList.size(); i++) {
            if(crmModelFiledList.get(i).getFieldName().equals("name")){
                k=i;break;
            }
        }
        crmModelFiledList.add(k+1,new CrmModelFiledVO("ownerUserId",FieldEnum.TEXT,"负责人",1).setIsNull(1));
        removeFieldByType(crmModelFiledList);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("联系人导入表");
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
        HSSFRow titleRow = sheet.createRow(0);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        titleRow.createCell(0).setCellValue("联系人导入模板(*)为必填项");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, crmModelFiledList.size()-1);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < crmModelFiledList.size(); i++) {
                CrmModelFiledVO filed = crmModelFiledList.get(i);
                List<String> setting = filed.getSetting().stream().map(Object::toString).collect(Collectors.toList());
                // 在第一行第一个单元格，插入选项
                HSSFCell cell = row.createCell(i);
                // 普通写入操作{
                cell.setCellValue(filed.getName() + (filed.getIsNull() == 1 ? "(*)" : ""));
                if (setting.size() != 0) {
                    String fieldName = "_" + filed.getFieldName();
                    fieldName = fieldName.replaceAll("[\\n`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]", "");
                    HSSFSheet hidden = wb.createSheet(fieldName);
                    HSSFCell sheetCell = null;
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
            response.setHeader("Content-Disposition", "attachment;filename=contacts_import.xls");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("联系人模板下载错误：", e);
        } finally {
            wb.close();
        }
    }

    /**
     * 全部导出
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
            response.setHeader("Content-Disposition", "attachment;filename=contacts.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出联系人错误：", e);
        }
    }

    /**
     * 标星
     *
     * @param id 联系人id
     */
    @Override
    public void star(Integer id) {
        LambdaQueryWrapper<CrmContactsUserStar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmContactsUserStar::getContactsId, id);
        wrapper.eq(CrmContactsUserStar::getUserId, UserUtil.getUserId());
        CrmContactsUserStar star = crmContactsUserStarService.getOne(wrapper);
        if (star == null) {
            star = new CrmContactsUserStar();
            star.setContactsId(id);
            star.setUserId(UserUtil.getUserId());
            crmContactsUserStarService.save(star);
        } else {
            crmContactsUserStarService.removeById(star.getId());
        }
    }

    @Override
    public List<CrmModelFiledVO> information(Integer contactsId) {
        return queryField(contactsId,true);
    }

    /**
     * 查询文件数量
     *
     * @param contactsId id
     * @return data
     */
    @Override
    public CrmInfoNumVO num(Integer contactsId) {
        CrmContacts crmContacts = getById(contactsId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        List<CrmField> crmFields = crmFieldService.queryFileField();
        List<String> batchIdList = new ArrayList<>();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmContactsData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmContactsData::getValue);
            wrapper.eq(CrmContactsData::getBatchId, crmContacts.getBatchId());
            wrapper.in(CrmContactsData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            batchIdList.addAll(crmContactsDataService.listObjs(wrapper, Object::toString));
        }
        batchIdList.add(crmContacts.getBatchId());
        batchIdList.addAll(crmActivityService.queryFileBatchId(crmContacts.getCustomerId(), getLabel().getType()));
        Map<String, Object> map = new HashMap<>();
        map.put("contactsId", contactsId);
        CrmInfoNumVO infoNumVO = getBaseMapper().queryNum(map);
        infoNumVO.setFileCount(fileService.queryNum(batchIdList).getData());
        infoNumVO.setMemberCount(ApplicationContextHolder.getBean(ICrmTeamMembersService.class).queryMemberCount(getLabel(),crmContacts.getContactsId(),crmContacts.getOwnerUserId()));
        return infoNumVO;
    }

    /**
     * 查询文件列表
     *
     * @param contactsId id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer contactsId) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmContacts crmContacts = getById(contactsId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmContacts.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmContactsData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmContactsData::getValue);
            wrapper.eq(CrmContactsData::getBatchId, crmContacts.getBatchId());
            wrapper.in(CrmContactsData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(crmContactsDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("联系人详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        List<String> stringList = crmActivityService.queryFileBatchId(crmContacts.getContactsId(), getLabel().getType());
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
    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<CrmContacts> list = lambdaQuery().select(CrmContacts::getContactsId,CrmContacts::getName).in(CrmContacts::getContactsId, ids).list();
        return list.stream().map(crmContacts -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmContacts.getContactsId());
            simpleCrmEntity.setName(crmContacts.getName());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
    }

    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"name", "telephone", "mobile"};
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    @Override
    public CrmEnum getLabel() {
        return CrmEnum.CONTACTS;
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
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME,1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME,1));
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER,1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER,1));
        filedList.add(new CrmModelFiledVO("teamMemberIds", FieldEnum.USER, 0));
        return filedList;
    }

    @Override
    public String getContactsName(int contactsId) {
        return lambdaQuery().select(CrmContacts::getName).eq(CrmContacts::getContactsId,contactsId).oneOpt()
                .map(CrmContacts::getName).orElse("");
    }




    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer contactsId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmContacts oldContacts = getById(updateInformationBO.getId());
            uniqueFieldIsAbnormal(record.getString("name"),record.getInteger("fieldId"),record.getString("value"),batchId);
            Map<String, Object> oldContactsMap = BeanUtil.beanToMap(oldContacts);
            if (record.getInteger("fieldType") == 1){
                Map<String,Object> crmContactsMap = new HashMap<>(oldContactsMap);
                crmContactsMap.put(record.getString("fieldName"),record.get("value"));
                CrmContacts crmContacts = BeanUtil.mapToBean(crmContactsMap, CrmContacts.class, true);
                actionRecordUtil.updateRecord(oldContactsMap, crmContactsMap, CrmEnum.CONTACTS,crmContacts.getName(),crmContacts.getContactsId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")),record.get("value")).eq("contacts_id",updateInformationBO.getId()).update();
                if ("name".equals(record.getString("fieldName"))){
                    ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(),"contacts",crmContacts.getContactsId().toString(),crmContacts.getName());
                }
            }else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2){
                CrmContactsData contactsData  = crmContactsDataService.lambdaQuery()
                        .select(CrmContactsData::getValue,CrmContactsData::getId).eq(CrmContactsData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmContactsData::getBatchId, batchId).one();
                String value = contactsData != null ? contactsData.getValue() : null;
                actionRecordUtil.publicContentRecord(CrmEnum.CONTACTS, BehaviorEnum.UPDATE,contactsId,oldContacts.getName(),record,value);
                String newValue = fieldService.convertObjectValueToString(record.getInteger("type"),record.get("value"),record.getString("value"));
                CrmContactsData crmContactsData = new CrmContactsData();
                crmContactsData.setId(contactsData != null ? contactsData.getId() : null);
                crmContactsData.setFieldId(record.getInteger("fieldId"));
                crmContactsData.setName(record.getString("fieldName"));
                crmContactsData.setValue(newValue);
                crmContactsData.setCreateTime(new Date());
                crmContactsData.setBatchId(batchId);
                crmContactsDataService.saveOrUpdate(crmContactsData);

            }
            updateField(record,contactsId);
        });
        this.lambdaUpdate().set(CrmContacts::getUpdateTime,new Date()).eq(CrmContacts::getContactsId,contactsId).update();
    }

    @Override
    public BasePage<Map<String,Object>> queryBusiness(CrmBusinessPageBO businessPageBO) {
        return getBaseMapper().queryBusiness(businessPageBO.parse(),businessPageBO.getContactsId());
    }

    @Override
    public void relateBusiness(CrmRelateBusinessBO relateBusinessBO) {
        crmContactsBusinessService.lambdaUpdate().eq(CrmContactsBusiness::getContactsId,relateBusinessBO.getContactsId()).remove();
        for (Integer businessId:relateBusinessBO.getBusinessIds()){
            CrmContactsBusiness crmContactsBusiness = new CrmContactsBusiness();
            crmContactsBusiness.setContactsId(relateBusinessBO.getContactsId());
            crmContactsBusiness.setBusinessId(businessId);
            crmContactsBusinessService.save(crmContactsBusiness);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unrelateBusiness(CrmRelateBusinessBO relateBusinessBO) {
        //delete from 72crm_crm_contacts_business where contacts_id = #para(contactsId) and business_id in (#fori(ids))
        crmContactsBusinessService.lambdaUpdate().eq(CrmContactsBusiness::getContactsId,relateBusinessBO.getContactsId())
                .in(CrmContactsBusiness::getBusinessId,relateBusinessBO.getBusinessIds()).remove();
        //UPDATE `72crm_crm_business` SET `contacts_id` = NULL WHERE contacts_id = #para(contactsId) and `business_id`  in (#fori(ids))
        crmBusinessService.lambdaUpdate().set(CrmBusiness::getContactsId,null)
                .eq(CrmBusiness::getContactsId,relateBusinessBO.getContactsId())
                .in(CrmBusiness::getBusinessId,relateBusinessBO.getBusinessIds()).update();
    }
}
