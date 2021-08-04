package com.kakarote.hrm.entity.BO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UpdateInformationBO {

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("联系人id(添加修改联系人需要)")
    private Integer contactsId;

    @ApiModelProperty("字段列表")
    private List<InformationFieldBO> dataList;

    @Getter
    @Setter
    public static class InformationFieldBO implements Serializable {

        private static final long serialVersionUID=1L;

        @TableId(value = "id", type = IdType.AUTO)
        private Integer id;

        private Integer fieldId;

        private Integer labelGroup;

        @ApiModelProperty(value = "自定义字段英文标识")
        private String fieldName;

        @ApiModelProperty(value = "字段名称")
        private String name;

        @ApiModelProperty(value = "字段值")
        private Object fieldValue;

        @ApiModelProperty(value = "字段值描述")
        private String fieldValueDesc;

        @ApiModelProperty(value = "是否固定 1 是 0 否")
        private Integer isFixed;

        @ApiModelProperty(value = "字段类型")
        private Integer type;

        @ApiModelProperty(value = "员工id")
        private Integer employeeId;


    }
}
