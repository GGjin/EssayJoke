package com.gg.baselibrary.manager;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 * Created by GG on 2017/9/19.
 *
 * Activity的管理类
 */

public class AppManager {


    // Activity栈
    private static Stack<Activity> mActivities;
    // 单例模式
    private static AppManager mInstance;

    private AppManager() {
        mActivities = new Stack<>();
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void detach(Activity activity) {
        int size = mActivities.size();
        for (int i = 0; i < size; i++) {
            Activity a = mActivities.get(i);
            if (a == activity) {
                mActivities.remove(i);
                i--;
                size--;
            }
        }
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return mActivities.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivities.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            int size = mActivities.size();
            for (int i = 0; i < size; i++) {
                Activity a = mActivities.get(i);
                if (a == activity) {
                    mActivities.remove(i);
                    a.finish();
                    i--;
                    size--;
                }

            }
        }

    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        int size = mActivities.size();
        for (int i = 0; i < size; i++) {
            Activity a = mActivities.get(i);
            if (a.getClass().getCanonicalName().equals(cls.getCanonicalName())) {
                mActivities.remove(i);
                a.finish();
                i--;
                size--;
            }
        }
    }

    /**
     * 指定的activity实例是否存活
     *
     * @param activity
     * @return
     */
    public boolean activityInstanceIsLive(Activity activity) {
        if (mActivities == null) {
            Log.w("AppManager", "activityStack == null when activityInstanceIsLive");
            return false;
        }
        return mActivities.contains(activity);
    }

    /**
     * 指定的activity class是否存活(一个activity可能有多个实例)
     *
     * @param activityClass
     * @return
     */
    public boolean activityClassIsLive(Class<?> activityClass) {
        if (mActivities == null) {
            Log.w("AppManager", "mActivityList == null when activityClassIsLive");
            return false;
        }
        for (Activity activity : mActivities) {
            if (activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < mActivities.size(); i++) {
            if (null != mActivities.get(i)) {
                mActivities.get(i).finish();
            }
        }
        mActivities.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
