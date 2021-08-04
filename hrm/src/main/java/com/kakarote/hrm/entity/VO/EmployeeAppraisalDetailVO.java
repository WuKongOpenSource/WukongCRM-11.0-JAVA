package com.kakarote.hrm.entity.VO;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeSeg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class EmployeeAppraisalDetailVO {

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("考核人")
    private String employeeName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "考核结果id")
    private Integer levelId;

    @ApiModelProperty(value = "考核结果")
    private String levelName;

    @ApiModelProperty("满分")
    private BigDecimal fullScore;

    @ApiModelProperty(value = "考核状态 1 待填写 2 待目标确认 3 待评定 4 待结果确认 5 终止绩效")
    private Integer status;

    @ApiModelProperty(value = "绩效状态 0 未开启考核 1 绩效填写中 2 绩效评定中 3 结果确认中 4 归档")
    private Integer appraisalStatus;

    @ApiModelProperty("是否可以填写 0 否 1 是")
    private Integer isWrite;

    @ApiModelProperty("目标确认人")
    private List<SimpleHrmEmployeeVO> confirmorsList;

    @ApiModelProperty("结果评定人")
    private List<EvaluatorsVO> evaluatorsList;

    @ApiModelProperty("结果确认人")
    private List<SimpleHrmEmployeeVO> resultConfirmors;

    @ApiModelProperty("固定考核项")
    private List<HrmAchievementEmployeeSeg> fixedSegList;

    @ApiModelProperty("非固定考核项")
    private List<HrmAchievementEmployeeSeg> noFixedSegList;

    @ApiModelProperty("考评结果")
    private List<EvaluatoResultVO> evaluatoResultList;

    @ApiModelProperty("考核模板")
    private AchievementTableTempVO tableTemp;



    @Getter
    @Setter
    public static class EvaluatorsVO extends SimpleHrmEmployeeVO{

        @ApiModelProperty(value = "权重")
        private BigDecimal weight;
    }




}
