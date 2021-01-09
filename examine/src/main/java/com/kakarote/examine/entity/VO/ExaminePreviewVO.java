package com.kakarote.examine.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/23
 */
@Data
public class ExaminePreviewVO {


    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 审批流预览信息
     * */
    private List<ExamineFlowVO>  examineFlowList;

}
