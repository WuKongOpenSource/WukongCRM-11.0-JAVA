package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryEmployeeAppraisalBO extends PageEntity {

    @ApiModelProperty("员工id")
    private Integer employeeId;
}
