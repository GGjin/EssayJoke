package com.gg.essayjoke;

import android.app.Application;

import com.gg.baselibrary.fixbug.FixDexManager;
import com.gg.baselibrary.ExceptionCrashHandler;

/**
 * Created by GG on 2017/8/25.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);


        try {
            FixDexManager fixDexManager = new FixDexManager(this);
            fixDexManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
