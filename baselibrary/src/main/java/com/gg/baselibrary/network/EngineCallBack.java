package com.gg.baselibrary.network;

import android.content.Context;

import java.util.Map;

/**
 * Created by GG on 2017/8/31.
 */

public interface EngineCallBack {
    public abstract void onSuccess(String request);

    public abstract void onFailure(Exception e);

    public abstract void onPreExecute(Context context, Map<String,Object> params);

}
