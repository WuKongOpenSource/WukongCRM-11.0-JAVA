package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecruitSurveyCountVO {

    @ApiModelProperty("正在招聘职位")
    Integer postCount;

    @ApiModelProperty("评选中")
    Integer chooseCount;

    @ApiModelProperty("待入职")
    Integer pendingEntryCount;

    @ApiModelProperty("已入职")
    Integer haveJoinedCount;
}
