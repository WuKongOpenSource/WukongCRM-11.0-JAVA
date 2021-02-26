package com.kakarote.core.field;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2021/1/13
 */
@Service
public class FieldServiceImpl implements FieldService {

    private static final String NUM_RESTRICT_REGEX = "^[\\-]?\\d+|[\\-]?\\d+\\.\\d*|[\\-]?\\d*\\.\\d+$";
    private static final String NUM_REGEX = "^\\d+$";

    @Autowired
    private AdminService adminService;


    @Override
    public Object convertValueByFormType(Object value, FieldEnum typeEnum){
        Object newValue = null;
        switch (typeEnum) {
            case USER:
                newValue = adminService.queryUserByIds(TagUtil.toLongSet(value.toString())).getData();
                break;
            case STRUCTURE:
                newValue = adminService.queryDeptByIds(TagUtil.toSet(value.toString())).getData();
                break;
            case FILE:
                newValue = ApplicationContextHolder.getBean(AdminFileService.class).queryFileList(value.toString()).getData();
                break;
            case HANDWRITING_SIGN:
                newValue = value.toString();
                break;
            case SINGLE_USER:
                newValue = adminService.queryUserById((Long) value).getData();
                break;
            case AREA:
            case CURRENT_POSITION:
            case AREA_POSITION:
                String valueStr = Optional.ofNullable(value).orElse("").toString();
                newValue = JSON.parse(valueStr);
                break;
            case DATE_INTERVAL:
                String dataStr = Optional.ofNullable(value).orElse("").toString();
                newValue = StrUtil.split(dataStr, Const.SEPARATOR);
                break;
            default:
                newValue = value;
                break;
        }
        return newValue;
    }
    
    
    @Override
    public <T> List<List<T>> convertFormPositionFieldList(List<T> fieldList, Function<T,Integer> groupMapper,
                                                          Function<T,Integer> sortMapper,Function<T,Integer> defaultSortMapper){
        List<List<T>> list = new ArrayList<>();
        Map<Integer, List<T>> crmFieldGroupMap = fieldList.stream().collect(Collectors.groupingBy(groupMapper));
        if (crmFieldGroupMap.size() > 0) {
            Map<Integer, List<T>> resultMap = new LinkedHashMap<>();
            crmFieldGroupMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEachOrdered(e -> resultMap.put(e.getKey(), e.getValue()));

            resultMap.forEach((key, value) -> {
                if (key != -1) {
                    value.sort(Comparator.comparing(sortMapper));
                    list.add(value);
                }
            });
            List<T> crmFields = resultMap.get(-1);
            if (CollUtil.isNotEmpty(crmFields)) {
                crmFields.sort(Comparator.comparing(defaultSortMapper));
                int size = crmFields.size();
                boolean isWithResidue = size % 2 != 0;
                List<T> temporaryList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    T crmField = crmFields.get(i);
                    temporaryList.add(crmField);
                    if (i % 2 == 1) {
                        list.add(temporaryList);
                        temporaryList = new ArrayList<>();
                        continue;
                    }
                    if (isWithResidue && i == size - 1) {
                        list.add(temporaryList);
                    }
                }
            }
        }
        return list;
    }


    @Override
    public boolean verifyStrForNumRestrict(String maxNumRestrict, String minNumRestrict) {
        if (StrUtil.isNotEmpty(maxNumRestrict) || StrUtil.isNotEmpty(minNumRestrict)){
            if (StrUtil.isNotEmpty(maxNumRestrict) && StrUtil.isEmpty(minNumRestrict)){
                return maxNumRestrict.matches(NUM_RESTRICT_REGEX);
            }else if (StrUtil.isEmpty(maxNumRestrict) && StrUtil.isNotEmpty(minNumRestrict)){
                return minNumRestrict.matches(NUM_RESTRICT_REGEX);
            }else {
                if (maxNumRestrict.matches(NUM_RESTRICT_REGEX) && minNumRestrict.matches(NUM_RESTRICT_REGEX)){
                    return Double.parseDouble(maxNumRestrict) > Double.parseDouble(minNumRestrict);
                }
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean verifyStrIsConformRegex(List<String> values, boolean isNonNull, String regex) {
        if (CollUtil.isEmpty(values)){
            return !isNonNull;
        }
        if (StrUtil.isEmpty(regex)){
            regex = NUM_REGEX;
        }
        boolean isNum = true;
        for (String value : values) {
            if (StrUtil.isEmpty(value)){
                if (isNonNull){
                    isNum = false;
                    break;
                }
                continue;
            }
            if (!value.matches(regex)){
                isNum = false;
                break;
            }
        }
        return isNum;
    }


    @Override
    public String convertObjectValueToString(Integer type, Object value, String defaultValue){
        boolean isNeedHandle = FieldEnum.AREA.getType().equals(type)
                || FieldEnum.AREA_POSITION.getType().equals(type)
                || FieldEnum.CURRENT_POSITION.getType().equals(type);
        if (isNeedHandle) {
            if (value instanceof JSONObject) {
                return !ObjectUtil.isEmpty(value) ? ((JSONObject) value).toJSONString() : "";
            }
            return !ObjectUtil.isEmpty(value) ? JSON.toJSONString(value) : "";
        }
        if (FieldEnum.DATE_INTERVAL.getType().equals(type)){
            if (value instanceof JSONObject){
                value = !ObjectUtil.isEmpty(value) ? JSON.parseObject(((JSONObject)value).toJSONString(),List.class) : null;
            }
            return !ObjectUtil.isEmpty(value) ? StrUtil.join(Const.SEPARATOR, value) : "";
        }
        return defaultValue;
    }
}
