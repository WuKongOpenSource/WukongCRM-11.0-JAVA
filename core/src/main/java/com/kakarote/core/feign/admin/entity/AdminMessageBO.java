package com.kakarote.core.feign.admin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@ApiModel("消息业务对象")
public class AdminMessageBO {

    @ApiModelProperty("消息类型")
    private Integer messageType;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("关联业务主键ID")
    private Integer typeId;

    @ApiModelProperty("发送人")
    private Long userId;

    @ApiModelProperty("接收人列表")
    private List<Long> ids;

    public void setIds(List<Long> ids) {
        HashSet<Long> hashSet = new HashSet<>(ids);
        this.ids = new ArrayList<>(hashSet);
    }
}
