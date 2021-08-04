package com.kakarote.hrm.entity.BO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetSlipRemarksBO {
    private List<Integer> ids;

    private String remarks;
}
