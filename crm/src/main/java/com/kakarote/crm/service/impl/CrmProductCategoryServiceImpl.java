package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.RecursionUtil;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.entity.BO.CrmProductCategoryBO;
import com.kakarote.crm.entity.PO.CrmProduct;
import com.kakarote.crm.entity.PO.CrmProductCategory;
import com.kakarote.crm.mapper.CrmProductCategoryMapper;
import com.kakarote.crm.service.ICrmProductCategoryService;
import com.kakarote.crm.service.ICrmProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品分类表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class CrmProductCategoryServiceImpl extends BaseServiceImpl<CrmProductCategoryMapper, CrmProductCategory> implements ICrmProductCategoryService {

    @Autowired
    private ICrmProductService crmProductService;

    /**
     * 通过ID查询
     *
     * @param id id
     * @return data
     */
    @Override
    public CrmProductCategory queryById(Integer id) {
        return getById(id);
    }

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 保存数据
     *
     * @param productCategory data
     */
    @Override
    public void saveAndUpdate(CrmProductCategory productCategory) {
        if (productCategory.getCategoryId() != null) {
            updateById(productCategory);
            ElasticUtil.batchUpdateEsData(elasticsearchRestTemplate.getClient(),"product",productCategory.getCategoryId().toString(),productCategory.getName());
        } else {
            save(productCategory);
        }
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Override
    public void deleteById(Integer id) {
        Integer num = crmProductService.lambdaQuery().eq(CrmProduct::getCategoryId, id).ne(CrmProduct::getStatus,3).count();
        if (num > 0) {
            throw new CrmException(CrmCodeEnum.CRM_PRODUCT_CATEGORY_ERROR);
        }
        num = lambdaQuery().eq(CrmProductCategory::getPid, id).count();
        if (num > 0) {
            throw new CrmException(CrmCodeEnum.CRM_PRODUCT_CATEGORY_CHILD_ERROR);
        }
        removeById(id);
    }

    /**
     * 查询产品列表
     *
     * @param type 类型
     * @return data
     */
    @Override
    public List<CrmProductCategoryBO> queryList(String type) {
        List<CrmProductCategory> categoryList = list();
        if (StrUtil.isEmpty(type)) {
            return categoryList.stream().map(obj -> BeanUtil.copyProperties(obj, CrmProductCategoryBO.class)).collect(Collectors.toList());
        }
        return RecursionUtil.getChildListTree(categoryList, "pid", 0, "categoryId", "children", CrmProductCategoryBO.class);
    }

    /**
     * 查询产品列表
     *
     * @return data
     */
    @Override
    public List<String> queryListName() {
        QueryWrapper<CrmProductCategory> wrapper = new QueryWrapper<>();
        wrapper.select("name");
        return listObjs(wrapper, Object::toString);
    }

    @Override
    public String getProductCategoryName(int categoryId) {
        return lambdaQuery().select(CrmProductCategory::getName).eq(CrmProductCategory::getCategoryId,categoryId).oneOpt()
                .map(CrmProductCategory::getName).orElse("");
    }

    /**
     * 通过名称查询产品类别
     *
     * @param name name
     * @return data
     */
    @Override
    public CrmProductCategory queryFirstCategoryByName(String name) {
        QueryWrapper<CrmProductCategory> wrapper = new QueryWrapper<>();
        wrapper.select("category_id");
        wrapper.eq("name", name);
        return getOne(wrapper);
    }

    @Override
    public List<Integer> queryId(List<Integer> list, Integer categoryId) {
        if (categoryId == null){
            return new ArrayList<>();
        }
        if (list == null){
            list = new ArrayList<>();
        }
        list.add(categoryId);
        CrmProductCategory productCategory = getById(categoryId);
        if (productCategory != null && productCategory.getPid() != 0){
            queryId(list,productCategory.getPid());
        }else {
            Collections.reverse(list);
        }

        return list;
    }
}
