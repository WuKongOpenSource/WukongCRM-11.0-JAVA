package com.kakarote.core.feign.crm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JiaS
 * @date 2020/12/25
 */
@Data
public class SimpleCrmInfo implements Serializable {

    private static final long serialVersionUID=1L;

    private String name;
    private Integer examineRecordId;
    private Integer createUserId;
    private Integer examineType;


    private String category;
    private Integer categoryId;
    private Integer categoryCiteId;
    private String categoryCiteName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date realInvoiceDate;

    private SimpleUser createUser;
}
