package com.gg.essayjoke.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gg.essayjoke.R;
import com.gg.essayjoke.model.SelectImageBean;
import com.gg.essayjoke.utils.ImageLoader;

import java.util.List;

/**
 * Creator :  GG
 * Time    :  2017/9/27
 * Mail    :  gg.jin.yu@gmail.com
 * Explain :  adapter for select image
 */

public class SelectImageAdapter extends BaseQuickAdapter<SelectImageBean, BaseViewHolder> {

    public SelectImageAdapter(@Nullable List<SelectImageBean> data) {
        super(R.layout.item_select_image, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SelectImageBean item) {
        if (TextUtils.isEmpty(item.getPath())) {// show camera image
            holder.setImageResource(R.id.select_img, R.drawable.ic_camera).setVisible(R.id.select_state_img, false);
        } else {
            Log.e("item" , item.getPath());
            ImageLoader.getInstance().displayImage(mContext,item.getPath(),(ImageView) holder.getView(R.id.select_img));
        }
    }
}
