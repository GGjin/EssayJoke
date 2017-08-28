package com.gg.baselibrary.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by GG on 2017/8/29.
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    private View mParentView;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }

    private void createAndBindView() {

        if (mParams.mParent == null) {
            ViewGroup activityRoot = (ViewGroup) ((Activity) (mParams.mContext)).findViewById(android.R.id.content);
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }


        //需要单独处理Activity  Activity的实现方式和AppCompatActivity的不一样
        if (mParams.mParent == null) {
            return;
        }

        mParentView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(), mParams.mParent, false);

        mParams.mParent.addView(mParentView, 0);

        applyViews();
    }


    public void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (TextUtils.isEmpty(text)) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
    }

    public <T extends View> T findViewById(int viewId) {
        return (T) mParentView.findViewById(viewId);
    }


    public abstract static class Builder {

        public AbsNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            P = new AbsNavigationParams(context, parent);
        }

        public Builder(Context context) {
            P = new AbsNavigationParams(context, null);
        }

        public abstract AbsNavigationBar create();


        public static class AbsNavigationParams {

            public Context mContext;

            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                mContext = context;
                mParent = parent;
            }


        }


    }
}
