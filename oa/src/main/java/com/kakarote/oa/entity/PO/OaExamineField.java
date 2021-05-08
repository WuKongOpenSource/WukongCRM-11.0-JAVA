package com.kakarote.oa.entity.PO;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakarote.core.common.Const;
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
 * @author zhangzhiwei
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine_field")
@ApiModel(value="OaExamineField对象", description="自定义字段表")
public class OaExamineField implements Serializable {

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

    @ApiModelProperty(value = "字段说明  特别用途 - 明细表格：添加字段说明 | 单选/多选： 标识开启逻辑表单")
    private String remark;

    @ApiModelProperty(value = "输入提示")
    private String inputTips;

    @ApiModelProperty(value = "最大长度")
    private Integer maxLength;

    @ApiModelProperty(value = "默认值")
    @TableField(fill = FieldFill.UPDATE)
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

    @ApiModelProperty(value = "审批ID label为10需要")
    private Integer examineCategoryId;

    @ApiModelProperty(value = "字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中")
    private Integer fieldType;

    @ApiModelProperty(value = "样式百分比  1 100%  2 75%  3 50%  4 25%")
    private Integer stylePercent;

    @ApiModelProperty(value = "精度，允许的最大小数位/地图精度/明细表格、逻辑表单展示方式")
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
    private String formType;

    @TableField(exist = false)
    private List<String> setting;

    @TableField(exist = false)
    private Object value;



    /**
     * 坐标
     * */
    @TableField(exist = false)
    @ApiModelProperty(value = "x轴")
    @JsonIgnore
    private Integer xAxis;

    @TableField(exist = false)
    @ApiModelProperty(value = "y轴")
    @JsonIgnore
    private Integer yAxis;

    @TableField(exist = false)
    @ApiModelProperty(value = "逻辑表单数据")
    private Map<String, Object> optionsData;

    @TableField(exist = false)
    @ApiModelProperty(value = "扩展字段")
    private List<OaExamineFieldExtend> fieldExtendList;


    public void setFormPosition(String formPosition) {
        this.formPosition = formPosition;
        if (StrUtil.isNotEmpty(formPosition)){
            if (formPosition.contains(Const.SEPARATOR)){
                String[] axisArr = formPosition.split(Const.SEPARATOR);
                if (axisArr.length == 2){
                    if (axisArr[0].matches("[0-9]+") && axisArr[1].matches("[0-9]+")) {
                        this.xAxis = Integer.valueOf(axisArr[0]);
                        this.yAxis = Integer.valueOf(axisArr[1]);
                        return;
                    }
                }
            }
        }
        this.xAxis = -1;
        this.yAxis = -1;
    }
}
