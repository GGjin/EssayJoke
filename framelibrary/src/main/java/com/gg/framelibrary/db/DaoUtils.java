package com.gg.framelibrary.db;

/**
 * Created by GG on 2017/9/3.
 */

public class DaoUtils {

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getComlumnType(String type) {
        String value = null;

        if (type.contains("String")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        }
        return value;
    }


}
