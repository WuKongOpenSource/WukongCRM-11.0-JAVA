package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmFieldMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义字段表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
@Service
@Slf4j
public class CrmFieldServiceImpl extends BaseServiceImpl<CrmFieldMapper, CrmField> implements ICrmFieldService {

    private static final String PRODUCT_STATUS_URL = "/crmProduct/updateStatus";

    @Autowired
    private ICrmFieldSortService crmFieldSortService;

    @Autowired
    private ICrmFieldConfigService crmFieldConfigService;

    @Autowired
    private ICrmRoleFieldService crmRoleFieldService;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private AdminService adminService;


    @Autowired
    private ICrmCustomerPoolFieldStyleService crmCustomerPoolFieldStyleService;

    /**
     * 查询自定义字段列表
     *
     * @return data
     */
    @Override
    public List<CrmFieldsBO> queryFields() {
        List<CrmField> list = query().select("IFNULL(MAX(update_time),'2000-01-01 00:00:00') as updateTime", "label")
                .in("label", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 17))
                .groupBy("label").list();
        return list.stream().map(field -> {
            CrmFieldsBO crmFieldsBO = new CrmFieldsBO();
            crmFieldsBO.setLabel(field.getLabel());
            crmFieldsBO.setUpdateTime(DateUtil.formatDateTime(field.getUpdateTime()));
            crmFieldsBO.setName(CrmEnum.parse(field.getLabel()).getRemarks() + "管理");
            return crmFieldsBO;
        }).collect(Collectors.toList());
    }

    /**
     * 保存自定义字段列表
     *
     * @param crmFieldBO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveField(CrmFieldBO crmFieldBO) {
        Integer label = crmFieldBO.getLabel();
        CrmEnum crmEnum = CrmEnum.parse(label);
        if (crmFieldBO.getData().size() == 0) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        if (crmFieldBO.getData().size() > Const.QUERY_MAX_SIZE) {
            throw new CrmException(CrmCodeEnum.CRM_FIELD_NUM_ERROR);
        }
        List<Integer> fieldIds = new ArrayList<>();
        //数据处理
        crmFieldBO.getData().forEach(crmField -> {
            //关于非隐藏字段的判断
            if (crmField.getIsHidden().equals(1)) {
                String[] fieldNameArr = new String[0];
                if (crmField.getLabel().equals(CrmEnum.LEADS.getType())) {
                    fieldNameArr = new String[]{"leads_id", "leads_name", "level"};
                } else if (crmField.getLabel().equals(CrmEnum.CUSTOMER.getType())) {
                    fieldNameArr = new String[]{"customer_name", "level"};
                } else if (crmField.getLabel().equals(CrmEnum.CONTACTS.getType())) {
                    fieldNameArr = new String[]{"customer_id", "name"};
                } else if (crmField.getLabel().equals(CrmEnum.PRODUCT.getType())) {
                    fieldNameArr = new String[]{"name", "category_id", "price", "是否上下架"};
                } else if (crmField.getLabel().equals(CrmEnum.BUSINESS.getType())) {
                    fieldNameArr = new String[]{"business_name", "contract_id"};
                } else if (crmField.getLabel().equals(CrmEnum.CONTRACT.getType())) {
                    fieldNameArr = new String[]{"customer_id", "business_id", "num", "money", "order_date"};
                } else if (crmField.getLabel().equals(CrmEnum.RECEIVABLES.getType())) {
                    fieldNameArr = new String[]{"customer_id", "contract_id", "number", "plan_id"};
                } else if (crmField.getLabel().equals(CrmEnum.RETURN_VISIT.getType())) {
                    fieldNameArr = new String[]{"customer_id", "contract_id"};
                }
                List<String> keyList = Arrays.asList(fieldNameArr);
                if (keyList.contains(crmField.getFieldName())) {
                    throw new CrmException(CrmCodeEnum.SYSTEM_RELATED_FIELDS_CANNOT_BE_HIDDEN);
                }
                if (crmField.getIsNull() == 1) {
                    throw new CrmException(CrmCodeEnum.REQUIRED_OPTIONS_CANNOT_BE_HIDDEN);
                }
            }
            crmField.setLabel(label);
            if (crmField.getFieldId() != null) {
                fieldIds.add(crmField.getFieldId());
            }
        });
        QueryWrapper<CrmField> queryWrapper = new QueryWrapper<>();
        //查询需要删除的
        List<CrmField> removeFieldList = list(queryWrapper.eq("label", label));
        removeFieldList.removeIf(crmField -> fieldIds.contains(crmField.getFieldId()));
        for (CrmField field : removeFieldList) {
            if (Arrays.asList(2, 4).contains(field.getOperating())) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
            }
        }
        //不在上面循环,避免一些不可恢复的数据
        for (CrmField field : removeFieldList) {
            //删除自定义字段
            removeById(field.getFieldId());
            //删除排序字段
            crmFieldSortService.remove(new QueryWrapper<CrmFieldSort>().eq("field_id", field.getFieldId()));
            //删除elasticsearch
            ElasticUtil.removeField(restTemplate.getClient(), field.getFieldName(), label);
            //删除字段授权
            crmRoleFieldService.deleteRoleField(field.getFieldId());
            switch (crmEnum) {
                case LEADS:
                    ApplicationContextHolder.getBean(ICrmLeadsDataService.class).lambdaUpdate().eq(CrmLeadsData::getFieldId, field.getFieldId()).remove();
                    break;
                case CUSTOMER:
                    ApplicationContextHolder.getBean(ICrmCustomerDataService.class).lambdaUpdate().eq(CrmCustomerData::getFieldId, field.getFieldId()).remove();
                    //删除公海字段
                    ApplicationContextHolder.getBean(ICrmCustomerPoolFieldStyleService.class).lambdaUpdate().eq(CrmCustomerPoolFieldStyle::getFieldName, StrUtil.toCamelCase(field.getFieldName())).remove();
                    ApplicationContextHolder.getBean(ICrmCustomerPoolFieldSortService.class).lambdaUpdate().eq(CrmCustomerPoolFieldSort::getFieldName, StrUtil.toCamelCase(field.getFieldName())).remove();
                    ApplicationContextHolder.getBean(ICrmCustomerPoolFieldSettingService.class).lambdaUpdate().eq(CrmCustomerPoolFieldSetting::getFieldName, StrUtil.toCamelCase(field.getFieldName())).remove();
                    break;
                case CONTACTS:
                    ApplicationContextHolder.getBean(ICrmContactsDataService.class).lambdaUpdate().eq(CrmContactsData::getFieldId, field.getFieldId()).remove();
                    break;
                case BUSINESS:
                    ApplicationContextHolder.getBean(ICrmBusinessDataService.class).lambdaUpdate().eq(CrmBusinessData::getFieldId, field.getFieldId()).remove();
                    break;
                case CONTRACT:
                    ApplicationContextHolder.getBean(ICrmContractDataService.class).lambdaUpdate().eq(CrmContractData::getFieldId, field.getFieldId()).remove();
                    break;
                case RECEIVABLES:
                    ApplicationContextHolder.getBean(ICrmReceivablesDataService.class).lambdaUpdate().eq(CrmReceivablesData::getFieldId, field.getFieldId()).remove();
                    break;
                case RETURN_VISIT:
                    ApplicationContextHolder.getBean(ICrmReturnVisitDataService.class).lambdaUpdate().eq(CrmReturnVisitData::getFieldId, field.getFieldId()).remove();
                    break;
                default:
                    break;
            }
        }

        //保存
        AtomicInteger sort = new AtomicInteger(0);
        crmFieldBO.getData().forEach(field -> {
            field.setSorting(sort.getAndIncrement());
            if (field.getDefaultValue() instanceof Collection) {
                field.setDefaultValue(StrUtil.join(Const.SEPARATOR, field.getDefaultValue()));
            }
            List<CrmFieldSort> crmFieldSortList = new ArrayList<>();
            QueryWrapper<CrmFieldSort> crmFieldSortQueryWrapper = new QueryWrapper<>();
            crmFieldSortQueryWrapper.select("user_id").eq("label", label).groupBy("user_id");
            List<Long> userIdList = crmFieldSortService.listObjs(crmFieldSortQueryWrapper, obj -> Long.valueOf(obj.toString()));
            if (field.getFieldId() != null) {
                updateById(field);
                UpdateWrapper<CrmRoleField> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("field_name", field.getFieldName());
                updateWrapper.set("name", field.getName());
                updateWrapper.eq("field_id", field.getFieldId());
                crmRoleFieldService.update(updateWrapper);
                UpdateWrapper<CrmFieldSort> wrapper = new UpdateWrapper<>();
                wrapper.set("name", field.getName());
                wrapper.set("type", field.getType());
                wrapper.eq("field_id", field.getFieldId());
                crmFieldSortService.update(wrapper);
                if (field.getIsHidden() == 1) {
                    crmFieldSortService.lambdaUpdate().eq(CrmFieldSort::getFieldId, field.getFieldId()).remove();
                } else {
                    Integer count = crmFieldSortService.lambdaQuery().eq(CrmFieldSort::getFieldId, field.getFieldId()).count();
                    if (count == 0) {
                        crmFieldSortService.lambdaUpdate().eq(CrmFieldSort::getFieldId, field.getFieldId()).remove();
                        userIdList.forEach(userId -> {
                            CrmFieldSort fieldSort = new CrmFieldSort();
                            fieldSort.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
                            fieldSort.setName(field.getName());
                            fieldSort.setType(field.getType());
                            fieldSort.setFieldId(field.getFieldId());
                            fieldSort.setIsHide(0);
                            fieldSort.setLabel(field.getLabel());
                            fieldSort.setStyle(100);
                            fieldSort.setUserId(userId);
                            fieldSort.setSort(field.getSorting());
                            crmFieldSortList.add(fieldSort);
                        });
                    }
                }
            } else {
                QueryWrapper<CrmField> wrapper = new QueryWrapper<>();
                wrapper.select("field_name").eq("label", label);
                String nextFieldName = crmFieldConfigService.getNextFieldName(label, field.getType(), listObjs(wrapper, Object::toString), Const.AUTH_DATA_RECURSION_NUM, true);
                field.setFieldName(nextFieldName);
                save(field);
                crmRoleFieldService.saveRoleField(field);
                if (field.getIsHidden() == 0) {
                    userIdList.forEach(userId -> {
                        CrmFieldSort fieldSort = new CrmFieldSort();
                        fieldSort.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
                        fieldSort.setName(field.getName());
                        fieldSort.setType(field.getType());
                        fieldSort.setFieldId(field.getFieldId());
                        fieldSort.setIsHide(0);
                        fieldSort.setLabel(field.getLabel());
                        fieldSort.setStyle(100);
                        fieldSort.setUserId(userId);
                        fieldSort.setSort(99);
                        crmFieldSortList.add(fieldSort);
                    });
                }
            }
            crmFieldSortService.saveBatch(crmFieldSortList, Const.BATCH_SAVE_SIZE);
        });
    }

    /**
     * 保存自定义字段列表
     *
     * @param label       label
     * @param isQueryHide 是否查询隐藏字段
     * @return data
     */
    @Override
    public List<CrmField> list(Integer label, boolean isQueryHide) {
        QueryWrapper<CrmField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label", label);
        if (!isQueryHide) {
            queryWrapper.eq("is_hidden", 0);
        }
        queryWrapper.orderByAsc("sorting");
        List<CrmField> fieldList = list(queryWrapper);
        fieldList.forEach(field -> {
            recordToFormType2(field, FieldEnum.parse(field.getType()));
        });
        return fieldList;
    }

    /**
     * 查询模块字段列表
     *
     * @param label label
     * @return data
     */
    @Override
    public List<CrmFieldSortVO> queryListHead(Integer label) {
        List<CrmFieldSortVO> fieldSortVOList = crmFieldSortService.queryListHead(label);
        if (!UserUtil.isAdmin()) {
            List<String> list = crmRoleFieldService.queryNoAuthField(label);
            List<String> fieldList = list.stream().map(fieldName -> {
                if ("businessId".equals(fieldName)) {
                    return "businessName";
                } else if ("companyUserId".equals(fieldName)) {
                    return "companyUserName";
                } else if ("contactsId".equals(fieldName)) {
                    return "contactsName";
                } else if ("customerId".equals(fieldName)) {
                    return "customerName";
                } else if ("planId".equals(fieldName)) {
                    return "planNum";
                } else if ("contractId".equals(fieldName)) {
                    return "contractNum";
                } else if ("ownerUserId".equals(fieldName)) {
                    return "ownerUserName";
                } else {
                    return fieldName;
                }
            }).collect(Collectors.toList());
            fieldSortVOList.removeIf(field -> fieldList.contains(field.getFieldName()));
        }
        return fieldSortVOList;
    }

    /**
     * 修改字段宽度
     *
     * @param fieldStyle data
     */
    @Override
    public void setFieldStyle(CrmFieldStyleBO fieldStyle) {
        CrmFieldSort crmFieldSort = crmFieldSortService.getById(fieldStyle.getId());
        if (crmFieldSort != null) {
            crmFieldSort.setStyle(fieldStyle.getWidth());
            crmFieldSortService.updateById(crmFieldSort);
        }
    }

    /**
     * 修改字段配置
     *
     * @param fieldSort data
     */
    @Override
    public void setFieldConfig(CrmFieldSortBO fieldSort) {
        List<CrmFieldSort> fieldSortList = crmFieldSortService.query().eq("label", fieldSort.getLabel()).list();
        List<Integer> noHideIds = fieldSort.getNoHideIds();
        for (int i = 0; i < fieldSortList.size(); i++) {
            CrmFieldSort sort = fieldSortList.get(i);
            if (noHideIds.contains(sort.getId())) {
                sort.setSort(noHideIds.indexOf(sort.getId()) + 1);
            }
            if (fieldSort.getHideIds().contains(sort.getId())) {
                sort.setIsHide(1);
                continue;
            }
            if (fieldSort.getNoHideIds().contains(sort.getId())) {
                sort.setIsHide(0);
            }
        }
        crmFieldSortService.updateBatchById(fieldSortList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 查询字段配置
     *
     * @param label 类型
     */
    @Override
    public JSONObject queryFieldConfig(Integer label) {
        //查出自定义字段，查看顺序表是否存在该字段，没有则插入，设为隐藏
        List<CrmFieldSort> fieldList = crmFieldSortService.lambdaQuery().eq(CrmFieldSort::getLabel, label).eq(CrmFieldSort::getUserId, UserUtil.getUserId()).select(CrmFieldSort::getId, CrmFieldSort::getName, CrmFieldSort::getFieldName, CrmFieldSort::getIsHide)
                .orderByAsc(CrmFieldSort::getSort).list();
        List<CrmRoleField> roleFields = crmRoleFieldService.queryUserFieldAuth(label, 2);
//        List<String> nameList = roleFields.stream().map(crmRoleField -> StrUtil.toCamelCase(crmRoleField.getFieldName())).collect(Collectors.toList());
        List<String> nameList = roleFields.stream().map(crmRoleField -> {
            String fieldName = StrUtil.toCamelCase(crmRoleField.getFieldName());
            if ("businessId".equals(fieldName)) {
                return "businessName";
            } else if ("companyUserId".equals(fieldName)) {
                return "companyUserName";
            } else if ("contactsId".equals(fieldName)) {
                return "contactsName";
            } else if ("customerId".equals(fieldName)) {
                return "customerName";
            } else if ("planId".equals(fieldName)) {
                return "planNum";
            } else if ("contractId".equals(fieldName)) {
                return "contractNum";
            } else if ("ownerUserId".equals(fieldName)) {
                return "ownerUserName";
            } else {
                return fieldName;
            }
        }).collect(Collectors.toList());
        if (label.equals(CrmEnum.CUSTOMER.getType()) || label.equals(CrmEnum.CUSTOMER_POOL.getType())) {
            //地区定位,详细地址不在字段授权配置,这这里默认展示
            nameList.add("detailAddress");
            nameList.add("address");
        }
        Map<Integer, List<CrmFieldSort>> collect = new HashMap<>();
        boolean isAdmin = UserUtil.isAdmin();
        for (CrmFieldSort crmFieldSort : fieldList) {
            if (isAdmin || nameList.contains(crmFieldSort.getFieldName())) {
                collect.computeIfAbsent(crmFieldSort.getIsHide(), k -> new ArrayList<>()).add(crmFieldSort);
            }
        }
        if (!collect.containsKey(1)) {
            collect.put(1, new ArrayList<>());
        }
        if (!collect.containsKey(0)) {
            collect.put(0, new ArrayList<>());
        }
        Integer count = ApplicationContextHolder.getBean(ICrmCustomerPoolService.class).lambdaQuery().eq(CrmCustomerPool::getStatus, 1).count();
        return new JSONObject().fluentPut("poolConfig", count > 1 ? 0 : 1).fluentPut("value", collect.get(0)).fluentPut("hideValue", collect.get(1));
    }


    /**
     * 查询字段信息
     *
     * @param crmModel data
     * @return data
     */
    @Override
    public List<CrmModelFiledVO> queryField(CrmModel crmModel) {
        List<CrmRoleField> roleFieldList = crmRoleFieldService.queryUserFieldAuth(crmModel.getLabel(), 1);
        Map<String, Integer> levelMap = new HashMap<>();
        roleFieldList.forEach(crmRoleField -> {
            levelMap.put(StrUtil.toCamelCase(crmRoleField.getFieldName()), crmRoleField.getAuthLevel());
        });
        QueryWrapper<CrmField> wrapper = new QueryWrapper<>();
        wrapper.eq("label", crmModel.getLabel()).eq("is_hidden", 0).orderByAsc("sorting");
        List<CrmField> crmFieldList = list(wrapper);
        crmFieldList.removeIf(field -> (!UserUtil.isAdmin() && Objects.equals(1, levelMap.get(StrUtil.toCamelCase(field.getFieldName())))));
        List<CrmModelFiledVO> fieldList = crmFieldList.stream().map(field -> {
            CrmModelFiledVO crmModelFiled = BeanUtil.copyProperties(field, CrmModelFiledVO.class);
            FieldEnum typeEnum = FieldEnum.parse(crmModelFiled.getType());
            if (UserUtil.isAdmin()) {
                crmModelFiled.setAuthLevel(3);
            } else {
                String fieldName = StrUtil.toCamelCase(crmModelFiled.getFieldName());
                crmModelFiled.setAuthLevel(levelMap.get(fieldName));
            }
            if (!crmModel.isEmpty()) {
                Object value = crmModel.get(StrUtil.toCamelCase(crmModelFiled.getFieldName()));
                if (ObjectUtil.isEmpty(value)) {
                    crmModelFiled.setValue(null);
                } else {
                    switch (typeEnum) {
                        case USER:
                            crmModelFiled.setValue(adminService.queryUserByIds(TagUtil.toLongSet(value.toString())).getData());
                            break;
                        case STRUCTURE:
                            crmModelFiled.setValue(adminService.queryDeptByIds(TagUtil.toSet(value.toString())).getData());
                            break;
                        case FILE:
                            List<FileEntity> data = ApplicationContextHolder.getBean(AdminFileService.class).queryFileList(value.toString()).getData();
                            crmModelFiled.setValue(data);
                            break;
                        case SINGLE_USER:
                            crmModelFiled.setValue(adminService.queryUserById((Long) value).getData());
                            break;
                        default:
                            crmModelFiled.setValue(value);
                            break;
                    }
                }
            }
            recordToFormType(crmModelFiled, typeEnum);
            return crmModelFiled;
        }).collect(Collectors.toList());
        CrmEnum crmEnum = CrmEnum.parse(crmModel.getLabel());
        if (crmEnum == CrmEnum.RECEIVABLES || crmEnum == CrmEnum.CONTRACT || crmEnum == CrmEnum.RETURN_VISIT) {
            AdminConfig numberSetting = adminService.queryFirstConfigByNameAndValue("numberSetting", crmEnum.getType().toString()).getData();
            Integer status = numberSetting.getStatus();
            if (status == 1) {
                for (CrmModelFiledVO field : fieldList) {
                    String fieldName = field.getFieldName();
                    boolean b = "num".equals(fieldName) || "number".equals(fieldName) || "visitNumber".equals(fieldName);
                    if (b && field.getFieldType() == 1) {
                        field.setAutoGeneNumber(1);
                    } else {
                        field.setAutoGeneNumber(0);
                    }
                }
            }
        }else if (crmEnum == CrmEnum.PRODUCT){
            boolean isWithStatus = false;
            Long userId = UserUtil.getUserId();
            String key = userId.toString();
            List<String> noAuthMenuUrls = BaseUtil.getRedis().get(key);
            if (noAuthMenuUrls != null && noAuthMenuUrls.contains(PRODUCT_STATUS_URL)) {
                isWithStatus = true;
            }
            if (isWithStatus) {
                for (CrmModelFiledVO field : fieldList) {
                    if ( "status".equals(field.getFieldName())) {
                        field.setAuthLevel(2);
                        break;
                    }
                }
            }
        }
        return fieldList;
    }

    @Override
    public List<CrmModelFiledVO> queryField(Integer type) {
        QueryWrapper<CrmField> wrapper = new QueryWrapper<>();
        wrapper.eq("label", type).orderByAsc("sorting");
        List<CrmField> crmFieldList = list(wrapper);
        return crmFieldList.stream().map(field -> BeanUtil.copyProperties(field, CrmModelFiledVO.class)).collect(Collectors.toList());
    }

    @Autowired
    private ICrmLeadsService crmLeadsService;

    @Autowired
    private ICrmCustomerService customerService;

    @Autowired
    private ICrmCustomerPoolRelationService customerPoolRelationService;

    @Autowired
    private ICrmCustomerPoolService customerPoolService;


    @Override
    public CrmFieldVerifyBO verify(CrmFieldVerifyBO verifyBO) {
        CrmField field = getById(verifyBO.getFieldId());
        CrmEnum crmEnum = CrmEnum.parse(field.getLabel());
        if (field.getFieldType().equals(1)) {
            Integer count = getBaseMapper().verifyFixedField(crmEnum.getTable(), field.getFieldName(), verifyBO.getValue().trim(), verifyBO.getBatchId(), crmEnum.getType());
            if (count < 1) {
                verifyBO.setStatus(1);
            } else {
                //添加特殊验证
                if (crmEnum.equals(CrmEnum.LEADS)) {
                    if ("leads_name".equals(field.getFieldName())) {
                        CrmLeads leads = crmLeadsService.lambdaQuery().select(CrmLeads::getLeadsId, CrmLeads::getOwnerUserId)
                                .eq(CrmLeads::getLeadsName, verifyBO.getValue().trim())
                                .ne(StrUtil.isNotEmpty(verifyBO.getBatchId()), CrmLeads::getBatchId, verifyBO.getBatchId()).last("limit 1").one();
                        verifyBO.setOwnerUserName(UserCacheUtil.getUserInfo(leads.getOwnerUserId()).getRealname());
                    } else if ("mobile".equals(field.getFieldName())) {
                        CrmLeads leads = crmLeadsService.lambdaQuery().select(CrmLeads::getLeadsId, CrmLeads::getOwnerUserId)
                                .eq(CrmLeads::getMobile, verifyBO.getValue().trim())
                                .ne(StrUtil.isNotEmpty(verifyBO.getBatchId()), CrmLeads::getBatchId, verifyBO.getBatchId()).last("limit 1").one();
                        verifyBO.setOwnerUserName(UserCacheUtil.getUserInfo(leads.getOwnerUserId()).getRealname());
                    }
                } else if (crmEnum.equals(CrmEnum.CUSTOMER)) {
                    if ("customer_name".equals(field.getFieldName())) {
                        CrmCustomer customer = customerService.lambdaQuery().select(CrmCustomer::getCustomerId, CrmCustomer::getOwnerUserId).
                                eq(CrmCustomer::getCustomerName, verifyBO.getValue().trim()).ne(CrmCustomer::getStatus, 3)
                                .ne(StrUtil.isNotEmpty(verifyBO.getBatchId()), CrmCustomer::getBatchId, verifyBO.getBatchId()).last("limit 1").one();
                        if (customer.getOwnerUserId() != null) {
                            verifyBO.setOwnerUserName(UserCacheUtil.getUserInfo(customer.getOwnerUserId()).getRealname());
                        } else {
                            List<Integer> poolIds = customerPoolRelationService.lambdaQuery().select(CrmCustomerPoolRelation::getPoolId).eq(CrmCustomerPoolRelation::getCustomerId, customer.getCustomerId()).list()
                                    .stream().map(CrmCustomerPoolRelation::getPoolId).collect(Collectors.toList());
                            List<String> poolNames = customerPoolService.lambdaQuery().select(CrmCustomerPool::getPoolName).in(CrmCustomerPool::getPoolId, poolIds).list()
                                    .stream().map(CrmCustomerPool::getPoolName).collect(Collectors.toList());
                            verifyBO.setPoolNames(poolNames);
                        }
                    } else if ("mobile".equals(field.getFieldName())) {
                        CrmCustomer customer = customerService.lambdaQuery().eq(CrmCustomer::getMobile, verifyBO.getValue().trim()).ne(CrmCustomer::getStatus, 3)
                                .ne(StrUtil.isNotEmpty(verifyBO.getBatchId()), CrmCustomer::getBatchId, verifyBO.getBatchId()).last("limit 1").one();
                        if (customer.getOwnerUserId() != null) {
                            verifyBO.setOwnerUserName(UserCacheUtil.getUserInfo(customer.getOwnerUserId()).getRealname());
                        } else {
                            List<Integer> poolIds = customerPoolRelationService.lambdaQuery().select(CrmCustomerPoolRelation::getPoolId).eq(CrmCustomerPoolRelation::getCustomerId, customer.getCustomerId()).list()
                                    .stream().map(CrmCustomerPoolRelation::getPoolId).collect(Collectors.toList());
                            List<String> poolNames = customerPoolService.lambdaQuery().select(CrmCustomerPool::getPoolName).in(CrmCustomerPool::getPoolId, poolIds).list()
                                    .stream().map(CrmCustomerPool::getPoolName).collect(Collectors.toList());
                            verifyBO.setPoolNames(poolNames);
                        }
                    }
                }
            }
        } else {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.filter(QueryBuilders.termQuery(StrUtil.toCamelCase(field.getFieldName()), verifyBO.getValue().trim()));
            if(StrUtil.isNotEmpty(verifyBO.getBatchId())){
                queryBuilder.mustNot(QueryBuilders.termQuery("batchId",verifyBO.getBatchId()));
            }
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(queryBuilder)
                    .withSearchType(SearchType.DEFAULT)
                    .withIndices(crmEnum.getIndex())
                    .withTypes("_doc").build();
            long count = restTemplate.count(searchQuery);
            if (count < 1) {
                verifyBO.setStatus(1);
            }
        }
        return verifyBO;
    }

    /**
     * 查询类型是文件的自定义字段
     *
     * @return data
     */
    @Override
    public List<CrmField> queryFileField() {
        return lambdaQuery().eq(CrmField::getType, FieldEnum.FILE.getType()).list();
    }

    @Autowired
    private TaskExecutor executorService;

    public class SaveEs implements Runnable {
        CrmEnum crmEnum;
        List<CrmModelFiledVO> crmModelFiledList;
        List<Map<String, Object>> mapList;
        String index;

        public SaveEs(CrmEnum crmEnum, List<CrmModelFiledVO> crmModelFiledList, List<Map<String, Object>> mapList) {
            this.crmEnum = crmEnum;
            this.crmModelFiledList = crmModelFiledList;
            this.mapList = mapList;
            this.index = crmEnum.getIndex();
        }

        public SaveEs(CrmEnum crmEnum, List<CrmModelFiledVO> crmModelFiledList, List<Map<String, Object>> mapList, String index) {
            this.crmEnum = crmEnum;
            this.crmModelFiledList = crmModelFiledList;
            this.mapList = mapList;
            this.index = index;
        }

        @Override
        public void run() {
            log.warn("线程id{}", Thread.currentThread().getName());
            mapList.forEach(map -> {
                map.remove("cid");
                map.remove("tid");
                crmModelFiledList.forEach(modelField -> {
                    if (map.get(modelField.getFieldName()) == null) {
                        map.remove(modelField.getFieldName());
                        return;
                    }
                    switch (crmEnum) {
                        case LEADS:
                            ApplicationContextHolder.getBean(CrmLeadsServiceImpl.class).setOtherField(map);
                            break;
                        case CUSTOMER:
                            ApplicationContextHolder.getBean(CrmCustomerServiceImpl.class).setOtherField(map);
                            break;
                        case CUSTOMER_POOL:
                            ApplicationContextHolder.getBean(CrmCustomerServiceImpl.class).setOtherField(map);
                            break;
                        case CONTACTS:
                            ApplicationContextHolder.getBean(CrmContactsServiceImpl.class).setOtherField(map);
                            break;
                        case BUSINESS:
                            ApplicationContextHolder.getBean(CrmBusinessServiceImpl.class).setOtherField(map);
                            break;
                        case CONTRACT:
                            ApplicationContextHolder.getBean(CrmContractServiceImpl.class).setOtherField(map);
                            break;
                        case RECEIVABLES:
                            ApplicationContextHolder.getBean(CrmReceivablesServiceImpl.class).setOtherField(map);
                            break;
                        case PRODUCT:
                            ApplicationContextHolder.getBean(CrmProductServiceImpl.class).setOtherField(map);
                            String batchId = (String) map.get("batchId");
                            CrmProductData unit = ApplicationContextHolder.getBean(ICrmProductDataService.class).lambdaQuery().eq(CrmProductData::getBatchId, batchId).eq(CrmProductData::getName, "unit").one();
                            if (unit != null) {
                                map.put("unit", unit.getValue());
                            } else {
                                map.put("unit", "");
                            }
                            break;
                        case RETURN_VISIT:
                            ApplicationContextHolder.getBean(CrmReturnVisitServiceImpl.class).setOtherField(map);
                            break;
                        default:
                            break;
                    }

                    if (FieldEnum.DATE.getType().equals(modelField.getType())) {
                        Object value = map.remove(modelField.getFieldName());
                        if (value instanceof Date) {
                            map.put(modelField.getFieldName(), DateUtil.formatDate((Date) value));
                        }
                    }
                    if (modelField.getFieldType() == 0 && Arrays.asList(3, 8, 9, 10, 11, 12).contains(modelField.getType())) {
                        Object value = map.remove(modelField.getFieldName());
                        if (value != null) {
                            map.put(modelField.getFieldName(), StrUtil.splitTrim(value.toString(), ","));
                        } else {
                            map.put(modelField.getFieldName(), new ArrayList<>());
                        }
                    }

                    if (FieldEnum.DATETIME.getType().equals(modelField.getType())) {
                        Object value = map.remove(modelField.getFieldName());
                        if (value instanceof Date) {
                            map.put(modelField.getFieldName(), DateUtil.formatDateTime((Date) value));
                        }
                    }
                });
            });
            ElasticUtil.initData(restTemplate.getClient(), mapList, crmEnum, index);
            mapList.clear();
        }
    }

    /**
     * 初始化数据
     *
     * @param type type
     * @return data
     */
    @Override
    public Integer initData(Integer type) {
        if (type == 9) {
            savePool();
            return 1;
        }
        CrmEnum crmEnum = CrmEnum.parse(type);
        Integer lastId = 0;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("table", crmEnum == CrmEnum.RETURN_VISIT ? "visit" : crmEnum.getTable());
        dataMap.put("tableName", crmEnum.getTable());
        List<CrmField> crmFields = lambdaQuery().eq(CrmField::getLabel, type).in(CrmField::getFieldType,0,2).groupBy(CrmField::getFieldName).list()
                .stream().peek(field -> field.setFieldName(StrUtil.toCamelCase(field.getFieldName()))).collect(Collectors.toList());
        dataMap.put("fields", crmFields);
        dataMap.put("label", type);
        dataMap.put("lastId", lastId);
        List<CrmModelFiledVO> crmModelFiledList = queryInitField(type);
        ElasticUtil.init(crmModelFiledList, ApplicationContextHolder.getBean(ElasticsearchRestTemplate.class).getClient(), CrmEnum.parse(type));
        while (true) {
            List<Map<String, Object>> mapList = getBaseMapper().initData(dataMap);
            if (mapList.size() == 0) {
                break;
            }
            Object o = mapList.get(mapList.size() - 1).get((crmEnum == CrmEnum.RETURN_VISIT ? "visit" : crmEnum.getTable()) + "Id");
            lastId = TypeUtils.castToInt(o);
            dataMap.put("lastId", lastId);
            log.warn("最后数据id:{},线程id{}", lastId, Thread.currentThread().getName());
            executorService.execute(new SaveEs(crmEnum, crmModelFiledList, mapList));
        }
        return 1;

    }

    private void savePool() {
        CrmEnum crmEnum = CrmEnum.CUSTOMER;
        List<CrmCustomerPoolRelation> list = ApplicationContextHolder.getBean(ICrmCustomerPoolRelationService.class).list();
        Map<Integer, List<CrmCustomerPoolRelation>> collect = list.stream().collect(Collectors.groupingBy(CrmCustomerPoolRelation::getCustomerId));
        Set<Integer> integers = collect.keySet();
        BulkRequest bulkRequest = new BulkRequest();
        for (Integer integer : integers) {
            List<CrmCustomerPoolRelation> poolRelationList = collect.get(integer);
            List<Integer> poolId = new ArrayList<>();
            for (CrmCustomerPoolRelation relation : poolRelationList) {
                poolId.add(relation.getPoolId());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("poolId", poolId);
            UpdateRequest request = new UpdateRequest(crmEnum.getIndex(), "_doc", integer.toString());
            request.doc(map);
            bulkRequest.add(request);
            if (bulkRequest.requests().size() >= 1000) {
                try {
                    restTemplate.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
                    restTemplate.refresh(crmEnum.getIndex());
                } catch (IOException e) {
                    log.error("es修改失败", e);
                }
                bulkRequest = new BulkRequest();
            }
        }
        try {
            if (bulkRequest.requests().size() == 0) {
                return;
            }
            restTemplate.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            restTemplate.refresh(crmEnum.getIndex());
        } catch (IOException e) {
            log.error("es修改失败", e);
        }
    }

    private List<CrmModelFiledVO> queryInitField(Integer type) {
        QueryWrapper<CrmField> wrapper = new QueryWrapper<>();
        wrapper.eq("label", type).orderByAsc("sorting");
        wrapper.groupBy("field_name");
        List<CrmField> crmFieldList = list(wrapper);
        CrmEnum crmEnum = CrmEnum.parse(type);
        List<CrmModelFiledVO> filedList = crmFieldList.stream().map(field -> BeanUtil.copyProperties(field, CrmModelFiledVO.class)).collect(Collectors.toList());
        switch (crmEnum) {
            case LEADS: {
                filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("lastContent", FieldEnum.TEXTAREA, 1));
                filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));

                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                break;
            }
            case CUSTOMER: {
                filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("lastContent", FieldEnum.TEXTAREA, 1));
                filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("receiveTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("dealTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("poolTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("roUserId", FieldEnum.CHECKBOX, 1));
                filedList.add(new CrmModelFiledVO("rwUserId", FieldEnum.CHECKBOX, 1));
                filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("status", FieldEnum.TEXT, 1));

                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("dealStatus", FieldEnum.SELECT, 1));
                filedList.add(new CrmModelFiledVO("detailAddress", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("address", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("preOwnerUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("preOwnerUserName", FieldEnum.TEXT, 1));
                break;
            }
            case CONTACTS: {
                filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));

                filedList.add(new CrmModelFiledVO("customerName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                break;
            }
            case PRODUCT: {
                filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));

                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("categoryName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("unit", FieldEnum.SELECT, 1));
                break;
            }
            case BUSINESS: {
                filedList.add(new CrmModelFiledVO("typeId", FieldEnum.SELECT, 1));
                filedList.add(new CrmModelFiledVO("statusId", FieldEnum.SELECT, 1));
                filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("nextTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("receiveTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("roUserId", FieldEnum.CHECKBOX, 1));
                filedList.add(new CrmModelFiledVO("rwUserId", FieldEnum.CHECKBOX, 1));
                filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("status", FieldEnum.TEXT, 1));

                filedList.add(new CrmModelFiledVO("customerName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("typeName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("statusName", FieldEnum.TEXT, 1));
                break;
            }
            case CONTRACT: {
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
                filedList.add(new CrmModelFiledVO("receivedMoney", FieldEnum.FLOATNUMBER, 1));
                filedList.add(new CrmModelFiledVO("unreceivedMoney", FieldEnum.FLOATNUMBER, 1));

//                客户名称，商机名称，公司签约人（员工），客户签约人（联系人），负责人，创建人
                filedList.add(new CrmModelFiledVO("customerName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("businessName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("contactsName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("companyUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("contractMoney", FieldEnum.FLOATNUMBER, 1));
                break;
            }
            case RECEIVABLES: {
                filedList.add(new CrmModelFiledVO("lastTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("ownerUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("createUserId", FieldEnum.USER, 1));
                filedList.add(new CrmModelFiledVO("checkStatus", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("planId", FieldEnum.TEXT, 1));
                //客户名称，合同编号，负责人，创建人
                filedList.add(new CrmModelFiledVO("customerName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("contractNum", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("planNum", FieldEnum.NUMBER, 1));
                filedList.add(new CrmModelFiledVO("contractMoney", FieldEnum.FLOATNUMBER, 1));
                break;
            }
            case RETURN_VISIT: {
//                回访人，客户名称，联系人，合同编号，创建人
                filedList.add(new CrmModelFiledVO("customerName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("contactsName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("contractNum", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("ownerUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, 1));
                filedList.add(new CrmModelFiledVO("updateTime", FieldEnum.DATETIME, 1));
                filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATETIME, 1));
                break;
            }
            default:
                break;
        }
        return filedList;
    }

    private void dateCheck(Integer label, Integer type) {
        List<String> name = lambdaQuery().select(CrmField::getFieldName).eq(CrmField::getLabel, label).list().stream().map(CrmField::getFieldName).collect(Collectors.toList());
        String nextFieldName = crmFieldConfigService.getNextFieldName(label, type, name, Const.AUTH_DATA_RECURSION_NUM, false);
        Integer integer = getBaseMapper().dataCheck(nextFieldName, label, type);
        if (integer > 0) {
            dateCheck(label, type);
        }
    }

    @Override
    public void recordToFormType(CrmModelFiledVO record, FieldEnum typeEnum) {
        record.setFormType(typeEnum.getFormType());
        switch (typeEnum) {
            case CHECKBOX:
                record.setDefaultValue(StrUtil.splitTrim((CharSequence) record.getDefaultValue(), Const.SEPARATOR));
                record.setValue(StrUtil.splitTrim((CharSequence) record.getValue(), Const.SEPARATOR));
            case SELECT:
                if (CollUtil.isEmpty(record.getSetting())) {
                    record.setSetting(new ArrayList<>(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR)));
                }
                break;
            case USER:
            case STRUCTURE:
                record.setDefaultValue(new ArrayList<>(0));
                break;
            default:
                record.setSetting(new ArrayList<>());
                break;
        }
    }

    private void recordToFormType2(CrmField record, FieldEnum typeEnum) {
        record.setFormType(typeEnum.getFormType());
        switch (typeEnum) {
            case CHECKBOX:
                record.setDefaultValue(StrUtil.splitTrim((CharSequence) record.getDefaultValue(), Const.SEPARATOR));
            case SELECT:
                record.setSetting(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR));
                break;
            case USER:
            case STRUCTURE:
                record.setDefaultValue(new ArrayList<>(0));
                break;
            default:
                record.setSetting(new ArrayList<>());
                break;
        }
    }

    @Override
    public long queryCustomerFieldDuplicateByNoFixed(String name, Object value) {
        TermQueryBuilder termQuery = QueryBuilders.termQuery(name, value);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(termQuery)
                .withSearchType(SearchType.DEFAULT)
                .withIndices(CrmEnum.CUSTOMER.getIndex())
                .withTypes("_doc").build();
        return restTemplate.count(searchQuery);
    }

    @Override
    public Integer queryCustomerFieldDuplicateByFixed(String name, Object value) {
        return getBaseMapper().queryCustomerFieldDuplicateByFixed(name, value);
    }


    @Override
    public void setPoolFieldStyle(CrmFieldStyleBO fieldStyle) {
        Integer poolId = fieldStyle.getPoolId();
        CrmCustomerPoolFieldStyle fleldStyle = crmCustomerPoolFieldStyleService.lambdaQuery().eq(CrmCustomerPoolFieldStyle::getPoolId, fieldStyle.getPoolId())
                .eq(CrmCustomerPoolFieldStyle::getFieldName, fieldStyle.getField()).eq(CrmCustomerPoolFieldStyle::getUserId, UserUtil.getUserId())
                .last("limit 1").one();
        if (fleldStyle != null) {
            fleldStyle.setStyle(fieldStyle.getWidth());
            crmCustomerPoolFieldStyleService.updateById(fleldStyle);
        } else {
            fleldStyle = new CrmCustomerPoolFieldStyle();
            fleldStyle.setPoolId(poolId);
            fleldStyle.setCreateTime(new Date());
            fleldStyle.setStyle(fieldStyle.getWidth());
            fleldStyle.setFieldName(fieldStyle.getField());
            fleldStyle.setUserId(UserUtil.getUserId());
            crmCustomerPoolFieldStyleService.save(fleldStyle);
        }
    }

    @Override
    public void changeEsIndex(List<Integer> labels) {
        for (Integer label : labels) {
            List<CrmModelFiledVO> crmModelFiledList = queryInitField(label);
            Map<String, Object> properties = new HashMap<>(crmModelFiledList.size());
            crmModelFiledList.forEach(crmField -> properties.put(crmField.getFieldName(), ElasticUtil.parseType(crmField.getType())));
            Map<String, Object> mapping = new HashMap<>();
            mapping.put("properties", properties);
            CrmEnum crmEnum = CrmEnum.parse(label);
            String indexAlias = crmEnum.getIndex();
            String createIndex = indexAlias + ":" + DateUtil.formatDate(DateUtil.date());
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(createIndex);
            createIndexRequest.mapping(mapping);
            try {
                restTemplate.deleteIndex(createIndex);
                CreateIndexResponse createIndexResponse = restTemplate.getClient().indices().create(createIndexRequest, RequestOptions.DEFAULT);
                boolean acknowledged = createIndexResponse.isAcknowledged();
                if (acknowledged) {
                    //初始化数据
                    Integer lastId = 0;
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("table", crmEnum == CrmEnum.RETURN_VISIT ? "visit" : crmEnum.getTable());
                    dataMap.put("tableName", crmEnum.getTable());
                    List<CrmField> crmFields = lambdaQuery().eq(CrmField::getLabel, crmEnum.getType()).groupBy(CrmField::getFieldName).list()
                            .stream().filter(field -> field.getFieldType() == 0).peek(field -> field.setFieldName(StrUtil.toCamelCase(field.getFieldName()))).collect(Collectors.toList());
                    dataMap.put("fields", crmFields);
                    dataMap.put("label", crmEnum.getType());
                    dataMap.put("lastId", lastId);
                    while (true) {
                        List<Map<String, Object>> mapList = getBaseMapper().initData(dataMap);
                        if (mapList.size() == 0) {
                            break;
                        }
                        Object o = mapList.get(mapList.size() - 1).get((crmEnum == CrmEnum.RETURN_VISIT ? "visit" : crmEnum.getTable()) + "Id");
                        lastId = TypeUtils.castToInt(o);
                        dataMap.put("lastId", lastId);
                        log.warn("最后数据id:{},线程id{}", lastId, Thread.currentThread().getName());
                        executorService.execute(new SaveEs(crmEnum, crmModelFiledList, mapList, createIndex));
                    }
                    //修改索引别名
                    IndicesAliasesRequest aliasRequest = new IndicesAliasesRequest();
                    GetAliasesResponse alias = restTemplate.getClient().indices().getAlias(new GetAliasesRequest(indexAlias, indexAlias + "_backup"), RequestOptions.DEFAULT);
                    Map<String, Set<AliasMetaData>> oldAliases = alias.getAliases();
                    if (CollUtil.isNotEmpty(oldAliases)) {
                        TreeSet<String> oldIndexSet = new TreeSet<>(oldAliases.keySet());
                        String lastIndex = oldIndexSet.last();
                        aliasRequest.addAliasAction(IndicesAliasesRequest.AliasActions.remove().index(lastIndex).alias(indexAlias));
                        aliasRequest.addAliasAction(IndicesAliasesRequest.AliasActions.add().index(lastIndex).alias(indexAlias + "_backup"));
                        Set<String> deleteIndexs = oldIndexSet.stream().filter(index -> !index.equals(lastIndex)).collect(Collectors.toSet());
                        if (CollUtil.isNotEmpty(deleteIndexs)) {
                            restTemplate.getClient().indices().delete(new DeleteIndexRequest(deleteIndexs.toArray(new String[0])), RequestOptions.DEFAULT);
                        }
                    }
                    aliasRequest.addAliasAction(IndicesAliasesRequest.AliasActions.add().index(createIndex).alias(indexAlias));
                    restTemplate.getClient().indices().updateAliases(aliasRequest, RequestOptions.DEFAULT);
                } else {
                    throw new CrmException(CrmCodeEnum.INDEX_CREATE_FAILED);
                }
            } catch (IOException e) {
                log.error("数据初始化异常:{}", e.getMessage());
                throw new CrmException(CrmCodeEnum.INDEX_CREATE_FAILED);
            }
        }
    }

    @Override
    public List<ExamineField> queryExamineField(Integer label) {
        List<CrmField> crmFields = lambdaQuery().eq(CrmField::getIsNull,1).in(CrmField::getType, 3, 5, 6, 9).eq(CrmField::getLabel, (Objects.equals(1, label) ? 6 : 7)).list();
        crmFields.forEach(field -> {
            recordToFormType2(field, FieldEnum.parse(field.getType()));
        });
        return crmFields.stream().map(field -> BeanUtil.copyProperties(field, ExamineField.class)).collect(Collectors.toList());
    }
}
