package com.kakarote.crm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 字段排序表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
@Data
@Accessors(chain = true)
@ApiModel(value="CrmFieldSort列表对象", description="字段排序表")
public class CrmFieldSortVO implements Serializable {

    private static final long serialVersionUID=1L;
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "字段ID")
    private Integer fieldId;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String formType;

    @ApiModelProperty(value = "展示名称")
    private String name;

    @ApiModelProperty(value = "字段类型")
    private Integer type;

    @ApiModelProperty(value = "字段宽度")
    private Integer width;



}
