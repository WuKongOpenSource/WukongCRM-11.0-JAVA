package com.kakarote.oa.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.examine.entity.ExamineInfoVo;
import com.kakarote.core.feign.examine.service.ExamineService;
import com.kakarote.core.utils.ExcelParseUtil;
import com.kakarote.oa.entity.BO.AuditExamineBO;
import com.kakarote.oa.entity.BO.ExamineExportBO;
import com.kakarote.oa.entity.BO.ExaminePageBO;
import com.kakarote.oa.entity.BO.GetExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import com.kakarote.oa.entity.PO.OaExamineField;
import com.kakarote.oa.entity.VO.ExamineVO;
import com.kakarote.oa.service.IOaExamineFieldService;
import com.kakarote.oa.service.IOaExamineService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 审批表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/oaExamine")
@Slf4j
public class OaExamineController {

    @Autowired
    private IOaExamineService oaExamineService;

    @Autowired
    private IOaExamineFieldService examineFieldService;

    @Autowired
    private ExamineService examineService;

    @ApiOperation("我发起的审批")
    @PostMapping("/myInitiate")
    public Result<BasePage<ExamineVO>> myInitiate(@RequestBody ExaminePageBO examinePageBO) {
        BasePage<ExamineVO> page = oaExamineService.myInitiate(examinePageBO);
        return Result.ok(page);
    }


    @ApiOperation("我审批的")
    @PostMapping("/myOaExamine")
    public Result<BasePage<ExamineVO>> myOaExamine(@RequestBody ExaminePageBO examinePageBO) {
        BasePage<ExamineVO> page = oaExamineService.myOaExamine(examinePageBO);
        return Result.ok(page);
    }

    @ApiExplain("获取指定的审批信息")
    @PostMapping("/getOaExamineById")
    public Result<ExamineVO> getOaExamineById(@RequestParam("oaExamineId") Integer oaExamineId) {
        ExamineVO examineVO = oaExamineService.getOaExamineById(oaExamineId);
        return Result.ok(examineVO);
    }


    @ApiOperation("查询详情或比编辑字段")
    @PostMapping("/getField")
    public Result<List> getField(@RequestBody GetExamineFieldBO getExamineFieldBO){
        if (StrUtil.isNotEmpty(getExamineFieldBO.getType())) {
            return Result.ok(oaExamineService.getField(getExamineFieldBO));
        }
        return Result.ok(oaExamineService.getFormPositionField(getExamineFieldBO));
    }


    @PostMapping("/setOaExamine")
    @ApiOperation("创建审批")
    public Result setOaExamine(@RequestBody JSONObject jsonObject){
        oaExamineService.setOaExamine(jsonObject);
        return Result.ok();
    }


    @PostMapping("/auditExamine")
    @ApiOperation("审批")
    public Result auditExamine(@RequestBody AuditExamineBO auditExamineBO){
        oaExamineService.oaExamine(auditExamineBO);
        return Result.ok();
    }


    @PostMapping("/queryOaExamineInfo/{examineId}")
    @ApiOperation("查询审批详情")
    public Result<ExamineVO> queryOaExamineInfo(@PathVariable String examineId){
        ExamineVO examineVO = oaExamineService.queryOaExamineInfo(examineId);
        return Result.ok(examineVO);
    }


    @PostMapping("/queryExamineRecordList")
    @ApiOperation("查询审批步骤")
    public Result<JSONObject> queryExamineRecordList(@RequestParam("recordId") String recordId){
        JSONObject jsonObject = oaExamineService.queryExamineRecordList(recordId);
        return Result.ok(jsonObject);
    }


    @PostMapping("/queryExamineLogList")
    @ApiOperation("查询审批历史")
    public Result<List<JSONObject>> queryExamineLogList(@RequestParam("recordId") Integer recordId){
        List<JSONObject> jsonObjects = oaExamineService.queryExamineLogList(recordId);
        return Result.ok(jsonObjects);
    }


    @PostMapping("/deleteOaExamine")
    @ApiOperation("删除审批")
    public Result deleteOaExamine(@RequestParam("examineId") Integer examineId){
        oaExamineService.deleteOaExamine(examineId);
        return Result.ok();
    }

    /**
     * @author hmb
     * 查询审批步骤
     */
    @PostMapping("/queryExaminStep")
    @ApiOperation("查询审批步骤")
    public Result<OaExamineCategory> queryExaminStep(@RequestParam("categoryId") String categoryId){
        OaExamineCategory examineCategory = oaExamineService.queryExaminStep(categoryId);
        return Result.ok(examineCategory);
    }




