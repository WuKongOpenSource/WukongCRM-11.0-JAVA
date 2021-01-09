package com.kakarote.examine.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("审批条件数据BO")
public class ExamineConditionDataSaveBO {

    @ApiModelProperty(value = "字段ID")
    private Integer fieldId;

    @ApiModelProperty(value = "字段中文名")
    private String name;

    @ApiModelProperty(value = "字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划")
    private Integer type;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "连接条件 1 等于 2 大于 3 小于 4 大于等于 5 小于等于 6 两者之间 7 包含 8 员工/部门/角色 11完全等于")
    private Integer conditionType;

    @ApiModelProperty(value = "值，json数组格式")
    private Object values;
}
