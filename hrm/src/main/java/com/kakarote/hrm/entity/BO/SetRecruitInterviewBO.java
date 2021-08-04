package com.kakarote.hrm.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SetRecruitInterviewBO {

    @ApiModelProperty(value = "面试id")
    private Integer interviewId;

    @ApiModelProperty(value = "候选人id(单个需要)")
    private Integer candidateId;

    @ApiModelProperty(value = "候选人id列表(批量需要)")
    private List<Integer> candidateIds;

    @ApiModelProperty(value = "面试方式 1现场面试 2电话面试 3视频面试")
    private Integer type;

    @ApiModelProperty(value = "面试官id")
    private Integer interviewEmployeeId;

    @ApiModelProperty(value = "其他面试官")
    private List<Integer> otherInterviewEmployeeIds;

    @ApiModelProperty(value = "面试时间")
    private Date interviewTime;

    @ApiModelProperty(value = "面试地址")
    private String address;
}
