package com.kakarote.hrm.constant;

import lombok.Getter;

@Getter
public enum TaxType {
    /**
     * 性别枚举
     */
    SALARY_TAX_TYPE(1,"工资薪金"),REMUNERATION_TAX_TYPE(2,"劳务报酬"),NO_TAX_TYPE(3,"不计税");

    private int value;
    private String name;

    TaxType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static TaxType  parse(int type){
        for(TaxType value : TaxType.values()){
            if(value.value == type){
                return value;
            }
        }
        return SALARY_TAX_TYPE;
    }

}
