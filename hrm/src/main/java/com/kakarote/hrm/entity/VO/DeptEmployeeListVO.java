package com.kakarote.hrm.entity.VO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeptEmployeeListVO {

    private List<DeptEmployeeVO> deptList;

    private List<SimpleHrmEmployeeVO> employeeList;
}
