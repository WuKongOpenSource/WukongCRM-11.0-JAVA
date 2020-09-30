package com.kakarote.crm.entity.VO;

import cn.hutool.core.util.StrUtil;
import com.kakarote.crm.constant.FieldEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhiwei
 * crm的新增，保存需要查询的字段对象
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel("crm需要的自定义字段对象")
public class CrmModelFiledVO implements Serializable {

    public CrmModelFiledVO() {

    }

    public CrmModelFiledVO(String fieldName, String name) {
        this.fieldName = fieldName;
        this.name = name;
    }

    public CrmModelFiledVO(String fieldName, FieldEnum fieldEnum) {
        this.fieldName = StrUtil.toCamelCase(fieldName);
        this.type = fieldEnum.getType();
        this.formType = fieldEnum.getFormType();
    }

    public CrmModelFiledVO(String fieldName, FieldEnum fieldEnum, Integer fieldType) {
        this.fieldName = StrUtil.toCamelCase(fieldName);
        this.type = fieldEnum.getType();
        this.formType = fieldEnum.getFormType();
        this.fieldType = fieldType;
    }

    public CrmModelFiledVO(String fieldName, FieldEnum fieldEnum, String name, Integer fieldType) {
        this.fieldName = StrUtil.toCamelCase(fieldName);
        this.type = fieldEnum.getType();
        this.formType = fieldEnum.getFormType();
        this.name = name;
        this.fieldType = fieldType;
    }

    @ApiModelProperty(value = "主键ID")
    private Integer fieldId;

    @ApiModelProperty(value = "自定义字段英文标识")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String formType;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划")
    private Integer type;

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

    @ApiModelProperty(value = "如果类型是选项，此处不能为空，多个选项以，隔开")
    private String options;

    @ApiModelProperty(value = "字段类型")
    private Integer fieldType;


    @ApiModelProperty(value = "设置列表")
    private List<Object> setting = new ArrayList<>();

    @ApiModelProperty(value = "权限 1不可编辑不可查看 2可查看不可编辑 3可编辑可查看")
    private Integer authLevel;

    @ApiModelProperty(value = "value")
    private Object value;

    @ApiModelProperty(value = "系统字段")
    private Integer sysInformation;

    @ApiModelProperty("开启自动编号 0 否 1 是")
    private Integer autoGeneNumber;

    public CrmModelFiledVO setFieldName(String fieldName) {
        this.fieldName = StrUtil.toCamelCase(fieldName);
        return this;
    }
}
