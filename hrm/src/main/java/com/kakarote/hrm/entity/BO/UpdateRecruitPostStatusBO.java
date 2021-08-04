package com.kakarote.hrm.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRecruitPostStatusBO {

    @ApiModelProperty(value = "职位id")
    private Integer postId;

    @ApiModelProperty(value = "0 停止  1 启用")
    private Integer status;

    @ApiModelProperty(value = "停用原因")
    private String reason;
}
