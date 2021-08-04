package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QueryCleanCandidateBO {

    @ApiModelProperty("候选人状态")
    private List<Integer> status;

    @ApiModelProperty("天数")
    private Integer day;

}
