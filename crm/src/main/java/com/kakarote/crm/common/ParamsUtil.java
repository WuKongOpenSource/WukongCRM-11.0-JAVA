package com.kakarote.crm.common;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author wyq
 */
public class ParamsUtil {

    public static final BigDecimal SYSTEM_MAX_MONEY = new BigDecimal(999999999999999L);

    public static boolean isValid(String param){
        if(param==null){
            return true;
        }
        String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
                + "(\\b(select|update|union|and|or|delete|insert|trancate|char|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

        Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

        if (sqlPattern.matcher(param).find()) {
            return false;
        }
        return true;
    }

    public static boolean isOutMoney(BigDecimal money){
        if (money == null){
            return false;
        }
        return money.compareTo(SYSTEM_MAX_MONEY)>0;
    }
}
