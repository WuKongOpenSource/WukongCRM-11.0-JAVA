package com.kakarote.hrm.entity.BO;

import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitExamineBO {

    @ApiModelProperty("薪资记录id")
    private Integer sRecordId;

    @ApiModelProperty("审核人id")
    private Long checkUserId;

    private Integer examineRecordId;

    private Integer checkStatus;

    @ApiModelProperty("审批数据")
    private ExamineRecordSaveBO examineFlowData;
}
