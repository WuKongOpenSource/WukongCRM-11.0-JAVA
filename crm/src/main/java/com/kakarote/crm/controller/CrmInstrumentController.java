package com.kakarote.crm.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmSearchParamsBO;
import com.kakarote.crm.entity.PO.CrmActivity;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.CrmInstrumentService;
import com.kakarote.crm.service.ICrmInstrumentSortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * CRM仪表盘 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
@Slf4j
@RestController
@RequestMapping("/crmInstrument/")
@Api(tags = "仪表盘相关接口")
public class CrmInstrumentController {

    @Autowired
    private ICrmInstrumentSortService sortService;

    @Autowired
    private CrmInstrumentService instrumentService;



    @PostMapping("/queryModelSort")
    @ApiOperation("查询模块排序")
    public Result<JSONObject> queryModelSort() {
        JSONObject object = sortService.queryModelSort();
        return Result.ok(object);
    }

    @PostMapping("/setModelSort")
    @ApiOperation("修改模块排序")
    public Result setModelSort(@RequestBody JSONObject jsonObject) {
        sortService.setModelSort(jsonObject);
        return R.ok();
    }

    @PostMapping("/queryBulletin")
    @ApiOperation("查询销售简报")
    public Result<JSONObject> queryBulletin(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.queryBulletin(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/queryBulletinInfo")
    @ApiOperation("查询销售简报详情")
    public Result<BasePage<Map<String, Object>>> queryBulletinInfo(@RequestBody BiParams biParams) {
        BasePage<Map<String, Object>> basePage = instrumentService.queryBulletinInfo(biParams);
        return R.ok(basePage);
    }

    @PostMapping("/forgottenCustomerCount")
    @ApiOperation("遗忘客户统计")
    public Result<JSONObject> forgottenCustomerCount(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.forgottenCustomerCount(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/sellFunnel")
    @ApiOperation("销售漏斗")
    public Result<JSONObject> sellFunnel(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.sellFunnel(biParams);
        return R.ok(jsonObject);
    }


    @PostMapping("/sellFunnelBusinessList")
    @ApiOperation("销售漏斗商机状态列表")
    public Result<BasePage<Map<String, Object>>> sellFunnelBusinessList(@RequestBody CrmSearchParamsBO crmSearchParamsBO) {
        BasePage<Map<String, Object>> mapBasePage = instrumentService.sellFunnelBusinessList(crmSearchParamsBO);
        return R.ok(mapBasePage);
    }

    @PostMapping("/salesTrend")
    @ApiOperation("销售趋势")
    public Result<JSONObject> salesTrend(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.salesTrend(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/queryDataInfo")
    @ApiOperation("数据汇总")
    public Result<JSONObject> queryDataInfo(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.queryDataInfo(biParams);
        return R.ok(jsonObject);
    }

    @PostMapping("/queryPerformance")
    @ApiOperation("业绩指标")
    public Result<JSONObject> queryPerformance(@RequestBody BiParams biParams) {
        JSONObject jsonObject = instrumentService.queryPerformance(biParams);
        return R.ok(jsonObject);
    }


    @PostMapping("/queryRecordList")
    @ApiOperation("查询跟进记录统计列表")
    public Result<BasePage<CrmActivity>> queryRecordList(@RequestBody BiParams biParams){
        BasePage<CrmActivity> page = instrumentService.queryRecordList(biParams);
        return Result.ok(page);
    }


    @PostMapping("/queryRecordCount")
    @ApiOperation("查询销售简报的跟进记录统计")
    public Result<List<JSONObject>> queryRecordCount(@RequestBody BiParams biParams) {
        List<JSONObject> data = instrumentService.queryRecordCount(biParams);
        return Result.ok(data);
    }

    /**
     * 遗忘客户列表
     */
    @PostMapping("/forgottenCustomerPageList")
    @ApiOperation("遗忘客户列表")
    public Result<BasePage<Map<String,Object>>> forgottenCustomerPageList(@RequestBody BiParams biParams){
        BasePage<Map<String,Object>> page = instrumentService.forgottenCustomerPageList(biParams);
        return Result.ok(page);
    }


    @PostMapping("/unContactCustomerPageList")
    @ApiOperation("未联系客户列表")
    public Result<BasePage<Map<String,Object>>> unContactCustomerPageList(@RequestBody BiParams biParams){
        BasePage<Map<String,Object>> page = instrumentService.unContactCustomerPageList(biParams);
        return Result.ok(page);
    }


    @PostMapping("/importRecordList")
    @ApiOperation("excel导入跟进记录")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_IMPORT,object = "excel导入跟进记录",detail = "excel导入跟进记录")
    public Result<JSONObject> importRecordList(@RequestParam("file") MultipartFile file,@RequestParam("crmType") Integer crmType) {
        JSONObject object = instrumentService.importRecordList(file,crmType);
        return R.ok(object);
    }


    @PostMapping("/downloadRecordExcel")
    @ApiOperation("获取导入模板")
    public void downloadExcel(HttpServletResponse response,@RequestParam("crmType") Integer crmType) {
        //客户 ：跟进内容（必填）、创建人（必填）、所属客户（必填）、跟进时间、跟进方式、相关联系人、相关商机
        //非客户：跟进内容（必填）、创建人（必填）、所属线索/联系人/商机/合同（必填）、跟进时间、跟进方式
        if (!Arrays.asList(1,2,3,5,6).contains(crmType)){
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        List<CrmModelFiledVO> filedList = new ArrayList<>();
        filedList.add(new CrmModelFiledVO("content", FieldEnum.TEXT, "*跟进内容", 1));
        filedList.add(new CrmModelFiledVO("createUserName", FieldEnum.TEXT, "*创建人", 1));
        if (crmType == 2){
            filedList.add(new CrmModelFiledVO("crmTypeName", FieldEnum.TEXT, "*所属客户", 1));
        }else {
            filedList.add(new CrmModelFiledVO("crmTypeName", FieldEnum.TEXT, "*所属" + crmEnum.getRemarks(), 1));
        }
        filedList.add(new CrmModelFiledVO("createTime", FieldEnum.DATE, "跟进时间-例:2020-2-1", 1));
        filedList.add(new CrmModelFiledVO("category", FieldEnum.TEXT, "跟进方式", 1));
        if (crmType == 2){
            filedList.add(new CrmModelFiledVO("contactsNames", FieldEnum.TEXT, "相关联系人", 1));
            filedList.add(new CrmModelFiledVO("businessNames", FieldEnum.TEXT, "相关商机", 1));
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("跟进记录导入表");
        sheet.setDefaultRowHeight((short) 400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < filedList.size(); i++) {
            CrmModelFiledVO crmModelFiledVO = filedList.get(i);
            sheet.setDefaultColumnStyle(i, textStyle);
            sheet.setColumnWidth(i, 20 * 256);
        }
        try {
            HSSFRow hintRow = sheet.createRow(0);
            String desc = CrmEnum.CONTRACT.equals(crmEnum) ? "编号" : "名称";
            hintRow.createCell(0).setCellValue("注意事项：\n" +
                            "1、表头标“*”的红色字体为必填项\n" +
                            "2、跟进时间：推荐格式为2020-2-1\n" +
                            "3、若相关数据有多条时用“/”区分例如：杭州科技有限公司／卡卡罗特软件科技有限公司\n" +
                            "4、所属" + crmEnum.getRemarks() + "中的" + crmEnum.getRemarks() + "需要存在系统中，" +
                                "且填写的所属" + crmEnum.getRemarks() + desc +"与系统中的" + crmEnum.getRemarks() + desc +"必须保持一致否则会导入失败\n" +
                            "5、创建人为系统员工，请填写系统员工“姓名”，若匹配不到系统员工，则会导致导入失败\n" +
                            "6、如果系统中存在多个" + desc + "重复的情况，会默认导入到最新的数据中");
            hintRow.setHeight((short) 2100);
            CellStyle hintStyle = wb.createCellStyle();
            hintStyle.setWrapText(true);
            hintStyle.setAlignment(HorizontalAlignment.LEFT);
            hintRow.getCell(0).setCellStyle(hintStyle);
            CellRangeAddress hintRegion = new CellRangeAddress(0, 0, 0, filedList.size() - 1);
            sheet.addMergedRegion(hintRegion);

            HSSFRow row = sheet.createRow(1);
            row.setHeight((short) 400);
            for (int i = 0; i < filedList.size(); i++) {
                CrmModelFiledVO crmModelFiledVO = filedList.get(i);
                // 在第一行第一个单元格，插入选项
                HSSFCell cell = row.createCell(i);
                if (crmModelFiledVO.getName().contains("*")){
                    HSSFRichTextString richString = new HSSFRichTextString(crmModelFiledVO.getName());
                    HSSFFont wbFont = wb.createFont();
                    wbFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                    richString.applyFont(wbFont);
                    cell.setCellValue(richString);
                    continue;
                }
                // 普通写入操作
                String cellValue = crmModelFiledVO.getName();
                cell.setCellValue(cellValue);
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=record_import.xls");
            wb.write(response.getOutputStream());

        } catch (Exception e) {
            log.error("error",e);
        } finally {
            try {
                wb.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }



    @PostMapping("/exportRecordList")
    @ApiOperation("导出跟进记录")
    public void exportRecordList(@RequestBody BiParams biParams, HttpServletResponse response){
        Integer crmType = biParams.getLabel();
        if (!Arrays.asList(1,2,3,5,6).contains(crmType)){
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        List<CrmActivity> list = instrumentService.exportRecordList(biParams);
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            if (crmType == 2) {
                writer.addHeaderAlias("crmTypeName", "所属客户");
            }
            writer.addHeaderAlias("content", "跟进内容");
            writer.addHeaderAlias("createUserName", "跟进人");
            if (crmType != 2) {
                writer.addHeaderAlias("crmTypeName", "所属" + crmEnum.getRemarks());
            }
            writer.addHeaderAlias("createTime", "跟进时间");
            writer.addHeaderAlias("category", "跟进方式");
            writer.addHeaderAlias("nextTime", "下次联系时间");
            if (crmType == 2) {
                writer.addHeaderAlias("contactsNames", "相关联系人");
                writer.addHeaderAlias("businessNames", "相关商机");
            }
            int columnNum = crmType == 2 ? 8 : 6;
            writer.merge(columnNum - 1, "跟进记录");
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < columnNum; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=crm_record.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出跟进记录错误：", e);
        }
    }

}
