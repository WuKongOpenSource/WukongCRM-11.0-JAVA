package com.kakarote.core.config;

import cn.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 全局日期统一处理
 * @author zhangzhiwei
 */

@Component
public class DateConverterConfig implements Converter<String, Date> {

    @Override
    public Date convert(@Nullable String dateStr) {
        return DateUtil.parse(dateStr);
    }

}