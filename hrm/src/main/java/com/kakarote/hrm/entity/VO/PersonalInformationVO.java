package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmEmployeeCertificate;
import com.kakarote.hrm.entity.PO.HrmEmployeeEducationExperience;
import com.kakarote.hrm.entity.PO.HrmEmployeeTrainingExperience;
import com.kakarote.hrm.entity.PO.HrmEmployeeWorkExperience;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@ApiModel(description="员工个人信息")
public class PersonalInformationVO {

    @ApiModelProperty("个人基本信息")
    private List<InformationFieldVO> information;

    @ApiModelProperty("通讯信息")
    private List<InformationFieldVO> communicationInformation;

    @ApiModelProperty("教育经历")
    private List<HrmEmployeeEducationExperience> educationExperienceList;

    @ApiModelProperty("工作经历")
    private List<HrmEmployeeWorkExperience> workExperienceList;

    @ApiModelProperty("证书")
    private List<HrmEmployeeCertificate> certificateList;

    @ApiModelProperty("联系人")
    private List<Map<String, Object>> hrmEmployeeContacts;

    @ApiModelProperty("培训经历")
    private List<HrmEmployeeTrainingExperience> trainingExperienceList;

}
