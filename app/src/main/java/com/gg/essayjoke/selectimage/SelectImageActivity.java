package com.gg.essayjoke.selectimage;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gg.essayjoke.R;
import com.gg.essayjoke.selectimage.model.SelectImageBean;
import com.gg.essayjoke.utils.GlideApp;
import com.gg.essayjoke.utils.GlideRequest;
import com.gg.essayjoke.utils.GlideRequests;
import com.gg.framelibrary.BaseSkinActivity;
import com.gg.framelibrary.skin.SkinResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Creator : GG
 * Time    : 2017/10/20
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public class SelectImageActivity extends BaseSkinActivity {

    private ArrayList<SelectImageBean> mImages = new ArrayList<>();

    ImageAdapter mImageAdapter;
    NewAdapter mNewAdapter;

    private static final String STATE_POSITION_INDEX = "state_position_index";
    private static final String STATE_POSITION_OFFSET = "state_position_offset";

    public static final String KEY_IMAGES = "IMAGES";

    public static final String KEY_SELECT_IMAGES = "SELECT_IMAGES";

    public static final String KEY_POSITION = "POSITION";

    private static final int KEY_REQUEST_CODE = 0x0033;

    private static final int PRELOAD_AHEAD_ITEMS = 30;

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
    private ArrayList<SelectImageBean> mSelectList;

    private ArrayList<SelectImageBean> mImageList;

    private GridLayoutManager layoutManager;




    private GlideRequest<Drawable> fullRequest;
    private int photoSize;


    @BindView(R.id.image_rv) RecyclerView mRecyclerView;
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
    protected void initArguments() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

        final GlideRequests glideRequests = GlideApp.with(this);
        fullRequest = glideRequests
                .asDrawable()
                .centerCrop();



        photoSize = getPageSize(R.dimen.medium_photo_side);


        final int gridMargin = getResources().getDimensionPixelOffset(R.dimen.grid_margin);
        int spanCount = getResources().getDisplayMetrics()
                .widthPixels / (photoSize + (2 * gridMargin));

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

//        mImageAdapter = new ImageAdapter(this, mImages,fullRequest);
//
//        mRecyclerView.setAdapter(mImageAdapter);

        mNewAdapter = new NewAdapter(mImages,fullRequest);

        mRecyclerView.setAdapter(mNewAdapter);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                outRect.set(gridMargin, gridMargin, gridMargin, gridMargin);
            }
        });

        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
//                ImageAdapter.ImageViewHolder viewHolder = (ImageAdapter.ImageViewHolder) holder;
//                glideRequests.clear(viewHolder.mImageView);
                BaseViewHolder viewHolder = (BaseViewHolder) holder;
                glideRequests.clear(viewHolder.getView(R.id.select_img));
            }
        });

        int heightCount = getResources().getDisplayMetrics().heightPixels / photoSize;
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 3 * heightCount * 2);
        mRecyclerView.setItemViewCacheSize(0);


        FixedPreloadSizeProvider<SelectImageBean> preloadSizeProvider =
                new FixedPreloadSizeProvider<>(photoSize, photoSize);
//        RecyclerViewPreloader<SelectImageBean> preLoader = new RecyclerViewPreloader<>(
//                GlideApp.with(this), mImageAdapter, preloadSizeProvider, 30);
        RecyclerViewPreloader<SelectImageBean> preLoader = new RecyclerViewPreloader<>(
                GlideApp.with(this), mNewAdapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);

        mRecyclerView.addOnScrollListener(preLoader);

        getSupportLoaderManager().initLoader(0, null, mCallbacks);
    }

    @Override
    protected void initDate() {

    }

    private int getPageSize(int id) {
        return getResources().getDimensionPixelSize(id);
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
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3]
                            + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"},
                    IMAGE_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.getCount() > 0) {
                data.moveToFirst();
                while (data.moveToNext()) {
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    mImages.add(new SelectImageBean(name, path, time));
                }
            }

            mNewAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    /**
     * 根据选中图片的数量改变提示文字
     */
    private void exchangeViewShow() {
        if (mSelectList.size() > 0) { //至少选择了一张

        } else {

        }
        mNumTv.setText(mSelectList.size() + "/" + mMaxCount);
    }

    public class ImageAdapter<ImageViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter implements ListPreloader
            .PreloadModelProvider<SelectImageBean> {
        private GlideRequest<Drawable> fullRequest;

        private ArrayList<SelectImageBean> mData;
        private Context mContext;

        public ImageAdapter(Context context, ArrayList<SelectImageBean> list, GlideRequest<Drawable> fullRequest) {
            mData = list;
            mContext = context;
            this.fullRequest = fullRequest;

        }


        @Override
        public ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageAdapter.ImageViewHolder viewHolder = new ImageAdapter.ImageViewHolder(LayoutInflater.from(mContext).inflate(R
                    .layout.item_select_image, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageAdapter.ImageViewHolder viewHolder = (ImageAdapter.ImageViewHolder) holder;
            fullRequest.load(mData.get(position).getPath()).into(viewHolder.mImageView);
//            Glide.with(mContext).load(mData.get(position)).into(viewHolder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        private class ImageViewHolder extends RecyclerView.ViewHolder {

            private ImageView mImageView;

            public ImageViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.select_img);
            }
        }

        @Override
        public List<SelectImageBean> getPreloadItems(int position) {
            return mData.subList(position, position + 1);
        }

        @Override
        public RequestBuilder getPreloadRequestBuilder(SelectImageBean item) {
            return fullRequest
                    /*.thumbnail(thumbRequest.load(item.getPath()))*/
                    .load(item.getPath());
        }

    }

    public class NewAdapter extends BaseQuickAdapter<SelectImageBean, BaseViewHolder> implements ListPreloader
            .PreloadModelProvider<SelectImageBean> {
        GlideRequest fullRequest;

        public NewAdapter(@Nullable List<SelectImageBean> data, GlideRequest fullRequest) {
            super(R.layout.item_select_image, data);
            this.fullRequest = fullRequest;
        }

        protected void convert(BaseViewHolder helper, SelectImageBean item) {
           fullRequest.load(item.getPath()).into((ImageView) helper.getView(R.id.select_img));
        }

        @Override
        public List<SelectImageBean> getPreloadItems(int position) {
            return mData.subList(position, position + 1);
        }

        @Override
        public RequestBuilder getPreloadRequestBuilder(SelectImageBean item) {
            return fullRequest
                    /*.thumbnail(thumbRequest.load(item.getPath()))*/
                    .load(item.getPath());
        }
    }
}
