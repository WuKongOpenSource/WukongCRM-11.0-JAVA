package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
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
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.*;
import com.kakarote.crm.mapper.CrmBusinessMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 商机表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
@Slf4j
public class CrmBusinessServiceImpl extends BaseServiceImpl<CrmBusinessMapper, CrmBusiness> implements CrmPageService, ICrmBusinessService {

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private ICrmBusinessDataService crmBusinessDataService;

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmBusinessProductService crmBusinessProductService;

    @Autowired
    private ICrmBusinessChangeService crmBusinessChangeService;

    @Autowired
    private ICrmContactsBusinessService crmContactsBusinessService;

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;

    @Autowired
    private ICrmBusinessUserStarService crmBusinessUserStarService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 大的搜索框的搜索字段
     *
     * @return fields
     */
    @Override
    public String[] appendSearch() {
        return new String[]{"businessName"};
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    @Override
    public CrmEnum getLabel() {
        return CrmEnum.BUSINESS;
    }

    /**
     * 查询所有字段
     *
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryDefaultField() {
        List<CrmModelFiledVO> filedList = crmFieldService.queryField(getLabel().getType());
        filedList.add(new CrmModelFiledVO("typeId", FieldEnum.SELECT, 1));
        filedList.add(new CrmModelFiledVO("statusId", FieldEnum.SELECT, 1));
        filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("receiveTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("nextTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
        filedList.add(new CrmModelFiledVO("roUserId", FieldEnum.CHECKBOX, 1));
        filedList.add(new CrmModelFiledVO("rwUserId", FieldEnum.CHECKBOX, 1));
        filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
        filedList.add(new CrmModelFiledVO("status", FieldEnum.TEXT, 1));
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
        }
        List<CrmModelFiledVO> crmModelFiledVOS = crmFieldService.queryField(crmModel);
        Optional<CrmModelFiledVO> optional = crmModelFiledVOS.stream().filter(record -> "remark".equals(record.getFieldName())).findFirst();
        optional.ifPresent(crmModelFiledVOS::remove);
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("type_id").setName("商机状态组").setValue(crmModel.get("type_id")).setFormType("business_type").setSetting(new ArrayList<>()).setIsNull(1).setFieldType(1).setValue(crmModel.get("typeId")).setAuthLevel(3));
        Object statusId = crmModel.get("statusId");
        if(!Objects.equals(0,crmModel.get("isEnd"))){
            statusId = crmModel.get("isEnd");
        }
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("status_id").setName("商机阶段").setValue(crmModel.get("status_id")).setFormType("business_status").setSetting(new ArrayList<>()).setIsNull(1).setFieldType(1).setValue(statusId).setAuthLevel(3));
        optional.ifPresent(crmModelFiledVOS::add);
        JSONObject object = new JSONObject();
        object.fluentPut("discountRate", crmModel.get("discountRate")).fluentPut("product", crmBusinessProductService.queryList(id)).fluentPut("totalPrice", crmModel.get("totalPrice"));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("product").setName("产品").setValue(object).setFormType("product").setSetting(new ArrayList<>()).setIsNull(0).setFieldType(1));
        return crmModelFiledVOS;
    }

    @Autowired
    private ICrmBusinessTypeService businessTypeService;

    /**
     * 分页查询
     *
     * @param search
     * @return
     */
    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        BasePage<Map<String, Object>> basePage = queryList(search);
        Long userId = UserUtil.getUserId();
        List<Integer> starIds = crmBusinessUserStarService.starList(userId);
        basePage.getList().forEach(map -> {
            map.put("star", starIds.contains((Integer)map.get("businessId"))?1:0);
            CrmListBusinessStatusVO crmListBusinessStatusVO = businessTypeService.queryListBusinessStatus(TypeUtils.castToInt(map.get("typeId")), TypeUtils.castToInt(map.get("statusId")), (Integer) map.get("isEnd"));
            map.put("businessStatusCount",crmListBusinessStatusVO);
        });
        SearchRequest searchRequest = new SearchRequest(getIndex());
        searchRequest.types(getDocType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = createQueryBuilder(search);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.aggregation(AggregationBuilders.sum("businessSumMoney").field("money"));
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchCount = elasticsearchRestTemplate.getClient().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchCount.getAggregations();
            Map<String, Object> countMap = new HashMap<>();
            ParsedSum businessSumMoney = aggregations.get("businessSumMoney");
            countMap.put("businessSumMoney", businessSumMoney.getValue());
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
            crmModel.setLabel(CrmEnum.BUSINESS.getType());
            crmModel.setOwnerUserName(UserCacheUtil.getUserName(crmModel.getOwnerUserId()));
            crmModel.put("createUserName", UserCacheUtil.getUserName((Long) crmModel.get("createUserId")));
            crmBusinessDataService.setDataByBatchId(crmModel);
            List<String> stringList = ApplicationContextHolder.getBean(ICrmRoleFieldService.class).queryNoAuthField(crmModel.getLabel());
            stringList.forEach(crmModel::remove);
        } else {
            crmModel = new CrmModel(CrmEnum.BUSINESS.getType());
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
    public void addOrUpdate(CrmBusinessSaveBO crmModel) {
        CrmBusiness crmBusiness = BeanUtil.copyProperties(crmModel.getEntity(), CrmBusiness.class);
        List<CrmBusinessProduct> businessProductList = crmModel.getProduct();
        String batchId = StrUtil.isNotEmpty(crmBusiness.getBatchId()) ? crmBusiness.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_business_data"));
        crmBusinessDataService.saveData(crmModel.getField(), batchId);
        if (crmBusiness.getBusinessId() != null) {
            crmBusiness.setUpdateTime(DateUtil.date());
            /*
                不修改商机阶段
             */
            crmBusiness.setStatusId(null);
            actionRecordUtil.updateRecord(BeanUtil.beanToMap(getById(crmBusiness.getBusinessId())), BeanUtil.beanToMap(crmBusiness), CrmEnum.BUSINESS, crmBusiness.getBusinessName(), crmBusiness.getBusinessId());
            updateById(crmBusiness);
            crmBusinessProductService.deleteByBusinessId(crmBusiness.getBusinessId());
            crmBusiness = getById(crmBusiness.getBusinessId());
            ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(), "business", crmBusiness.getBusinessId().toString(), crmBusiness.getBusinessName());
        } else {
            crmBusiness.setCreateTime(DateUtil.date());
            crmBusiness.setUpdateTime(DateUtil.date());
            crmBusiness.setCreateUserId(UserUtil.getUserId());
            crmBusiness.setOwnerUserId(UserUtil.getUserId());
            crmBusiness.setBatchId(batchId);
            crmBusiness.setRwUserId(Const.SEPARATOR);
            crmBusiness.setRoUserId(Const.SEPARATOR);
            save(crmBusiness);
            if (crmModel.getContactsId() != null) {
                crmContactsBusinessService.save(crmBusiness.getBusinessId(), crmModel.getContactsId());
            }
            crmActivityService.addActivity(2, CrmActivityEnum.BUSINESS, crmBusiness.getBusinessId());
            actionRecordUtil.addRecord(crmBusiness.getBusinessId(), CrmEnum.BUSINESS, crmBusiness.getBusinessName());
        }
        for (CrmBusinessProduct crmBusinessProduct : businessProductList) {
            crmBusinessProduct.setBusinessId(crmBusiness.getBusinessId());
        }
        crmBusinessProductService.save(businessProductList);
        crmModel.setEntity(BeanUtil.beanToMap(crmBusiness));
        savePage(crmModel, crmBusiness.getBusinessId(), false);
    }

    @Override
    public void setOtherField(Map<String, Object> map) {
        String ownerUserName = UserCacheUtil.getUserName((Long) map.get("ownerUserId"));
        map.put("ownerUserName", ownerUserName);
        String createUserName = UserCacheUtil.getUserName((Long) map.get("createUserId"));
        map.put("createUserName", createUserName);
        String customerName = crmCustomerService.getCustomerName((Integer) map.get("customerId"));
        map.put("customerName", customerName);
        ICrmBusinessStatusService businessStatusService = ApplicationContextHolder.getBean(ICrmBusinessStatusService.class);
        ICrmBusinessTypeService businessTypeService = ApplicationContextHolder.getBean(ICrmBusinessTypeService.class);
        Integer typeId = (Integer) map.get("typeId");
        Integer statusId = (Integer) map.get("statusId");
        CrmBusinessType crmBusinessType = businessTypeService.query().select("name").eq("type_id", typeId).one();
        map.put("typeName", crmBusinessType != null ? crmBusinessType.getName() : "");
        CrmBusinessStatus crmBusinessStatus = businessStatusService.query().select("name").eq("status_id", statusId).one();
        map.put("statusName", crmBusinessStatus != null ? crmBusinessStatus.getName() : "");
    }

    @Override
    public Dict getSearchTransferMap() {
        return Dict.create().set("customerId", "customerName");
    }

    /**
     * 删除商机数据
     *
     * @param ids ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        ids.forEach(id -> {
            if (AuthUtil.isRwAuth(id, CrmEnum.BUSINESS)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
        });
        int number = crmContractService.lambdaQuery().in(CrmContract::getBusinessId, ids).ne(CrmContract::getCheckStatus, 7).count();
        if (number > 0) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_JOIN_ERROR);
        }
        LambdaQueryWrapper<CrmBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CrmBusiness::getBatchId);
        wrapper.in(CrmBusiness::getBusinessId, ids);
        List<String> batchIdList = listObjs(wrapper, Object::toString);

        //删除跟进记录
        crmActivityService.deleteActivityRecord(CrmActivityEnum.BUSINESS, ids);
        //删除字段操作记录
        crmActionRecordService.deleteActionRecord(CrmEnum.BUSINESS, ids);
        //todo 文件相关暂不处理
        //删除商机和联系人关联
        crmContactsBusinessService.removeByBusinessId(ids.toArray(new Integer[0]));
        //删除商机产品关联
        crmBusinessProductService.deleteByBusinessId(ids.toArray(new Integer[0]));
        //删除自定义字段
        crmBusinessDataService.deleteByBatchId(batchIdList);
        //修改商机状态
        LambdaUpdateWrapper<CrmBusiness> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CrmBusiness::getStatus, 3);
        updateWrapper.in(CrmBusiness::getBusinessId, ids);
        update(updateWrapper);
        deletePage(ids);

    }

    @Override
    public BasePage<CrmContacts> queryContacts(CrmContactsPageBO pageEntity) {
        BasePage<CrmContacts> contactsBasePage = pageEntity.parse();
        return getBaseMapper().queryContacts(contactsBasePage, pageEntity.getBusinessId());
    }

    /**
     * 查询详情信息
     *
     * @param businessId 商机id
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> information(Integer businessId) {
        CrmModel crmModel = queryById(businessId);
        List<String> keyList = Arrays.asList("businessName", "dealDate", "money", "remark");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间", "更新时间", "下次联系时间", "最后跟进时间", "已收款金额", "未收款金额");
        List<CrmModelFiledVO> crmModelFiledVOS = queryInformation(crmModel, keyList);
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("statusName").setName("商机阶段").setValue(new JSONObject().fluentPut("statusId", crmModel.get("statusId")).fluentPut("statusName", crmModel.get("statusName"))).setFormType("statusName").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("typeName").setName("商机状态组").setValue(new JSONObject().fluentPut("typeId", crmModel.get("typeId")).fluentPut("typeName", crmModel.get("typeName"))).setFormType("typeName").setFieldType(1));
        crmModelFiledVOS.add(new CrmModelFiledVO().setFieldName("customerId").setName("客户名称").setValue(new JSONObject().fluentPut("customerId", crmModel.get("customerId")).fluentPut("customerName", crmModel.get("customerName"))).setFormType("customer").setFieldType(1));
        List<CrmModelFiledVO> filedVOS = crmModelFiledVOS.stream().sorted(Comparator.comparingInt(r -> -r.getFieldType())).peek(r -> {
            r.setFieldType(null);
            r.setSetting(null);
            r.setType(null);
            if("statusName".equals(r.getFieldName()) && !Objects.equals(0,crmModel.get("isEnd"))){
                JSONObject value = (JSONObject) r.getValue();
                value.put("statusId",crmModel.get("isEnd"));
                String statusName;
                if(Objects.equals(1,crmModel.get("isEnd"))){
                    statusName = "赢单";
                } else if(Objects.equals(2,crmModel.get("isEnd"))){
                    statusName = "输单";
                } else if(Objects.equals(3,crmModel.get("isEnd"))){
                    statusName = "无效";
                } else {
                    statusName = "其他";
                }
                value.put("statusName",statusName);
            }
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
     * 修改商机负责人
     *
     * @param changOwnerUserBO data
     */
    @Override
    public void changeOwnerUser(CrmBusinessChangOwnerUserBO changOwnerUserBO) {
        if (changOwnerUserBO.getIds().size() == 0) {
            return;
        }
        String ownerUserName = UserCacheUtil.getUserName(changOwnerUserBO.getOwnerUserId());

        changOwnerUserBO.getIds().forEach(id -> {
            if (AuthUtil.isRwAuth(id, CrmEnum.BUSINESS)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }

            String memberId = "," + changOwnerUserBO.getOwnerUserId() + ",";
            getBaseMapper().deleteMember(memberId, id);
            CrmBusiness business = getById(id);
            if (Objects.equals(2, changOwnerUserBO.getTransferType()) && !Objects.equals(business.getOwnerUserId(), changOwnerUserBO.getOwnerUserId())) {
                if (1 == changOwnerUserBO.getPower()) {
                    business.setRoUserId(business.getRoUserId() + business.getOwnerUserId() + Const.SEPARATOR);
                }
                if (2 == changOwnerUserBO.getPower()) {
                    business.setRwUserId(business.getRwUserId() + business.getOwnerUserId() + Const.SEPARATOR);
                }
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_BUSINESS_USER, business.getBusinessId(), business.getBusinessName(), business.getOwnerUserId());
            }
            business.setOwnerUserId(changOwnerUserBO.getOwnerUserId());
            updateById(business);
            actionRecordUtil.addConversionRecord(id, CrmEnum.BUSINESS, changOwnerUserBO.getOwnerUserId(), business.getBusinessName());
            //修改es
            Map<String, Object> map = new HashMap<>();
            map.put("ownerUserId", changOwnerUserBO.getOwnerUserId());
            map.put("ownerUserName", ownerUserName);
            map.put("rwUserId", business.getRwUserId());
            map.put("roUserId", business.getRoUserId());
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
            writer.merge(headList.size() - 1, "商机信息");
            if (dataList.size() == 0) {
                Map<String, Object> record = new HashMap<>();
                headList.forEach(head -> record.put(head.getFieldName(), ""));
                dataList.add(record);
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
            response.setHeader("Content-Disposition", "attachment;filename=business.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出商机错误：", e);
        }
    }

    /**
     * 标星
     *
     * @param businessId 商机id
     */
    @Override
    public void star(Integer businessId) {
        LambdaQueryWrapper<CrmBusinessUserStar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmBusinessUserStar::getBusinessId, businessId);
        wrapper.eq(CrmBusinessUserStar::getUserId, UserUtil.getUserId());
        CrmBusinessUserStar star = crmBusinessUserStarService.getOne(wrapper);
        if (star == null) {
            star = new CrmBusinessUserStar();
            star.setBusinessId(businessId);
            star.setUserId(UserUtil.getUserId());
            crmBusinessUserStarService.save(star);
        } else {
            crmBusinessUserStarService.removeById(star.getId());
        }
    }

    /**
     * 查询文件数量
     *
     * @param businessId id
     * @return data
     */
    @Override
    public CrmInfoNumVO num(Integer businessId) {
        CrmBusiness crmBusiness = getById(businessId);
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CONTRACT, 1);
        String contactsConditions = AuthUtil.getCrmAuthSql(CrmEnum.CONTACTS, "b", 1);
        Map<String, Object> map = new HashMap<>();
        map.put("businessId", businessId);
        map.put("conditions", conditions);
        map.put("contactsConditions", contactsConditions);
        CrmInfoNumVO infoNumVO = getBaseMapper().queryNum(map);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        List<CrmField> crmFields = crmFieldService.queryFileField();
        List<String> batchIdList = new ArrayList<>();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmBusinessData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmBusinessData::getValue);
            wrapper.eq(CrmBusinessData::getBatchId, crmBusiness.getBatchId());
            wrapper.in(CrmBusinessData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            batchIdList.addAll(crmBusinessDataService.listObjs(wrapper, Object::toString));
        }
        batchIdList.add(crmBusiness.getBatchId());
        batchIdList.addAll(crmActivityService.queryFileBatchId(crmBusiness.getBusinessId(), getLabel().getType()));
        infoNumVO.setFileCount(fileService.queryNum(batchIdList).getData());
        Set<String> member = new HashSet<>();
        member.addAll(StrUtil.splitTrim(crmBusiness.getRoUserId(), Const.SEPARATOR));
        member.addAll(StrUtil.splitTrim(crmBusiness.getRwUserId(), Const.SEPARATOR));
        if (crmBusiness.getOwnerUserId() != null) {
            member.add(crmBusiness.getOwnerUserId().toString());
        }
        infoNumVO.setMemberCount(member.size());
        Integer productCount = crmBusinessProductService.lambdaQuery().eq(CrmBusinessProduct::getBusinessId, businessId).count();
        infoNumVO.setProductCount(productCount);
        return infoNumVO;
    }

    /**
     * 查询文件列表
     *
     * @param businessId id
     * @return file
     */
    @Override
    public List<FileEntity> queryFileList(Integer businessId) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmBusiness crmBusiness = getById(businessId);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmBusiness.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        List<CrmField> crmFields = crmFieldService.queryFileField();
        if (crmFields.size() > 0) {
            LambdaQueryWrapper<CrmBusinessData> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmBusinessData::getValue);
            wrapper.eq(CrmBusinessData::getBatchId, crmBusiness.getBatchId());
            wrapper.in(CrmBusinessData::getFieldId, crmFields.stream().map(CrmField::getFieldId).collect(Collectors.toList()));
            List<FileEntity> data = fileService.queryFileList(crmBusinessDataService.listObjs(wrapper, Object::toString)).getData();
            data.forEach(fileEntity -> {
                fileEntity.setSource("商机详情");
                fileEntity.setReadOnly(1);
                fileEntityList.add(fileEntity);
            });
        }
        List<String> stringList = crmActivityService.queryFileBatchId(crmBusiness.getBusinessId(), getLabel().getType());
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

    /**
     * 设置首要联系人
     *
     * @param contactsBO data
     */
    @Override
    public void setContacts(CrmFirstContactsBO contactsBO) {
        CrmBusiness crmBusiness = getById(contactsBO.getBusinessId());
        crmBusiness.setContactsId(contactsBO.getContactsId());
        updateById(crmBusiness);
    }

    /**
     * 获取团队成员
     *
     * @param businessId 商机ID
     * @return data
     */
    @Override
    public List<CrmMembersSelectVO> getMembers(Integer businessId) {
        CrmBusiness crmBusiness = getById(businessId);
        List<CrmMembersSelectVO> recordList = new ArrayList<>();
        if (crmBusiness.getOwnerUserId() != null) {
            UserInfo userInfo = UserCacheUtil.getUserInfo(crmBusiness.getOwnerUserId());
            CrmMembersSelectVO crmMembersVO = new CrmMembersSelectVO();
            crmMembersVO.setUserId(userInfo.getUserId());
            crmMembersVO.setRealname(UserCacheUtil.getUserName(userInfo.getUserId()));
            crmMembersVO.setDeptName(UserCacheUtil.getDeptName(userInfo.getDeptId()));
            crmMembersVO.setPower("负责人权限");
            crmMembersVO.setGroupRole("负责人");
            crmMembersVO.setPost(userInfo.getPost());
            recordList.add(crmMembersVO);
        }
        String roUserId = crmBusiness.getRoUserId();
        String rwUserId = crmBusiness.getRwUserId();
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
            if (AuthUtil.isCrmOperateAuth(CrmEnum.BUSINESS, id)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            CrmBusiness oldBusiness = lambdaQuery().eq(CrmBusiness::getBusinessId,id).ne(CrmBusiness::getStatus,3).one();
            if (oldBusiness == null){
                continue;
            }
            Long ownerUserId = oldBusiness.getOwnerUserId();
            for (Long memberId : crmMemberSaveBO.getMemberIds()) {
                if (ownerUserId.equals(memberId)) {
                    throw new CrmException(CrmCodeEnum.CRM_MEMBER_ADD_ERROR);
                }
                oldBusiness.setRoUserId(oldBusiness.getRoUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
                oldBusiness.setRwUserId(oldBusiness.getRwUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
                String name = lambdaQuery().select(CrmBusiness::getBusinessName).eq(CrmBusiness::getBusinessId, id).one().getBusinessName();
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_BUSINESS_USER, oldBusiness.getBusinessId(), oldBusiness.getBusinessName(), oldBusiness.getOwnerUserId());
                actionRecordUtil.addMemberActionRecord(CrmEnum.BUSINESS, id, memberId, name);
            }
            if (1 == crmMemberSaveBO.getPower()) {
                oldBusiness.setRoUserId(oldBusiness.getRoUserId() + StrUtil.join(Const.SEPARATOR, crmMemberSaveBO.getMemberIds()) + Const.SEPARATOR);
            } else if (2 == crmMemberSaveBO.getPower()) {
                oldBusiness.setRwUserId(oldBusiness.getRwUserId() + StrUtil.join(Const.SEPARATOR, crmMemberSaveBO.getMemberIds()) + Const.SEPARATOR);
            }
            updateById(oldBusiness);
            Map<String, Object> map = new HashMap<>();
            map.put("roUserId", oldBusiness.getRoUserId());
            map.put("rwUserId", oldBusiness.getRwUserId());
            updateField(map, Collections.singletonList(id));
        }

    }

    /**
     * 删除团队成员
     *
     * @param crmMemberSaveBO data
     */
    @Override
    public void deleteMember(CrmMemberSaveBO crmMemberSaveBO) {
        for (Integer id : crmMemberSaveBO.getIds()) {
            if (AuthUtil.isCrmOperateAuth(CrmEnum.BUSINESS, id)) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            deleteMembers(id, crmMemberSaveBO.getMemberIds());
        }

    }

    private void deleteMembers(Integer id, List<Long> memberIds) {
        CrmBusiness oldBusiness = getById(id);
        Long ownerUserId = oldBusiness.getOwnerUserId();
        for (Long memberId : memberIds) {
            if (ownerUserId.equals(memberId)) {
                throw new CrmException(CrmCodeEnum.CRM_MEMBER_DELETE_ERROR);
            }
            oldBusiness.setRoUserId(oldBusiness.getRoUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
            oldBusiness.setRwUserId(oldBusiness.getRwUserId().replace(Const.SEPARATOR + memberId.toString() + Const.SEPARATOR, Const.SEPARATOR));
            if (!memberId.equals(UserUtil.getUserId())) {
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_BUSINESS_REMOVE_TEAM, oldBusiness.getBusinessId(), oldBusiness.getBusinessName(), memberId);
                actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.BUSINESS, id, memberId, false, oldBusiness.getBusinessName());
            } else {
                crmCustomerService.addTermMessage(AdminMessageEnum.CRM_BUSINESS_TEAM_EXIT, oldBusiness.getBusinessId(), oldBusiness.getBusinessName(), oldBusiness.getOwnerUserId());
                actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.BUSINESS, id, memberId, true, oldBusiness.getBusinessName());
            }
        }
        updateById(oldBusiness);
        Map<String, Object> map = new HashMap<>();
        map.put("roUserId", oldBusiness.getRoUserId());
        map.put("rwUserId", oldBusiness.getRwUserId());
        updateField(map, Collections.singletonList(id));
    }

    /**
     * 退出团队
     *
     * @param businessId 商机ID
     */
    @Override
    public void exitTeam(Integer businessId) {
        deleteMembers(businessId, Collections.singletonList(UserUtil.getUserId()));
    }

    /**
     * 商机关联联系人
     *
     * @param relevanceBusinessBO 业务对象
     */
    @Override
    public void relateContacts(CrmRelevanceBusinessBO relevanceBusinessBO) {
        relevanceBusinessBO.getContactsIds().forEach(id -> {
            crmContactsBusinessService.save(relevanceBusinessBO.getBusinessId(), id);
        });
    }

    /**
     * 商机解除关联联系人
     *
     * @param relevanceBusinessBO 业务对象
     */
    @Override
    public void unrelateContacts(CrmRelevanceBusinessBO relevanceBusinessBO) {
        CrmBusiness crmBusiness = getById(relevanceBusinessBO.getBusinessId());
        relevanceBusinessBO.getContactsIds().forEach(r -> {
            if (Objects.equals(r, crmBusiness.getContactsId())) {
                lambdaUpdate().set(CrmBusiness::getContactsId,null).eq(CrmBusiness::getBusinessId, crmBusiness.getBusinessId()).update();
            }
        });
        LambdaQueryWrapper<CrmContactsBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmContactsBusiness::getBusinessId, relevanceBusinessBO.getBusinessId());
        wrapper.in(CrmContactsBusiness::getContactsId, relevanceBusinessBO.getContactsIds());
        crmContactsBusinessService.remove(wrapper);
    }

    @Override
    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        List<CrmBusiness> list = query().in("business_id", ids).list();
        return list.stream().map(crmBusiness -> {
            SimpleCrmEntity simpleCrmEntity = new SimpleCrmEntity();
            simpleCrmEntity.setId(crmBusiness.getBusinessId());
            simpleCrmEntity.setName(crmBusiness.getBusinessName());
            return simpleCrmEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public String getBusinessName(int businessId) {
        Optional<CrmBusiness> crmBusiness = lambdaQuery().select(CrmBusiness::getBusinessName).eq(CrmBusiness::getBusinessId, businessId).oneOpt();
        return crmBusiness.map(CrmBusiness::getBusinessName).orElse("");
    }


    @Override
    public void updateInformation(CrmUpdateInformationBO updateInformationBO) {
        String batchId = updateInformationBO.getBatchId();
        Integer businessId = updateInformationBO.getId();
        updateInformationBO.getList().forEach(record -> {
            CrmBusiness oldBusiness = getById(updateInformationBO.getId());
            Map<String, Object> oldBusinessMap = BeanUtil.beanToMap(oldBusiness);
            if (record.getInteger("fieldType") == 1) {
                Map<String, Object> crmBusinessMap = new HashMap<>(oldBusinessMap);
                crmBusinessMap.put(record.getString("fieldName"), record.get("value"));
                CrmBusiness crmBusiness = BeanUtil.mapToBean(crmBusinessMap, CrmBusiness.class, true);
                actionRecordUtil.updateRecord(oldBusinessMap, crmBusinessMap, CrmEnum.BUSINESS, crmBusiness.getBusinessName(), crmBusiness.getBusinessId());
                update().set(StrUtil.toUnderlineCase(record.getString("fieldName")), record.get("value")).eq("business_id",updateInformationBO.getId()).update();
                if ("businessName".equals(record.getString("fieldName"))) {
                    ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(), "business", crmBusiness.getBusinessId().toString(), crmBusiness.getBusinessName());
                }
            } else if (record.getInteger("fieldType") == 0 || record.getInteger("fieldType") == 2) {

                CrmBusinessData businessData = crmBusinessDataService.lambdaQuery().select(CrmBusinessData::getValue).eq(CrmBusinessData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmBusinessData::getBatchId, batchId).one();
                String value = businessData != null ? businessData.getValue() : null;
                String detail = actionRecordUtil.getDetailByFormTypeAndValue(record,value);
                actionRecordUtil.publicContentRecord(CrmEnum.BUSINESS, BehaviorEnum.UPDATE, businessId, oldBusiness.getBusinessName(), detail);
                boolean bol = crmBusinessDataService.lambdaUpdate()
                        .set(CrmBusinessData::getName,record.getString("fieldName"))
                        .set(CrmBusinessData::getValue, record.getString("value"))
                        .eq(CrmBusinessData::getFieldId, record.getInteger("fieldId"))
                        .eq(CrmBusinessData::getBatchId, batchId).update();
                if (!bol) {
                    CrmBusinessData crmBusinessData = new CrmBusinessData();
                    crmBusinessData.setFieldId(record.getInteger("fieldId"));
                    crmBusinessData.setName(record.getString("fieldName"));
                    crmBusinessData.setValue(record.getString("value"));
                    crmBusinessData.setCreateTime(new Date());
                    crmBusinessData.setBatchId(batchId);
                    crmBusinessDataService.save(crmBusinessData);
                }
            }
            updateField(record, businessId);
        });
    }

    @Override
    public JSONObject queryProduct(CrmBusinessQueryRelationBO businessQueryProductBO) {
        Integer businessId = businessQueryProductBO.getBusinessId();
        CrmBusiness business = getById(businessId);
        JSONObject record = getBaseMapper().querySubtotalByBusinessId(businessId);
        record.put("money", business.getTotalPrice());
        BasePage<JSONObject> page = getBaseMapper().queryProduct(businessQueryProductBO.parse(), businessId);
        record.put("list", page.getList());
        return record;
    }

    @Override
    public BasePage<JSONObject> queryContract(CrmBusinessQueryRelationBO businessQueryRelationBO) {
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.BUSINESS, "a", 1);
        Integer businessId = businessQueryRelationBO.getBusinessId();
        BasePage<JSONObject> page = getBaseMapper().queryContract(businessQueryRelationBO.parse(), businessId, conditions);
        return page;
    }

    @Override
    public List<String> eventDealBusiness(CrmEventBO crmEventBO) {
        return getBaseMapper().eventDealBusiness(crmEventBO);
    }

    @Override
    public BasePage<Map<String, Object>> eventDealBusinessPageList(QueryEventCrmPageBO eventCrmPageBO) {
        Long userId = eventCrmPageBO.getUserId();
        Long time = eventCrmPageBO.getTime();
        if (userId == null) {
            userId = UserUtil.getUserId();
        }
        List<Integer> businessIds = getBaseMapper().eventDealBusinessPageList(userId, new Date(time));
        if (businessIds.size() == 0) {
            return new BasePage<>();
        }
        List<String> collect = businessIds.stream().map(Object::toString).collect(Collectors.toList());
        CrmSearchBO crmSearchBO = new CrmSearchBO();
        crmSearchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "id", CrmSearchBO.FieldSearchEnum.ID, collect)));
        crmSearchBO.setLabel(CrmEnum.CUSTOMER.getType());
        crmSearchBO.setPage(eventCrmPageBO.getPage());
        crmSearchBO.setLimit(eventCrmPageBO.getLimit());
        BasePage<Map<String, Object>> page = queryPageList(crmSearchBO);
        return page;
    }

    @Override
    public List<String> eventBusiness(CrmEventBO crmEventBO) {
        return getBaseMapper().eventBusiness(crmEventBO);
    }

    @Override
    public BasePage<Map<String, Object>> eventBusinessPageList(QueryEventCrmPageBO eventCrmPageBO) {
        Long userId = eventCrmPageBO.getUserId();
        Long time = eventCrmPageBO.getTime();
        if (userId == null) {
            userId = UserUtil.getUserId();
        }
        List<Integer> businessIds = getBaseMapper().eventBusinessPageList(userId, new Date(time));
        if (businessIds.size() == 0) {
            return new BasePage<>();
        }
        List<String> collect = businessIds.stream().map(Object::toString).collect(Collectors.toList());
        CrmSearchBO crmSearchBO = new CrmSearchBO();
        crmSearchBO.setSearchList(Collections.singletonList(new CrmSearchBO.Search("_id", "id", CrmSearchBO.FieldSearchEnum.ID, collect)));
        crmSearchBO.setLabel(CrmEnum.CUSTOMER.getType());
        crmSearchBO.setPage(eventCrmPageBO.getPage());
        crmSearchBO.setLimit(eventCrmPageBO.getLimit());
        BasePage<Map<String, Object>> page = queryPageList(crmSearchBO);
        return page;
    }
}
