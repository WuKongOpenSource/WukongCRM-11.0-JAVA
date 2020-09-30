package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmQueryExamineStepBO {

    @ApiModelProperty("审核对象id")
    private Integer id;

    @ApiModelProperty("审核类型 1 合同 2 回款 3 发票 4 薪资")
    private Integer categoryType;
}
