package com.gg.essayjoke;

import android.support.v7.widget.RecyclerView;

import com.gg.framelibrary.BaseSkinActivity;
import com.gg.framelibrary.skin.SkinResource;

import butterknife.BindView;

/**
 * Creator : GG
 * Time    : 2017/10/6
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public class RecyclerViewActivity extends BaseSkinActivity {
    @BindView(R.id.rv) RecyclerView mRv;

    @Override
    public void changeSkin(SkinResource skinResource) {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initArguments() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initDate() {

    }
}
