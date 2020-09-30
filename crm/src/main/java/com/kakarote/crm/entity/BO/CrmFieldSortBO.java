package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangzhiwei
 */
@Data
@ToString
@ApiModel(value = "CrmFieldSort字段调整对象", description = "字段排序表")
public class CrmFieldSortBO {

    @ApiModelProperty(value = "不隐藏的ID")
    private List<Integer> noHideIds;

    @ApiModelProperty(value = "隐藏的ID")
    private List<Integer> hideIds;

    @NotNull
    @ApiModelProperty(value = "label", required = true)
    private Integer label;
}
