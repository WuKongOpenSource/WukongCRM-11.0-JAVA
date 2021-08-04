package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuerySendDetailListBO extends PageEntity {

    private Integer id;

    private String search;

    @ApiModelProperty("查看状态 0 未查看 1 已查看")
    private Integer readStatus;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("备注")
    private Integer deptId;
}
