package com.kakarote.oa.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.PO.OaLogRule;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaLogRuleService extends BaseService<OaLogRule> {

    List<OaLogRule> queryOaLogRuleList();


    void setOaLogRule(List<OaLogRule> ruleList);
}
