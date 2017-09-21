package com.gg.framelibrary.http;

import android.content.Context;
import android.text.TextUtils;

import com.gg.baselibrary.network.EngineCallBack;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by GG on 2017/8/30.
 */

public abstract class HttpCallBack<T> implements EngineCallBack {


    public abstract void onSuccess(T t);

    public void onPreExecute() {

    }

    ;

    public void onSuccess(String request) {

        if ( !TextUtils.isEmpty(request)) {
            Gson gson = new Gson();
            // data:{"name","darren"}   data:"请求失败"
            T objResult = (T) gson.fromJson(request, Utils.analysisClazzInfo(this));
            onSuccess(objResult);
        }

    }

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        //做一些 公共参数的初始化  然后 添加开始网络请求的回调   需要时重写方法 就获取了回调
        // 公共参数
        params.put("app_name", "joke_essay");
        params.put("version_name", "5.7.0");
        params.put("ac", "wifi");
        params.put("device_id", "30036118478");
        params.put("device_brand", "Xiaomi");
        params.put("update_version_code", "5701");
        params.put("manifest_version_code", "570");
        params.put("longitude", "113.000366");
        params.put("latitude", "28.171377");
        params.put("device_platform", "android");
        onPreExecute();
    }
}
