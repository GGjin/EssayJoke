package com.gg.essayjoke.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Creator : GG
 * Time    : 2017/10/6
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public class WrapRecyclerView extends RecyclerView {


    private WrapRecyclerViewAdapter mRecyclerViewAdapter;

    public WrapRecyclerView(Context context) {
        super(context);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        mRecyclerViewAdapter = new WrapRecyclerViewAdapter(adapter);
        super.setAdapter(adapter);
    }

    /**
     * add header view
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (mRecyclerViewAdapter != null)
            mRecyclerViewAdapter.addHeaderView(view);
    }

    /**
     * add footer view
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (mRecyclerViewAdapter != null)
            mRecyclerViewAdapter.addFooterView(view);

    }

    /**
     * remove header view
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        if (mRecyclerViewAdapter != null)
            mRecyclerViewAdapter.removeHeaderView(view);
    }

    /**
     * remove footer view
     *
     * @param view
     */
    public void removeFooterView(View view) {
        if (mRecyclerViewAdapter != null)
            mRecyclerViewAdapter.removeFooterView(view);
    }


}
