package com.kakarote.crm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.entity.PO.CrmLeadsUserStar;
import com.kakarote.crm.mapper.CrmLeadsUserStarMapper;
import com.kakarote.crm.service.ICrmLeadsUserStarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户线索标星关系表  服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
@Service
public class CrmLeadsUserStarServiceImpl extends BaseServiceImpl<CrmLeadsUserStarMapper, CrmLeadsUserStar> implements ICrmLeadsUserStarService {

    @Override
    public Integer isStar(Object leadsId, Long userId) {
        return lambdaQuery().eq(CrmLeadsUserStar::getUserId, userId).eq(CrmLeadsUserStar::getLeadsId, leadsId).last(" limit 1").count();
    }

    @Override
    public List<Integer> starList(Long userId) {
        return lambdaQuery().select(CrmLeadsUserStar::getLeadsId).eq(CrmLeadsUserStar::getUserId,userId).list().stream()
                .map(CrmLeadsUserStar::getLeadsId).collect(Collectors.toList());
    }
}
