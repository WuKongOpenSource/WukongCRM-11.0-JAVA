package com.kakarote.admin.entity.BO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeptUserListByHrmBO {

    private List<Long> userIdList;

    private List<Integer> deptIdList;
}
