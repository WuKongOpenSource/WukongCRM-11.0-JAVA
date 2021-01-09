package com.kakarote.examine.entity.BO;

import com.kakarote.examine.entity.PO.ExamineFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ApiModel("当前审批人BO")
@Accessors(chain = true)
public class ExamineUserBO {

    @ApiModelProperty("当前审批人列表")
    private List<Long> userList;

    @ApiModelProperty("当前审批角色")
    private Integer roleId;

    @ApiModelProperty("审批类型")
    private Integer type;

    @ApiModelProperty("审批流程")
    private ExamineFlow examineFlow;
}
