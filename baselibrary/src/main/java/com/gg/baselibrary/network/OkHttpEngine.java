package com.gg.baselibrary.network;

import android.content.Context;

import java.util.Map;

/**
 * Created by GG on 2017/8/30.
 */

public class OkHttpEngine implements IHttpEngine {



    @Override
    public <T> void get(Context context, String url, Map<String, Object> params, HttpCallBack<T> callback, boolean cache) {

    }

    @Override
    public <T> void post(Context context, String url, Map<String, Object> params, HttpCallBack<T> callback, boolean cache) {

    }
}
