package com.kakarote.crm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("打印模板BO")
@Data
public class CrmPrintTemplateBO extends PageEntity {

    @ApiModelProperty("类型")
    private Integer type;

}
