package com.kakarote.core.field;

import com.kakarote.core.common.FieldEnum;

import java.util.List;
import java.util.function.Function;

/**
 * @author JiaS
 * @date 2021/1/13
 */
public interface FieldService {

    /**
     * 转换数据库值得格式供前端使用
     * */
    Object convertValueByFormType(Object value, FieldEnum typeEnum);


    /**
     * 转换字段列表根据表单定位
     * */
    <T> List<List<T>> convertFormPositionFieldList(List<T> fieldList, Function<T,Integer> groupMapper,
                                                          Function<T,Integer> sortMapper,Function<T,Integer> defaultSortMapper);



    /**
     * 验证表单字段限制的数值是否正确
     * */
    public boolean verifyStrForNumRestrict(String maxNumRestrict,String minNumRestrict);

    /**
     * 验证字符串是否符合规则
     * */
    public boolean verifyStrIsConformRegex(List<String> values,boolean isNonNull,String regex);


    /**
     * 将符合条件的字段值转换成str
     * */
    public String convertObjectValueToString(Integer type,Object value,String defaultValue);
}
