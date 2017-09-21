package com.gg.framelibrary;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.gg.baselibrary.base.BaseActivity;
import com.gg.framelibrary.skin.SkinManager;
import com.gg.framelibrary.skin.attr.SkinAttr;
import com.gg.framelibrary.skin.attr.SkinView;
import com.gg.framelibrary.skin.callback.ISkinChangeListener;
import com.gg.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.gg.framelibrary.skin.support.SkinSupport;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/8/24.
 */

public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory, ISkinChangeListener {


    private static final String TAG = "BaseSkinActivity";
    //留出位置    添加功能

    private SkinAppCompatViewInflater mAppCompatViewInflater;
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, this);


        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        //拦截到Views的创建  获取View之后要去解析

        //1 创建View

        View view = createView(parent, name, context, attrs);

        //2 解析属性 src textColor background 自定义属性
        if (view != null) {
            List<SkinAttr> skinAttrs = SkinSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrs);

            //3 统一交给SkinManager管理

            managerSkinView(skinView);

            //4 判断一下 需不需要换肤
            SkinManager.getInstance().checkChangeSkin(skinView);
        }


        Log.w(TAG, "view -> " + view);


        return view;
    }


    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unregister(this);

        super.onDestroy();
    }

    /**
     * SkinView的统一管理方法
     *
     * @param skinView
     */
    private void managerSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this, skinViews);
        }
        skinViews.add(skinView);
    }


    public View createView(View parent, final String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }


    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = this.getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

}
