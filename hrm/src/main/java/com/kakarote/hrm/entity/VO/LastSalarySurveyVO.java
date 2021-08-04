package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class LastSalarySurveyVO {

    @ApiModelProperty("上月薪资记录id")
    private Integer sRecordId;

    @ApiModelProperty("计薪人员")
    private Integer total;

    @ApiModelProperty("实发工资")
    private BigDecimal totalSalary;

    @ApiModelProperty("部门占比")
    private List<DeptProportion> deptProportionList;

    @Getter
    @Setter
    public static class DeptProportion{

        @ApiModelProperty("部门id")
        private Integer deptId;

        @ApiModelProperty("部门名称")
        private String deptName;

        @ApiModelProperty("占比")
        private BigDecimal proportion;

        @ApiModelProperty("部门薪资")
        private BigDecimal totalSalary;
    }
}
