package com.kakarote.examine.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JiaS
 * @date 2020/12/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExaminePageBO extends PageEntity {
    @ApiModelProperty(value = "0 OA 1 合同 2 回款 3发票 4薪资 5 采购审核 6采购退货审核 7销售审核 8 销售退货审核 9付款单审核10 回款单审核11盘点审核12调拨审核")
    private Integer label;

    private Boolean isPart;

    private Integer status;

    private Integer categoryId;

}
