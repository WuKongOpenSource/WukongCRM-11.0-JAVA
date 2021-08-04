package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ResultConfirmByAppraisalIdVO {
    
    @ApiModelProperty("绩效id")
    private Integer appraisalId;

    @ApiModelProperty("绩效名称")
    private String appraisalName;

    @ApiModelProperty("总数")
    private Integer totalNum;

    @ApiModelProperty("待确认数")
    private Integer waitingNum;

    @ApiModelProperty("考评总分数")
    private BigDecimal fullScore;

    @ApiModelProperty(value = "是否开启强制分布 1 是 0 否")
    private Integer isForce;

    @ApiModelProperty("是否在范围内 0 否 1 是")
    private Integer hasInRange;

    @ApiModelProperty("等级段详情")
    private List<LevelDetailInfosBean> levelDetailInfos;

    @Getter
    @Setter
    public static class LevelDetailInfosBean {

        @ApiModelProperty("等级id")
        private Integer levelId;

        @ApiModelProperty("等级名称")
        private String levelName;

        @ApiModelProperty(value = "最小分数")
        private BigDecimal minScore;

        @ApiModelProperty(value = "最大分数")
        private BigDecimal maxScore;

        @ApiModelProperty(value = "最小人数比例")
        private Integer minNum;

        @ApiModelProperty(value = "最大人数比例")
        private Integer maxNum;

        @ApiModelProperty("现状")
        private BigDecimal actualWeight;

        @ApiModelProperty("人数")
        private Integer actualNum;

        @ApiModelProperty("员工")
        private List<EmployeesBean> employees;


    }
    @Getter
    @Setter
    public static class EmployeesBean {

        @ApiModelProperty(value = "员工id")
        private Integer employeeId;

        @ApiModelProperty(value = "员工姓名")
        private String employeeName;

        @ApiModelProperty(value = "手机")
        private String mobile;

        @ApiModelProperty(value = "员工绩效id")
        private Integer employeeAppraisalId;

        @ApiModelProperty(value = "评分")
        private BigDecimal score;

        @ApiModelProperty(value = "考核结果")
        private Integer levelId;
        @ApiModelProperty(value = "员工部门名称")
        private String deptName;

        @ApiModelProperty(value = "员工岗位")
        private String post;


    }
}
