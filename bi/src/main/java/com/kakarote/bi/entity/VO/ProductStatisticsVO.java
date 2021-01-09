package com.kakarote.bi.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("产品销售情况统计VO")
public class ProductStatisticsVO {

    @ApiModelProperty("产品ID")
    private Integer productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品分类名称")
    private String categoryName;

    @ApiModelProperty("数量合计")
    private Integer num;

    @ApiModelProperty("合同数")
    private Integer contractNum;

    @ApiModelProperty("订单产品小计")
    private BigDecimal total;

    @ApiModelProperty("数量")
    private Integer count;
}
