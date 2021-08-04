package com.kakarote.hrm.common;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.ResultCode;

/**
 * @author huangmingbo
 * 管理后台响应错误代码枚举类
 */

public enum HrmCodeEnum implements ResultCode {
    //人力资源错误code
    IDENTITY_INFORMATION_IS_ILLEGAL(6001,"%s:身份证不合法"),
    CUSTOM_FORM_NAME_CANNOT_BE_REPEATED(6002,"自定义表单名称不能重复"),
    CUSTOM_FORM_NAME_DUPLICATES_SYSTEM_FIELD(6003,"[%s]自定义表单名称与系统字段重复，请使用其他字段"),
    THE_EMPLOYEE_HAS_ALREADY_HANDLED_THE_RESIGNATION(6004,"该员工已经办理了离职"),
    SOCIAL_SECURITY_SCHEMES_ARE_USED_BY_EMPLOYEES(6005,"社保方案有员工使用，不能删除"),
    CAN_ONLY_DELETE_PERFORMANCES_THAT_HAVE_NOT_BEEN_EVALUATED(6006,"只能删除未开启考核或终止的绩效"),
    YOU_ARE_NOT_THE_CURRENT_REVIEWER(6007,"你不是当前审核人"),
    RESULTS_ASSESSMENT_NOT_COMPLETED(6008,"结果评定未完成,不能结果确认"),
    CAN_ONLY_MODIFY_SALARY_AND_INCOME_TAX(6009,"只能修改工资薪金所得税"),
    THERE_ARE_EMPLOYEES_UNDER_THE_DEPARTMENT(6010,"部门下有员工,不能删除"),
    THIS_MONTH_S_SALARY_HAS_BEEN_GENERATED(6011,"本月薪资已生成"),
    SOCIAL_SECURITY_DATA_IS_NOT_GENERATED_THIS_MONTH(6012,"社保数据未生成"),
    JOB_NUMBER_EXISTED(6013,"%s工号已存在"),
    NO_INITIAL_CONFIGURATION(6014,"没有初始化配置"),
    FIELD_ALREADY_EXISTS(6015,"%s已存在"),
    PHONE_NUMBER_ALREADY_EXISTS(6016,"%s:手机号已存在"),
    THERE_ARE_SUB_DEPARTMENTS_THAT_CANNOT_BE_DELETED(6017,"部门下有子部门,不能删除"),
    CAN_T_ARCHIVE(6018,"有员工的考核状态不是考核完成或考核终止,请处理后归档"),
    LAST_RATING_IS_EMPTY(6019,"您是第一个评定人"),
    CAN_T_MODIFY_PROGRESS(6020,"接过确认或终止不能修改进度"),
    SALARY_GROUP_NOT_CONFIG(6021,"薪资组未配置"),
    INSURANCE_NOT_CONFIG(6022,"社保方案未配置"),
    THE_SALARY_GROUP_ALREADY_HAS_SELECTED_EMPLOYEE(6023,"薪资组已存在选择员工"),
    THE_SALARY_GROUP_ALREADY_HAS_SELECTED_DEPT(6024,"薪资组已存在选择部门"),
    SALARY_NEEDS_EXAMINE(6025,"薪资需要审核才能添加下月薪资"),
    UNABLE_TO_MATCH_HRM_EMPLOYEES(6026,"匹配不到人力资源员工"),
    SALARY_CANNOT_BE_DELETED(6027,"只有一个月薪资记录,不能删除"),
    ATTENDANCE_DATA_ERROR(6028,"考勤数据错误"),
    INSURANCE_CANNOT_BE_DELETED(6029,"只有一个月社保记录,不能删除"),
    CAN_T_PASS(6030,"强制分布已开启，超过规定比例不能确认通过"),
    NO_EXAMINER(6031,"没有考核人员,请确认考核人员是否删除"),
    PARENT_DOES_NOT_EXIST(6032,"员工%s上级不存在,无法“%s”"),
    DEPT_DOES_NOT_EXIST(6033,"员工%s所在部门不存在,无法“%s”"),
    DEPT_MAIN_EMPLOYEE_DOES_NOT_EXIST(6034,"员工%s所在部门没有负责人,无法“%s”"),
    PARENT_MAIN_EMPLOYEE_DEPT_DOES_NOT_EXIST(6035,"%s员工所在上级部门没有负责人,无法“%s”"),
    DESIGNATION_EMPLOYEE_DEPT_DOES_NOT_EXIST(6036,"找不到员工%s,无法“%s”"),
    RESULT_CONFIRM_EMPLOYEE_NO_EXIST(6037,"员工%s不存在,不能开启结果确认!"),
    DEPT_CODE_ALREADY_EXISTS(6038,"部门编码已存在"),
    PHONE_NUMBER_ERROR(6039,"%s:手机号格式错误"),
    DATA_IS_DUPLICATED_WITH_MULTIPLE_UNIQUE_FIELDS(6040,"数据与多条唯一性字段重复"),
    REQUIRED_FIELDS_ARE_NOT_FILLED(6041,"必填字段未填写"),
    ADDITIONAL_DEDUCTION_DATA_ERROR(6042,"附加扣除项数据错误"),
    CUMULATIVE_TAX_OF_LAST_MONTH_DATA_ERROR(6043,"截止上月个税累计数据错误"),
    DEFAULT_TEMPLATE_CANNOT_BE_DELETED(6044,"默认模板不能删除!"),
    EXIST_CHANGE_RECORD(6045,"员工当前存在正在进行中的调薪任务，无法再次新增调薪任务!"),
    CHANGE_SALARY_NOT_FIX_SALARY(6046,"已调薪员工无法定薪"),
    TOP_LEVEL_DEPARTMENT_CANNOT_BE_DELETED(6047,"顶级部门不能删除"),
    YOU_HAVE_NO_EMPLOYEES_IN_HUMAN_RESOURCES(6048,"您在人力资源没有员工"),
    NO_INTERVIEW_ARRANGEMENT(6049,"暂无面试安排"),
    THE_FIELD_NUM_RESTRICT_ERROR(6050,"自定义表单限制的数值格式错误"),
    THE_FIELD_DETAIL_TABLE_FORMAT_ERROR(6051,"请设置表格内的具体字段！"),
    HRM_FIELD_NUM_ERROR(6052, "每个模块最多存在"+ Const.QUERY_MAX_SIZE +"个字段"),
    ;


    HrmCodeEnum(int code, String msg) {
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
