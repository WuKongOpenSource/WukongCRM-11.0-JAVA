package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@ApiModel("字段验证对象")
public class CrmFieldVerifyBO {

    @ApiModelProperty("字段ID")
    private Integer fieldId;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("batchId")
    private String batchId;

    @ApiModelProperty("状态")
    private Integer status = 0;

    @ApiModelProperty("负责人")
    private String ownerUserName;

    @ApiModelProperty("公海名称")
    private List<String> poolNames;
}
