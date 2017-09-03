package com.gg.framelibrary.http;

import android.content.Context;
import android.util.Log;

import com.gg.baselibrary.network.EngineCallBack;
import com.gg.baselibrary.network.IHttpEngine;
import com.gg.framelibrary.utils.SPHttpCache;
import com.gg.framelibrary.utils.Utils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by GG on 2017/8/30.
 */

public class OKHttpEngine implements IHttpEngine {
    private SPHttpCache mHttpCache;

    public OKHttpEngine(){
        mHttpCache = new SPHttpCache();
    }

    @Override
    public void get(final Context context, String url, Map<String, Object> params, final EngineCallBack callback, final boolean cache) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
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

        final String jointUrl = Utils.jointParams(url, params);  //打印
        // 缓存问题
        Log.e("Post请求路径：", jointUrl);  // 缓存写到  SP 里面，多级缓存（内存中 30条,数据库 ，文件中 ）
        final String cacheJson = mHttpCache.getCache(context,jointUrl);
        // 写一大堆处理逻辑 ，内存怎么扩展等等
//        if (cache && !TextUtils.isEmpty(cacheJson)) {
//            Gson gson = new Gson();
//            // data:{"name","darren"}   data:"请求失败"
//            T objResult = (T) gson.fromJson(cacheJson, Utils.analysisClazzInfo(callback));
//            callback.onSuccess(objResult);
//        }

        Request.Builder requestBuilder = new Request.Builder().url(jointUrl).tag(context);
        //可以省略，默认是GET请求
        Request request = requestBuilder.build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                // 失败
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultJson = response.body().string();

                Log.e("TAG",resultJson.equals(cacheJson)+"");
                if (cache && resultJson.equals(cacheJson)) {
                    return;
                }
//                // 1.JSON解析转换
//                // 2.显示列表数据
//                // 3.缓存数据
//                Gson gson = new Gson();
//                // data:{"name","darren"}   data:"请求失败"
//                T objResult = (T) gson.fromJson(resultJson,
//                        Utils.analysisClazzInfo(callback));
                callback.onSuccess(resultJson);

                if (cache) {
                    mHttpCache.saveCache(context,jointUrl, resultJson);
                }
            }
        });
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, EngineCallBack callback, boolean cache) {

    }
}
