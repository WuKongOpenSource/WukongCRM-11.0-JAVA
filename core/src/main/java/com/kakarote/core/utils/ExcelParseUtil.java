package com.kakarote.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.FieldEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ExcelParseUtil {

    /**
     * 统一导出数据模板
     */
    public static void exportExcel(List<? extends Map<String, Object>> dataList, ExcelParseService excelParseService, List<?> list) {
        exportExcel(dataList, excelParseService, list, BaseUtil.getResponse());
    }


    /**
     * 统一导出数据模板
     */
    public static void exportExcel(List<? extends Map<String, Object>> dataList, ExcelParseService excelParseService, List<?> list, HttpServletResponse response) {
        try (ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(excelParseService.isXlsx())) {
            List<ExcelDataEntity> headList = excelParseService.parseData(list, false);
            Map<String, Integer> headMap = new HashMap<>(headList.size(), 1.0f);
            headList.forEach(head -> {
                writer.addHeaderAlias(head.getFieldName(), head.getName());
                if (Arrays.asList(FieldEnum.AREA.getType(), FieldEnum.AREA_POSITION.getType(), FieldEnum.CURRENT_POSITION.getType(), FieldEnum.DETAIL_TABLE.getType()).contains(head.getType())) {
                    headMap.put(head.getFieldName(), head.getType());
                }
            });
            // 取消数据的黑色边框以及数据左对齐
            CellStyle cellStyle = writer.getCellStyle();
            cellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderTop(BorderStyle.NONE);
            cellStyle.setBorderBottom(BorderStyle.NONE);
            cellStyle.setBorderLeft(BorderStyle.NONE);
            cellStyle.setBorderRight(BorderStyle.NONE);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            Font defaultFont = writer.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            cellStyle.setFont(defaultFont);
            // 取消数字格式的数据的黑色边框以及数据左对齐
            CellStyle cellStyleForNumber = writer.getStyleSet().getCellStyleForNumber();
            cellStyleForNumber.setBorderTop(BorderStyle.NONE);
            cellStyleForNumber.setBorderBottom(BorderStyle.NONE);
            cellStyleForNumber.setBorderLeft(BorderStyle.NONE);
            cellStyleForNumber.setBorderRight(BorderStyle.NONE);
            cellStyleForNumber.setAlignment(HorizontalAlignment.LEFT);
            cellStyleForNumber.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
            cellStyleForNumber.setFont(defaultFont);
            // 设置数据
            dataList.forEach(record -> excelParseService.castData(record, headMap));
            //设置行高以及列宽
            writer.setRowHeight(-1, 20);
            writer.setColumnWidth(-1, 20);
            //只保留别名中的字段
            writer.setOnlyAlias(true);
            if (dataList.size() == 0) {
                Map<String, Object> record = new HashMap<>();
                headList.forEach(head -> record.put(head.getFieldName(), ""));
                writer.write(Collections.singletonList(record), true);
            } else {
                writer.write(dataList, true);
            }
            CellStyle style = writer.getHeadCellStyle();
            style.setAlignment(HorizontalAlignment.LEFT);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelParseService.getExcelName() + "信息", "utf-8") + ".xls" + (excelParseService.isXlsx() ? "x" : ""));
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出客户错误：", e);
        }
    }

    /**
     * 统一下载导入模板
     */
    public static void importExcel(ExcelParseService excelParseService, List<?> list) {
        importExcel(excelParseService, list, BaseUtil.getResponse(),null);
    }

    /**
     * 统一下载导入模板
     */
    public static void importExcel(ExcelParseService excelParseService, List<?> list, HttpServletResponse response,String module) {
        List<ExcelDataEntity> dataEntities = excelParseService.parseData(list, true);
        try (ExcelWriter writer = ExcelUtil.getWriter(excelParseService.isXlsx())) {
            //因为重复合并单元格会导致样式丢失，所以先获取全部字段一次合并
            int sum = dataEntities.stream().mapToInt(data -> excelParseService.addCell(null, 0, 0, data.getFieldName())).sum();
            writer.renameSheet(excelParseService.getExcelName() + "导入模板");
            writer.merge(dataEntities.size() - 1 + sum, excelParseService.getMergeContent(module), true);
            writer.getHeadCellStyle().setAlignment(HorizontalAlignment.LEFT);
            writer.getHeadCellStyle().setWrapText(true);
            Font headFont = writer.createFont();
            headFont.setFontHeightInPoints((short) 11);
            writer.getHeadCellStyle().setFont(headFont);
            writer.getHeadCellStyle().setFillPattern(FillPatternType.NO_FILL);
            writer.getOrCreateRow(0).setHeightInPoints(120);
            writer.setRowHeight(-1, 20);
            //设置样式
            for (int i = 0, k = dataEntities.size(), z = 0; i < k; i++, z++) {
                ExcelDataEntity dataEntity = dataEntities.get(i);
                //会新增cell或者对当前cell做调整，直接跳过默认处理
                int n = excelParseService.addCell(writer, z, 1, dataEntity.getFieldName());
                if (n > 0) {
                    z += n;
                    continue;
                }
                CellStyle columnStyle = writer.getOrCreateColumnStyle(z);
                //设置统一字体
                columnStyle.setFont(headFont);
                DataFormat dateFormat = writer.getWorkbook().createDataFormat();
                if (Objects.equals(dataEntities.get(i).getType(), FieldEnum.DATE.getType())) {
                    columnStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATE_PATTERN));
                } else if (Objects.equals(dataEntities.get(i).getType(), FieldEnum.DATETIME.getType())) {
                    columnStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATETIME_PATTERN));
                } else {
                    columnStyle.setDataFormat(dateFormat.getFormat("@"));
                }
                writer.setColumnWidth(z, 20);
                Cell cell = writer.getOrCreateCell(z, 1);
                //必填字段的特殊处理
                if (Objects.equals(1, dataEntity.getIsNull())) {
                    cell.setCellValue("*" + dataEntity.getName());
                    CellStyle cellStyle = writer.getOrCreateCellStyle(z, 1);
                    Font cellFont = writer.createFont();
                    cellFont.setFontHeightInPoints((short) 11);
                    cellFont.setColor(cellFont.COLOR_RED);
                    cellStyle.setFont(cellFont);
                    cell.setCellStyle(cellStyle);
                } else {
                    cell.setCellValue(dataEntity.getName());
                }
                //选择类型增加下拉框
                if (CollUtil.isNotEmpty(dataEntity.getSetting())) {
                    String[] array = dataEntity.getSetting().stream().map(data->{
                        if(data instanceof JSONObject && ((JSONObject) data).containsKey("name")){
                            return ((JSONObject) data).getString("name");
                        }
                        return data.toString();
                    }).toArray(String[]::new);
                    writer.addSelect(new CellRangeAddressList(2, 10002, z, z), array);
                }
            }
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelParseService.getExcelName() + "导入模板", "utf-8") + ".xls" + (excelParseService.isXlsx() ? "x" : ""));
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("下载" + excelParseService.getExcelName() + "导入模板错误", e);
        }

    }

    @Data
    public static class ExcelDataEntity {

        /* 字段名称 */
        private String fieldName;

        /* 展示名称 */
        private String name;

        /* 字段类型 */
        private Integer type;

        /* 是否必填 1 是 0 否 */
        private Integer isNull;

        /* 设置列表 */
        private List<Object> setting;

        public ExcelDataEntity() {
        }

        public ExcelDataEntity(String fieldName, String name, Integer type) {
            this.fieldName = fieldName;
            this.name = name;
            this.type = type;
        }
    }

    public static abstract class ExcelParseService {


        /**
         * 统一处理数据
         *
         * @param list        请求头数据
         * @param importExcel 是否是导入模板
         * @return 转化后的请求头数据
         */
        public List<ExcelDataEntity> parseData(List<?> list, boolean importExcel) {
            List<ExcelDataEntity> entities = list.stream().map(obj -> {
                if (obj instanceof ExcelDataEntity) {
                    return (ExcelDataEntity) obj;
                }
                return BeanUtil.copyProperties(obj, ExcelDataEntity.class);
            }).collect(Collectors.toList());
            if (importExcel) {
                entities.removeIf(head -> ExcelParseUtil.removeFieldByType(head.getType()));
            } else {
                entities.removeIf(head -> FieldEnum.HANDWRITING_SIGN.getType().equals(head.getType()));
            }
            return entities;
        }

        /**
         * 自定义数据处理，不需要处理直接返回原数据即可
         *
         * @param record 列
         */
        public abstract void castData(Map<String, Object> record, Map<String, Integer> headMap);

        /**
         * 获取excel表格名称
         *
         * @return 表格名称
         */
        public abstract String getExcelName();

        /**
         * 导入的时候需要的可能需要新增字段场景
         *
         * @param writer    writer
         * @param x         – X坐标，从0计数，即列号
         * @param y         – Y坐标，从0计数，即行号
         * @param fieldName 字段名称
         * @return 新增的行数
         */
        public int addCell(ExcelWriter writer, Integer x, Integer y, String fieldName) {
            return 0;
        }

        /**
         * 是否是xlsx格式，xlsx导出会比xlx3倍左右，谨慎使用
         *
         * @return isXlsx
         */
        public boolean isXlsx() {
            return false;
        }

        public String getMergeContent(String module) {
           if (module != null && module.equals("user")){
                return "注意事项：\n" +
                        "1、表头标“*”的红色字体为必填项\n" +
                        "2、手机号：目前只支持中国大陆的11位手机号码；且手机号不允许重复\n" +
                        "3、登录密码：密码由6-20位字母、数字组成\n" +
                        "4、部门：上下级部门间用\"/\"隔开，且从最上级部门开始，例如“上海分公司/市场部/市场一部”。如出现相同的部门，则默认导入组织架构中顺序靠前的部门\n";
            }else {
                return "注意事项：\n" +
                        "1、表头标“*”的红色字体为必填项\n" +
                        "2、日期时间：推荐格式为2020-02-02 13:13:13\n" +
                        "3、日期：推荐格式为2020-02-02\n" +
                        "4、手机号：支持6-15位数字（包含国外手机号格式）\n" +
                        "5、邮箱：只支持邮箱格式\n" +
                        "6、多行文本：字数限制为800字";
            }
        }


    }

    /**
     * 不支持导入的字段
     */
    private static final List<Integer> typeList = Arrays.asList(FieldEnum.FILE.getType(), FieldEnum.CHECKBOX.getType(), FieldEnum.USER.getType(), FieldEnum.STRUCTURE.getType(),
            FieldEnum.AREA.getType(), FieldEnum.AREA_POSITION.getType(), FieldEnum.CURRENT_POSITION.getType(), FieldEnum.DATE_INTERVAL.getType(), FieldEnum.BOOLEAN_VALUE.getType(),
            FieldEnum.HANDWRITING_SIGN.getType(), FieldEnum.DESC_TEXT.getType(), FieldEnum.DETAIL_TABLE.getType(), FieldEnum.CALCULATION_FUNCTION.getType()
    );


    /**
     * 删除不支持导入的字段
     *
     * @return true为不支持导入
     */
    public static boolean removeFieldByType(Integer type) {
        return typeList.contains(type);
    }

    public static ExcelDataEntity toEntity(String fieldName, String name) {
        return new ExcelDataEntity(fieldName,name,FieldEnum.TEXT.getType());
    }

    public static ExcelDataEntity toEntity(String fieldName, String name, Integer type) {
        return new ExcelDataEntity(fieldName,name,type);
    }
}
