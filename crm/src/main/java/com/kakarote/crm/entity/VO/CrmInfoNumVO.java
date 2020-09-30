package com.kakarote.crm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("crm详情页数量VO")
public class CrmInfoNumVO {

    @ApiModelProperty("商机数量")
    private Integer businessCount;

    @ApiModelProperty("呼叫记录数量")
    private Integer callRecordCount;

    @ApiModelProperty("联系人数量")
    private Integer contactCount;

    @ApiModelProperty("合同数量")
    private Integer contractCount;

    @ApiModelProperty("文件数量")
    private Integer fileCount;

    @ApiModelProperty("发票数量")
    private Integer invoiceCount;

    @ApiModelProperty("团队成员数量")
    private Integer memberCount;

    @ApiModelProperty("回款数量")
    private Integer receivablesCount;

    @ApiModelProperty("回访数量")
    private Integer returnVisitCount;

    @ApiModelProperty("商品数量")
    private Integer productCount;
}
