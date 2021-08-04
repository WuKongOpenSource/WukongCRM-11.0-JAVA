package com.kakarote.hrm.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChangeSalaryRecordDetailVO extends SalaryRecordHeadVO{

    private Integer id;

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("试用期工资")
    private ChangeSalaryRecordVO proSalary;

    @ApiModelProperty(value = "试用期调整前工资")
    private String proBeforeSum;

    @ApiModelProperty(value = "试用期调整后工资")
    private String proAfterSum;

    @ApiModelProperty("转正后工资")
    private ChangeSalaryRecordVO salary;

    @ApiModelProperty(value = "正式调整前工资")
    private String beforeSum;

    @ApiModelProperty(value = "正式调整后工资")
    private String afterSum;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enableDate;

    @ApiModelProperty(value = "状态 0 未生效 1 已生效 2 已取消")
    private Integer status;

    @ApiModelProperty(value = "调薪原因 0 入职定薪 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他")
    private Integer changeReason;

    @ApiModelProperty("是否可以更新")
    private Boolean isUpdate;

    @ApiModelProperty("是否可以取消")
    private Boolean isCancel;

    @ApiModelProperty("是否可以删除")
    private Boolean isDelete;



}
