package com.kakarote.admin.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CpSignatureVO {

    @ApiModelProperty("随机字符串")
    private String noncestr;

    @ApiModelProperty("时间戳")
    private Long timestamp;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("企业id")
    private String corpid;

    private Long agentId;

}
