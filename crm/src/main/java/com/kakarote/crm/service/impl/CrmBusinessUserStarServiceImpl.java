package com.kakarote.crm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.entity.PO.CrmBusinessUserStar;
import com.kakarote.crm.mapper.CrmBusinessUserStarMapper;
import com.kakarote.crm.service.ICrmBusinessUserStarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户商机标星关系表  服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
public class CrmBusinessUserStarServiceImpl extends BaseServiceImpl<CrmBusinessUserStarMapper, CrmBusinessUserStar> implements ICrmBusinessUserStarService {
    /**
     * 判断用户是否关注
     *
     * @param businessId id
     * @param userId     用户ID
     * @return int
     */
    @Override
    public Integer isStar(Object businessId, Long userId) {
        return lambdaQuery().eq(CrmBusinessUserStar::getUserId, userId).eq(CrmBusinessUserStar::getBusinessId, businessId).last(" limit 1").count();
    }

    @Override
    public List<Integer> starList(Long userId) {
        return lambdaQuery().select(CrmBusinessUserStar::getBusinessId).eq(CrmBusinessUserStar::getUserId,userId).list()
                .stream().map(CrmBusinessUserStar::getBusinessId).collect(Collectors.toList());
    }
}
