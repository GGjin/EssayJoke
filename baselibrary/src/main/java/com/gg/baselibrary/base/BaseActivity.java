package com.gg.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gg.baselibrary.ioc.ViewUtils;

/**
 * Created by GG on 2017/8/24.
 * Activity的基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());

        ViewUtils.inject(this);

        initTitle();

        initView();

        initDate();

    }

    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化头部
     */
    protected abstract void initTitle();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initDate();

    /**
     * 启动Activity
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 带Bundle的启动Activity
     *
     * @param clazz
     * @param bundle
     */
    protected void startActivity(Class<?> clazz,@Nullable Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * @param clazz
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode) {
        startActivityForResult(clazz, requestCode, null);
    }


    /**
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode,@Nullable Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
