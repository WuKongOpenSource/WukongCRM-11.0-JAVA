package com.kakarote.admin.common;

import com.kakarote.core.common.ResultCode;

/**
 * @author zhangzhiwei
 * 管理后台响应错误代码枚举类
 */

public enum AdminCodeEnum implements ResultCode {
    //客户模块管理
    ADMIN_MODULE_CLOSE_ERROR(1101, "客户管理模块不能关闭"),
    ADMIN_DATA_EXIST_ERROR(1102, "数据不存在"),
    ADMIN_PASSWORD_INTENSITY_ERROR(1106, "密码必须由 6-20位字母、数字组成"),
    ADMIN_USER_EXIST_ERROR(1107, "用户已存在！"),
    ADMIN_PARENT_USER_ERROR(1109, "这个用户的下属不能设置为直属上级！"),
    ADMIN_PARENT_DEPT_ERROR(1110, "这个部门的下属不能设置为直属部门！"),
    ADMIN_DEPT_REMOVE_EXIST_USER_ERROR(1111, "这个部门下有员工，不能删除！"),
    ADMIN_DEPT_REMOVE_EXIST_DEPT_ERROR(1112, "这个部门下有下级部门，不能删除！"),
    ADMIN_USER_NOT_ROLE_ERROR(1113, "请先给用户设置角色"),
    ADMIN_USER_NOT_DEPT_ERROR(1114, "请先给用户设置部门"),
    ADMIN_SUPER_USER_DISABLED_ERROR(1116, "超级管理员用户不可禁用"),
    ADMIN_ROLE_NAME_EXIST_ERROR(1117, "角色名称已存在"),
    ADMIN_PHONE_CODE_ERROR(1118, "手机验证码出错！"),
    ADMIN_PHONE_REGISTER_ERROR(1119, "手机号已被注册！"),
    ADMIN_PHONE_VERIFY_ERROR(1120, "手机号校验出错！"),
    ADMIN_PHONE_EXIST_ERROR(1121, "手机号不存在！"),
    ADMIN_SMS_SEND_FREQUENCY_ERROR(1122, "短信发送频率过高，请稍候再试！"),
    ADMIN_SMS_SEND_ERROR(1123, "发送验证码失败，请稍候再试！"),
    ADMIN_MANAGE_UPDATE_ERROR(1124, "超级管理员账号需要到悟空个人中心修改手机号信息！"),
    ADMIN_USER_NOT_EXIST_ERROR(1125, "用户不存在！"),
    ADMIN_ACCOUNT_ERROR(1126, "账号不能和原账号相同！"),
    ADMIN_PASSWORD_ERROR(1127, "密码输入错误！"),
    ADMIN_USERNAME_EDIT_ERROR(1128, "用户名不可修改！"),
    ADMIN_USER_HIS_TABLE_ERROR(1129, "开通人数已达上限！"),
    ADMIN_PARENT_USER_ERROR1(1130, "直属上级不能为自己！"),
    ADMIN_PRODUCT_DATA_ERROR(1131, "产品不存在！"),
    ADMIN_MARKETING_DATA_ERROR(1132, "积分不足！"),
    ADMIN_USER_NEEDS_AT_LEAST_ONE_ROLE(1133, "用户至少需要一个角色！"),
    ADMIN_PASSWORD_EXPIRE_ERROR(1134, "密码验证已过期，请重新进行验证！"),
    ADMIN_PASSWORD_INVALID_ERROR(1135, "无效的密码！"),
    ADMIN_ROLE_NOT_EXIST_ERROR(1136, "请先关联角色！"),
    ADMIN_LANGUAGE_PACK_NAME_ERROR(1137, "语言包名称有误！"),
    ADMIN_LANGUAGE_PACK_EXIST_USER_ERROR(1138, "这个语言包有用户正在使用，不可删！"),
    ADMIN_LANGUAGE_PACK_CHOINESE_ERROR(1139, "缺少中文语言包！"),
    ADMIN_DEFAULT_ROLE_CANNOT_BE_DELETED(1140, "默认角色不能删除!"),
    ADMIN_USER_REAL_NAME_EXIST_ERROR(1141, "用户姓名重复!"),
    ADMIN_DEPT_NOT_EXIST_ERROR(1142, "部门已不存在！"),
    /**
     * 企业微信错误code从1200开始
     */
    ADMIN_CP_ERROR(1200, "%s!"),
    ADMIN_CP_DOES_NOT_EXIST(1201, "企业不存在,请先绑定应用!"),
    ;

    AdminCodeEnum(int code, String msg) {
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
