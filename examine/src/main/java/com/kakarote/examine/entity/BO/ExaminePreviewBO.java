package com.kakarote.examine.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author JiaS
 * @date 2020/12/15
 */
@Data
@ApiModel("预览审批流程BO")
public class ExaminePreviewBO {

    @ApiModelProperty(value = "0 OA 1 合同 2 回款 3发票 4薪资 5 采购审核 6采购退货审核 7销售审核 8 销售退货审核 9付款单审核10 回款单审核11盘点审核12调拨审核")
    private Integer label;

    @ApiModelProperty(value = "审批数据")
    private Map<String, Object> dataMap;

    @ApiModelProperty(value = "审批记录id")
    private Integer recordId;

    @ApiModelProperty(value = "审批id")
    private Long examineId;

    @ApiModelProperty(value = "是否初始审批 0是 1否")
    private Integer status;

    @ApiModelProperty(value = "审批发起人")
    private Long ownerUserId;
}
