package com.kakarote.crm.entity.BO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EsUpdatePropertiesBO {

    private String idField;

    private String nameField;

    private List<String> indexs;
}
