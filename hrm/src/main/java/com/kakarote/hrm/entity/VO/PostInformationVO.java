package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmEmployeeQuitInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostInformationVO {

    @ApiModelProperty("岗位基本信息")
    private List<InformationFieldVO> information;

    @ApiModelProperty("离职信息")
    private HrmEmployeeQuitInfo employeeQuitInfo;
}
