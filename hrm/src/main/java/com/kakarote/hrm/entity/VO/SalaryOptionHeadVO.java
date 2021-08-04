package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SalaryOptionHeadVO {

    @ApiModelProperty("薪资项code")
    private Integer code;

    @ApiModelProperty("薪资项名称")
    private String name;

    @ApiModelProperty("薪资项名称")
    private Integer isFixed;

    @ApiModelProperty("值")
    private String value;

    public SalaryOptionHeadVO(Integer code, String name, Integer isFixed) {
        this.code = code;
        this.name = name;
        this.isFixed = isFixed;
    }
}
