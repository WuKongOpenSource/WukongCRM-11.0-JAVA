package com.kakarote.core.servlet.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件操作实体类对象
 *
 * @author zhangzhiwei
 */
@Data
@ApiModel("文件对象")
public class FileEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件ID")
    private String fileId;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件大小")
    private Long size;

    @ApiModelProperty("批次ID")
    private String batchId;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("是否只读")
    private Integer readOnly;

    private String isPublic;

    @JsonIgnore
    private String path;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
        if ("1".equals(isPublic)){
            this.url =  path;
        }else {
            this.url = "/adminFile/down/" + fileId;
        }
    }
}
