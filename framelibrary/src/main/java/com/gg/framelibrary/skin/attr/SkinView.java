package com.gg.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by GG on 2017/9/16.
 */

public class SkinView {

    private View mView;

    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    public void skin(){
        for (SkinAttr skinAttr : mSkinAttrs) {
            skinAttr.skin(mView);
        }
    }
}
