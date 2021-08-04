package com.kakarote.hrm.entity.BO;

import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetFixSalaryRecordBO {

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("试用期工资")
    private List<ChangeSalaryOptionVO> proSalary;

    @ApiModelProperty("试用期总计")
    private String proSum;

    @ApiModelProperty("转正后工资")
    private List<ChangeSalaryOptionVO> salary;

    @ApiModelProperty("转正后总计")
    private String sum;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("定薪记录id")
    private Integer id;
}
