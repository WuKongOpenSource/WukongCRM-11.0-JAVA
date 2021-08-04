package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("自定义表头字段返回")
public class EmployeeHeadFieldVO {

    @ApiModelProperty(value = "表头配置id")
    private Integer id;

    @ApiModelProperty(value = "字段排序")
    private Integer sort;

    @ApiModelProperty(value = "字段宽度")
    private Integer width;

    @ApiModelProperty(value = "自定义字段ID")
    private Integer fieldId;

    @ApiModelProperty(value = "自定义字段英文标识")
    private String fieldName;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选   10 日期时间 11 邮箱 12 籍贯地区")
    private Integer type;

    @ApiModelProperty(value = "关联表类型 0 不需要关联 1 hrm员工 2 hrm部门 3 hrm职位 4 系统用户 5 系统部门 6 招聘渠道")
    private Integer componentType;

    @ApiModelProperty(value = "如果类型是选项，此处不能为空，使用kv格式")
    private String options;

    @ApiModelProperty(value = "是否固定字段 0 否 1 是")
    private Integer isFixed;

    @ApiModelProperty(value = "是否隐藏 0、不隐藏 1、隐藏")
    private Integer isHide;


}
