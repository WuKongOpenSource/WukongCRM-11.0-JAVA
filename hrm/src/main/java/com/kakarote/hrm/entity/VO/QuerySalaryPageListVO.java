package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class QuerySalaryPageListVO {

    @ApiModelProperty("员工薪资记录id")
    private Integer sEmpRecordId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "工号")
    private String jobNumber;
    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "职位")
    private String post;

    @ApiModelProperty(value = "实际计薪时长")
    private BigDecimal actualWorkDay;

    @ApiModelProperty(value = "月计薪时长")
    private BigDecimal needWorkDay;

    @ApiModelProperty("薪资项值")
    private List<SalaryValue> salary;

    private Integer isDel;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SalaryValue{
        @ApiModelProperty("薪资项id")
        private Integer id;
        @ApiModelProperty("薪资项code")
        private Integer code;
        @ApiModelProperty("值")
        private String value;
        @ApiModelProperty("是否固定")
        private Integer isFixed;
        @ApiModelProperty("薪资项名称")
        private String name;
    }
}
