package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDeleteByBatchIdBO {

    private String batchId;

    @ApiModelProperty("1 附件 2 图片")
    private Integer type;
}
