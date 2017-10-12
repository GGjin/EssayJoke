package com.gg.essayjoke.selectimage;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.elvishew.xlog.XLog;
import com.gg.essayjoke.R;
import com.gg.essayjoke.selectimage.model.SelectImageBean;
import com.gg.essayjoke.utils.GsonUtil;
import com.gg.essayjoke.utils.ImageLoader;
import com.gg.essayjoke.utils.KeyWord;
import com.gg.essayjoke.utils.SharedPreferencesHelper;
import com.gg.framelibrary.BaseSkinActivity;
import com.gg.framelibrary.skin.SkinResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Creator : GG
 * Time    : 2017/10/11
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public class PhotoListActivity extends BaseSkinActivity {


    @BindView(R.id.photo_rv) RecyclerView mPhotoRv;
    @BindView(R.id.compile) TextView mCompile;
    @BindView(R.id.real_photo) TextView mRealPhoto;
    @BindView(R.id.select) TextView mSelect;
    @BindView(R.id.back) ImageView mBack;
    @BindView(R.id.num) TextView mNum;
    @BindView(R.id.publish) TextView mPublish;

    private ArrayList<SelectImageBean> mImages;
    private ArrayList<SelectImageBean> mSelectImages;
    private int mPosition;
    private PhotoListAdapter mPhotoListAdapter;

    @Override
    public void changeSkin(SkinResource skinResource) {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_photo_list;
    }

    @Override
    protected void initArguments() {
        Intent intent = getIntent();
        mSelectImages = intent.getParcelableArrayListExtra(SelectImageActivity.KEY_SELECT_IMAGES);
        mPosition = intent.getIntExtra(SelectImageActivity.KEY_POSITION, 0);

        String imageInfo = SharedPreferencesHelper.getInstance().init(this).getStringValue(KeyWord.IMAGE_PATH);
        mImages = (ArrayList<SelectImageBean>) GsonUtil.jsonToList(imageInfo, SelectImageBean.class);
        imageProgress(mPosition);
    }

    private void imageProgress(int count) {
        mNum.setText(count + "/" + mImages.size());
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

        mPhotoListAdapter = new PhotoListAdapter(mImages);


        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        pagerSnapHelper.findSnapView(linearLayoutManager);

        mPhotoRv.setLayoutManager(linearLayoutManager);

        pagerSnapHelper.attachToRecyclerView(mPhotoRv);

        mPhotoRv.setAdapter(mPhotoListAdapter);

        mPhotoRv.scrollToPosition(mPosition);

        mPhotoListAdapter.setScorllItemListener(new ScorllItemListener() {
            @Override
            public void getItemPosition(int position) {
                XLog.e(position);
                imageProgress(position);
            }
        });

        XLog.e(linearLayoutManager.findFirstVisibleItemPosition());

    }

    @Override
    protected void initDate() {

    }

    @OnClick({R.id.compile, R.id.real_photo, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.compile:
                break;
            case R.id.real_photo:
                break;
            case R.id.select:
                break;
        }
    }


    public class PhotoListAdapter extends BaseQuickAdapter<SelectImageBean, BaseViewHolder> {

        private ScorllItemListener mScorllItemListener;

        public PhotoListAdapter(@Nullable List<SelectImageBean> data) {
            super(R.layout.item_photo, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, SelectImageBean item) {
            if (!TextUtils.isEmpty(item.getPath())) {
                ImageLoader.getInstance().displayImage(
                        mContext, item.getPath(), (ImageView) holder.getView(R.id.image));
            }

            if (mScorllItemListener != null) {
                mScorllItemListener.getItemPosition(holder.getAdapterPosition());
            }
        }

        public void setScorllItemListener(ScorllItemListener scorllItemListener) {
            mScorllItemListener = scorllItemListener;
        }


    }
}
