package com.kakarote.examine.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("审批条件保存BO")
public class ExamineConditionSaveBO {

    @ApiModelProperty("排序，从小到大")
    private Integer sort;

    @ApiModelProperty("条件名称")
    private String conditionName;

    @ApiModelProperty("审批流程")
    private List<ExamineDataSaveBO> examineDataList;

    @ApiModelProperty("审批字段信息，可以为空数组")
    private List<ExamineConditionDataSaveBO> conditionDataList;
}
