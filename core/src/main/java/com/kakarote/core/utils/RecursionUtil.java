package com.kakarote.core.utils;

import cn.hutool.core.bean.BeanUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhangzhiwei
 * 递归相关的util包
 */
public class RecursionUtil {

    /**
     * 递归查询列表
     *
     * @param allList    总数据
     * @param parentName 上级的key
     * @param parentId   上级的值
     * @param idName     本级ID的key
     * @param returnName 返回的类型 null代表返回对象类型
     * @param <R>        list类型
     * @param <T>        返回类型
     * @return
     */
    public static <R, T> List<T> getChildList(List<R> allList, String parentName, Object parentId, String idName, String returnName) {
        return getChildList(allList, parentName, parentId, idName, returnName, Const.AUTH_DATA_RECURSION_NUM);
    }

    /**
     * 递归查询列表
     *
     * @param allList    总数据
     * @param parentName 上级的key
     * @param parentId   上级的值
     * @param idName     本级ID的key
     * @param returnName 返回的类型,null代表返回对象类型
     * @param depth      最大递归层级
     * @param <R>
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <R, T> List<T> getChildList(List<R> allList, String parentName, Object parentId, String idName, String returnName, Integer depth) {
        depth--;
        List<T> arrList = new ArrayList<>();
        if (depth < 0) {
            return arrList;
        }
        for (R r : allList) {
            Map<String, Object> beanMap = BeanUtil.beanToMap(r);
            if (!(beanMap.containsKey(idName) && beanMap.containsKey(parentName))) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_SUCH_PARAMENT_ERROR);
            }
            if (Objects.equals(parentId, beanMap.get(parentName))) {
                if (returnName == null) {
                    arrList.add((T) r);
                } else {
                    arrList.add((T) beanMap.get(returnName));
                }
                arrList.addAll(getChildList(allList, parentName, beanMap.get(idName), idName, returnName, depth));
            }
        }
        return arrList;
    }

    /**
     * 递归查询列表数
     *
     * @param allList    总数据
     * @param parentName 上级的key
     * @param parentId   上级的值
     * @param idName     本级ID的key
     * @param treeName   子级树名称
     * @param <R>        list类型
     * @return
     */
    public static <R, T> List<T> getChildListTree(List<R> allList, String parentName, Object parentId, String idName, String treeName, Class<T> clazz) {
        return getChildListTree(allList, parentName, parentId, idName, treeName, clazz, Const.AUTH_DATA_RECURSION_NUM);
    }

    /**
     * 递归查询列表树
     *
     * @param allList    总数据
     * @param parentName 上级的key
     * @param parentId   上级的值
     * @param idName     本级ID的key
     * @param treeName   子级树名称
     * @param depth      最大递归层级
     * @return data
     */

    public static <R, T> List<T> getChildListTree(List<R> allList, String parentName, Object parentId, String idName, String treeName, Class<T> clazz, Integer depth) {
        depth--;
        List<T> arrList = new ArrayList<>();
        if (depth < 0) {
            return arrList;
        }
        for (R data : allList) {
            Map<String, Object> beanMap = BeanUtil.beanToMap(data);
            if (!(beanMap.containsKey(idName) && beanMap.containsKey(parentName))) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_SUCH_PARAMENT_ERROR);
            }
            if (Objects.equals(parentId, beanMap.get(parentName))) {
                beanMap.put(treeName, getChildListTree(allList, parentName, beanMap.get(idName), idName, treeName, clazz, depth));
                T toBean = BeanUtil.mapToBean(beanMap, clazz, true);
                arrList.add(toBean);
            }
        }
        return arrList;
    }

}
