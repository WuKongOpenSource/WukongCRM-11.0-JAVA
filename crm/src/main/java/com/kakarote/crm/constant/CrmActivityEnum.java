package com.kakarote.crm.constant;

/**
 * @author hmb
 */

public enum CrmActivityEnum {
    /**
     * crm活动枚举
     */
    LEADS(1,"线索"),
    CUSTOMER(2,"客户"),
    CONTACTS(3,"联系人"),
    PRODUCT( 4,"产品"),
    BUSINESS(5,"商机"),
    CONTRACT(6,"合同"),
    RECEIVABLES(7,"回款"),
    LOG(8,"日志"),
    EXAMINE(9,"审批"),
    EVENT( 10,"日程" ),
    TASK( 11,"任务" ),
    MAIL( 12,"发邮件");

    CrmActivityEnum(int type, String remarks){
        this.type = type;
        this.remarks = remarks;
    }

    private final int  type;
    private final String remarks;

    public static CrmActivityEnum  parse(int type){
        for(CrmActivityEnum value : CrmActivityEnum.values()){
            if(value.type == type){
                return value;
            }
        }
        return null;
    }


    public int getType(){
        return type;
    }

    public String getRemarks(){
        return remarks;
    }
}
