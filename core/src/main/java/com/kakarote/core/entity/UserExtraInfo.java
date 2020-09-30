package com.kakarote.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author z
 * 用户的一些额外信息参数
 */
@Data
@AllArgsConstructor
public class UserExtraInfo implements Serializable {
    /**
     * 是否
     */
    public Integer extra = -1;

    public Date extraTime;
}
