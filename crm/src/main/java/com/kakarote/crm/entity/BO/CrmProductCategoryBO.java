package com.kakarote.crm.entity.BO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
@ApiModel("产品类型")
public class CrmProductCategoryBO {
    private Integer categoryId;
    private Integer pid;
    private String name;
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.label = name;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CrmProductCategoryBO> children;
}
