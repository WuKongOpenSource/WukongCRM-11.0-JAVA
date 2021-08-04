package com.kakarote.hrm.entity.BO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kakarote.hrm.entity.PO.HrmAchievementSeg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class SetAchievementTableBO {

    @TableId(value = "table_id", type = IdType.AUTO)
    private Integer tableId;

    @ApiModelProperty(value = "考核名称")
    @NotBlank(message = "模板名称不能为空")
    private String tableName;

    @ApiModelProperty(value = "1 OKR模板 2 KPI模板")
    private Integer type;

    @ApiModelProperty(value = "考核表描述")
    private String description;

    @ApiModelProperty("是否员工填写权重 0 否 1 是")
    private Integer isEmpWeight;

    @ApiModelProperty(value = " 1 使用 0 删除")
    private Integer status;

    @ApiModelProperty("固定考核项")
    private List<HrmAchievementSeg> fixedSegList;

    @ApiModelProperty("非固定考核项")
    private List<HrmAchievementSeg> noFixedSegList;


}
