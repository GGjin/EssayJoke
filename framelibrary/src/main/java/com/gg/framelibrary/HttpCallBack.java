package com.gg.framelibrary;

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

        onPreExecute();
    }
}
