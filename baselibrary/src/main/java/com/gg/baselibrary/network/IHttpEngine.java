package com.gg.baselibrary.network;

import android.content.Context;

import java.util.Map;

/**
 * Created by GG on 2017/8/30.
 */

public interface IHttpEngine {

     void get(Context context, String url, Map<String, Object> params,
                    final EngineCallBack callback, final boolean cache);

     void post(Context context, String url, Map<String, Object> params,
                     final EngineCallBack callback, final boolean cache);

}
