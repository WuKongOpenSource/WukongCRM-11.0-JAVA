package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
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
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmProductStatusBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmProductMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 产品表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
@Slf4j
public class CrmProductServiceImpl extends BaseServiceImpl<CrmProductMapper, CrmProduct> implements ICrmProductService, CrmPageService {

    private static final String PRODUCT_STATUS_URL = "/crmProduct/updateStatus";

    @Autowired
    private ICrmProductDataService crmProductDataService;

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmProductCategoryService crmProductCategoryService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminFileService adminFileService;



    @Autowired
    private ICrmProductDetailImgService productDetailImgService;

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
        crmModel.setLabel(getLabel().getType());
        List<CrmModelFiledVO> crmModelFiledVoS = crmFieldService.queryField(crmModel);
        for (CrmModelFiledVO crmModelFiledVO : crmModelFiledVoS) {
            if ("categoryId".equals(crmModelFiledVO.getFieldName())) {
                List<Integer> list = crmProductCategoryService.queryId(null, (Integer) crmModelFiledVO.getValue());
                if (CollUtil.isNotEmpty(list)){
                    crmModelFiledVO.setValue(list);
                }else {
                    crmModelFiledVO.setValue(null);
                }
            }
        }

        int authLevel = 3;
        Long userId = UserUtil.getUserId();
        String key = userId.toString();
        List<String> noAuthMenuUrls = BaseUtil.getRedis().get(key);
        if (noAuthMenuUrls != null && noAuthMenuUrls.contains(PRODUCT_STATUS_URL)) {
            authLevel = 2;
        }
        List<Object> statusList = new ArrayList<>();
        statusList.add(new JSONObject().fluentPut("name", "上架").fluentPut("value", 1));
        statusList.add(new JSONObject().fluentPut("name", "下架").fluentPut("value", 0));
        crmModelFiledVoS.add(new CrmModelFiledVO("status", FieldEnum.SELECT, "是否上下架", 1).setIsNull(1).setSetting(statusList).setValue(crmModel.get("status")).setAuthLevel(authLevel));
        if(appendInformation){
            List<CrmModelFiledVO> modelFiledVOS = appendInformation(crmModel);
            crmModelFiledVoS.addAll(modelFiledVOS);
        }
        return crmModelFiledVoS;
    }

    @Override
    public List<List<CrmModelFiledVO>> queryFormPositionField(Integer id) {
        CrmModel crmModel = queryById(id);
        crmModel.setLabel(getLabel().getType());
        List<List<CrmModelFiledVO>> crmModelFiledVoS = crmFieldService.queryFormPositionFieldVO(crmModel);
        for (List<CrmModelFiledVO> filedVOList : crmModelFiledVoS) {
            for (CrmModelFiledVO crmModelFiledVO : filedVOList) {
                if ("categoryId".equals(crmModelFiledVO.getFieldName())) {
                    List<Integer> list = crmProductCategoryService.queryId(null, (Integer) crmModelFiledVO.getValue());
                    if (CollUtil.isNotEmpty(list)) {
                        crmModelFiledVO.setValue(list);
                    } else {
                        crmModelFiledVO.setValue(null);
                    }
                }
            }
        }

        int authLevel = 3;
        Long userId = UserUtil.getUserId();
        String key = userId.toString();
        List<String> noAuthMenuUrls = BaseUtil.getRedis().get(key);
        if (noAuthMenuUrls != null && noAuthMenuUrls.contains(PRODUCT_STATUS_URL)) {
            authLevel = 2;
        }
        List<Object> statusList = new ArrayList<>();
        statusList.add(new JSONObject().fluentPut("name", "上架").fluentPut("value", 1));
        statusList.add(new JSONObject().fluentPut("name", "下架").fluentPut("value", 0));
        CrmModelFiledVO crmModelFiledVO = new CrmModelFiledVO("status", FieldEnum.SELECT, "是否上下架", 1).setIsNull(1).setSetting(statusList).setValue(crmModel.get("status")).setAuthLevel(authLevel);
        crmModelFiledVO.setStylePercent(50);
        crmModelFiledVoS.add(ListUtil.toList(crmModelFiledVO));
        return crmModelFiledVoS;
    }

    /**
     * 分页查询
     *
     * @param search 搜索添加
     * @return data
     */
    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        BasePage<Map<String, Object>> basePage = queryList(search,false);
        basePage.getList().forEach(map -> {
            String status = map.get("status").toString();
            map.put("status", Objects.equals("1", status) ? "上架" : "下架");
        });
        return basePage;
    }

    @Override
    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<CrmProduct> list = lambdaQuery().select(CrmProduct::getProductId,CrmProduct::getName).in(CrmProduct::getProductId, ids).list();
        return list.stream().map(crmProduct -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmProduct.getProductId());
            simpleCrmEntity.setName(crmProduct.getName());
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
            Integer count = lambdaQuery().eq(CrmProduct::getProductId, id).ne(CrmProduct::getStatus, 3).count();
            if (count == 0) {
                throw new CrmException(CrmCodeEnum.CRM_DATE_REMOVE_ERROR);
            }
            crmModel = getBaseMapper().queryById(id, UserUtil.getUserId());
            crmModel.setLabel(CrmEnum.PRODUCT.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            crmProductDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
            Optional<CrmProductDetailImg> detailImgOpt = productDetailImgService.lambdaQuery().eq(CrmProductDetailImg::getProductId, id).oneOpt();
            if (detailImgOpt.isPresent()) {
                CrmProductDetailImg detailImg = detailImgOpt.get();
                if (detailImg.getMainFileIds() != null) {
                    List<FileEntity> mainFileList = adminFileService.queryByIds(TagUtil.toLongSet(detailImg.getMainFileIds())).getData();
                    crmModel.put("mainFileList", mainFileList);
                } else {
                    crmModel.put("mainFileList", new ArrayList<>());
                }
                if (detailImg.getDetailFileIds() != null) {
                    List<FileEntity> detailFileList = adminFileService.queryByIds(TagUtil.toLongSet(detailImg.getDetailFileIds())).getData();
                    crmModel.put("detailFileList", detailFileList);
                } else {
                    crmModel.put("detailFileList", new ArrayList<>());
                }
            } else {
                crmModel.put("mainFileList", new ArrayList<>());
                crmModel.put("detailFileList", new ArrayList<>());
            }
        } else {
            crmModel = new CrmModel(CrmEnum.PRODUCT.getType());
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
        CrmProduct crmProduct = BeanUtil.copyProperties(crmModel.getEntity(), CrmProduct.class);
        String batchId = StrUtil.isNotEmpty(crmProduct.getBatchId()) ? crmProduct.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_product_data"));
        crmProductDataService.saveData(crmModel.getField(), batchId);
        if (crmProduct.getProductId() == null) {
            crmProduct.setCreateUserId(UserUtil.getUserId());
            crmProduct.setCreateTime(DateUtil.date());
            crmProduct.setUpdateTime(DateUtil.date());
            if (crmProduct.getOwnerUserId() == null) {
                crmProduct.setOwnerUserId(UserUtil.getUserId());
            }
            crmProduct.setBatchId(batchId);
            save(crmProduct);
            actionRecordUtil.addRecord(crmProduct.getProductId(), CrmEnum.PRODUCT, crmProduct.getName());
        } else {
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(getById(crmProduct.getProductId())), BeanUtil.beanToMap(crmProduct), CrmEnum.PRODUCT, crmProduct.getName(), crmProduct.getProductId());
            crmProduct.setUpdateTime(DateUtil.date());
            updateById(crmProduct);
            crmProduct = getById(crmProduct.getProductId());
        }
        Optional<CrmProductDetailImg> detailImgOpt = productDetailImgService.lambdaQuery().eq(CrmProductDetailImg::getProductId, crmProduct.getProductId()).oneOpt();
        if (detailImgOpt.isPresent()){
            CrmProductDetailImg crmProductDetailImg = detailImgOpt.get();
            crmProductDetailImg.setDetailFileIds((String) crmModel.getEntity().get("detailFileIds"));
            crmProductDetailImg.setMainFileIds((String) crmModel.getEntity().get("mainFileIds"));
            productDetailImgService.updateById(crmProductDetailImg);
        }else {
            CrmProductDetailImg crmProductDetailImg = new CrmProductDetailImg();
            crmProductDetailImg.setProductId(crmProduct.getProductId());
            crmProductDetailImg.setDetailFileIds((String) crmModel.getEntity().get("detailFileIds"));
            crmProductDetailImg.setMainFileIds((String) crmModel.getEntity().get("mainFileIds"));
            productDetailImgService.save(crmProductDetailImg);
        }
        crmModel.setEntity(BeanUtil.beanToMap(crmProduct));
        savePage(crmModel, crmProduct.getProductId(),isExcel);
    }

    @Override
    public void setOtherField(Map<String, Object> map) {
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName", createUserName);
        CrmProductCategory productCategory = crmProductCategoryService.getById((Serializable) map.get("categoryId"));
        if (productCategory != null){
            map.put("categoryName", productCategory.getName());
        }else {
            map.put("categoryName", "");
        }
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName",ownerUserName);
    }


    /**
     * 删除数据
     *
     * @param ids ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        LambdaQueryWrapper<CrmProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmProduct::getBatchId);
        wrapper.in(CrmProduct::getProductId, ids);
        List<String> batchIdList = listObjs(wrapper, Object::toString);
        //删除字段操作记录
        crmActionRecordService.deleteActionRecord(CrmEnum.PRODUCT, ids);
        if (CollUtil.isNotEmpty(batchIdList)){
            //删除自定义字段
            //TODO 不删除,产品单位是自定义字段,删除后关联的产品没有单位
//            crmProductDataService.deleteByBatchId(batchIdList);
        }
        LambdaUpdateWrapper<CrmProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CrmProduct::getStatus, 3);
        updateWrapper.in(CrmProduct::getProductId, ids);
        update(updateWrapper);
        //todo 删除文件,暂不处理
        //删除es数据
        deletePage(ids);
    }

    /**
     * 修改负责人
     *
     * @param ids            id列表
     * @param newOwnerUserId 新负责人ID
     */
    @Override
    public void changeOwnerUser(List<Integer> ids, Long newOwnerUserId) {
        LambdaUpdateWrapper<CrmProduct> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(CrmProduct::getProductId, ids);
        wrapper.set(CrmProduct::getOwnerUserId, newOwnerUserId);
        update(wrapper);
        for (Integer id : ids) {
            actionRecordUtil.addConversionRecord(id,CrmEnum.PRODUCT,newOwnerUserId,getById(id).getName());
        }
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
     * @param response 产品id
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
        HSSFSheet sheet = wb.createSheet("产品导入表");
        sheet.setDefaultRowHeight((short) 400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < crmModelFiledList.size(); i++) {
            CrmModelFiledVO filed = crmModelFiledList.get(i);
            if (FieldEnum.DATE.getType().equals(filed.getType()) || FieldEnum.DATETIME.getType().equals(filed.getType())) {
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(FieldEnum.DATE.getType().equals(filed.getType()) ? DatePattern.NORM_DATE_PATTERN : DatePattern.NORM_DATETIME_PATTERN));
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
        titleRow.createCell(0).setCellValue("产品导入模板(*)为必填项");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, crmModelFiledList.size()-1);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < crmModelFiledList.size(); i++) {
                CrmModelFiledVO filed = crmModelFiledList.get(i);
                List<String> setting = filed.getSetting().stream().map(Object::toString).collect(Collectors.toList());
                if("status".equals(filed.getFieldName())){
                    setting = filed.getSetting().stream().map(obj->{
                      JSONObject object = (JSONObject) obj;
                      return object.getString("name");
                    }).collect(Collectors.toList());
                }
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(filed.getName() + (filed.getIsNull() == 1 ? "(*)" : ""));
                if ("categoryId".equals(filed.getFieldName())) {
                    setting = crmProductCategoryService.queryListName();
                }
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
            response.setHeader("Content-Disposition", "attachment;filename=product_import.xls");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("下载产品模板error:", e);
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
            writer.merge(headList.size() - 1, "产品信息");
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
            response.setHeader("Content-Disposition", "attachment;filename=product.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出产品错误：", e);
        }
    }

    /**
     * 修改产品状态
     *
     * @param productStatus status
     */
    @Override
    public void updateStatus(CrmProductStatusBO productStatus) {
        Integer status = Objects.equals(0, productStatus.getStatus()) ? 0 : 1;
        LambdaUpdateWrapper<CrmProduct> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CrmProduct::getStatus, status);
        wrapper.in(CrmProduct::getProductId, productStatus.getIds());
        update(wrapper);
        updateField("status",status,productStatus.getIds());
    }

    @Override
    public List<CrmModelFiledVO> information(Integer productId) {
        return queryField(productId,true);
    }

    /**
     * 查询文件数量
     *
     * @param productId id
     * @return data
     */
    @Override
    public CrmInfoNumVO num(Integer productId) {
        CrmProduct crmProduct = getById(productId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        List<CrmField> crmFields = crmFieldService.queryFileField();
        List<String> batchIdList = new ArrayList<>();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmProductData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmProductData::getValue);
            wrapper.eq(CrmProductData::getBatchId, crmProduct.getBatchId());
            wrapper.in(CrmProductData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            batchIdList.addAll(crmProductDataService.listObjs(wrapper, Object::toString));
        }
        batchIdList.add(crmProduct.getBatchId());
        batchIdList.addAll(crmActivityService.queryFileBatchId(crmProduct.getProductId(), getLabel().getType()));
        CrmInfoNumVO infoNumVO = new CrmInfoNumVO();
        infoNumVO.setFileCount(fileService.queryNum(batchIdList).getData());
        return infoNumVO;
    }

    /**
     * 查询文件列表
     *
     * @param productId id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer productId) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmProduct crmProduct = getById(productId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmProduct.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmProductData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmProductData::getValue);
            wrapper.eq(CrmProductData::getBatchId, crmProduct.getBatchId());
            wrapper.in(CrmProductData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(crmProductDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("产品详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        return fileEntityList;
    }


    /**
     * 查询产品对象
     *
     * @return list
     */
    @Override
    public List<SimpleCrmEntity> querySimpleEntity() {
        List<CrmProduct> list = lambdaQuery().ne(CrmProduct::getStatus,3).list();
        return list.stream().map(crmProduct -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmProduct.getProductId());
            simpleCrmEntity.setName(crmProduct.getName());
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
        return new String[]{"name"};
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    @Override
    public CrmEnum getLabel() {
        return CrmEnum.PRODUCT;
    }

    /**
     * 查询所有字段
     *
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        List<CrmModelFiledVO> filedList = crmFieldService.queryField(getLabel().getType());
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME,1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME,1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER,1));
        filedList.add(new CrmModelFiledVO("status", FieldEnum.TEXT,1));
        return filedList;
    }


    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer productId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmProduct oldProduct = getById(updateInformationBO.getId());
            uniqueFieldIsAbnormal(record.getString("name"),record.getInteger("fieldId"),record.getString("value"),batchId);
            Map<String, Object> oldProductMap = BeanUtil.beanToMap(oldProduct);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmProductMap = new HashMap<>(oldProductMap);
                crmProductMap.put(record.getString("fieldName"), record.get("value"));
                CrmProduct crmProduct = BeanUtil.mapToBean(crmProductMap, CrmProduct.class, true);
                actionRecordUtil.updateRecord(oldProductMap, crmProductMap, CrmEnum.PRODUCT, crmProduct.getName(), crmProduct.getProductId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("product_id",updateInformationBO.getId()).update();
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {
                CrmProductData productData = crmProductDataService.lambdaQuery().select(CrmProductData::getValue,CrmProductData::getId).eq(CrmProductData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmProductData::getBatchId, batchId).one();
                String value = productData != null ? productData.getValue() : null;
                actionRecordUtil.publicContentRecord(CrmEnum.PRODUCT, BehaviorEnum.UPDATE, productId, oldProduct.getName(), record,value);
                String newValue = fieldService.convertObjectValueToString(record.getInteger("type"),record.get("value"),record.getString("value"));
                CrmProductData crmProductData = new CrmProductData();
                crmProductData.setId(productData != null ? productData.getId() : null);
                crmProductData.setFieldId(record.getInteger("fieldId"));
                crmProductData.setName(record.getString("fieldName"));
                crmProductData.setValue(newValue);
                crmProductData.setCreateTime(new Date());
                crmProductData.setBatchId(batchId);
                crmProductDataService.saveOrUpdate(crmProductData);
            }
            updateField(record,productId);
        });
        this.lambdaUpdate().set(CrmProduct::getUpdateTime,new Date()).eq(CrmProduct::getProductId,productId).update();
    }
}
