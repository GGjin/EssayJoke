package com.gg.framelibrary.skin.config;

import android.content.Context;

/**
 * Creator:  GG
 * Time   :  2017/9/20.
 * Mail   :  gg.jin.yu@gmail.com
 * Explain:
 */

public class SkinPreUtils {

    private static SkinPreUtils mInstance;

    private Context mContext;

    private SkinPreUtils() {
    }

    public static SkinPreUtils getInstance() {
        if (mInstance == null) {
            synchronized (SkinPreUtils.class) {
                if (mInstance == null)
                    mInstance = new SkinPreUtils();
            }
        }
        return mInstance;
    }

    public SkinPreUtils init(Context context) {
        mContext = context.getApplicationContext();
        return mInstance;
    }


    /**
     * 保存当前皮肤的路径
     *
     * @param path
     */
    public void saveSkinPath(String path) {
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE).edit().putString(SkinConfig.SKIN_PATH_NAME, path).apply();
    }

    /**
     * 获取当前皮肤的路径
     * @return
     */
    public String getSkinPath(){
      return   mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE).getString(SkinConfig.SKIN_PATH_NAME,"");
    }


    public void clearSkinInfo() {

        saveSkinPath("");
    }
}
