package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateCandidateStatusBO {

    @ApiModelProperty("候选人id数组")
    private List<Integer> candidateIds;

    @ApiModelProperty("1:恢复到新候选人,2:移动到初选通过,4:移动到面试通过,5:移动到已发offer,6:移动到待入职,7:淘汰候选人")
    private Integer status;


}
