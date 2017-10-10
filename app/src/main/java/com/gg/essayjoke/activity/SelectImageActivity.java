package com.gg.essayjoke.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gg.essayjoke.R;
import com.gg.essayjoke.model.SelectImageBean;
import com.gg.essayjoke.utils.Api;
import com.gg.essayjoke.utils.GlideApp;
import com.gg.essayjoke.utils.GlideRequest;
import com.gg.essayjoke.utils.GlideRequests;
import com.gg.framelibrary.BaseSkinActivity;
import com.gg.framelibrary.skin.SkinResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Creator:  GG
 * Time   :  2017/9/27
 * Mail   :  gg.jin.yu@gmail.com
 * Explain:
 */

public class SelectImageActivity extends BaseSkinActivity {

    private static final String STATE_POSITION_INDEX = "state_position_index";
    private static final String STATE_POSITION_OFFSET = "state_position_offset";

    private static final int PRELOAD_AHEAD_ITEMS = 15;

    //返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "extra_result";

    //加载所有的数据
    private static final int LOADER_TYPE = 0x0001;

    //照片选择模式  多选
    public static final int MODE_MULTI = 0x0011;
    //照片选择模式  单选
    public static final int MODE_SINGLE = 0x0012;
    //默认的照片选择模式
    private int mMode = MODE_MULTI;
    //图片最大选择数
    private int mMaxCount = 9;
    //设置是否显示相机位置  默认true
    private boolean mShowCamera = true;
    //已经选择好的图片
    private ArrayList<String> mResultList;

    private GridLayoutManager layoutManager;

    private GlideRequest<Drawable> fullRequest;
    private GlideRequest<Drawable> thumbRequest;

    private SelectImageAdapter mSelectImageAdapter;

    private int photoSize;


    @BindView(R.id.image_rv) RecyclerView mImageRv;
    @BindView(R.id.image_video_tv) TextView mImageVideoTv;
    @BindView(R.id.num_tv) TextView mNumTv;
    @BindView(R.id.preview_tv) TextView mPreviewTv;

    @Override
    public void changeSkin(SkinResource skinResource) {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

        initRVList();

    }

    private void initRVList() {


        final GlideRequests glideRequests = GlideApp.with(this);
        fullRequest = glideRequests
                .asDrawable()
                .centerCrop();

        thumbRequest = glideRequests
                .asDrawable()
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(Api.SQUARE_THUMB_SIZE)
                .transition(withCrossFade());


        photoSize = getPageSize(R.dimen.medium_photo_side);



        final int gridMargin = getResources().getDimensionPixelOffset(R.dimen.grid_margin);
        int spanCount = getResources().getDisplayMetrics().widthPixels / (photoSize + (2 * gridMargin));

        layoutManager = new GridLayoutManager(this, spanCount);
        mImageRv.setLayoutManager(layoutManager);


        mImageRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                outRect.set(gridMargin, gridMargin, gridMargin, gridMargin);
            }
        });

        mImageRv.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                BaseViewHolder viewHolder = (BaseViewHolder) holder;
                glideRequests.clear(viewHolder.getView(R.id.select_img));
            }
        });

        int heightCount = getResources().getDisplayMetrics().heightPixels / photoSize;
        mImageRv.getRecycledViewPool().setMaxRecycledViews(0, 3 * heightCount * 2);
        mImageRv.setItemViewCacheSize(0);


//        if (savedInstanceState != null) {
//            int index = savedInstanceState.getInt(STATE_POSITION_INDEX);
//            int offset = savedInstanceState.getInt(STATE_POSITION_OFFSET);
//            layoutManager.scrollToPositionWithOffset(index, offset);
//        }
    }

    @Override
    protected void initDate() {
        initImageList();
    }

    private void initImageList() {
        getLoaderManager().initLoader(LOADER_TYPE, null, mCallbacks);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID

        };


        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"},
                    IMAGE_PROJECTION[2] + " DESC");

            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            ArrayList<SelectImageBean> images = new ArrayList<>();

            //设置是否显示相机位置
            if (mShowCamera)
                images.add(new SelectImageBean("camera", "", 0));

            //如果有数据变量
            if (cursor != null & cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
//                    XLog.e(path + " " + name + " " + dateTime+images.size());

                    if (!pathExist(path)) {
                        continue;
                    }

                    images.add(new SelectImageBean(name, path, dateTime));
                }
            }
            showListData(images);
        }

        /**
         * 判断文件是否存在
         * @param path
         * @return
         */
        private boolean pathExist(String path) {
            if (!TextUtils.isEmpty(path)) {
                return new File(path).exists();
            }
            return false;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void showListData(ArrayList<SelectImageBean> images) {

        mSelectImageAdapter = new SelectImageAdapter(images);

        mImageRv.setAdapter(mSelectImageAdapter);

        FixedPreloadSizeProvider<SelectImageBean> preloadSizeProvider =
                new FixedPreloadSizeProvider<>(photoSize, photoSize);
        RecyclerViewPreloader<SelectImageBean> preLoader = new RecyclerViewPreloader<>
                (GlideApp.with(this), mSelectImageAdapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);

        mImageRv.addOnScrollListener(preLoader);

    }

    private int getPageSize(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @OnClick({R.id.image_video_tv, R.id.preview_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_video_tv:
                break;
            case R.id.preview_tv:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageRv != null) {
            int index = layoutManager.findFirstVisibleItemPosition();
            View topView = mImageRv.getChildAt(0);
            int offset = topView != null ? topView.getTop() : 0;
            outState.putInt(STATE_POSITION_INDEX, index);
            outState.putInt(STATE_POSITION_OFFSET, offset);
        }
    }


    public class SelectImageAdapter extends BaseQuickAdapter<SelectImageBean, BaseViewHolder>
            implements ListPreloader.PreloadModelProvider<SelectImageBean> {

        public SelectImageAdapter(@Nullable List<SelectImageBean> data) {
            super(R.layout.item_select_image, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, SelectImageBean item) {
            if (TextUtils.isEmpty(item.getPath())) {// show camera image
                holder.setImageResource(R.id.select_img, R.drawable.ic_camera).setVisible(R.id.select_state_img, false);
            } else {

                fullRequest.load(item.getPath())
//                        .thumbnail(thumbRequest.load(item.getPath()))
                        .into((ImageView) holder.getView(R.id.select_img));

//            Log.e("item" , item.getPath());
//                ImageLoader.getInstance().displayImage(mContext,item.getPath(),(ImageView) holder.getView(R.id.select_img));
            }
        }

        @Override
        public List<SelectImageBean> getPreloadItems(int position) {
            return mData.subList(position, position + 1);
        }

        @Override
        public RequestBuilder getPreloadRequestBuilder(SelectImageBean item) {
            return fullRequest./*thumbnail(thumbRequest.load(item.getPath())).*/load(item.getPath());
        }
    }


}
