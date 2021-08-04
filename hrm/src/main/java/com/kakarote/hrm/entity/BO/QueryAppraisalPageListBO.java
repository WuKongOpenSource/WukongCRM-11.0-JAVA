package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryAppraisalPageListBO extends PageEntity {

    @ApiModelProperty("绩效状态 0 未开启考核 1 绩效填写中 2 绩效评定中 3 结果确认中 4 归档 5 进行中的绩效")
    private Integer status;
}
