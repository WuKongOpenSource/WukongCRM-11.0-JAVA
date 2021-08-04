package com.kakarote.hrm.entity.BO;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakarote.core.common.Const;
import com.kakarote.hrm.entity.PO.HrmFieldExtend;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class AddEmployeeFieldBO {

    @ApiModelProperty("字段类型")
    private Integer labelGroup;

    @ApiModelProperty("自定了字段列表")
    private List<EmployeeSaveField> data;

    @Getter
    @Setter
    public static class EmployeeSaveField{

        @ApiModelProperty(value = "主键ID")
        private Integer fieldId;

        @ApiModelProperty(value = "自定义字段英文标识")
        private String fieldName;

        @ApiModelProperty(value = "字段名称")
        private String name;

        @ApiModelProperty(value = "字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选   10 日期时间 11 邮箱 12 籍贯地区")
        private Integer type;

        @ApiModelProperty(value = "关联表类型 0 不需要关联 1 hrm员工 2 hrm部门 3 hrm职位 4 系统用户 5 系统部门 6 招聘渠道")
        private Integer componentType;

        @ApiModelProperty(value = "标签 1 个人信息 2 岗位信息")
        private Integer label;

        @ApiModelProperty(value = "标签分组 * 1 员工个人信息 2 通讯信息  7 联系人 11 岗位信息 ")
        private Integer labelGroup;

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

        @ApiModelProperty(value = "如果类型是选项，此处不能为空，使用kv格式")
        private String options;

        @ApiModelProperty(value = "是否固定字段 0 否 1 是")
        private Integer isFixed;

        @ApiModelProperty(value = "操作权限")
        private Integer operating;

        @ApiModelProperty(value = "是否隐藏  0不隐藏 1隐藏")
        private Integer isHidden;

        @ApiModelProperty(value = "是否在列表头展示 0 否 1 是")
        private Integer isHeadField;

        @ApiModelProperty(value = "样式百分比  1 100%  2 75%  3 50%  4 25%")
        private Integer stylePercent;

        @ApiModelProperty(value = "精度，允许的最大小数位/地图精度/明细表格、逻辑表单展示方式")
        @TableField(fill = FieldFill.UPDATE)
        private Integer precisions;

        @ApiModelProperty(value = "表单定位 坐标格式： 1,1")
        private String formPosition;

        @ApiModelProperty(value = "限制的最大数值")
        @TableField(fill = FieldFill.UPDATE)
        private String maxNumRestrict;

        @ApiModelProperty(value = "限制的最小数值")
        @TableField(fill = FieldFill.UPDATE)
        private String minNumRestrict;

        @ApiModelProperty(value = "表单辅助id，前端生成")
        private Integer formAssistId;

        @TableField(exist = false)
        @ApiModelProperty(value = "类型")
        private String formType;

        @TableField(exist = false)
        @ApiModelProperty(value = "设置列表")
        private List<Object> setting;
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
        private List<HrmFieldExtend> fieldExtendList;

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

}


