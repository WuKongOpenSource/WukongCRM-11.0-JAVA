package com.kakarote.hrm.entity.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelTemplateOption {
    private String name;

    private Integer code;

    private Integer parentCode;

    private List<ExcelTemplateOption> optionList;
}
