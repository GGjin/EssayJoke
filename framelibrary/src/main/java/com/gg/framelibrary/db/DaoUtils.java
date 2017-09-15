package com.gg.framelibrary.db;

import android.database.Cursor;

import java.lang.reflect.Method;

/**
 * Created by GG on 2017/9/3.
 */

public class DaoUtils {

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type) {
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

    public static Method cursorMethod(Class<?> type) throws Exception {
        String methodName = getColumnMethodName(type);
        return Cursor.class.getMethod(methodName, int.class);
    }

    private static String getColumnMethodName(Class<?> filedType) {
        return null;
    }

}
