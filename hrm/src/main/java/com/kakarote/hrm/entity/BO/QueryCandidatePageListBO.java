package com.kakarote.hrm.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class QueryCandidatePageListBO extends PageEntity {

    @ApiModelProperty("搜索")
    private String search;

    @ApiModelProperty(value = "职位")
    private Integer postId;

    @ApiModelProperty(value = "招聘负责人")
    private Integer ownerEmployeeId;

    @ApiModelProperty(value = "性别 1 男 2 女")
    private Integer sex;

    @ApiModelProperty(value = "最小年龄")
    private Integer minAge;

    @ApiModelProperty(value = "最大年龄")
    private Integer maxAge;

    @ApiModelProperty(value = "最小工作年限")
    private Integer minWorkTime;

    @ApiModelProperty(value = "最大工作年限")
    private Integer maxWorkTime;

    @ApiModelProperty(value = "学历 1小学 2初中 3高中 4大专 5本科 6硕士 7博士")
    private Integer education;

    @ApiModelProperty(value = "毕业院校")
    private String graduateSchool;

    @ApiModelProperty(value = "最近工作单位")
    private String latestWorkPlace;

    @ApiModelProperty(value = "招聘渠道")
    private Integer channelId;

    @ApiModelProperty(value = "面试官id")
    private Integer interviewEmployeeId;

    @ApiModelProperty(value = "面试时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<Date> interviewTime;

    @ApiModelProperty(value = "候选人状态")
    private Integer status;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<Date> createTime;

    @ApiModelProperty("招聘动态 3待入职 4 已入职")
    private Integer recruitSurvey;
}
