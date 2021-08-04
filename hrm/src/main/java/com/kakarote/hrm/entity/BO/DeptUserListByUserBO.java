package com.kakarote.hrm.entity.BO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeptUserListByUserBO {

    public List<Integer> employeeIdList;

    private List<Integer> deptIdList;
}
