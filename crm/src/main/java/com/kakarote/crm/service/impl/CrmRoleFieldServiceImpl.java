package com.kakarote.crm.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.PO.CrmRoleField;
import com.kakarote.crm.mapper.CrmRoleFieldMapper;
import com.kakarote.crm.service.ICrmFieldService;
import com.kakarote.crm.service.ICrmRoleFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色字段授权表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-22
 */
@Service
public class CrmRoleFieldServiceImpl extends BaseServiceImpl<CrmRoleFieldMapper, CrmRoleField> implements ICrmRoleFieldService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ICrmFieldService crmFieldService;

    /**
     * 查询用户所属字段权限
     *
     * @param label 类型
     * @return data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CrmRoleField> queryUserFieldAuth(Integer label, Integer authLevel) {
        List<CrmRoleField> list = queryUserRoleField(label);
        list.removeIf(crmRoleField -> crmRoleField.getAuthLevel() < authLevel);
        return list;
    }

    private List<CrmRoleField> queryUserRoleField(Integer label) {
        QueryWrapper<CrmRoleField> queryWrapper = new QueryWrapper<>();
        List<Integer> roleIds = UserUtil.getUser().getRoles();
        queryWrapper.select("field_id", "field_name", "max(auth_level) as auth_level", "field_type")
                .in("role_id", roleIds.size() == 0 ? ListUtil.toList("''") : roleIds)
                .eq("label", label)
                .groupBy("field_name");
        List<CrmRoleField> list = list(queryWrapper);
        if (list.size() == 0) {
            List<Integer> data = adminService.queryRoleByRoleType(2).getData();
            data.retainAll(UserUtil.getUser().getRoles());
            data.forEach(roleId -> {
                queryRoleField(roleId, label);
            });
            list = list(queryWrapper);
        }
        return list;
    }

    /**
     * 查询没有权限的字段
     *
     * @param label label
     * @return data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> queryNoAuthField(Integer label) {
        List<CrmRoleField> list = queryUserRoleField(label);
        list.removeIf(crmRoleField -> crmRoleField.getAuthLevel() > 1);
        List<String> collect = crmFieldService.lambdaQuery().select(CrmField::getFieldName).eq(CrmField::getIsHidden, 1).eq(CrmField::getLabel, label).list()
                .stream().map(field -> StrUtil.toCamelCase(field.getFieldName())).collect(Collectors.toList());
        collect.addAll(list.stream().map(field -> StrUtil.toCamelCase(field.getFieldName())).collect(Collectors.toList()));
        return collect;
    }

    /**
     * 查询字段授权设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CrmRoleField> queryRoleField(Integer roleId, Integer label) {
        List<CrmRoleField> fields = lambdaQuery().eq(CrmRoleField::getRoleId, roleId).eq(CrmRoleField::getLabel, label).list();
        //需要初始化
        if (fields.size() == 0) {
            LambdaQueryWrapper<CrmField> fieldQueryWrapper = new LambdaQueryWrapper<>();
            fieldQueryWrapper.select(CrmField::getFieldId,CrmField::getFieldName,CrmField::getName,CrmField::getFieldType);
            fieldQueryWrapper.eq(CrmField::getLabel,label);
            //不展示描述文字类型的字段
            fieldQueryWrapper.ne(CrmField::getType, FieldEnum.DESC_TEXT.getType());
            List<CrmField> list = crmFieldService.list(fieldQueryWrapper);
            list.forEach(crmField -> {
                CrmRoleField roleField = new CrmRoleField();
                roleField.setRoleId(roleId);
                roleField.setLabel(label);
                roleField.setFieldId(crmField.getFieldId());
                roleField.setFieldName(crmField.getFieldName());
                roleField.setName(crmField.getName());
                roleField.setAuthLevel(3);
                switch (label) {
                    case 1: {
                        if ("leads_name".equals(crmField.getFieldName())) {
                            roleField.setOperateType(3);
                        } else if ("next_time".equals(crmField.getFieldName())) {
                            roleField.setOperateType(4);
                        } else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                    case 2: {
                        if ("customer_name".equals(crmField.getFieldName())) {
                            roleField.setOperateType(3);
                        } else if ("next_time".equals(crmField.getFieldName())) {
                            roleField.setOperateType(4);
                        } else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                    case 3: {
                        if ("customer_id".equals(crmField.getFieldName())) {
                            roleField.setOperateType(4);
                        } else if ("next_time".equals(crmField.getFieldName())) {
                            roleField.setOperateType(4);
                        } else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                    case 4: {
                        if (Arrays.asList("name", "unit", "price", "category_id").contains(crmField.getFieldName())) {
                            roleField.setOperateType(3);
                        } else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                    case 5: {
                        if ("customer_id".equals(crmField.getFieldName())) {
                            roleField.setOperateType(4);
                        } else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                    case 6: {
                        if ("num".equals(crmField.getFieldName())) {
                            roleField.setOperateType(3);
                        } else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                    case 7: {
                        roleField.setOperateType(1);
                        break;
                    }
                    case 17: {
                        if (Arrays.asList("owner_user_id", "customer_id", "contract_id").contains(crmField.getFieldName())) {
                            roleField.setOperateType(4);
                        } else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                    case 18: {
                        if (Arrays.asList("contract_money", "contract_id","customer_id").contains(crmField.getFieldName())) {
                            roleField.setOperateType(4);
                        }else {
                            roleField.setOperateType(1);
                        }
                        break;
                    }
                }

                roleField.setFieldType(crmField.getFieldType());
                fields.add(roleField);
            });
            switch (label) {
                case 1: {
                    fields.add(new CrmRoleField(label, roleId, "owner_user_name", "负责人", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_content", "最后跟进记录", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_time", "最后跟进时间", 2, 2, 1));
                    break;
                }
                case 2: {
                    fields.add(new CrmRoleField(label, roleId, "owner_user_name", "负责人", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_content", "最后跟进记录", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_time", "最后跟进时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "receive_time", "负责人获取客户时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "deal_status", "成交状态", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "status", "锁定状态", 2, 2, 1));
//                    fields.add(new CrmRoleField(label, roleId, "detailAddress", "详细地址", 2, 4, 1));
//                    fields.add(new CrmRoleField(label, roleId, "address", "地区定位", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "pool_day", "距进入公海天数", 2, 2, 1));
                    break;
                }
                case 3: {
                    fields.add(new CrmRoleField(label, roleId, "owner_user_name", "负责人", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_time", "最后跟进时间", 2, 2, 1));
                    break;
                }
                case 4: {
                    fields.add(new CrmRoleField(label, roleId, "status", "是否上下架", 3, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "owner_user_name", "负责人", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    break;
                }
                case 5: {
                    fields.add(new CrmRoleField(label, roleId, "type_name", "商机阶段", 3, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "status_name", "商机状态组", 3, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "owner_user_name", "负责人", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_time", "最后跟进时间", 2, 2, 1));
                    break;
                }
                case 6: {
                    fields.add(new CrmRoleField(label, roleId, "owner_user_name", "负责人", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_time", "最后跟进时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "last_content", "最后跟进记录", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "received_money", "已收款金额", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "unreceived_money", "未收款金额", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "check_status", "审核状态", 2, 4, 1));
                    break;
                }
                case 7: {
                    fields.add(new CrmRoleField(label, roleId, "contractMoney", "合同金额", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "owner_user_name", "负责人", 2, 4, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "check_status", "审核状态", 2, 4, 1));
                    break;
                }
                case 17: {
                    fields.add(new CrmRoleField(label, roleId, "create_user_name", "创建人", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "create_time", "创建时间", 2, 2, 1));
                    fields.add(new CrmRoleField(label, roleId, "update_time", "更新时间", 2, 2, 1));
                    break;
                }
                default:
                    break;
            }
            saveBatch(fields);
        }
        return fields;
    }


    /**
     * 保存字段设置
     *
     * @param fields 字段列表
     */
    @Override
    public void saveRoleField(List<CrmRoleField> fields) {
        updateBatchById(fields);
    }

    /**
     * 内部调用，保存自定义字段
     *
     * @param crmField 自定义字段
     */
    @Override
    public void saveRoleField(CrmField crmField) {
        List<CrmRoleField> list = lambdaQuery().select(CrmRoleField::getRoleId).eq(CrmRoleField::getLabel, crmField.getLabel()).groupBy(CrmRoleField::getRoleId).list();
        List<CrmRoleField> roleFieldList = new ArrayList<>();
        list.forEach(field -> {
            CrmRoleField roleField = new CrmRoleField();
            roleField.setRoleId(field.getRoleId());
            roleField.setLabel(crmField.getLabel());
            roleField.setFieldId(crmField.getFieldId());
            roleField.setFieldName(crmField.getFieldName());
            roleField.setName(crmField.getName());
            roleField.setAuthLevel(3);
            roleField.setOperateType(1);
            roleField.setFieldType(crmField.getFieldType());
            roleFieldList.add(roleField);
        });
        saveBatch(roleFieldList);
    }

    /**
     * 内部调用，删除自定义字段
     *
     * @param fieldId field
     */
    @Override
    public void deleteRoleField(Integer fieldId) {
        removeByMap(new JSONObject().fluentPut("field_id", fieldId));
    }
}
