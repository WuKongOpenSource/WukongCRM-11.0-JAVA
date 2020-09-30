package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("场景设置BO")
public class CrmSceneConfigBO {

    @ApiModelProperty("正常展示ids")
    private List<Integer> noHideIds;

    @ApiModelProperty("隐藏ids")
    private List<Integer> hideIds;

    @ApiModelProperty("类型")
    private Integer type;
}
