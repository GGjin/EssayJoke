package com.gg.essayjoke;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.gg.baselibrary.navigationbar.AbsNavigationBar;


/**
 * Created by GG on 2017/8/29.
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {


    public DefaultNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);

    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyViews() {
        setText(R.id.middle_text, "title");

        setIcon(R.id.left_img, getParams().mLeftIconId);

        setIcon(R.id.middle_img, getParams().mMiddleIconId);

        setIcon(R.id.right_icon, getParams().mRightIconId);

        setText(R.id.left_text, getParams().mLeftText);

        setText(R.id.middle_text, getParams().mMiddleText);

        setText(R.id.right_text, getParams().mRightText);

        if (getParams().mLeftClickListener != null) {
            setOnClickListener(R.id.left_view, getParams().mLeftClickListener);
        }

        if (getParams().mMiddleClickListener != null) {
            setOnClickListener(R.id.middle_view, getParams().mLeftClickListener);
        }

        if (getParams().mRightClickListener != null) {
            setOnClickListener(R.id.right_view, getParams().mRightClickListener);
        }

        if (!TextUtils.isEmpty(getParams().mText))
            setText(getParams().mViewId, getParams().mText);

        if (getParams().mClickListener != null)
            setOnClickListener(getParams().mViewId, getParams().mClickListener);

    }

    public static class Builder extends AbsNavigationBar.Builder {

        DefaultNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context);
            P = new DefaultNavigationParams(context, null);
        }

        public Builder setText(int viewId, String text) {
            P.mViewId = viewId;
            P.mText = text;
            return this;
        }


        public Builder setClickListener(int viewId, View.OnClickListener listener) {
            P.mViewId = viewId;
            P.mClickListener = listener;
            return this;
        }


        @Override
        public DefaultNavigationBar create() {
            return new DefaultNavigationBar(P);
        }

        public Builder setLeftText(String text) {
            P.mLeftText = text;
            return this;
        }

        public Builder setMiddleText(String text) {
            P.mMiddleText = text;
            return this;
        }

        public Builder setRightText(String text) {
            P.mRightText = text;
            return this;
        }

        public Builder setLeftIcon(int imgId) {
            P.mLeftIconId = imgId;
            return this;
        }

        public Builder setMiddleIcon(int imgId) {
            P.mMiddleIconId = imgId;
            return this;
        }

        public Builder setRightIcon(int imgId) {
            P.mRightIconId = imgId;
            return this;
        }


        public Builder setLeftClickListener(View.OnClickListener listener) {
            P.mLeftClickListener = listener;
            return this;
        }

        public Builder setMiddleClickListener(View.OnClickListener listener) {
            P.mMiddleClickListener = listener;
            return this;
        }

        public Builder setRightClickListener(View.OnClickListener listener) {
            P.mRightClickListener = listener;
            return this;
        }

        public class DefaultNavigationParams extends AbsNavigationBar.Builder.AbsNavigationParams {

            public String mLeftText;
            public String mMiddleText;
            public String mRightText;
            public View.OnClickListener mLeftClickListener;
            public View.OnClickListener mMiddleClickListener;
            public View.OnClickListener mRightClickListener;
            public int mViewId;
            public String mText;
            public View.OnClickListener mClickListener;
            public int mLeftIconId = 0;
            public int mMiddleIconId = 0;
            public int mRightIconId = 0;

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
