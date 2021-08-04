package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EliminateCandidateBO {

    @ApiModelProperty("候选人id")
    private List<Integer> candidateIds;

    @ApiModelProperty("淘汰原因")
    private String eliminate;

    @ApiModelProperty("备注")
    private String remarks;
}
