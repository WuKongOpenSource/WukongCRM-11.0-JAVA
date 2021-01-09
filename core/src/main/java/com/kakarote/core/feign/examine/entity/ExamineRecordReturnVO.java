package com.kakarote.core.feign.examine.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("新增审核记录返回VO")
@NoArgsConstructor
public class ExamineRecordReturnVO implements Serializable {

    @ApiModelProperty("审核记录ID")
    private Integer recordId;

    @ApiModelProperty("审核状态")
    private Integer examineStatus;

    @ApiModelProperty("审核历史Id")
    private List<Integer> examineLogIds;

    @ApiModelProperty("审核用户Id")
    private List<Long> examineUserIds;


    public ExamineRecordReturnVO(Integer recordId, Integer examineStatus, List<Integer> examineLogIds) {
        this.recordId = recordId;
        this.examineStatus = examineStatus;
        this.examineLogIds = examineLogIds;
    }
}
