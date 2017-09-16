package com.gg.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by GG on 2017/9/16.
 */

public class SkinAttr {


    private String mName;

    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mName = resName;
        this.mSkinType=skinType;
    }


    public void skin(View view) {
        mSkinType.skin(view,mName);
    }
}
