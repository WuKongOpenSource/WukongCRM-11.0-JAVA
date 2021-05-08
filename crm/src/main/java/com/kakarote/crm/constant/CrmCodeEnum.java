package com.kakarote.crm.constant;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.ResultCode;

/**
 * @author zhangzhiwei
 * crm响应错误代码枚举类
 */

public enum CrmCodeEnum implements ResultCode {
    //客户模块管理
    ADMIN_MODULE_CLOSE_ERROR(2001, "客户管理模块不能关闭"),
    CRM_FIELD_NUM_ERROR(2002, "每个模块最多存在"+ Const.QUERY_MAX_SIZE +"个字段"),
    CRM_LEADS_TRANSFER_ERROR(2003, "已转换线索不可重复转换"),
    CRM_DATE_REMOVE_ERROR(2004, "数据已被删除"),
    CRM_PRODUCT_CATEGORY_ERROR(2005, "该产品类别已关联产品，不能删除！"),
    CRM_PRODUCT_CATEGORY_CHILD_ERROR(2006, "该类别下有其他产品类别！"),
    CRM_MEMBER_ADD_ERROR(2007, "负责人不能重复选为团队成员！"),
    CRM_MEMBER_DELETE_ERROR(2008, "负责人不能退出团队！"),
    CRM_CONTRACT_DATE_ERROR(2009, "合同结束时间应大于开始时间！"),
    CRM_CONTRACT_NUM_ERROR(2010, "合同编号已存在，请校对后再添加！"),
    CRM_CONTRACT_EXAMINE_STEP_ERROR(2011, "没有启动的审核步骤，不能添加！"),
    CRM_CONTRACT_EXAMINE_USER_ERROR(2012, "没有审批人，不能添加！"),
    CRM_CONTRACT_CANCELLATION_ERROR(2013, "合同已作废，不能编辑"),
    CRM_CONTRACT_EXAMINE_PASS_ERROR(2014, "已通过的合同不能编辑"),
    CRM_CONTRACT_EDIT_ERROR(2015, "不能编辑，请先撤回再编辑！"),
    CRM_CONTRACT_TRANSFER_ERROR(2016, "已作废的合同不能转移！"),
    CRM_DATA_JOIN_ERROR(2017, "该条数据与其他数据有必要关联，请勿删除"),
    CRM_RECEIVABLES_ADD_ERROR(2018, "当前合同未审核通过，不能添加回款"),
    CRM_RECEIVABLES_NUM_ERROR(2019, "回款编号已存在，请校对后再添加！"),
    CRM_RECEIVABLES_EDIT_ERROR(2020, "只能编辑自己创建的回款"),
    CRM_RECEIVABLES_PLAN_ERROR(2021, "该回款计划已收到回款，请勿编辑"),
    CRM_CUSTOMER_SETTING_USER_ERROR(2022, "拥有客户已达上限，无法新增"),
    NO_APPROVAL_STEP_CANNOT_BE_SAVED(2023, "没有审批步骤，无法保存"),
    CRM_CUSTOMER_LOCK_ERROR(2024, "已成交客户无需锁定"),
    CRM_CUSTOMER_LOCK_MAX_ERROR(2025, "有员工可锁定客户数达到上限"),
    CRM_EXAMINE_RECHECK_ERROR(2026, "该审批已撤回"),
    CRM_EXAMINE_AUTH_ERROR(2027, "当前用户没有审批权限"),
    CRM_EXAMINE_RECHECK_PASS_ERROR(2028, "该审核已通过，不能撤回"),
    CRM_NEXT_TIME_ERROR(2029, "最后跟进时间必须大于当前时间"),
    CRM_CRMRETURNVISIT_NUM_ERROR(2030, "回访编号已存在，请校对后再添加！"),
    CRM_ILLEGAL_CHARACTERS_ERROR(2031, "包含非法字符"),
    CRM_PHONE_FORMAT_ERROR(2032,"手机号格式错误"),
    CRM_PRICE_FORMAT_ERROR(2033,"价格应为数字格式"),
    CRM_DATETIME_FORMAT_ERROR(2034,"日期格式错误，例:2020-01-01 00:00:00"),
    CRM_DATE_FORMAT_ERROR(2035,"日期格式错误，例:2020-01-01"),
    CRM_CUSTOMER_POOL_EXIST_USER_ERROR(2036,"公海内有客户，不能停用"),
    CRM_CUSTOMER_POOL_LAST_ERROR(2037,"最后一个启用公海不能停用"),
    CRM_FIELD_EXISTED(2038,"%s已存在"),
    THE_NUMBER_OF_CUSTOMERS_HAS_REACHED_THE_LIMIT(2039,"该员工拥有客户数已达上限"),
    CRM_NO_AUTH(2040, "没有权限"),
    CRM_CUSTOMER_POOL_USER_IS_NULL_ERROR(2041,"公海管理员或公海成员不能为空"),
    CRM_CUSTOMER_POOL_EXIST_USER_DELETE_ERROR(2042,"公海内有客户，不能删除"),
    CRM_CUSTOMER_POOL_LAST_DELETE_ERROR(2043,"最后一个启用公海不能删除"),
    CRM_PRINT_TEMPLATE_NOT_EXIST_ERROR(2044,"使用的打印模板不能为空"),
    CRM_PRINT_PRE_VIEW_ERROR(2045,"仅支持pdf和word格式预览"),
    CRM_BUSINESS_TYPE_RATE_ERROR(2046,"赢单率不能大于100"),
    CRM_BUSINESS_TYPE_OCCUPY_ERROR(2047,"使用中的商机组不可以修改"),
    CRM_CONTRACT_CONFIG_ERROR(2048,"请设置提前提醒天数"),
    CRM_CUSTOMER_SETTING_USER_EXIST_ERROR(2049,"已经有员工或部门信息包含在别的规则里面"),
    CRM_NUMBER_SETTING_LENGTH_ERROR(2050, "编号规则不得少于两级"),
    CRM_NUMBER_SETTING_DATE_NOTNULL_ERROR(2051, "启用重新开始编号需要有日期编号规则"),
    CRM_CAN_ONLY_DELETE_FOLLOW_UP_RECORDS(2052, "只能删除跟进记录"),
    CRM_RECEIVABLES_EXAMINE_PASS_ERROR(2053, "已通过的回款不能编辑"),
    CRM_CUSTOMER_POOL_NOT_EXIST_ERROR(2054, "不存在启用的公海"),
    CRM_MARKETING_UNSYNCHRONIZED_DATA(2055, "有未同步数据，不能删除！"),
    CRM_MARKETING_STOP(2056, "该推广停止使用"),
    CRM_MARKETING_QR_CODE_EXPIRED(2057, "二维码过期"),
    CRM_MARKETING_CAN_ONLY_BE_FILLED_ONCE(2058, "只能填写一次"),
    CRM_MARKETING_DATA_SYNCED(2059, "数据已同步"),
    CRM_CUSTOMER_POOL_DISTRIBUTE_ERROR(2060, "无权分配"),
    CRM_CUSTOMER_POOL_RECEIVE_ERROR(2061, "领取客户数超过限制"),
    CRM_CUSTOMER_POOL_RECEIVE_NUMBER_ERROR(2062, "今日领取次数超过上限"),
    CRM_CUSTOMER_POOL_PRE_USER_RECEIVE_ERROR(2063, "前负责人在限制时间内不能领取"),
    CRM_DATA_DELETED(2064, "%s已删除"),
    CRM_CUSTOMER_POOL_REMIND_ERROR(2065, "没有开启待进入公海提醒的公海规则"),
    CRM_CONTRACT_EXPIRATION_REMIND_ERROR(2066, "合同到期提醒未开启"),
    CRM_RETURN_VISIT_REMIND_ERROR(2067, "回访提醒未开启"),
    CRM_INVOICE_EXAMINE_PASS_ERROR(2068, "已通过的发票不能编辑"),
    CRM_POOL_FIELD_HIDE_ERROR(2069, "至少显示2列"),
    CRM_POOL_TRANSFER_ERROR(2070, "不能向停用公海转入客户"),
    CRM_CALL_DATA_UPDATE_ERROR(2071, "暂不支持编辑操作！"),
    CRM_CALL_DATA_QUERY_ERROR(2072, "%s不正确！"),
    CRM_CALL_UPLOAD_ERROR(2073, "文件上传失败！"),
    CRM_CALL_DOWNLOAD_ERROR(2075, "没有录音文件！"),
    CAN_ONLY_DELETE_WITHDRAWN_AND_SUBMITTED_EXAMINE(2076, "只能删除撤回和未提交的审批！"),
    CRM_SYNC_FAILED(2077, "同步失败:%s！"),
    CRM_ONLY_SYNC_DATA_FOR_WHICH_YOU_ARE_RESPONSIBLE(2078, "只能同步本人负责的数据！"),
    CRM_FIELD_ALREADY_EXISTS(2079, "%s已存在！"),
    SYSTEM_RELATED_FIELDS_CANNOT_BE_HIDDEN(2080, "系统关联字段不能隐藏！"),
    REQUIRED_OPTIONS_CANNOT_BE_HIDDEN(2081, "必填选项不能隐藏！"),
    INDEX_CREATE_FAILED(2082, "%s索引创建失败,数据初始化异常！"),
    CRM_CONTACTS_DATA_ERROR(2083, "检测到没有绑定客户，请确认！"),
    CRM_CONTRACT_EXAMINE_PASS_HINT_ERROR(2085, "已通过的合同需要作废后才可编辑！"),
    CRM_ACTIVITY_FORM_NONENTITY_ERROR(2086, "活动表单已不存在！"),
    THE_FIELD_NAME_OF_THE_FORM_CANNOT_BE_REPEATED(2087,"自定义表单字段名称不能重复"),
    THE_FIELD_NUM_RESTRICT_ERROR(2088,"自定义表单限制的数值格式错误"),
    THE_FIELD_DETAIL_TABLE_FORMAT_ERROR(2089,"清设置表格内的具体字段！")
    ;


    CrmCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
