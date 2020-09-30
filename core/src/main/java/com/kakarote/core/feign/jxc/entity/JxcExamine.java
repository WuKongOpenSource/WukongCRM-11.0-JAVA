package com.kakarote.core.feign.jxc.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author JiaS
 * @date 2020/9/7
 */
@Data
@ToString
@ApiModel(value = "JxcExamine对象", description = "保存审核信息")
public class JxcExamine {

    private Integer categoryType;

    @ApiModelProperty("审核类型")
    private Integer examineType;

    private JSONObject examineObj;

    @ApiModelProperty("负责人")
    private Long ownerUserId;
}
