package com.kakarote.hrm.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetInterviewResultBO {
    @ApiModelProperty(value = "面试id")
    private Integer interviewId;

    @ApiModelProperty(value = "候选人id")
    private Integer candidateId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "面试情况 1面试未完成 2面试通过 3面试未通过 4 面试取消")
    private Integer result;

    @ApiModelProperty(value = "评价")
    private String evaluate;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
}
