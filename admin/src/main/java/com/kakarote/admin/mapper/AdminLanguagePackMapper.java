package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.PO.AdminLanguagePack;
import com.kakarote.admin.entity.VO.AdminLanguagePackVO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;

/**
 * <p>
 * 语言包表 Mapper 接口
 * </p>
 *
 * @author zmj
 * @since 2020-12-02
 */
public interface AdminLanguagePackMapper extends BaseMapper<AdminLanguagePack> {

    /**
     * 查询用户列表
     * @param page 分页参数
     * @return data
     */
    BasePage<AdminLanguagePackVO> queryLanguagePackList(BasePage<AdminLanguagePackVO> page);
}
