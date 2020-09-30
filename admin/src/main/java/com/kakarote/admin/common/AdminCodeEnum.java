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
    ADMIN_PARENT_USER_NOTNULL_ERROR(1105, "启用重新开始编号需要有日期编号规则"),
    ADMIN_PASSWORD_INTENSITY_ERROR(1106, "密码必须由 6-20位字母、数字组成"),
    ADMIN_USER_EXIST_ERROR(1107, "用户已存在！"),
    ADMIN_PARENT_USER_ERROR(1109, "这个用户的下属不能设置为直属上级！"),
    ADMIN_PARENT_DEPT_ERROR(1110, "这个部门的下属不能设置为直属部门！"),
    ADMIN_DEPT_REMOVE_EXIST_USER_ERROR(1111, "这个部门下有员工，不能删除！"),
    ADMIN_DEPT_REMOVE_EXIST_DEPT_ERROR(1112, "这个部门下有下级部门，不能删除！"),
    ADMIN_USER_NOT_ROLE_ERROR(1113, "请先给用户设置角色"),
    ADMIN_USER_NOT_DEPT_ERROR(1114, "请先给用户设置部门"),
    ADMIN_USER_NOT_PARENT_ERROR(1115, "请先给用户设置直属上级"),
    ADMIN_SUPER_USER_DISABLED_ERROR(1116, "超级管理员用户不可禁用"),
    ADMIN_ROLE_NAME_EXIST_ERROR(1117, "角色名称已存在"),
    ADMIN_PHONE_CODE_ERROR(1118, "手机验证码出错！"),
    ADMIN_PHONE_REGISTER_ERROR(1119, "手机号已被注册！"),
    ADMIN_USER_NOT_EXIST_ERROR(1125, "用户不存在！"),
    ADMIN_ACCOUNT_ERROR(1126, "账号不能和原账号相同！"),
    ADMIN_PASSWORD_ERROR(1127, "密码输入错误！"),
    ADMIN_USERNAME_EDIT_ERROR(1128, "用户名不可修改！"),
    ADMIN_PARENT_USER_ERROR1(1130, "直属上级不能为自己！"),
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
