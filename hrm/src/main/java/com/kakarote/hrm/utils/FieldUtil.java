package com.kakarote.hrm.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.kakarote.hrm.constant.ConditionType;
import com.kakarote.hrm.constant.FieldTypeEnum;
import com.kakarote.hrm.constant.IsEnum;
import com.kakarote.hrm.entity.BO.QueryFilterFieldBO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 自定义字段工具类
 * @author: hmb
 * @date:  2020-05-14 18:46
 */
public final class FieldUtil {

    public static Object convertFieldValue(Integer type,Object objValue,Integer isFixed){
        Object value;
        if (type == FieldTypeEnum.NUMBER.getValue()
        || (type == FieldTypeEnum.SELECT.getValue() && isFixed == IsEnum.YES.getValue())){
            value = Convert.toInt(objValue);
        }else {
            value = Convert.toStr(objValue);
        }
        return value;
    }

    /**
     * 高级筛选条件转换
     * @param filterList
     * @return
     */
    public static List<String> transferSqlCondition(List<QueryFilterFieldBO> filterList) {
        List<String> queryList = new ArrayList<>();
        if (CollUtil.isEmpty(filterList)){
            return queryList;
        }
        for (QueryFilterFieldBO filterFieldBO : filterList) {
            Integer conditionType = filterFieldBO.getConditionType();
            List<String> values = filterFieldBO.getValue();
            String name = filterFieldBO.getName();
            ConditionType conditionTypeEnum = ConditionType.parse(conditionType);
            FieldTypeEnum fieldTypeEnum = FieldTypeEnum.parse(filterFieldBO.getType());
            StringBuilder conditions = new StringBuilder();
            if (CollUtil.isNotEmpty(values) || conditionTypeEnum.equals(ConditionType.DATE) || conditionTypeEnum.equals(ConditionType.DATETIME)
                    || conditionTypeEnum.equals(ConditionType.IS_NULL) || conditionTypeEnum.equals(ConditionType.IS_NOT_NULL)) {
                conditions.append(" and ").append(name);
                if (fieldTypeEnum.equals(FieldTypeEnum.CHECKBOX)){
                    if (conditionTypeEnum.equals(ConditionType.CONTAINS)) {
                        for (int i = 0; i < values.size(); i++) {
                            String option = values.get(i);
                            if (i == 0) {
                                conditions.append(" is not null and find_in_set('").append(option).append("',").append(name).append(")");
                            } else {
                                conditions.append(" and find_in_set('").append(option).append("',").append(name).append(")");
                            }
                        }
                        conditions.append(")");
                    } else if (conditionTypeEnum.equals(ConditionType.IS)) {
                        //数据库是按固定顺序排序后存储，此处做同样处理即可精确匹配
                        CollectionUtil.sortByPinyin(values);
                        conditions.append(" = '").append(CollectionUtil.join(values, ",")).append("'");
                    }
                }
                switch (conditionTypeEnum){
                    case IS:
                        conditions.append(" = '").append(values.get(0)).append("'");
                        break;
                    case IS_NOT:
                        conditions.append(" != '").append(values.get(0)).append("'");
                        break;
                    case CONTAINS:
                        conditions.append(" like '%").append(values.get(0)).append("%'");
                        break;
                    case NOT_CONTAINS:
                        conditions.append(" not like '%").append(values.get(0)).append("%'");
                        break;
                    case IS_NULL:
                        conditions.append(" is null");
                        break;
                    case IS_NOT_NULL:
                        conditions.append(" is not null");
                        break;
                    case GT:
                        conditions.append(" > ").append(values.get(0));
                        break;
                    case EGT:
                        conditions.append(" >= ").append(values.get(0));
                        break;
                    case LT:
                        conditions.append(" < ").append(values.get(0));
                        break;
                    case ELT:
                        conditions.append(" <= ").append(values.get(0));
                        break;
                    case IN:
                        conditions.append(" in (").append(values.get(0)).append(")");
                        break;
                    case NOT_IN:
                        conditions.append("not in (").append(values.get(0)).append(")");
                        break;
                    case DATE:
                        conditions.append(" between '").append(values.get(0)).append("' and '").append(values.get(0)).append("'");
                        break;
                    case DATETIME:
                        conditions.append(" between '").append(values.get(0)).append("' and '").append(values.get(1)).append("'");
                        break;
                    default:break;
                }
            } else {
                continue;
            }
            queryList.add(conditions.toString());
        }
        return queryList;
    }
}
