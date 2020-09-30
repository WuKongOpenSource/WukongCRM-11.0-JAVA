package com.kakarote.gateway.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * @author zhangzhiwei
 * 网关路由
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "config_info_route")
public class GatewayRoute{
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String uri;

    private String routeId;

    private String predicates;

    private String filters;

    private String description;

    private Integer intercept;

    @TableLogic(value = "1")
    private Integer status;

    public Integer orders;

    @TableField(fill = FieldFill.INSERT)
    private LocalDate createTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDate updateTime;
}
