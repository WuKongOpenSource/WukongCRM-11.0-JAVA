package com.kakarote.crm.common;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;
import java.util.regex.Matcher;

/**
 * 保存CRM数据时验证数据格式
 */
public class CrmVerify {

    private CrmEnum crmEnum;

    public CrmVerify(CrmEnum crmEnum) {
        this.crmEnum = crmEnum;
    }

    public Result verify(CrmModelSaveBO model) {
        switch (crmEnum) {
            case LEADS:
                return verifyLeads(model);
            case CUSTOMER:
                return verifyLeads(model);
            case CONTACTS:
                return verifyLeads(model);
            case PRODUCT:
                return verifyProduct(model);
            default:
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
    }

    /**
     * 验证字段的正确性
     * 目前线索，客户，联系人需要验证的逻辑是相同的
     *
     * @param model json数据
     * @return 验证结果
     */
    private Result verifyLeads(CrmModelSaveBO model) {
        JSONObject entity = new JSONObject(model.getEntity());
        if (StrUtil.isNotEmpty(entity.getString("mobile"))) {
            if (!ReUtil.isMatch("^(\\+?0?\\d{2,4}\\-?)?\\d{6,11}$", entity.getString("mobile"))) {
                return R.error(CrmCodeEnum.CRM_PHONE_FORMAT_ERROR);
            }
        }
        if (StrUtil.isNotEmpty(entity.getString("next_time"))) {
            Result result = verifyDateTime(entity.getString("next_time"));
            if (!result.hasSuccess()) {
                return result;
            }
        }
        Result result = verifyField(model);
        if (!result.hasSuccess()) {
            return result;
        }
        return R.ok();
    }

    /**
     * 验证字段的正确性
     *
     * @param model json数据
     * @return 验证结果
     */
    private Result verifyProduct(CrmModelSaveBO model) {
        JSONObject entity = new JSONObject(model.getEntity());
        if (StrUtil.isNotEmpty(entity.getString("price"))) {
            if (!ReUtil.isMatch(PatternPool.MONEY, entity.getString("price"))) {
                return R.error(CrmCodeEnum.CRM_PRICE_FORMAT_ERROR);
            }
        }
        Result result = verifyField(model);
        if (!result.hasSuccess()) {
            return result;
        }
        return R.ok();
    }

    /**
     * 验证自定义字段的正确性
     *
     * @param modelSaveBO json数据
     * @return 验证结果
     */
    private Result verifyField(CrmModelSaveBO modelSaveBO) {
        List<CrmModelFiledVO> array = modelSaveBO.getField();
        for (CrmModelFiledVO object : array) {
            int type = object.getType();
            Object val = object.getValue();
            if (val == null) {
                continue;
            }
            String value = val.toString();
            if (StrUtil.isEmpty(value)) {
                continue;
            }
            if (type == 4) {
                Result result = verifyDate(value);
                if (!result.hasSuccess()) {
                    return result;
                }
            } else if (type == 5) {
                if (!ReUtil.isMatch(PatternPool.NUMBERS, value)) {
                    return R.error(CrmCodeEnum.CRM_PRICE_FORMAT_ERROR);
                }
            } else if (type == 7) {
                if (!ReUtil.isMatch("^(\\+?0?\\d{2,4}\\-?)?\\d{6,11}$", value)) {
                    return R.error(CrmCodeEnum.CRM_PHONE_FORMAT_ERROR);
                }
            } else if (type == 13) {
                Result result = verifyDateTime(value);
                if (!result.hasSuccess()) {
                    return result;
                }
            }
        }
        return R.ok();
    }

    /**
     * 验证日期时间的正确性
     *
     * @param str 日期时间字符串
     * @return 验证结果
     */
    private Result verifyDateTime(String str) {
        if (DatePattern.NORM_DATETIME_PATTERN.length() != str.length()) {
            return R.error(CrmCodeEnum.CRM_DATETIME_FORMAT_ERROR);
        }
        //将日期切割成 yyyy-MM-dd 和 HH:mm:ss 两部分
        String[] dates = str.split(" ");
        if (dates.length != 2) {
            return R.error(CrmCodeEnum.CRM_DATETIME_FORMAT_ERROR);
        }
        if (!isTime(dates[0]) || !ReUtil.isMatch(PatternPool.TIME, dates[1])) {
            return R.error(CrmCodeEnum.CRM_DATETIME_FORMAT_ERROR);
        }
        return R.ok();
    }

    /**
     * 验证日期的正确性
     *
     * @param str 日期字符串
     * @return 验证结果
     */
    private Result verifyDate(String str) {
        if (!isTime(str)) {
            return R.error(CrmCodeEnum.CRM_DATETIME_FORMAT_ERROR);
        }
        return R.ok();
    }

    private boolean isTime(String str) {
        if (Validator.isMatchRegex(Validator.BIRTHDAY, str)) {
            Matcher matcher = Validator.BIRTHDAY.matcher(str);
            if (matcher.find()) {
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(3));
                int day = Integer.parseInt(matcher.group(5));
                // 验证月
                if (month < 1 || month > 12) {
                    return false;
                }

                // 验证日
                if (day < 1 || day > 31) {
                    return false;
                }
                // 检查几个特殊月的最大天数
                if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
                    return false;
                }
                if (month == 2) {
                    // 在2月，非闰年最大28，闰年最大29
                    return day < 29 || (day == 29 && DateUtil.isLeapYear(year));
                }
                return true;
            }
        }
        return false;
    }
}
