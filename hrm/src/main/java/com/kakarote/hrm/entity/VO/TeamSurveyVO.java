package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 团队概况饼状图属性
 * @author hmb
 */
@Data
public class TeamSurveyVO {


    @ApiModelProperty("员工状态占比")
    private List<TeamSurveyPropertiesVO> statusAnalysis;

    @ApiModelProperty("男女比例占比")
    private List<TeamSurveyPropertiesVO> sexAnalysis;

    @ApiModelProperty("成员年龄占比")
    private List<TeamSurveyPropertiesVO> ageAnalysis;

    @ApiModelProperty("成员司龄占比")
    private List<TeamSurveyPropertiesVO> companyAgeAnalysis;
}
