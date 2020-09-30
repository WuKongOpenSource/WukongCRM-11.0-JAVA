package com.kakarote.authorization.entity.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zhangzhiwei
 * 登录成功的vo
 */
@ApiModel(value="登录成功返回对象", description="登录成功返回对象")
@Accessors(chain = true)
@Data
@ToString
public class LoginVO {

    private String adminToken;
}
