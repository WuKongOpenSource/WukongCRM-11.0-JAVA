package com.kakarote.oa.common.log;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.kakarote.core.feign.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class SysLogUtil {


    @Autowired
    private AdminService adminService;



    /**
     * 属性kv
     */
    private static Map<String, Dict> propertiesMap = new HashMap<>();

    static {
        propertiesMap.put("log", Dict.create().set("content", "当前工作内容").set("tomorrow", "下日工作内容").set("question", "遇到的问题"));
    }

    /**
     * 更新记录
     *
     * @param oldObj  之前对象
     * @param newObj  新对象
     */
    public List<String> searchChange(Map<String, Object> oldObj, Map<String, Object> newObj, String type) {
        List<String> textList = new ArrayList<>();
        for (String oldKey : oldObj.keySet()) {
            for (String newKey : newObj.keySet()) {
                if (propertiesMap.get(type).containsKey(oldKey)) {
                    Object oldValue = oldObj.get(oldKey);
                    Object newValue = newObj.get(newKey);
                    if (oldValue instanceof Date) {
                        oldValue = DateUtil.formatDateTime((Date) oldValue);
                    }
                    if (newValue instanceof Date) {
                        newValue = DateUtil.formatDateTime((Date) newValue);
                    }
                    if (oldValue instanceof BigDecimal || newValue instanceof BigDecimal) {
                        oldValue = Convert.toBigDecimal(oldValue, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                        newValue = Convert.toBigDecimal(newValue, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                    }
                    if (newKey.equals(oldKey) && !oldValue.equals(newValue)) {
                        if (ObjectUtil.isEmpty(oldValue)) {
                            oldValue = "空";
                        }
                        if (ObjectUtil.isEmpty(newValue)) {
                            newValue = "空";
                        }
                        textList.add("将" + propertiesMap.get(type).get(oldKey) + " 由" + oldValue + "修改为" + newValue + "。");
                    }
                }
            }
        }
        return textList;
    }

}
