package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmAchievementAppraisal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AppraisalPageListVO extends HrmAchievementAppraisal {

    @ApiModelProperty("考核模板名称")
    private String tableName;

    @ApiModelProperty("考核范围")
    private String appraisalRange;

    @ApiModelProperty("统计")
    private Map<Integer,Long> statistics;

    @ApiModelProperty("是否可以归档 0 否 1 是")
    private Integer isArchive;


}
