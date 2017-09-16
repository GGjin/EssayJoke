package com.gg.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

/**
 * Created by GG on 2017/9/16.
 */

public class SkinResource {

    private Resources mSkinResources;
    private String mPackageName;

    public SkinResource(Context context, String skinPath) {
        try {
            Resources resources = context.getResources();
            AssetManager assetManager = AssetManager.class.newInstance();

            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
            //设置从哪里获取到皮肤的资源
            method.invoke(assetManager, skinPath);

            mSkinResources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());


            mPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Drawable getDrawableByName(String name) {
        try {
            int resId = mSkinResources.getIdentifier(name, "drawable", mPackageName);
            return mSkinResources.getDrawable(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ColorStateList getColorByName(String name) {
        try {
            int resId = mSkinResources.getIdentifier(name, "drawable", mPackageName);
            return mSkinResources.getColorStateList(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
