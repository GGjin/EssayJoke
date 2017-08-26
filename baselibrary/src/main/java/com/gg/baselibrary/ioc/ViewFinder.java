package com.gg.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by GG on 2017/8/22.
 */

public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(View view) {
        mView = view;
    }

    public ViewFinder(Activity activity) {
        mActivity = activity;
    }

    public View findViewById(int viewId) {
        return mActivity != null ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
    }
}
