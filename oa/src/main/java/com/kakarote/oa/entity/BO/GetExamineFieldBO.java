package com.kakarote.oa.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetExamineFieldBO {

    Integer examineId;
    @ApiModelProperty("1详情 2 编辑")
    Integer isDetail;
}
