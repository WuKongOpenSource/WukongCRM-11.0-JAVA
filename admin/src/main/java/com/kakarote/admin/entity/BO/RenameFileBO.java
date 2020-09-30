package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenameFileBO {

    @ApiModelProperty(value = "附件id")
    private Long fileId;

    @ApiModelProperty(value = "附件名称")
    private String name;
}
