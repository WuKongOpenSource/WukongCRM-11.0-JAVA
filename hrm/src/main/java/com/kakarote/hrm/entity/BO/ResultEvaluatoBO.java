package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ResultEvaluatoBO {

    @ApiModelProperty(value = "员工端考核id")
    private Integer employeeAppraisalId;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "考核等级")
    private Integer levelId;

    @ApiModelProperty(value = "评语")
    private String evaluate;

    @ApiModelProperty("状态 1 提交 2 驳回目标 3 驳回评定")
    private Integer status;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    @ApiModelProperty("考核项评分")
    private List<ResultEvaluatoSegBO> resultEvaluatoSegBOList;

    @Getter
    @Setter
    public static class ResultEvaluatoSegBO{
        @ApiModelProperty(value = "考核项id")
        private Integer segId;

        @ApiModelProperty(value = "评分")
        private BigDecimal score;

        @ApiModelProperty(value = "评语")
        private String evaluate;
    }
}
