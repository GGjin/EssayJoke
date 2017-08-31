package com.gg.framelibrary;

import android.content.Context;

import com.gg.baselibrary.network.EngineCallBack;

import java.util.Map;

/**
 * Created by GG on 2017/8/30.
 */

public abstract class HttpCallBack<T> implements EngineCallBack {


    public abstract void onSuccess(T request);

    public void onPreExecute() {

    }

    ;

    public void onSuccess(String request) {


    }

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        //做一些 公共参数的初始化  然后 添加开始网络请求的回调   需要时重写方法 就获取了回调

        onPreExecute();
    }
}
