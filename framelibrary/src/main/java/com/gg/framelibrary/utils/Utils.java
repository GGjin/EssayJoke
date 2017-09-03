package com.gg.framelibrary.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by GG on 2017/9/1.
 */

public class Utils {

    private Utils() {
    }


    public static String jointParams(String url, Map<String, Object> params) {

        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuilder stringBuilder = new StringBuilder(url);
        if (!url.contains("?")) {
            stringBuilder.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuilder.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    /**
     * 解析一个类上面的class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
