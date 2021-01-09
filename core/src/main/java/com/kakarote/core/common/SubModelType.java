package com.kakarote.core.common;

import lombok.Getter;

@Getter
public enum SubModelType {
    NULL(0,""),
    ADMIN_COMPANY_HOME(1,"企业首页"),
    ADMIN_APPLICATION_MANAGEMENT(2,"应用管理"),
    ADMIN_STAFF_MANAGEMENT(3,"员工管理"),
    ADMIN_DEPARTMENT_MANAGEMENT(4,"部门管理"),
    ADMIN_ROLE_PERMISSIONS(5,"角色权限"),
    ADMIN_PROJECT_MANAGEMENT(6,"项目管理"),
    ADMIN_CUSTOMER_MANAGEMENT(7,"客户管理"),
    ADMIN_HUMAN_RESOURCE_MANAGEMENT(8,"人力资源管理"),
    ADMIN_INVOICING_MANAGEMENT(9,"进销存管理"),
    ADMIN_OTHER_SETTINGS(10,"其他设置"),
    //crm
    CRM_LEADS(21,"线索"),
    CRM_CUSTOMER(22,"客户"),
    CRM_CONTACTS(23,"联系人"),
    CRM_BUSINESS(24,"商机"),
    CRM_CONTRACT(25,"合同"),
    CRM_RECEIVABLES(26,"回款"),
    CRM_INVOICE(27,"发票"),
    CRM_RETURN_VISIT(28,"回访"),
    CRM_PRODUCT(29,"产品"),
    CRM_MARKETING(30,"市场活动"),
    //oa
    OA_CALENDAR(41,"日历"),
    OA_LOG(42,"日志"),
    //work
    WORK_PROJECT(51,"项目"),
    WORK_TASK(52,"任务"),
    //hrm
    HRM_DEPT(61,"组织管理"),
    HRM_RECRUITMENT(62,"招聘职位"),
    HRM_CANDIDATE(63,"候选人"),
    HRM_EMPLOYEE(64,"员工管理"),
    HRM_SOCIAL_SECURITY(65,"社保管理"),
    HRM_SALARY(66,"薪资管理"),
    HRM_SALARY_FILE(67,"薪资档案"),
    HRM_SALARY_SLIP(68,"工资条"),
    HRM_APPRAISAL(69,"绩效考核"),
    //jxc
    JXC_SUPPLIER(81,"供应商"),
    JXC_PURCHASE_ORDER(82,"采购订单"),
    JXC_PURCHASE_RETURN(83,"采购退货"),
    JXC_PRODUCT_MANAGEMENT(84,"产品管理"),
    JXC_SALES_ORDER(85,"销售订单"),
    JXC_SALES_RETURN(86,"销售退货"),
    JXC_WAREHOUSE_MANAGEMENT(87,"仓库管理"),
    JXC_PRODUCT_INVENTORY(88,"产品库存"),
    JXC_PRODUCT_IN_STOCK(89,"产品入库"),
    JXC_PRODUCT_OUT_STOCK(90,"产品出库"),
    JXC_STOCK_TRANSFER(91,"库存调拨"),
    JXC_INVENTORY_CHECK(92,"库存盘点"),
    JXC_RECEIVABLES(93,"回款"),
    JXC_PAYMENT(94,"付款"),
    ;


    SubModelType(Integer label, String name) {
        this.label = label;
        this.name = name;
    }

    private Integer label;

    private String name;

    public static String valueOfName(Integer label){
        for (SubModelType modelType : values()) {
            if (label.equals(modelType.getLabel())){
                return modelType.getName();
            }
        }
        return "";
    }
}
