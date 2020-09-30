package com.kakarote.work.common;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

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
}
