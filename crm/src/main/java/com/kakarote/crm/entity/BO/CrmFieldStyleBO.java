package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangzhiwei
 * 字段宽度BO
 */
@Data
@ToString
@ApiModel(value="CrmFieldStyle字段宽度对象", description="字段排序表")
public class CrmFieldStyleBO {
    @ApiModelProperty(value = "主键ID")
    private Integer id;
    @ApiModelProperty(value = "宽度")
    private Integer width;
    @ApiModelProperty(value = "label")
    private Integer label;
    @ApiModelProperty(value = "公海id")
    private Integer poolId;
    private String field;
}
