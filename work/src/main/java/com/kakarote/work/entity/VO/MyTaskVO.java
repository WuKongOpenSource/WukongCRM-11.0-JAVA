package com.kakarote.work.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wyq
 */
@Data
@ApiModel("工作台任务信息")
@Accessors(chain = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MyTaskVO {

    @ApiModelProperty("列表名")
    private String title;

    @ApiModelProperty("工作台展示 0收件箱 1今天要做，2下一步要做，3以后要做")
    private Integer isTop;

    @ApiModelProperty("任务数量")
    private Integer count;

    @ApiModelProperty("任务列表")
    private List<TaskInfoVO> list;
}
