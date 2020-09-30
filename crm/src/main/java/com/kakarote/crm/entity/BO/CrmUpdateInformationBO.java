package com.kakarote.crm.entity.BO;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrmUpdateInformationBO {

    private List<JSONObject> list;

    private Integer label;

    private Integer id;

    private String batchId;
}
