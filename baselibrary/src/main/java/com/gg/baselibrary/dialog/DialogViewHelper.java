package com.gg.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by GG on 2017/8/27.
 */

class DialogViewHelper {

    private View mContentView;
    public SparseArray<WeakReference<View>> mViews;


    public DialogViewHelper(Context context, int themeResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(themeResId,null, false);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public DialogViewHelper(View view) {
        this();
        mContentView = view;
    }

    /**
     * 设置文本的显示
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param weakReference
     */
    public void setOnClickListener(int viewId, WeakReference<View.OnClickListener> weakReference) {
        View view = getView(viewId);
        View.OnClickListener listener = weakReference.get();

        if (view != null) {
            view.setOnClickListener(listener);
        }
    }


    public <T extends View> T getView(int viewId) {
        WeakReference<View> weakReference = mViews.get(viewId);
        View view = null;
        if (weakReference != null) {
            view = weakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null)
                mViews.put(viewId, new WeakReference<>(view));
        }

        return (T) view;
    }


    public View getContentView() {
        return mContentView;
    }
}
