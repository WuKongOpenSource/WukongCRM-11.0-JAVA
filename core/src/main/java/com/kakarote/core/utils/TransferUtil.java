package com.kakarote.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 类转换工具类
 * @author: hmb
 * @date: 2020-05-12 14:00
 */
public class TransferUtil {

    /**
     * list转换
     * @param source      源list
     * @param targetClass 转换class
     * @return
     */
    public static <S, T> List<T> transferList(List<S> source, Class<T> targetClass) {
        if (CollUtil.isEmpty(source)){
            return new ArrayList<>();
        }
        return source.stream().map(s -> BeanUtil.copyProperties(s, targetClass)).collect(Collectors.toList());
    }

}
