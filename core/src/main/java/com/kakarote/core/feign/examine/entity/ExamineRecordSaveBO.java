package com.kakarote.core.feign.examine.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("新增审核记录")
@AllArgsConstructor
@NoArgsConstructor
public class ExamineRecordSaveBO {

    @ApiModelProperty("对应类型")
    private Integer label;

    @ApiModelProperty("对应类型ID")
    private Integer typeId;

    @ApiModelProperty("审批记录ID")
    private Integer recordId;


    @ApiModelProperty("OA专用 审批ID")
    private Integer categoryId;

    @ApiModelProperty("数据map")
    private Map<String,Object> dataMap;

    @ApiModelProperty("自选成员列表")
    private List<ExamineGeneralBO> optionalList;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

}
