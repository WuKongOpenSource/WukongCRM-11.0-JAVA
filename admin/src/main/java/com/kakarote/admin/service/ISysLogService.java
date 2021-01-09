package com.kakarote.admin.service;

import com.kakarote.admin.entity.BO.QuerySysLogBO;
import com.kakarote.admin.entity.PO.LoginLog;
import com.kakarote.admin.entity.PO.SysLog;
import com.kakarote.core.entity.BasePage;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author hmb
 * @since 2020-11-25
 */
public interface ISysLogService {

    void saveSysLog(SysLog sysLog);

    void saveLoginLog(LoginLog loginLog);

    BasePage<SysLog> querySysLogPageList(QuerySysLogBO querySysLogBO);

    BasePage<LoginLog> queryLoginLogPageList(QuerySysLogBO querySysLogBO);
}
