package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeFieldManageVO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "入职状态 1 在职 2 待入职 ")
    private Integer entryStatus;

    @ApiModelProperty(value = "字段id")
    private Integer fieldId;

    @ApiModelProperty(value = "字段标识")
    private String fieldName;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "标签 1 个人信息 2 岗位信息 3 合同 4 工资社保")
    private Integer label;

    @ApiModelProperty(value = "标签分组 * 1 员工个人信息 2 通讯信息 3 教育经历 4 工作经历 5 证书/证件 6 培训经历 7 联系人 11 岗位信息 12 离职信息 21 合同信息 31 工资卡信息 32 社保信息")
    private Integer labelGroup;

    @ApiModelProperty(value = "是否管理员可见 0 否 1 是  2 禁用否 3 禁用是")
    private Integer isManageVisible;
}
