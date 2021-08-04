package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Range;
import com.kakarote.core.common.cache.HrmCacheKey;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TransferUtil;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.IsEnum;
import com.kakarote.hrm.entity.BO.AddInsuranceSchemeBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceScaleBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceTypeBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeSocialSecurityInfo;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthEmpRecord;
import com.kakarote.hrm.entity.PO.HrmInsuranceProject;
import com.kakarote.hrm.entity.PO.HrmInsuranceScheme;
import com.kakarote.hrm.entity.VO.InsuranceSchemeListVO;
import com.kakarote.hrm.entity.VO.InsuranceSchemeVO;
import com.kakarote.hrm.mapper.HrmInsuranceSchemeMapper;
import com.kakarote.hrm.service.IHrmEmployeeSocialSecurityService;
import com.kakarote.hrm.service.IHrmInsuranceMonthEmpRecordService;
import com.kakarote.hrm.service.IHrmInsuranceProjectService;
import com.kakarote.hrm.service.IHrmInsuranceSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 社保方案表 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmInsuranceSchemeServiceImpl extends BaseServiceImpl<HrmInsuranceSchemeMapper, HrmInsuranceScheme> implements IHrmInsuranceSchemeService {

    @Autowired
    private IHrmInsuranceProjectService insuranceProjectService;

    @Autowired
    private IHrmEmployeeSocialSecurityService employeeSocialSecurityService;

    @Autowired
    private HrmInsuranceSchemeMapper insuranceSchemeMapper;

    @Autowired
    private IHrmInsuranceMonthEmpRecordService insuranceMonthEmpRecordService;

    @Autowired
    private Redis redis;


    @Override
    public void setInsuranceScheme(AddInsuranceSchemeBO addInsuranceSchemeBO) {
        HrmInsuranceScheme insuranceScheme = BeanUtil.copyProperties(addInsuranceSchemeBO, HrmInsuranceScheme.class);
        if (insuranceScheme.getSchemeId() != null) {
            Integer schemeId = insuranceScheme.getSchemeId();
            lambdaUpdate().set(HrmInsuranceScheme::getIsDel, IsEnum.YES.getValue()).eq(HrmInsuranceScheme::getSchemeId, schemeId).update();
            insuranceProjectService.lambdaUpdate().set(HrmInsuranceProject::getIsDel, IsEnum.YES.getValue()).eq(HrmInsuranceProject::getSchemeId, schemeId).update();
            insuranceScheme.setSchemeId(null);
            save(insuranceScheme);
            //把使用该社保方案的员工更新新的社保方案
            employeeSocialSecurityService.lambdaUpdate().set(HrmEmployeeSocialSecurityInfo::getSchemeId, insuranceScheme.getSchemeId()).eq(HrmEmployeeSocialSecurityInfo::getSchemeId, schemeId).update();
            insuranceMonthEmpRecordService.lambdaUpdate().set(HrmInsuranceMonthEmpRecord::getSchemeId, insuranceScheme.getSchemeId()).eq(HrmInsuranceMonthEmpRecord::getSchemeId, schemeId).update();
        } else {
            save(insuranceScheme);
        }
        List<AddInsuranceSchemeBO.HrmInsuranceProjectBO> projectBOList = addInsuranceSchemeBO.getSocialSecurityProjectList();
        if (CollUtil.isNotEmpty(addInsuranceSchemeBO.getProvidentFundProjectList())) {
            projectBOList.addAll(addInsuranceSchemeBO.getProvidentFundProjectList());
        }
        List<HrmInsuranceProject> projectList = TransferUtil.transferList(projectBOList, HrmInsuranceProject.class);
        projectList.forEach(project -> project.setSchemeId(insuranceScheme.getSchemeId()));
        insuranceProjectService.saveBatch(projectList);
    }

    @Override
    public void deleteInsuranceScheme(Integer schemeId) {
        Integer count = employeeSocialSecurityService.lambdaQuery().eq(HrmEmployeeSocialSecurityInfo::getSchemeId, schemeId).count();
        if (count > 0) {
            throw new CrmException(HrmCodeEnum.SOCIAL_SECURITY_SCHEMES_ARE_USED_BY_EMPLOYEES);
        }
        lambdaUpdate().set(HrmInsuranceScheme::getIsDel, IsEnum.YES.getValue()).eq(HrmInsuranceScheme::getSchemeId, schemeId).update();
        insuranceProjectService.lambdaUpdate().set(HrmInsuranceProject::getIsDel, IsEnum.YES.getValue()).eq(HrmInsuranceProject::getSchemeId, schemeId).update();
    }

    @Override
    public BasePage<InsuranceSchemeListVO> queryInsuranceSchemePageList(PageEntity pageEntity) {
        return insuranceSchemeMapper.queryInsuranceSchemePageList(pageEntity.parse());
    }

    @Override
    public InsuranceSchemeVO queryInsuranceSchemeById(Integer schemeId) {
        HrmInsuranceScheme insuranceScheme = getById(schemeId);
        List<HrmInsuranceProject> projectList = insuranceProjectService.lambdaQuery().eq(HrmInsuranceProject::getSchemeId, schemeId).list();
        InsuranceSchemeVO insuranceSchemeVO = BeanUtil.copyProperties(insuranceScheme, InsuranceSchemeVO.class);
        List<AddInsuranceSchemeBO.HrmInsuranceProjectBO> hrmInsuranceProjectBOS = TransferUtil.transferList(projectList, InsuranceSchemeVO.HrmInsuranceProjectBO.class);
        Range<Integer> socialSecurityClosed = Range.closed(1, 9);
        Range<Integer> providentFundClosed = Range.closed(10, 11);
        List<AddInsuranceSchemeBO.HrmInsuranceProjectBO> socialSecurityList = new ArrayList<>();
        List<AddInsuranceSchemeBO.HrmInsuranceProjectBO> providentFundList = new ArrayList<>();
        hrmInsuranceProjectBOS.forEach(project -> {
            if (socialSecurityClosed.contains(project.getType())) {
                socialSecurityList.add(project);
            } else if (providentFundClosed.contains(project.getType())) {
                providentFundList.add(project);
            }
        });
        insuranceSchemeVO.setSocialSecurityProjectList(socialSecurityList);
        insuranceSchemeVO.setProvidentFundProjectList(providentFundList);
        return insuranceSchemeVO;
    }

    @Override
    public List<HrmInsuranceScheme> queryInsuranceSchemeList() {
        return lambdaQuery()
                .select(HrmInsuranceScheme::getSchemeId, HrmInsuranceScheme::getSchemeName, HrmInsuranceScheme::getCity)
                .eq(HrmInsuranceScheme::getIsDel, 0).list();
    }

    @Override
    public String queryInsuranceType(QueryInsuranceTypeBO queryInsuranceTypeBO) {
        String cityId = queryInsuranceTypeBO.getCityId();
        String key = HrmCacheKey.INSURANCE_TYPE_CACHE_KEY+ cityId;
        String body;
        if (redis.exists(key)) {
            body = redis.get(key);
        } else {
            body = HttpUtil.createPost("http://www.shebao100.cn/Common/GetHouseholdByCityID")
                    .form("CityID", cityId)
                    .execute().body();
            redis.setex(key,60*60*24*7,body);
        }
        return body;
    }

    @Override
    public String queryInsuranceScale(QueryInsuranceScaleBO queryInsuranceScaleBO) {
        String cityId = queryInsuranceScaleBO.getCityId();
        String hujiId = queryInsuranceScaleBO.getHujiId();
        String key = HrmCacheKey.INSURANCE_SCALE_CACHE_KEY + cityId+":"+hujiId;
        String body;
        if (redis.exists(key)) {
            body = redis.get(key);
        } else {
            body = HttpUtil.createPost("http://www.shebao100.cn/User/FunBaseQuery")
                    .form("cityId", cityId)
                    .form("hujiId", hujiId)
                    .form("X-Requested-With", "XMLHttpRequest")
                    .execute().body();
            redis.setex(key,60*60*24*7,body);
        }
        return body;
    }
}
