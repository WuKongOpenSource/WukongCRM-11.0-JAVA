package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePageBO extends PageEntity {
    @ApiModelProperty("0 待填写 1 已填写")
    private Integer status;
    private String search;
    private Integer appraisalId;
}
