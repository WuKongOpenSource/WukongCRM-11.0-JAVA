package com.kakarote.work.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wyq
 */
public class WorkUtil {
    public static List<Integer> toList(List<String> labelList) {
        List<Integer> list = new ArrayList<>();
        if (labelList == null || labelList.size() == 0) {
            return list;
        }
        labelList.forEach(ids -> {
            if (StrUtil.isNotEmpty(ids)) {
                for (String id : ids.split(",")) {
                    if (StrUtil.isNotEmpty(id)) {
                        list.add(Integer.valueOf(id));
                    }
                }
            }
        });
        return list;
    }


    /**
    * 判断集合有无交集
    * @date 2020/11/17 11:09
    * @param commaSplice
    * @param ids
    * @return 
    **/
    public static boolean verifyIntersection(String commaSplice, List<Long> ids){
        if (CollUtil.isEmpty(ids)){
            return true;
        }
        List<Long> ownerUserIds = StrUtil.splitTrim(commaSplice, Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList());;
        List<Long> intersection = ownerUserIds.stream().filter(ids::contains).collect(Collectors.toList());
        return intersection.size() > 0;
    }
}
