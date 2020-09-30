package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wyq
 */
@Data
@ApiModel("移动工作台任务更新信息")
public class UpdateTaskTopBo {
    @ApiModelProperty(value = "原列表id")
    @NotNull
    private Integer fromTopId;

    @ApiModelProperty(value = "原列表任务id")
    private List<Integer> fromList;

    @ApiModelProperty(value = "新列表id")
    @NotNull
    private Integer toTopId;

    @ApiModelProperty(value = "新列表任务id")
    private List<Integer> toList;
}
