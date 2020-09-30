package com.kakarote.crm.entity.BO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrmSaveExamineBO {

    @TableId(value = "examine_id", type = IdType.AUTO)
    private Integer examineId;

    @ApiModelProperty(value = "1 合同 2 回款 3发票 4薪资 5 采购审核 6采购退货审核 7销售审核 8 销售退货审核 9付款单审核10 回款单审核11盘点审核12调拨审核")
    private Integer categoryType;

    @ApiModelProperty(value = "审核类型 1 固定审批 2 授权审批")
    private Integer examineType;

    @ApiModelProperty(value = "审批流名称")
    private String name;

    @ApiModelProperty(value = "流程说明")
    private String remarks;

    @ApiModelProperty(value = "部门ID")
    private List<Integer> deptIds;

    @ApiModelProperty(value = "员工Id")
    private List<Long> userIds;

    @ApiModelProperty("步骤")
    private List<Step> step;


    @Getter
    @Setter
    public static class Step{
        private Integer stepType;
        private List<Long> checkUserId;
    }


}
