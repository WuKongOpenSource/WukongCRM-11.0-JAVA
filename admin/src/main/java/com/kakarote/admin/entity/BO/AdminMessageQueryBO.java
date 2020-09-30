package com.kakarote.admin.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("消息列表查询对象")
public class AdminMessageQueryBO extends PageEntity {

    @ApiModelProperty("是否已读")
    private Integer isRead;

    @ApiModelProperty("label")
    private Integer label;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("type")
    private Integer type;
}
