package com.kakarote.oa.entity.BO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTypeUserBO {


    private List<Integer> typeIds;

    private Long userId;
}
