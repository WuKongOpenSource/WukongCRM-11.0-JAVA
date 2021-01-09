package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/2
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_marketing_field")
@ApiModel(value="CrmMarketingField对象", description="活动字段表")
public class CrmMarketingField implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "field_id", type = IdType.AUTO)
    private Integer fieldId;

    @ApiModelProperty(value = "自定义字段英文标识")
    private String fieldName;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划")
    private Integer type;

    @ApiModelProperty(value = "字段说明")
    private String remark;

    @ApiModelProperty(value = "输入提示")
    private String inputTips;

    @ApiModelProperty(value = "最大长度")
    private Integer maxLength;

    @ApiModelProperty(value = "默认值")
    private Object defaultValue;

    @ApiModelProperty(value = "是否唯一 1 是 0 否")
    private Integer isUnique;

    @ApiModelProperty(value = "是否必填 1 是 0 否")
    private Integer isNull;

    @ApiModelProperty(value = "排序 从小到大")
    private Integer sorting;

    @ApiModelProperty(value = "如果类型是选项，此处不能为空，多个选项以，隔开")
    private String options;

    @ApiModelProperty(value = "是否可以删除修改 0 改删 1 改 2 删 3 无")
    private Integer operating;

    @ApiModelProperty(value = "是否隐藏  0不隐藏 1隐藏")
    private Integer isHidden;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "表单ID")
    private Integer formId;

    @ApiModelProperty(value = "字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中")
    private Integer fieldType;

    @TableField(exist = false)
    private String formType;

    @TableField(exist = false)
    private List<String> setting;

    @TableField(exist = false)
    private Object value;
}
