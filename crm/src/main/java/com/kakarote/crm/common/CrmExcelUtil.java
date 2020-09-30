package com.kakarote.crm.common;


import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmExcelUtil {

    public CrmExcelUtil() {
    }

    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

    /**
     * offset为主列, 市引用省一列的值，区引用市一列的值
     */
    public static void setDataValidation(String offset, HSSFSheet sheet, int rowNum, int colNum) {
        Integer i = rowNum + 1;
        DVConstraint formula = DVConstraint.createFormulaListConstraint("INDIRECT($" + offset + "$" + i + ")");
        CellRangeAddressList rangeAddressList = new CellRangeAddressList(rowNum, rowNum, colNum, colNum);
        HSSFDataValidation cacse = new HSSFDataValidation(rangeAddressList, formula);
        cacse.createErrorBox("error", "请选择正确的地区");
        sheet.addValidationData(cacse);
    }

    /**
     * 判断数字和excel字符列对应关系
     * @param columnNo 列
     * @return 对应列字符
     */
    public static String getCorrespondingLabel(int columnNo) {
        if (columnNo < 1 || columnNo > 16384) {
            throw new IllegalArgumentException();
        }
        String[] sources = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"
                , "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder sb = new StringBuilder(5);
        //求最右边的字母
        int remainder = columnNo % 26;
        //说明(num_n-1)=26，第26个字母是Z
        if (remainder == 0) {
            sb.append("Z");
            //因为接下来W-(num_n-1)也就是columnNo-remainder,所以需要把remainder赋值回26
            remainder = 26;
        } else {  //如果最右边字母不是Z的话，就去sources数组相应的位置取字母，remainder不用变
            sb.append(sources[remainder - 1]);
        }
        //用来判断接下来是否还有其他字母
        columnNo = (columnNo - remainder) / 26 - 1;

        //当前循环是求最后一个字母时（从右往左），(columnNo-remainder)/26就会是0，再减1也就是-1。
        //因此通过判断(columnNo-remainder)/26-1是否大于-1来判断结束
        while (columnNo > -1) {
            remainder = columnNo % 26;
            sb.append(sources[remainder]);
            columnNo = (columnNo - remainder) / 26 - 1;
        }
        //因为是从右往左解析的 所以需要反转
        return sb.reverse().toString();
    }

    /**
     * 获取市区对应关系
     * @return 对应关系
     */
    @SuppressWarnings("unchecked")
    public static Map<String,List<String>> getAreaMap() {
        HashMap<String,List<String>> map = null;
        try {
            map = JSONObject.parseObject(CrmExcelUtil.class.getClassLoader().getResourceAsStream("area.json"), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
