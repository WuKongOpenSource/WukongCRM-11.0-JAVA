package com.kakarote.core.feign.crm.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JiaS
 * @date 2020/9/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="客户分析对象", description="客户分析表")
public class CustomerStats implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户数量")
    private Long customerNum;

    @ApiModelProperty(value = "成交状态 0 未成交 1 已成交")
    private Integer dealStatus;

    @ApiModelProperty(value = "负责人ID")
    private Long ownerUserId;

    @ApiModelProperty(value = "创建时间(年月日)")
    private String createDate;

    @ApiModelProperty(value = "成交时间(年月日)")
    private String dealDate;

}
