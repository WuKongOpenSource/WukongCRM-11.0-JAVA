package com.kakarote.hrm.entity.VO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuerySalaryArchivesByIdVO {

    private Integer employeeId;

    private List<ChangeSalaryOptionVO> salaryOptions;

    private String total;

}
