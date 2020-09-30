package com.kakarote.oa.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExaminePageBO extends PageEntity {

    @ApiModelProperty("审批类型id")
    private Integer categoryId;

    private Integer status;

    private Date startTime;

    private Date endTime;
}
