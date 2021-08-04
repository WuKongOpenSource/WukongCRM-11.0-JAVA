package com.kakarote.hrm.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.hrm.entity.VO.ChangeSalaryRecordVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SetChangeSalaryRecordBO {

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

    @ApiModelProperty(value = "调薪原因 0 入职定薪 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他")
    private Integer changeReason;

    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enableDate;

    @ApiModelProperty("调薪记录id")
    private Integer id;
}
