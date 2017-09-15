package com.gg.framelibrary.utils;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    /**
     * 组装 post 的请求 body
     *
     * @param params
     * @return
     */
    public static RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    private static void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                builder.addFormDataPart(key, value + "");
                if (value instanceof File) {//提交的是文件
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                } else if (value instanceof List) {//提交的是 list 集合
                    try {
                        List<File> fileList = (List<File>) value;
                        for (int i = 0; i < fileList.size(); i++) {
                            File file = fileList.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     *
     * @param path
     * @return
     */
    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
