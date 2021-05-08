package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义字段表
 * </p>
 *
 * @author JiaS
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine_field_extend")
@ApiModel(value="OaExamineFieldExtend对象", description="OA自定义字段扩展表")
public class OaExamineFieldExtend implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "对应主字段id")
    private Integer parentFieldId;

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

    @ApiModelProperty(value = "是否可以删除修改")
    private Integer operating;

    @ApiModelProperty(value = "是否隐藏  0不隐藏 1隐藏")
    private Integer isHidden;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中")
    private Integer fieldType;

    @ApiModelProperty(value = "样式百分比%")
    private Integer stylePercent;

    @ApiModelProperty(value = "精度，允许的最大小数位")
    private Integer precisions;

    @ApiModelProperty(value = "表单定位 坐标格式： 1,1")
    private String formPosition;

    @ApiModelProperty(value = "限制的最大数值")
    private String maxNumRestrict;

    @ApiModelProperty(value = "限制的最小数值")
    private String minNumRestrict;

    @ApiModelProperty(value = "表单辅助id，前端生成")
    private Integer formAssistId;


    @TableField(exist = false)
    @ApiModelProperty(value = "类型")
    private String formType;

    @TableField(exist = false)
    @ApiModelProperty(value = "逻辑表单数据")
    private Map<String, Object> optionsData;

    @TableField(exist = false)
    @ApiModelProperty(value = "设置列表")
    private List<String> setting;

}
