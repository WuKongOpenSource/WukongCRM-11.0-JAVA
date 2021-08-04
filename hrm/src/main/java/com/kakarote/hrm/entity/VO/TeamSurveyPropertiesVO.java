package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 团队概况饼状图属性
 * @author hmb
 */
@Data
@Builder
public class TeamSurveyPropertiesVO {


    @ApiModelProperty("分类")
    private String name;

    @ApiModelProperty("比例")
    private String proportion;

    @ApiModelProperty("数量")
    private Long count;
}
