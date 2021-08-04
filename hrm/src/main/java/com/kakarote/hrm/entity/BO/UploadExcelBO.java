package com.kakarote.hrm.entity.BO;
import com.kakarote.core.entity.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("上传文件业务对象")
public class UploadExcelBO {


    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("负责人ID")
    private Long ownerUserId;

    @ApiModelProperty("重复是否继续")
    private Integer repeatHandling;

    @ApiModelProperty("messageId")
    private Long messageId;

    private UserInfo userInfo;
}
