package com.kakarote.oa.entity.VO;

import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.oa.entity.PO.OaExamine;
import com.kakarote.oa.entity.PO.OaExamineRecord;
import com.kakarote.oa.entity.PO.OaExamineTravel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExamineVO extends OaExamine {

//    @ApiModelProperty("审批状态")
//    private Integer examineStatus;
//    @ApiModelProperty("审批记录id")
//    private Integer examineRecordId;
    @ApiModelProperty("审批步骤id")
    private Integer examineStepId;
    @ApiModelProperty("审批类型标题")
    private String categoryTitle;
    @ApiModelProperty("审批类型")
    private Integer type;
    @ApiModelProperty("图标")
    private String examineIcon;

    private SimpleUser createUser;

    private String examineName;

    private List<FileEntity> img;

    private List<FileEntity> file;

    private String causeTitle;

    private Map<String,Integer> permission;

    private List<SimpleCrmEntity> customerList;
    private List<SimpleCrmEntity> contactsList;
    private List<SimpleCrmEntity> businessList;
    private List<SimpleCrmEntity> contractList;

    private OaExamineRecord record;

    private List<OaExamineTravel> examineTravelList;
}
