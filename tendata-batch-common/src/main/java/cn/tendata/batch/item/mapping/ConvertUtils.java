package cn.tendata.batch.item.mapping;

import java.text.ParseException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import cn.tendata.batch.item.mapping.MappingField.FieldType;
import cn.tendata.batch.util.JodaDateTimeUtils;

public abstract class ConvertUtils {

    public static Object convertTypedValue(Object val, MappingField field){
        Object target;
        switch (field.getType()) {
            case DATETIME:
                target = convertToDateTime(prepareToString(val), field);
                break;
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
                target = convertToNumber(prepareToString(val), field);
                break;
            case BOOL:
                target = convertToBool(prepareToString(val), field);
                break;
            default:
                target = normalizeWhitespace(prepareToString(val));
                break;
        }
        return target;
    }
    
    private static String prepareToString(Object obj){
        return StringUtils.trim(obj.toString());
    }
    
    private static String normalizeWhitespace(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
       return str.replaceAll(" +", " ");
      //  return str.replaceAll("\\s+", " ");
    }
    
    private static DateTime convertToDateTime(String str, MappingField field){
        try {
            return JodaDateTimeUtils.parseDateTime(str, field.getPatterns());
        } catch (ParseException e) {
            throw new IllegalStateException(
                    String.format("parse date error, name: %s, val: %s, patterns: %s",
                            field.getName(), str, field.getPatterns()), e);
        }
    }

    private static boolean convertToBool(String str, MappingField field){
        try {
            return BooleanUtils.toBooleanObject(str);
        } catch (Exception e) {
            throw new IllegalStateException(
                String.format("parse date to boolean error, name: %s, val: %s",
                    field.getName(), str), e);
        }
    }
    
    private static Number convertToNumber(String str, MappingField field){
        try {
            if(field.getType().equals(FieldType.INT))
                return NumberUtils.toInt(str);
            if(field.getType().equals(FieldType.LONG))
                return NumberUtils.toLong(str);
            if(field.getType().equals(FieldType.FLOAT))
                return NumberUtils.toFloat(str);
            if(field.getType().equals(FieldType.DOUBLE))
                return NumberUtils.toDouble(str);
            return NumberUtils.createNumber(str);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(String.format("parse number error, name: %s, val: %s", 
                    field.getName(), str), e);
        }
    }
}
