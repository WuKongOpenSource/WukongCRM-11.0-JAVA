package com.kakarote.oa.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.BO.LogBO;
import com.kakarote.oa.entity.PO.OaLog;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作日志表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaLogService extends BaseService<OaLog> {
    /**
     * 分页查询日志列表
     * @param bo bo
     * @return data
     */
    public BasePage<JSONObject> queryList(LogBO bo);

    /**
     * 随机获取一条日志欢迎语
     * @return data
     */
    public String getLogWelcomeSpeech();

    /**
     * 查询日志统计信息
     * @return data
     */
    public JSONObject queryLogBulletin();

    /**
     * 查询日志完成情况统计
     */
    public JSONObject queryCompleteStats(Integer type);

    public BasePage<JSONObject> queryCompleteOaLogList(LogBO bo);

    public BasePage<SimpleUser> queryIncompleteOaLogList(LogBO bo);

    public void saveAndUpdate(JSONObject object);

    public void deleteById(Integer logId);

    public BasePage<JSONObject> queryLogBulletinByType(LogBO bo);

    public List<JSONObject> queryLogRecordCount(Integer logId, Integer today);

    JSONObject queryById(Integer logId);

    public List<Map<String, Object>> export(LogBO logBO);


}
