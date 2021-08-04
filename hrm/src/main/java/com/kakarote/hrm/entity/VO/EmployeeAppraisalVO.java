package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ApiModel("员工绩效详情对象")
public class EmployeeAppraisalVO {

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("员工绩效id")
    private String employeeAppraisalId;

    @ApiModelProperty("分数")
    private BigDecimal score;
//    private String score;

    @ApiModelProperty("考核状态 1 待填写 2 待目标确认 3 待评定 4 待结果确认 5 终止绩效 6 考核完成")
    private Integer status;

    @ApiModelProperty("考核名称")
    private String appraisalName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "等级")
    private String resultLevel;

    @ApiModelProperty(value = "考评时间")
    private String appraisalTime;

}
