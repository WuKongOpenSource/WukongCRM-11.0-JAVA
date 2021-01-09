package com.kakarote.examine.entity.VO;

import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/18
 */
@Data
public class ExamineRecordVO {

    @ApiModelProperty(value = "业务类型")
    private Integer label;

    @ApiModelProperty(value = "是否有撤回权限  1 是 0否")
    private Integer isRecheck;

    @ApiModelProperty(value = "是否有审批权限  1 是 0否")
    private Integer isCheck;

    @ApiModelProperty(value = "审批创建人")
    private SimpleUser createUser;

    @ApiModelProperty(value = "审批流程")
    List<ExamineFlowDataVO> examineFlowList;


}
