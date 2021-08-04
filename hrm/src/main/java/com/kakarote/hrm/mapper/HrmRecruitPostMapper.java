package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryRecruitPostPageListBO;
import com.kakarote.hrm.entity.PO.HrmRecruitPost;
import com.kakarote.hrm.entity.VO.RecruitPostVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 招聘职位表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmRecruitPostMapper extends BaseMapper<HrmRecruitPost> {

    RecruitPostVO queryById(Integer postId);


    BasePage<RecruitPostVO> queryRecruitPostPageList(BasePage<RecruitPostVO> parse,@Param("data") QueryRecruitPostPageListBO queryRecruitPostPageListBO,
                                                     @Param("deptIds") Collection<Integer> deptIds);

    List<Integer> queryPostStatusList(@Param("deptIds") Collection<Integer> deptIds);

    void setRecruitPost(HrmRecruitPost recruitPost);
}
