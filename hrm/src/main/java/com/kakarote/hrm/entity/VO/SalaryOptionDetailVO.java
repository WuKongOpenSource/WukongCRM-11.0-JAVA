package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
public class SalaryOptionDetailVO {

    @ApiModelProperty("薪资模板项")
    private List<SalaryOptionVO> templateOption;

    @ApiModelProperty("开启模板项")
    private List<SalaryOptionVO> openOption;
}
