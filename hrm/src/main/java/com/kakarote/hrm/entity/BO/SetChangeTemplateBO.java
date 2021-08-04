package com.kakarote.hrm.entity.BO;

import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetChangeTemplateBO {

    private Integer id;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty("定薪项")
    private List<ChangeSalaryOptionVO> value;

}
