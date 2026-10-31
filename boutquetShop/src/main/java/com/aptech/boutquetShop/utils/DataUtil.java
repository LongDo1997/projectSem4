package com.aptech.boutquetShop.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

public class DataUtil {

    public static String queryLike(String request) {
        return !isNullOrEmpty(request) ? "%" + NVL(escapeString(request), "") + "%" : null;
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget) {
        return convertFromQueryResult(listSource, classTarget,"dd/MM/yyyy");
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget, String dateFormat) {
        Map<String, String> dateFormats = new HashMap<>();
        dateFormats.put("ALL", dateFormat);
        return convertFromQueryResult(listSource, classTarget, dateFormats);
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget, Map<String, String> dateFormats) {
        try {
            List<T> result = new ArrayList<>();
            Constructor<?> cons;
            Object target;
            List<Field> targetFields;
            for (Tuple sourceItem : listSource) {
                cons = classTarget.getConstructor();
                target = cons.newInstance();
                targetFields = getFields(classTarget);
                for (Field targetFieldItem : targetFields) {
                    setFieldData(sourceItem, targetFieldItem, target, dateFormats);
                }
                result.add((T) target);
            }
            return result;
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static <T> List<Field> getFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class clazz = (Class) t;
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
    private static void setFieldData(
            Tuple sourceItem, Field targetFieldItem, Object target, Map<String, String> dateFormats
    ) throws IllegalAccessException, ParseException {
        List<TupleElement<?>> sourceFields = sourceItem.getElements();
        String fieldName = targetFieldItem.getName().toLowerCase();
        targetFieldItem.setAccessible(true);
        Class<?> targetFieldType = targetFieldItem.getType();
        String targetFieldTypeName = targetFieldType.getName();
        String sourceFieldName, sourceFieldNameRemoveUnderscore, dateFormat;
        Object sourceItemData;
        for (TupleElement<?> sourceFieldItem : sourceFields) {
            sourceFieldName = sourceFieldItem.getAlias();
            sourceFieldNameRemoveUnderscore = sourceFieldItem.getAlias().replace("_", "");
            if (!fieldName.equalsIgnoreCase(sourceFieldNameRemoveUnderscore)) continue;
            if (dateFormats.size() == 1) {
                dateFormat = dateFormats.get("ALL");
            } else {
                dateFormat = dateFormats.get(sourceFieldName);
            }
            sourceItemData = sourceItem.get(sourceFieldName);
            if (sourceItemData == null) continue;
            if (String.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                if (sourceItemData instanceof String || sourceItemData instanceof Long || sourceItemData instanceof Character) {
                    targetFieldItem.set(target, sourceItemData);
                } else if (sourceItemData instanceof Date) {
                    targetFieldItem.set(target, sourceItemData);
                }
            } else if (Long.class.getName().equalsIgnoreCase(targetFieldTypeName) || long.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, sourceItemData);
            } else if (Double.class.getName().equalsIgnoreCase(targetFieldTypeName) || double.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, sourceItemData);
            } else if (Boolean.class.getName().equalsIgnoreCase(targetFieldTypeName) || boolean.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, "true".equalsIgnoreCase((String) sourceItemData));
            } else if (Date.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, sourceItemData);
            } else if (Timestamp.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, sourceItemData);
            } else if (BigDecimal.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, sourceItemData);
            } else if (Integer.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, sourceItemData);
            }
        }
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        if (cs == null || ((String) cs).isEmpty()) {
            return true;
        }
        int strLen = cs.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String NVL(String s, String defaultStr) {
        return Objects.isNull(s) ? defaultStr : s;
    }

    public static String escapeString(String str) {
        if(str != null && str.contains("_")){
            str = str.replace("_","\\_");
        }
        if(str != null && str.contains("%")){
            str = str.replace("%","\\%");
        }
        return str;
    }



}