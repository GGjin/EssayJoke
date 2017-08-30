package com.gg.baselibrary.network;

import android.content.Context;

import java.util.Map;

/**
 * Created by GG on 2017/8/30.
 */

public interface IHttpEngine {

    <T> void get(Context context, String url, Map<String, Object> params,
                    final HttpCallBack<T> callback, final boolean cache);

    <T> void post(Context context, String url, Map<String, Object> params,
                     final HttpCallBack<T> callback, final boolean cache);

}
