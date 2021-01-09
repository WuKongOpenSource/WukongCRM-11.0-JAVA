package com.kakarote.examine.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 审批条件扩展字段表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_condition_data")
@ApiModel(value="ExamineConditionData对象", description="审批条件扩展字段表")
public class ExamineConditionData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "条件ID")
    private Integer conditionId;

    @ApiModelProperty(value = "审批流程ID")
    private Integer flowId;

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
    private String value;

    @ApiModelProperty(value = "批次ID")
    private String batchId;


    @TableField(exist = false)
    @ApiModelProperty(value = "原始json字符串值")
    private String backupValue;


}
