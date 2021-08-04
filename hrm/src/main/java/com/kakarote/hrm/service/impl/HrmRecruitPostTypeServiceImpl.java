package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.RecursionUtil;
import com.kakarote.hrm.entity.PO.HrmRecruitPostType;
import com.kakarote.hrm.mapper.HrmRecruitPostTypeMapper;
import com.kakarote.hrm.service.IHrmRecruitPostTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 职位类型 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmRecruitPostTypeServiceImpl extends BaseServiceImpl<HrmRecruitPostTypeMapper, HrmRecruitPostType> implements IHrmRecruitPostTypeService {

    @Override
    public List<HrmRecruitPostType> queryPostType() {
        List<HrmRecruitPostType> list = list();
        List<HrmRecruitPostType> childListTree = RecursionUtil.getChildListTree(list, "parentId", 0, "id", "children", HrmRecruitPostType.class);
        return childListTree;
    }
}
