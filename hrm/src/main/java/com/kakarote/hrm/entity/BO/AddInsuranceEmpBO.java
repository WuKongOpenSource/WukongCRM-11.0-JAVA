package com.kakarote.hrm.entity.BO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddInsuranceEmpBO {

    private List<Integer> employeeIds;

    private Integer iRecordId;

}
