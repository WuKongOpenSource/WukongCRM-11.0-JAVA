package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmAchievementSeg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AchievementTableTempVO {

    @ApiModelProperty("模板id")
    private Integer tableId;

    @ApiModelProperty(value = "考核名称")
    private String tableName;

    @ApiModelProperty(value = "1 OKR模板 2 KPI模板")
    private Integer type;

    @ApiModelProperty(value = "考核表描述")
    private String description;

    @ApiModelProperty("是否员工填写权重 0 否 1 是")
    private Integer isEmpWeight;


    @ApiModelProperty("固定考核项模板")
    private List<HrmAchievementSeg> fixedSegListTemp;

    @ApiModelProperty("非固定考核项模板")
    private List<HrmAchievementSeg> noFixedSegListTemp;


}
