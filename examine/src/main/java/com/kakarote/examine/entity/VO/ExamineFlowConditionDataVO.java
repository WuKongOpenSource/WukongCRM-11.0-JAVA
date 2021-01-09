package com.kakarote.examine.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("审批条件数据BO")
public class ExamineFlowConditionDataVO {

    @ApiModelProperty(value = "字段ID")
    private Integer fieldId;

    @ApiModelProperty(value = "字段中文名")
    private String name;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划")
    private Integer type;

    @ApiModelProperty(value = "连接条件 1 等于 2 大于 3 小于 4 大于等于 5 小于等于 6 两者之间 7 包含 8 员工/部门/角色 11完全等于")
    private Integer conditionType;

    @ApiModelProperty(value = "值，json数组格式")
    private Object values;


    @ApiModelProperty(value = "原始json字符串值")
    private String backupValue;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamineFlowConditionDataVO that = (ExamineFlowConditionDataVO) o;
        return  Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(conditionType, that.conditionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, conditionType);
    }
}
