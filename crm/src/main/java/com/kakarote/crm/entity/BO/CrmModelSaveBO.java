package com.kakarote.crm.entity.BO;

import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * crm模块保存的BO
 *
 * @author zhangzhiwei
 */
@Data
@ToString
@ApiModel("crm保存对象")
public class CrmModelSaveBO {
    @ApiModelProperty(value = "实体类对象")
    private Map<String, Object> entity;
    @ApiModelProperty(value = "自定义字段对象")
    private List<CrmModelFiledVO> field;
}
