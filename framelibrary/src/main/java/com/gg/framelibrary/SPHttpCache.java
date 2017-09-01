package com.gg.framelibrary;

import android.content.Context;

/**
 * Created by GG on 2017/9/1.
 */

public class SPHttpCache {

    public void saveCache(Context context,String finalUrl, String resultJson){
        PreferencesUtil.getInstance().init(context).saveParam(finalUrl,resultJson);
    }

    public String getCache(Context context,String finalUrl){
        return (String) PreferencesUtil.getInstance().init(context).getObject(finalUrl);
    }
}
