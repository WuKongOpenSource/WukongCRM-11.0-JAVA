package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmRecruitPost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruitPostVO extends HrmRecruitPost {


    @ApiModelProperty("已入职人数")
    private Integer hasEntryNum;

    @ApiModelProperty("招聘进度")
    private String recruitSchedule;

    @ApiModelProperty("职位类型名称")
    private String postTypeName;

    @ApiModelProperty(value = "负责人")
    private String ownerEmployeeName;

    @ApiModelProperty(value = "面试官")
    private String interviewEmployeeName;
}
