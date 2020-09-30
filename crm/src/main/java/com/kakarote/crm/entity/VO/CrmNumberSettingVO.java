package com.kakarote.crm.entity.VO;

import com.kakarote.crm.entity.PO.CrmNumberSetting;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwei
 * 编号设置VO
 */
@ToString
@Data
@ApiModel("应用管理设置")
public class CrmNumberSettingVO {

    @ApiModelProperty(value = "主键ID", required = true)
    private Integer settingId;

    @ApiModelProperty(value = "状态", required = true, allowableValues = "0,1")
    private Integer status;

    @ApiModelProperty(value = "编号类型",required = true)
    private String label;

    @ApiModelProperty("规则集列表")
    private List<CrmNumberSetting> setting;
}
