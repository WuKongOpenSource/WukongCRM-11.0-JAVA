package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmSalaryConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QueryInItConfigVO {

    @ApiModelProperty("其他初始化配置")
    private HrmSalaryConfig otherInitConfig;

    @ApiModelProperty("状态初始化配置")
    private Map<Integer,Integer> statusInitConfig;
}
