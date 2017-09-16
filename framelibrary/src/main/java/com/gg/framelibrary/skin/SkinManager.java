package com.gg.framelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.util.ArrayMap;

import com.gg.framelibrary.skin.attr.SkinView;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by GG on 2017/9/16.
 * <p>
 * 皮肤的管理类
 */

public class SkinManager {
    private static SkinManager mSkinManager;

    private Context mContext;

    private SkinResource mSkinResource;

    private Map<Activity, List<SkinView>> mSkinViews = new ArrayMap<>();

    static {
        mSkinManager = new SkinManager();
    }

    public static SkinManager getInstance() {
        return mSkinManager;
    }


    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        //校验签名

        //初始化资源管理

        mSkinResource= new SkinResource(mContext, skinPath);

        //改变皮肤
        Set<Activity> keys = mSkinViews.keySet();
        for (Activity key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }


        return 0;
    }

    /**
     * 恢复默认
     *
     * @return
     */
    public int restoreDefault() {


        return 0;

    }

    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册  存储view和属性
     * @param activity
     * @param skinViews
     */
    public void register(Activity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity, skinViews);
    }

    public SkinResource getSkinResource(){
        return mSkinResource;
    }
}
