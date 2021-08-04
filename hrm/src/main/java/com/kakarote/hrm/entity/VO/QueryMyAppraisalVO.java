package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class QueryMyAppraisalVO {

    @ApiModelProperty("员工绩效id")
    private Integer employeeAppraisalId;

    @ApiModelProperty("绩效id")
    private Integer appraisalId;

    @ApiModelProperty("绩效名称")
    private String appraisalName;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("考核时间")
    private String appraisalTime;

    @ApiModelProperty("跟进人")
    private String followUpEmployeeName;

    @ApiModelProperty("分数")
    private BigDecimal score;

    @ApiModelProperty("考核状态")
    private Integer status;

    @ApiModelProperty("考评结果")
    private String levelName;
    @ApiModelProperty("是否展示草稿 0 否 1 是")
    private Integer isDraft;


}
