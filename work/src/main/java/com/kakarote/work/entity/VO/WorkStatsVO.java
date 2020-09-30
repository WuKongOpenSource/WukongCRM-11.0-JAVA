package com.kakarote.work.entity.VO;

import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wyq
 */
@Data
@Accessors(chain = true)
@ApiModel("项目统计")
public class WorkStatsVO {
    @ApiModelProperty("项目任务列表统计")
    private List<WorkClassStatsVO> classStatistics;

    @ApiModelProperty("项目任务标签统计")
    private List<WorkLabelStatsVO> labelStatistics;

    @ApiModelProperty("项目成员任务统计")
    private List<WorkUserStatsVO> memberTaskStatistics;

    @ApiModelProperty("项目负责人")
    private List<SimpleUser> ownerList;

    @ApiModelProperty("项目任务统计")
    private WorkTaskStatsVO taskStatistics;

    @ApiModelProperty("项目成员")
    private List<SimpleUser> userList;
}
