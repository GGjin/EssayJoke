package com.gg.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.gg.framelibrary.skin.attr.SkinView;
import com.gg.framelibrary.skin.callback.ISkinChangeListener;
import com.gg.framelibrary.skin.config.SkinConfig;
import com.gg.framelibrary.skin.config.SkinPreUtils;

import java.io.File;
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

    private Map<ISkinChangeListener, List<SkinView>> mSkinViews = new ArrayMap<>();

    private SkinManager() {
    }


    public static SkinManager getInstance() {
        if (mSkinManager == null) {
            synchronized (SkinManager.class) {
                if (mSkinManager == null)
                    mSkinManager = new SkinManager();
            }
        }
        return mSkinManager;
    }


    public void init(Context context) {
        mContext = context.getApplicationContext();


        String currentSkinPath = SkinPreUtils.getInstance().init(mContext).getSkinPath();

        if (TextUtils.isEmpty(currentSkinPath)){
            return;
        }

        File file = new File(currentSkinPath);

        if (!file.exists()) {
            //文件不存在的话 就清空皮肤

            SkinPreUtils.getInstance().init(mContext).clearSkinInfo();
            return;
        }

        String mPackageName = mContext.getPackageManager().getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES).packageName;

        if (TextUtils.isEmpty(mPackageName)) {
            //包名获取不到的情况下 也不能够正常换肤

            SkinPreUtils.getInstance().init(mContext).clearSkinInfo();
            return;
        }

        //最好校验一下签名

        //有文件的话 就吧Resource初始化
        mSkinResource = new SkinResource(mContext, currentSkinPath);

    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {

        File file = new File(skinPath);

        //如果传入路径的文件不存在 就清空皮肤
        if (!file.exists()) {
            SkinPreUtils.getInstance().init(mContext).clearSkinInfo();
            return SkinConfig.SKIN_FILE_INEXISTENCE;
        }


        //获取一下包名 看是否能够获取到包名
        String mPackageName = mContext.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;

        if (TextUtils.isEmpty(mPackageName)) {
            //包名获取不到的情况下 也不能够正常换肤
            return SkinConfig.SKIN_FILE_ERROR;
        }


        //当前皮肤如果一样的话 就不需要更换
        String currentSkinPath = SkinPreUtils.getInstance().init(mContext).getSkinPath();

        if (skinPath.equals(currentSkinPath)){
            return SkinConfig.SKIN_CHANGE_FAIL;
        }

        //校验签名

        //初始化资源管理

        mSkinResource = new SkinResource(mContext, skinPath);

        //改变皮肤
        changeSkin();

        //保存皮肤的状态
        saveSkinStatus(skinPath);


        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 保存皮肤的路径
     *
     * @param skinPath
     */
    private void saveSkinStatus(String skinPath) {
        SkinPreUtils.getInstance().init(mContext).saveSkinPath(skinPath);
    }

    /**
     * 修改皮肤
     */
    private void changeSkin() {
        Set<ISkinChangeListener> keys = mSkinViews.keySet();
        for (ISkinChangeListener key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
    }

    /**
     * 恢复默认
     *
     * @return
     */
    public int restoreDefault() {

        //判断当前有没有皮肤，没有皮肤就不要执行任何方法；

        String currentSkinPath = SkinPreUtils.getInstance().init(mContext).getSkinPath();

        //没有皮肤地址的话就返回fail
        if (TextUtils.isEmpty(currentSkinPath))
            return SkinConfig.SKIN_CHANGE_FAIL;

        //获取当前apk 的路径
        String skinPath = mContext.getPackageResourcePath();

        mSkinResource = new SkinResource(mContext, skinPath);

        changeSkin();

        SkinPreUtils.getInstance().clearSkinInfo();

        return SkinConfig.SKIN_CHANGE_SUCCESS;

    }

    public List<SkinView> getSkinViews(ISkinChangeListener activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册  存储view和属性
     *
     * @param skinChangeListener
     * @param skinViews
     */
    public void register(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViews.put(skinChangeListener, skinViews);
    }

    /**
     * 删除对Activity的引用
     *
     * @param skinChangeListener
     */
    public void unregister(ISkinChangeListener skinChangeListener) {
        mSkinViews.remove(skinChangeListener);
    }


    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    /**
     * 检测要不要换肤
     *
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {

        String currentSkinPath = SkinPreUtils.getInstance().init(mContext).getSkinPath();

        //皮肤路径不为空的时候 就更换皮肤
        if (!TextUtils.isEmpty(currentSkinPath)) {
            skinView.skin();
        }
    }
}
