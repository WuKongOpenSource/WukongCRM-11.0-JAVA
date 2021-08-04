package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmSalarySlipOption;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuerySalarySlipListVO {

    private Integer id;

    private Integer year;

    private Integer month;

    private Integer readStatus;

    @ApiModelProperty("薪资项")
    private List<HrmSalarySlipOption> salarySlipOptionList;
}
