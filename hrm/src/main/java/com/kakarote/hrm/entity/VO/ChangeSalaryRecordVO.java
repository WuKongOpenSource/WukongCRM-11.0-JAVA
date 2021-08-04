package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ChangeSalaryRecordVO {
    @ApiModelProperty("转正后工资")
    private List<ChangeSalaryOptionVO> oldSalary;

    @ApiModelProperty("转正后工资")
    private List<ChangeSalaryOptionVO> newSalary;
}
