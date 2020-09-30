package com.kakarote.core.common;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

public class RangeValidator implements ConstraintValidator<RangeValidated,Object> {

    private String[] minFieldName;
    private String[] maxFieldName;


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Class<?> currentClass = value.getClass();
        try {
            boolean bol = true;
            for (int i = 0; i < minFieldName.length; i++) {
                Field mintField = currentClass.getDeclaredField(minFieldName[i]);
                Field maxField = currentClass.getDeclaredField(maxFieldName[i]);
                mintField.setAccessible(true);
                maxField.setAccessible(true);
                if (mintField.get(value) == null || maxField.get(value) == null){
                    continue;
                }
                if (mintField.getType().equals(Integer.class)) {
                    Integer min = (Integer)mintField.get(value);
                    Integer max = (Integer)maxField.get(value);
                    if (min == -1 && max == -1){
                        continue;
                    }
                    bol = min<max;
                }else if (mintField.getType().equals(Double.class)){
                    Double min = (Double)mintField.get(value);
                    Double max = (Double)maxField.get(value);
                    if (min == -1 && max == -1){
                        continue;
                    }
                    bol = min<max;
                }else if (mintField.getType().equals(Long.class)){
                    Long min = (Long)mintField.get(value);
                    Long max = (Long)maxField.get(value);
                    if (min == -1 && max == -1){
                        continue;
                    }
                    bol = min<max;
                }else if (mintField.getType().equals(BigDecimal.class)){
                    BigDecimal min = (BigDecimal)mintField.get(value);
                    BigDecimal max = (BigDecimal)maxField.get(value);
                    if (min.equals(new BigDecimal(-1)) && min.equals(new BigDecimal(-1))){
                        continue;
                    }
                    bol = min.compareTo(max)<0;
                }else if (mintField.getType().equals(Date.class)){
                    Date min = (Date)mintField.get(value);
                    Date max = (Date)maxField.get(value);
                    bol = min.getTime()<max.getTime();
                }
            }
            return bol;
        }  catch (Exception e) {
            return false;
        }
    }

    @Override
    public void initialize(RangeValidated constraintAnnotation) {
        minFieldName = constraintAnnotation.minFieldName();
        maxFieldName = constraintAnnotation.maxFieldName();
    }
}
