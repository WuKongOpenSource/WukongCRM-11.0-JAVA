package com.kakarote.admin.service.impl;

import com.kakarote.admin.entity.PO.LoginLog;
import com.kakarote.admin.mapper.AdminLoginLogMapper;
import com.kakarote.admin.service.IAdminLoginLogService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统登录日志表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminLoginLogServiceImpl extends BaseServiceImpl<AdminLoginLogMapper, LoginLog> implements IAdminLoginLogService {

}
