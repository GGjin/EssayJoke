package com.gg.essayjoke.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Creator : GG
 * Time    : 2017/10/6
 * Mail    : gg.jin.yu@gmail.com
 * Explain : an adapter can add header and footer
 */

public class WrapRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter mRealAdapter;

    //HeaderView list
    private ArrayList<View> mHeaderViews;
    //FooterView list
    private ArrayList<View> mFooterViews;


    public WrapRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        mRealAdapter = adapter;

        mRealAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }
        });


        mHeaderViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        // Header (negative positions will throw an IndexOutOfBoundsException)
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return createHeaderAndFooterViewHolder(mHeaderViews.get(position));
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mRealAdapter != null) {
            adapterCount = mRealAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                //introduction Real itemViewType, not position
                return mRealAdapter.createViewHolder(parent,mRealAdapter.getItemViewType(adjPosition));
            }
        }

        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
        return createHeaderAndFooterViewHolder(mFooterViews.get(adjPosition - adapterCount));
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    /**
     * create ViewHolder for header view
     *
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder createHeaderAndFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mHeaderViews.size() + mRealAdapter.getItemCount() + mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * add header view
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (!mHeaderViews.contains(view)) {
            mHeaderViews.add(view);
            notifyDataSetChanged();
        }
    }

    /**
     * add footer view
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (!mFooterViews.contains(view)) {
            mFooterViews.add(view);
            notifyDataSetChanged();
        }
    }

    /**
     * remove header view
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        if (mHeaderViews.contains(view)) {
            mHeaderViews.remove(view);
            notifyDataSetChanged();
        }
    }

    /**
     * remove footer view
     *
     * @param view
     */
    public void removeFooterView(View view) {
        if (mFooterViews.contains(view)) {
            mFooterViews.remove(view);
            notifyDataSetChanged();
        }
    }


}
