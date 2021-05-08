package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmFieldMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Autowired
    private FieldService fieldService;

    @Autowired
    private ICrmFieldExtendService crmFieldExtendService;

    /**
     * 查询自定义字段列表
     *
     * @return data
     */
    @Override
    public List<CrmFieldsBO> queryFields() {
        List<CrmField> list = query().select("IFNULL(MAX(update_time),'2000-01-01 00:00:00') as updateTime", "label")
                .in("label", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 17,18))
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
                }else if (crmField.getLabel().equals(CrmEnum.INVOICE.getType())) {
                    fieldNameArr = new String[]{"invoice_id", "invoice_apply_number"};
                }
                List<String> keyList = Arrays.asList(fieldNameArr);
                if (keyList.contains(crmField.getFieldName())) {
                    throw new CrmException(CrmCodeEnum.SYSTEM_RELATED_FIELDS_CANNOT_BE_HIDDEN);
                }
                if (crmField.getIsNull() == 1) {
                    throw new CrmException(CrmCodeEnum.REQUIRED_OPTIONS_CANNOT_BE_HIDDEN);
                }
            }
            //判断数字格式限制的小数位数是否正确
            if(fieldService.equalsByType(crmField.getType(),FieldEnum.NUMBER,FieldEnum.FLOATNUMBER,FieldEnum.PERCENT)){
                if (!fieldService.verifyStrForNumRestrict(crmField.getMaxNumRestrict(),crmField.getMinNumRestrict())){
                    throw new CrmException(CrmCodeEnum.THE_FIELD_NUM_RESTRICT_ERROR);
                }
            }
            //判断明细表格的数据是否正确
            if (FieldEnum.DETAIL_TABLE.getType().equals(crmField.getType())){
                if (CollUtil.isEmpty(crmField.getFieldExtendList())){
                    throw new CrmException(CrmCodeEnum.THE_FIELD_DETAIL_TABLE_FORMAT_ERROR);
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
            String cover = Integer.toBinaryString(1 << 8).substring(1);
            String s = Integer.toBinaryString(field.getOperating());
            String num = s.length() < 8 ? cover.substring(s.length()) + s : s;
            char i = num.charAt(1);
            if (i==0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
            }
        }
        //不在上面循环,避免一些不可恢复的数据
        for (CrmField field : removeFieldList) {
            if(!Objects.equals(0,field.getFieldType())) {
                continue;
            }
            //删除自定义字段
            if (FieldEnum.DETAIL_TABLE.getType().equals(field.getType())){
                crmFieldExtendService.deleteCrmFieldExtend(field.getFieldId());
            }
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
                case INVOICE:
                    ApplicationContextHolder.getBean(ICrmInvoiceDataService.class).lambdaUpdate().eq(CrmInvoiceData::getFieldId, field.getFieldId()).remove();
                    break;
                default:
                    break;
            }
        }

        //保存
        AtomicInteger sort = new AtomicInteger(0);
        crmFieldBO.getData().forEach(field -> {
            field.setSorting(sort.getAndIncrement());
            if (ObjectUtil.isEmpty(field.getDefaultValue())){
                field.setDefaultValue("");
            }else {
                boolean isNeedHandle = fieldService.equalsByType(field.getType(),FieldEnum.AREA,FieldEnum.AREA_POSITION,FieldEnum.CURRENT_POSITION);
                if (isNeedHandle) {
                    field.setDefaultValue(JSON.toJSONString(field.getDefaultValue()));
                }
                if (field.getDefaultValue() instanceof Collection) {
                    field.setDefaultValue(StrUtil.join(Const.SEPARATOR, field.getDefaultValue()));
                }
            }
            List<CrmFieldSort> crmFieldSortList = new ArrayList<>();
            QueryWrapper<CrmFieldSort> crmFieldSortQueryWrapper = new QueryWrapper<>();
            crmFieldSortQueryWrapper.select("user_id").eq("label", label).groupBy("user_id");
            List<Long> userIdList = crmFieldSortService.listObjs(crmFieldSortQueryWrapper, obj -> Long.valueOf(obj.toString()));
            if (field.getFieldId() != null) {
                if (FieldEnum.DETAIL_TABLE.getType().equals(field.getType())){
                    crmFieldExtendService.saveOrUpdateCrmFieldExtend(field.getFieldExtendList(),field.getFieldId(),true);
                }
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
                if (FieldEnum.DETAIL_TABLE.getType().equals(field.getType())){
                    crmFieldExtendService.saveOrUpdateCrmFieldExtend(field.getFieldExtendList(),field.getFieldId(),false);
                }

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

    @Override
    public List<List<CrmField>> queryFormPositionFieldList(Integer label, boolean isQueryHide){
        List<CrmField> fieldList = list(label,isQueryHide);
        return fieldService.convertFormPositionFieldList(fieldList,CrmField::getXAxis,CrmField::getYAxis, CrmField::getSorting);
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
        fieldSortVOList.forEach(fieldSort -> {
            if ("website".equals(fieldSort.getFieldName())) {
                fieldSort.setFormType(FieldEnum.WEBSITE.getFormType());
            }else {
                fieldSort.setFormType(FieldEnum.parse(fieldSort.getType()).getFormType());
            }
        });
        fieldSortVOList.removeIf(fieldSort -> fieldService.equalsByType(fieldSort.getType(),FieldEnum.DESC_TEXT,FieldEnum.DETAIL_TABLE));
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
        Long userId = UserUtil.getUserId();
        crmFieldSortService.lambdaUpdate().eq(CrmFieldSort::getLabel,fieldSort.getLabel()).eq(CrmFieldSort::getUserId,userId).remove();
        List<CrmFieldSort> fieldSortList = crmFieldSortService.queryAllFieldSortList(fieldSort.getLabel(), userId);
        List<CrmFieldSort> noHideFields = fieldSort.getNoHideFields();
        List<CrmFieldSort> hideFields = fieldSort.getHideFields();

        for (CrmFieldSort sort : fieldSortList) {
            if (hideFields.contains(sort)) {
                sort.setIsHide(1);
                continue;
            }
            int index = noHideFields.indexOf(sort);
            if (index >= 0) {
                sort.setIsHide(0);
                sort.setSort(index + 1);
                CrmFieldSort crmFieldSort = noHideFields.get(index);
                sort.setStyle(crmFieldSort.getStyle());
            }
        }
        crmFieldSortService.saveBatch(fieldSortList, Const.BATCH_SAVE_SIZE);
    }

    /**
     * 查询字段配置
     *
     * @param label 类型
     */
    @Override
    public JSONObject queryFieldConfig(Integer label) {
        //查出自定义字段，查看顺序表是否存在该字段，没有则插入，设为隐藏
        List<CrmFieldSort> fieldList = crmFieldSortService.lambdaQuery().eq(CrmFieldSort::getLabel, label).eq(CrmFieldSort::getUserId, UserUtil.getUserId())
                .select(CrmFieldSort::getId, CrmFieldSort::getName, CrmFieldSort::getFieldName, CrmFieldSort::getIsHide)
                .notIn(CrmFieldSort::getType,Arrays.asList(8,50))
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
                    crmModelFiled.setValue(fieldService.convertValueByFormType(value,typeEnum));
                }
            }
            recordToFormType(crmModelFiled, typeEnum);
            return crmModelFiled;
        }).collect(Collectors.toList());
        CrmEnum crmEnum = CrmEnum.parse(crmModel.getLabel());
        if (crmEnum == CrmEnum.RECEIVABLES || crmEnum == CrmEnum.CONTRACT || crmEnum == CrmEnum.RETURN_VISIT || crmEnum == CrmEnum.INVOICE) {
            AdminConfig numberSetting = adminService.queryFirstConfigByNameAndValue("numberSetting", crmEnum.getType().toString()).getData();
            Integer status = numberSetting.getStatus();
            if (status == 1) {
                for (CrmModelFiledVO field : fieldList) {
                    String fieldName = field.getFieldName();
                    boolean b = Arrays.asList("num","number","visitNumber","invoiceApplyNumber").contains(fieldName);
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
        }else if(crmEnum == CrmEnum.CUSTOMER){
            fieldList.forEach(field -> {
                if ("website".equals(field.getFieldName())){
                    field.setFormType(FieldEnum.WEBSITE.getFormType());
                }
            });
        }
        return fieldList;
    }

    @Override
    public List<List<CrmModelFiledVO>> queryFormPositionFieldVO(CrmModel crmModel) {
        List<CrmModelFiledVO> crmModelFiledVOList = queryField(crmModel);
        return fieldService.convertFormPositionFieldList(crmModelFiledVOList,CrmModelFiledVO::getXAxis,CrmModelFiledVO::getYAxis,CrmModelFiledVO::getSorting);
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

    @Override
    public void recordToFormType(CrmModelFiledVO record, FieldEnum typeEnum) {
        record.setFormType(typeEnum.getFormType());
        switch (typeEnum) {
            case CHECKBOX:
                record.setDefaultValue(StrUtil.splitTrim((CharSequence) record.getDefaultValue(), Const.SEPARATOR));
                record.setValue(StrUtil.splitTrim((CharSequence) record.getValue(), Const.SEPARATOR));
            case SELECT:
                if(Objects.equals(record.getRemark(),FieldEnum.OPTIONS_TYPE.getFormType())) {
                    if (CollUtil.isEmpty(record.getOptionsData())) {
                        JSONObject optionsData = JSON.parseObject(record.getOptions());
                        record.setOptionsData(optionsData);
                        record.setSetting(new ArrayList<>(optionsData.keySet()));
                    }
                }else {
                    if (CollUtil.isEmpty(record.getSetting())) {
                        record.setSetting(new ArrayList<>(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR)));
                    }
                }
                break;
            case DATE_INTERVAL:
                record.setDefaultValue(StrUtil.split((String)record.getDefaultValue(), Const.SEPARATOR));
                if (record.getValue() instanceof String){
                    record.setValue(StrUtil.split((String)record.getValue(), Const.SEPARATOR));
                }
                break;
            case USER:
            case STRUCTURE:
                record.setDefaultValue(new ArrayList<>(0));
                break;
            case AREA:
            case AREA_POSITION:
            case CURRENT_POSITION:
                String defaultValue = Optional.ofNullable(record.getDefaultValue()).orElse("").toString();
                record.setDefaultValue(JSON.parse(defaultValue));
                if (record.getValue() instanceof String) {
                    String value = Optional.ofNullable(record.getValue()).orElse("").toString();
                    record.setValue(JSON.parse(value));
                }
                break;
            case DETAIL_TABLE:
                if (CollUtil.isEmpty(record.getFieldExtendList())) {
                    record.setFieldExtendList(crmFieldExtendService.queryCrmFieldExtend(record.getFieldId()));
                }
                break;
            case DESC_TEXT:
                record.setValue(record.getDefaultValue());
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
                if(Objects.equals(record.getRemark(),FieldEnum.OPTIONS_TYPE.getFormType())) {
                    JSONObject optionsData = JSON.parseObject(record.getOptions());
                    record.setOptionsData(optionsData);
                    record.setSetting(new ArrayList<>(optionsData.keySet()));
                }else {
                    record.setSetting(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR));
                }
                break;
            case DATE_INTERVAL:
                String dataValueStr = Optional.ofNullable(record.getDefaultValue()).orElse("").toString();
                record.setDefaultValue(StrUtil.split(dataValueStr, Const.SEPARATOR));
                break;
            case USER:
            case STRUCTURE:
                record.setDefaultValue(new ArrayList<>(0));
                break;
            case AREA:
            case AREA_POSITION:
            case CURRENT_POSITION:
                String defaultValue = Optional.ofNullable(record.getDefaultValue()).orElse("").toString();
                record.setDefaultValue(JSON.parse(defaultValue));
                break;
            case DETAIL_TABLE:
                record.setFieldExtendList(crmFieldExtendService.queryCrmFieldExtend(record.getFieldId()));
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
    public List<ExamineField> queryExamineField(Integer label) {
        List<CrmField> crmFields = lambdaQuery().eq(CrmField::getIsNull,1).in(CrmField::getType, 3, 5, 6, 9).eq(CrmField::getLabel, (Objects.equals(1, label) ? 6 : 7)).list();
        crmFields.forEach(field -> {
            recordToFormType2(field, FieldEnum.parse(field.getType()));
        });
        return crmFields.stream().map(field -> BeanUtil.copyProperties(field, ExamineField.class)).collect(Collectors.toList());
    }
}
