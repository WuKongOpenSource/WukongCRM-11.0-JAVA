package com.kakarote.crm.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
@ApiModel("crm首要联系人BO对象")
public class CrmMemberSaveBO {

    @ApiModelProperty("id")
    private List<Integer> ids;

    @ApiModelProperty("成员ids")
    private List<Long> memberIds;

    @ApiModelProperty("权限（1.只读 2.读写）")
    private Integer power;

    @ApiModelProperty("变更类型 1、联系人 2、商机 3、合同")
    private List<Integer> changeType;

    @ApiModelProperty("过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiresTime;

    public CrmMemberSaveBO() {
    }

    public CrmMemberSaveBO(List<Integer> ids, CrmMemberSaveBO crmMemberSaveBO) {
        this.ids = ids;
        this.memberIds = crmMemberSaveBO.getMemberIds();
        this.power = crmMemberSaveBO.getPower();
        this.expiresTime = crmMemberSaveBO.getExpiresTime();
    }

    public CrmMemberSaveBO(Integer id,Long member){
        this.ids = Collections.singletonList(id);
        this.memberIds = Collections.singletonList(member);
    }
}
