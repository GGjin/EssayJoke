/**
 *
 */
package com.gg.framelibrary.utils;

import android.content.Context;
import android.widget.Toast;

import com.gg.baselibrary.manager.AppManager;


public class ToastUtil {

    public static void showLong(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String info) {
        Toast.makeText(AppManager.getAppManager().currentActivity(), info, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int info) {
        Toast.makeText(AppManager.getAppManager().currentActivity(), info, Toast.LENGTH_LONG).show();
    }

    public static void showShort(int info) {
        Toast.makeText(AppManager.getAppManager().currentActivity(), info, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String info) {
        Toast.makeText(AppManager.getAppManager().currentActivity(), info, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}





