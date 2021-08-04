package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddEmployeeFieldManageBO {
    @ApiModelProperty("员工工号")
    private String jobNumber;
    @ApiModelProperty(value = "候选人id")
    private Integer candidateId;
    @ApiModelProperty(value = "员工Id")
    private Integer employeeId;
    @ApiModelProperty(value = "入职状态 1 在职 2 待入职 ")
    private Integer entryStatus;
    @ApiModelProperty("员工个人字段列表")
    private List<EmployeeFieldBO> employeeFieldList;
    @ApiModelProperty("员工岗位字段列表")
    private List<EmployeeFieldBO> postFieldList;

    @Getter
    @Setter
    public static class EmployeeFieldBO {
        @ApiModelProperty(value = "主键ID")
        private Integer fieldId;

        @ApiModelProperty(value = "标签分组 * 1 员工个人信息 2 通讯信息 3 教育经历 4 工作经历 5 证书/证件 6 培训经历 7 联系人 11 岗位信息 12 离职信息 21 合同信息 31 工资卡信息 32 社保信息")
        private Integer labelGroup;

        @ApiModelProperty(value = "自定义字段英文标识")
        private String fieldName;

        @ApiModelProperty(value = "字段名称")
        private String name;

        @ApiModelProperty(value = "字段值")
        private Object fieldValue;

        @ApiModelProperty(value = "字段值描述")
        private String fieldValueDesc;
        @ApiModelProperty(value = "是否固定 1 是 0 否")
        private Integer isFixed;

        @ApiModelProperty(value = "字段类型")
        private Integer type;

    }
}
