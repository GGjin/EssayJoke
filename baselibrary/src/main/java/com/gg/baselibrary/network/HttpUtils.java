package com.gg.baselibrary.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.Map;

/**
 * Created by GG on 2017/8/30.
 */

public class HttpUtils {

    private final int TYPE_POST = 0x0001;
    private final int TYPE_GET = 0x0002;


    private Context mContext;

    private int mType = TYPE_GET;

    private Map<String, Object> mParams;

    private IHttpEngine mHttpEngine;
    private String mUrl;


    private HttpUtils(Context context) {
        mContext = context;
        mParams = new ArrayMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    public HttpUtils get() {
        mType = TYPE_GET;
        return this;
    }

    public HttpUtils post() {
        mType = TYPE_POST;
        return this;
    }

    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addparams(Map<String, Object> map) {
        mParams.putAll(map);
        return this;
    }

    public HttpUtils setEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
        return this;
    }

    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }


    public void execute(HttpCallBack callBack) {

        if (TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("请传入获取的网址");
        }

        switch (mType) {
            case TYPE_GET:
                mHttpEngine.get(mContext, mUrl, mParams, callBack, false);
                break;
            case TYPE_POST:
                mHttpEngine.post(mContext, mUrl, mParams, callBack, false);
                break;
        }

    }

    public void execute() {
        execute(null);
    }


}
