package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmEmployeeSalaryCard;
import com.kakarote.hrm.entity.PO.HrmEmployeeSocialSecurityInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalarySocialSecurityVO {

    @ApiModelProperty("工资卡信息")
    private HrmEmployeeSalaryCard salaryCard;

    @ApiModelProperty("社保信息")
    private HrmEmployeeSocialSecurityInfo socialSecurityInfo;
}