    /**
     * 导出
     */
    @PostMapping("/export")
    @ApiOperation("查询审批步骤")
    public void export(@RequestBody ExamineExportBO examineExportBO,HttpServletResponse response) {
        Integer categoryId = examineExportBO.getCategoryId();
        ExamineInfoVo examineInfoVo = examineService.queryExamineById(Long.valueOf(categoryId)).getData();
        examineExportBO.setCategoryId(examineInfoVo.getExamineInitId().intValue());
        Integer type = examineInfoVo.getOaType();
        List<OaExamineField> fieldList = new ArrayList<>();
        if (type == 0) {
            fieldList = examineFieldService.lambdaQuery().eq(OaExamineField::getExamineCategoryId,categoryId).list();
        }
        List<Map<String, Object>> list = oaExamineService.export(examineExportBO,examineInfoVo,fieldList);

        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("category", "审批类型"));
        dataList.add(ExcelParseUtil.toEntity("createTime", "创建日期"));
        dataList.add(ExcelParseUtil.toEntity("createUserName", "创建人"));
        dataList.add(ExcelParseUtil.toEntity("examineStatus", "状态"));
        dataList.add(ExcelParseUtil.toEntity("examineUserName", "当前审批人"));
        switch (type) {
            case 1:
                dataList.add(ExcelParseUtil.toEntity("content", "审批内容"));
                dataList.add(ExcelParseUtil.toEntity("remark", "备注"));
                break;
            case 2:
                dataList.add(ExcelParseUtil.toEntity("content", "审批内容"));
                dataList.add(ExcelParseUtil.toEntity("startTime", "开始时间"));
                dataList.add(ExcelParseUtil.toEntity("endTime", "结束时间"));
                dataList.add(ExcelParseUtil.toEntity("duration", "时长"));
                dataList.add(ExcelParseUtil.toEntity("remark", "备注"));
                break;
            case 3:
                dataList.add(ExcelParseUtil.toEntity("content", "出差事由"));
                dataList.add(ExcelParseUtil.toEntity("remark", "备注"));
                dataList.add(ExcelParseUtil.toEntity("duration", "出差总天数"));
                dataList.add(ExcelParseUtil.toEntity("vehicle", "交通工具"));
                dataList.add(ExcelParseUtil.toEntity("trip", "单程往返"));
                dataList.add(ExcelParseUtil.toEntity("startAddress", "出发城市"));
                dataList.add(ExcelParseUtil.toEntity("endAddress", "目的城市"));
                dataList.add(ExcelParseUtil.toEntity("startTime", "开始时间"));
                dataList.add(ExcelParseUtil.toEntity("endTime", "结束时间"));
                dataList.add(ExcelParseUtil.toEntity("travelDuration", "时长"));
                dataList.add(ExcelParseUtil.toEntity("description", "出差备注"));
                break;
            case 4:
                dataList.add(ExcelParseUtil.toEntity("content", "加班原因"));
                dataList.add(ExcelParseUtil.toEntity("startTime", "开始时间"));
                dataList.add(ExcelParseUtil.toEntity("endTime", "结束时间"));
                dataList.add(ExcelParseUtil.toEntity("duration", "加班总天数"));
                dataList.add(ExcelParseUtil.toEntity("remark", "备注"));
                break;
            case 5:
                dataList.add(ExcelParseUtil.toEntity("content", "差旅内容"));
                dataList.add(ExcelParseUtil.toEntity("money", "报销总金额"));
                dataList.add(ExcelParseUtil.toEntity("remark", "备注"));
                dataList.add(ExcelParseUtil.toEntity("startAddress", "出发城市"));
                dataList.add(ExcelParseUtil.toEntity("endAddress", "目的城市"));
                dataList.add(ExcelParseUtil.toEntity("startTime", "开始时间"));
                dataList.add(ExcelParseUtil.toEntity("endTme", "结束时间"));
                dataList.add(ExcelParseUtil.toEntity("traffic", "交通费"));
                dataList.add(ExcelParseUtil.toEntity("stay", "住宿费"));
                dataList.add(ExcelParseUtil.toEntity("diet", "餐饮费"));
                dataList.add(ExcelParseUtil.toEntity("other", "其他费用"));
                dataList.add(ExcelParseUtil.toEntity("travelMoney", "合计"));
                dataList.add(ExcelParseUtil.toEntity("description", "费用明细描述"));
                break;
            case 6:
                dataList.add(ExcelParseUtil.toEntity("content", "审批内容"));
                dataList.add(ExcelParseUtil.toEntity("money", "借款金额"));
                dataList.add(ExcelParseUtil.toEntity("remark", "备注"));
                break;
            case 0:
                fieldList.forEach(field -> dataList.add(ExcelParseUtil.toEntity(field.getFieldName(), field.getName())));
                break;
            default:
                break;
        }
        dataList.add(ExcelParseUtil.toEntity("relateCrmWork", "关联业务"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "日志";
            }
        }, dataList);
    }

    @PostMapping("/transfer")
    @ApiOperation("转换审批")
    public Result<List<ExamineVO>> transfer(@RequestBody List<ExamineVO> recordList){
        List<ExamineVO> transfer = oaExamineService.transfer(recordList);
        return Result.ok(transfer);
    }



    @ApiOperation("查询字段")
    @PostMapping("/queryExamineField")
    public Result<List<ExamineField>> queryExamineField(@RequestParam("categoryId") Integer categoryId){
        List<OaExamineField> oaExamineFields = examineFieldService.queryField(categoryId);
        List<ExamineField> examineFields = new ArrayList<>();
        oaExamineFields.forEach(oaExamineField -> {
            boolean isNeed = Objects.equals(oaExamineField.getIsNull(),1) && ListUtil.toList(3,5,6,9).contains(oaExamineField.getType());
            if (isNeed){
                examineFields.add(BeanUtil.copyProperties(oaExamineField, ExamineField.class));
            }
        });
        return Result.ok(examineFields);
    }

    @ApiOperation("查询条件字段")
    @PostMapping("/queryConditionData")
    public Result<Map<String, Object>> getDataMapForNewExamine(@RequestBody ExamineConditionDataBO examineConditionDataBO){
        return Result.ok(oaExamineService.getDataMapForNewExamine(examineConditionDataBO));
    }

    @PostMapping("/updateCheckStatusByNewExamine")
    public Result<Boolean> updateCheckStatusByNewExamine(@RequestBody ExamineConditionDataBO examineConditionDataBO){
        return Result.ok(oaExamineService.updateCheckStatusByNewExamine(examineConditionDataBO));
    }
}

