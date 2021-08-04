package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AppraisalSurveyVO {


    private Integer appraisalId;

    @ApiModelProperty(value = "考核名称")
    private String appraisalName;

    @ApiModelProperty(value = "考核表模板id")
    private Integer tableId;

    private String appraisalTime;

    @ApiModelProperty("考核模板名称")
    private String tableName;

    @ApiModelProperty(value = "考核开始时间")
    private Date startTime;

    @ApiModelProperty(value = "考核结束时间")
    private Date endTime;

    @ApiModelProperty("等级分布")
    private List<ScoreLevelsBean> scoreLevels;

    @Getter
    @Setter
    public static class ScoreLevelsBean {

        @ApiModelProperty("等级id")
        private int levelId;
        @ApiModelProperty("等级名称")
        private String levelName;
        @ApiModelProperty("数量")
        private int num;
    }
}
