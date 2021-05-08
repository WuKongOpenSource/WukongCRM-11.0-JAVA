package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * crm自定义字段
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="crm自定义字段", description="crm自定义字段")
public class CrmFieldDataBO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "字段Id")
    private Integer fieldId;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "字段值")
    private String value;

    @ApiModelProperty(value = "batchId")
    private String batchId;

    @ApiModelProperty(value = "对应主表ID")
    private String typeId;

    @ApiModelProperty(value = "字段类型")
    private Integer type;

}
