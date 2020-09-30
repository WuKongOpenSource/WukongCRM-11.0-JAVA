package com.kakarote.oa.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.oa.common.OaCodeEnum;
import com.kakarote.oa.constart.FieldEnum;
import com.kakarote.oa.entity.BO.ExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import com.kakarote.oa.entity.PO.OaExamineData;
import com.kakarote.oa.entity.PO.OaExamineField;
import com.kakarote.oa.mapper.OaExamineFieldMapper;
import com.kakarote.oa.service.IOaExamineCategoryService;
import com.kakarote.oa.service.IOaExamineDataService;
import com.kakarote.oa.service.IOaExamineFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义字段表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-22
 */
@Service
public class OaExamineFieldServiceImpl extends BaseServiceImpl<OaExamineFieldMapper, OaExamineField> implements IOaExamineFieldService {

    @Autowired
    private OaExamineFieldMapper examineFieldMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminFileService adminFileService;

    @Override
    public List<OaExamineField> queryField(Integer id) {
        List<OaExamineField> list = lambdaQuery().eq(OaExamineField::getExamineCategoryId, id).orderByAsc(OaExamineField::getSorting).list();
        recordToFormType(list);
        return list;
    }

    @Override
    public void recordToFormType(List<OaExamineField> list) {
        for (OaExamineField record : list) {
            FieldEnum typeEnum = FieldEnum.parse(record.getType());
            record.setFormType(typeEnum.getFormType());
            switch (typeEnum) {
                case CHECKBOX:
                    record.setDefaultValue(StrUtil.splitTrim((CharSequence) record.getDefaultValue(), Const.SEPARATOR));
                case SELECT:
                    record.setSetting(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR));
                    break;
                case USER:
                case STRUCTURE:
                    record.setDefaultValue(new ArrayList<>(0));
                    break;
                default:
                    record.setSetting(new ArrayList<>());
                    break;
            }
        }
    }

    @Override
    public List<OaExamineField> queryFieldByBatchId(Integer categoryId, String batchId) {
        return examineFieldMapper.queryFieldByBatchId(categoryId, batchId);
    }

    @Override
    public void transferFieldList(List<OaExamineField> recordList, Integer isDetail) {
        recordList.forEach(record -> {
            Integer dataType = record.getType();
            if (isDetail == 2) {
                if (FieldEnum.USER.getType().equals(dataType)) {
                    if (ObjectUtil.isNotEmpty(record.getValue())) {
                        Result<List<SimpleUser>> listResult = adminService.queryUserByIds(TagUtil.toLongSet((String) record.getValue()));
                        record.setValue(listResult.getData());
                    }
                } else if (FieldEnum.STRUCTURE.getType().equals(dataType)) {
                    if (ObjectUtil.isNotEmpty(record.getValue())) {
                        Result<List<SimpleDept>> listResult = adminService.queryDeptByIds(TagUtil.toSet((String) record.getValue()));
                        record.setValue(listResult.getData());
                    }
                }else if (FieldEnum.CHECKBOX.getType().equals(dataType)) {
                    if (ObjectUtil.isNotEmpty(record.getValue())) {
                        String[] split = StrUtil.split((String) record.getValue(), ",");
                        record.setValue(split);
                    }
                }
            } else {
                if (FieldEnum.USER.getType().equals(dataType)) {
                    if (ObjectUtil.isNotEmpty(record.getValue())) {
                        Result<List<SimpleUser>> listResult = adminService.queryUserByIds(TagUtil.toLongSet((String) record.getValue()));
                        String value = listResult.getData().stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
                        record.setValue(value);
                    }
                } else if (FieldEnum.STRUCTURE.getType().equals(dataType)) {
                    if (ObjectUtil.isNotEmpty(record.getValue())) {
                        Result<List<SimpleDept>> listResult = adminService.queryDeptByIds(TagUtil.toSet((String) record.getValue()));
                        String value = listResult.getData().stream().map(SimpleDept::getName).collect(Collectors.joining(","));
                        record.setValue(value);
                    }
                }
            }
            if (dataType.equals(FieldEnum.FILE.getType())) {
                if (ObjectUtil.isNotEmpty(record.getValue())) {
                    Result<List<FileEntity>> fileList = adminFileService.queryFileList((String) record.getValue());
                    record.setValue(fileList.getData());
                }
            }
        });
    }

    @Autowired
    private IOaExamineCategoryService categoryService;

    @Autowired
    private IOaExamineDataService examineDataService;

    @Override
    public void saveField(ExamineFieldBO examineFieldBO) {
        List<OaExamineField> data = examineFieldBO.getData();
        Map<String, List<OaExamineField>> collect = data.stream().collect(Collectors.groupingBy(OaExamineField::getName));
        for (Map.Entry<String, List<OaExamineField>> entry : collect.entrySet()) {
            if (entry.getValue().size() > 1) {
                throw new CrmException(OaCodeEnum.THE_NAME_OF_THE_CUSTOM_FORM_CANNOT_BE_REPEATED);
            }
        }
        Integer categoryId = examineFieldBO.getCategoryId();
        OaExamineCategory category = categoryService.getById(categoryId);
        if (category != null && Objects.equals(category.getIsSys(),1)) {
            throw new CrmException(OaCodeEnum.SYSTEM_EXAMINE_CAN_NOT_MODIFY);
        }
        List<Integer> arr = data.stream().filter(oaExamineField -> oaExamineField.getFieldId() != null).map(OaExamineField::getFieldId).collect(Collectors.toList());
        if (arr.size() > 0) {
            getBaseMapper().deleteByChooseId(arr, categoryId);
            getBaseMapper().deleteByFieldValue(arr, categoryId);
        }
        for (int i = 0; i < data.size(); i++) {
            OaExamineField entity = data.get(i);
            entity.setUpdateTime(DateUtil.date());
            if (entity.getFieldType() == null || entity.getFieldType() == 0) {
                entity.setFieldName(entity.getName());
            }
            entity.setExamineCategoryId(categoryId);
            entity.setSorting(i);
            if (entity.getFieldId() != null) {
                if (!(entity.getDefaultValue() instanceof String)) {
                    entity.setDefaultValue(entity.getDefaultValue().toString());
                }
                updateById(entity);
                examineDataService.lambdaUpdate().set(OaExamineData::getName,entity.getName()).eq(OaExamineData::getFieldId,entity.getFieldId()).update();
            } else {
                save(entity);
            }
        }
    }
}
