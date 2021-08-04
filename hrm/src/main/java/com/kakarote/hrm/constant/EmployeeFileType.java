package com.kakarote.hrm.constant;

import lombok.Getter;

@Getter
public enum EmployeeFileType {
    /**
     * 11、身份证原件 12、学历证明 13、个人证件照 14、身份证复印件 15、工资银行卡 16、社保卡 17、公积金卡 18、获奖证书 19、其他 21、劳动合同 22、入职简历 23、入职登记表 24、入职体检单 25、上家公司离职证明 26、转正申请表 27、其他 31、离职审批 32、离职证明 33 、其他
     */
    //基本信息
    ORIGINAL_ID(11,"身份证原件"),EDUCATION_CERTIFICATE(12,"学历证明"),PERSONAL_ID_PHOTO(13,"个人证件照"),COPY_OF_ID_CARD(14,"身份证复印件"),
    payroll_bank_card(15,"工资银行卡"),SOCIAL_SECURITY_CARD(16,"社保卡"),PROVIDENT_FUND_CARD(17,"公积金卡"),AWARD_CERTIFICATE(18,"获奖证书"),
    INFORMATION_OTHER(19,"基本信息其他"),
    //档案资料
    LABOR_CONTRACT(21,"劳动合同"),ENTRY_RESUME(22,"入职简历"),ENTRY_REGISTRATION_FORM(23,"入职登记表"),ENTRY_MEDICAL_CHECKLIST(24,"入职体检单"),BEFORE_PROOF_OF_SEPARATION(25,"上家公司离职证明"),CORRECTION_APPLICATION_FORM(26,"转正申请表"),RECORD_OTHER(27,"档案资料其他"),
    //离职资料
    RESIGNATION_APPROVAL(31,"离职审批"),PROOF_OF_SEPARATION(32,"离职证明"),RESIGNATION_OTHER(33,"离职资料其他")
    ;

    EmployeeFileType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String  parseName(int type){
        for(EmployeeFileType value : EmployeeFileType.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }




    private int value;
    private String name;
}
