package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateInsuranceProjectBO {

    @ApiModelProperty("员工每月社保记录id")
    private Integer iEmpRecordId;

    @ApiModelProperty("员工每月社保记录id数组(批量操作)")
    private List<Integer> iEmpRecordIds;

    @ApiModelProperty("参保方案id")
    private Integer schemeId;

    @ApiModelProperty("修改后的参保项目")
    private List<Project> projectList;

    @Getter
    @Setter
    public static class Project {
        @ApiModelProperty("项目id")
        private Integer projectId;

        @ApiModelProperty(value = "默认基数")
        private BigDecimal defaultAmount;

        @ApiModelProperty(value = "公司缴纳金额")
        private BigDecimal corporateAmount;

        @ApiModelProperty(value = "个人缴纳金额")
        private BigDecimal personalAmount;

    }
}
