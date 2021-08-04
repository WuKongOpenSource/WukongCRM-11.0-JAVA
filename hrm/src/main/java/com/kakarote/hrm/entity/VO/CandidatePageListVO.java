package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmRecruitCandidate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CandidatePageListVO extends HrmRecruitCandidate {

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("面试官")
    private String interviewEmployeeName;

    @ApiModelProperty("其他面试官")
    private String otherInterviewEmployeeName;
    @ApiModelProperty(value = "面试官id")
    private Integer interviewEmployeeId;

    @ApiModelProperty(value = "其他面试官")
    private String otherInterviewEmployeeIds;
    @ApiModelProperty("面试方式 1现场面试 2电话面试 3视频面试")
    private Integer interviewType;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "面试时间")
    private Date interviewTime;

    @ApiModelProperty(value = "招聘负责人")
    private String ownerEmployeeName;

    @ApiModelProperty("职位状态")
    private Integer postStatus;

    private Integer deptId;

    private String deptName;

    @ApiModelProperty(value = "面试结果 1面试未完成 2面试通过 3面试未通过 4 面试取消")
    private Integer interviewResult;
}
