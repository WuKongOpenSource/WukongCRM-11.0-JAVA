package com.kakarote.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * @author zhangzhiwei
 * 分页需继承的类
 */
@Data
public class PageEntity {
    @ApiModelProperty("当前页数")
    private Integer page = 1;

    @ApiModelProperty("每页展示条数")
    private Integer limit = 15;

    @ApiModelProperty(value = "是否分页，0:不分页 1 分页", allowableValues = "0,1")
    private Integer pageType = 1;

    public <T> BasePage<T> parse() {
        BasePage<T> page = new BasePage<>(getPage(), getLimit());
        if (Objects.equals(0, pageType)) {
            page.setSize(10000);
        }
        return page;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
        if (pageType == 0) {
            limit = 10000;
        }
    }

    public Integer getLimit() {
        if (limit > 100 && 1 == pageType) {
            limit = 100;
        }
        return limit;
    }
}
