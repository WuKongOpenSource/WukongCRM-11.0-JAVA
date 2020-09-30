package com.kakarote.oa.entity.BO;

import com.kakarote.oa.entity.PO.OaExamineField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hmb
 * 保存字段
 */
@Data
@ToString
@ApiModel(value="ExamineField保存对象", description="审批自定义字段表")
public class ExamineFieldBO {

    @ApiModelProperty(value = "审批类型id")
    @NotNull
    private Integer categoryId;

    @ApiModelProperty(value = "ExamineField对象列表")
    private List<OaExamineField> data;
}
