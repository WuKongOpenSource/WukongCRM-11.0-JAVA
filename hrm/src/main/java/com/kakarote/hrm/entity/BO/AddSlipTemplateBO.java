package com.kakarote.hrm.entity.BO;

import com.kakarote.hrm.entity.PO.HrmSalarySlipTemplateOption;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class AddSlipTemplateBO {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "模板名称")
    @NotEmpty(message = "模板名称不能为空")
    private String templateName;

    @ApiModelProperty(value = "是否隐藏空的工资项 0 不隐藏 1 隐藏")
    private Integer hideEmpty;

    @ApiModelProperty(value = "工资条模板项")
    private List<HrmSalarySlipTemplateOption> slipTemplateOption;

}
