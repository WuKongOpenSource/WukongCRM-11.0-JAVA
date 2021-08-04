package com.kakarote.hrm.utils;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.awt.Color;

/**
 * @author hmb
 */
public final class SalaryExcelUtil {

    private static final Color HEAD_COLOR = new Color(197, 217, 241);

    /**
     * 创建单元格并填充值
     * @param wb 工作边
     * @param row 行
     * @param column 列
     * @param value 需要填充的值
     */
    public static void createHeadCell(XSSFWorkbook wb, Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(new XSSFColor(HEAD_COLOR, new DefaultIndexedColorMap()));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        XSSFFont font = wb.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    public static void createBodyCell(XSSFWorkbook wb, Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = wb.createFont();
        font.setBold(false);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }
}
