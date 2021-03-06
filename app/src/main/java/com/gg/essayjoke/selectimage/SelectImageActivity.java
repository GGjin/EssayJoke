package com.gg.essayjoke.selectimage;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.gg.essayjoke.R;
import com.gg.essayjoke.selectimage.model.SelectImageBean;
import com.gg.essayjoke.utils.GlideApp;
import com.gg.essayjoke.utils.GlideRequest;
import com.gg.essayjoke.utils.GlideRequests;
import com.gg.framelibrary.BaseSkinActivity;
import com.gg.framelibrary.skin.SkinResource;
import com.gg.framelibrary.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Creator:  GG
 * Time   :  2017/9/27
 * Mail   :  gg.jin.yu@gmail.com
 * Explain:
 */

public class SelectImageActivity extends BaseSkinActivity {

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

    private ImageAdapter mImageAdapter;

    private int photoSize;


    @BindView(R.id.image_rv) RecyclerView mImageRv;
    @BindView(R.id.image_video_tv) TextView mImageVideoTv;
    @BindView(R.id.num_tv) TextView mNumTv;
    @BindView(R.id.preview_tv) TextView mPreviewTv;

    @Override
    public void changeSkin(SkinResource skinResource) {

    }

    @Override
    protected void initArguments() {
        mSelectList = new ArrayList<>();
        mImageList = new ArrayList<>();
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

        photoSize = getPageSize(R.dimen.medium_photo_side);


        final int gridMargin = getResources().getDimensionPixelOffset(R.dimen.grid_margin);
        int spanCount = getResources().getDisplayMetrics()
                .widthPixels / (photoSize + (2 * gridMargin));

        layoutManager = new GridLayoutManager(this, spanCount);
        mImageRv.setLayoutManager(layoutManager);

        mImageAdapter = new ImageAdapter(this,mImageList,mSelectList,fullRequest);

        mImageRv.setAdapter(mImageAdapter);


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
                ImageAdapter.ImageViewHolder viewHolder = (ImageAdapter.ImageViewHolder) holder;
                glideRequests.clear(viewHolder.mImageView);
            }
        });

        int heightCount = getResources().getDisplayMetrics().heightPixels / photoSize;
        mImageRv.getRecycledViewPool().setMaxRecycledViews(0, 3 * heightCount * 2);
        mImageRv.setItemViewCacheSize(0);


        FixedPreloadSizeProvider<SelectImageBean> preloadSizeProvider =
                new FixedPreloadSizeProvider<>(photoSize, photoSize);
        RecyclerViewPreloader<SelectImageBean> preLoader = new RecyclerViewPreloader<>(
                GlideApp.with(this), mImageAdapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);

        mImageRv.addOnScrollListener(preLoader);

    }

    @Override
    protected void initDate() {

        initImageList();

        exchangeViewShow();
    }

    /**
     * 根据选中图片的数量改变提示文字
     */
    private void exchangeViewShow() {
        if (mSelectList.size() > 0) { //至少选择了一张

        } else {

        }
        mNumTv.setText(mSelectList.size() + "/" + mMaxCount);
    }

    /**
     * 异步任务获取手机内的所有图片
     */
    private void initImageList() {
        getSupportLoaderManager().initLoader(LOADER_TYPE, null, mCallbacks);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

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
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                                    + IMAGE_PROJECTION[3] + "=? ",
                            new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                    return cursorLoader;
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    ArrayList<SelectImageBean> images = new ArrayList<>();

                    //如果有数据变量
                    if (data != null & data.getCount() > 0) {
                        data.moveToFirst();
                        while (data.moveToNext()) {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
//                    XLog.e(path + " " + name + " " + dateTime+images.size());

                            if (!pathExist(path)) {
                                continue;
                            }

                            images.add(new SelectImageBean(name, path, dateTime));
                        }
                    }

                    mImageList.clear();

                    //设置是否显示相机位置
                    if (mShowCamera) {
                        mImageList.add(new SelectImageBean("camera", "", 0));
                    }
                    mImageList.addAll(images);

                    mImageAdapter.notifyDataSetChanged();
//
                }

                @Override
                public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

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

            };

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
            default:

        }
    }
    public class ImageAdapter<ImageViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter  implements ListPreloader.PreloadModelProvider<SelectImageBean>{
        private GlideRequest<Drawable> fullRequest;

        private ArrayList<SelectImageBean> mData;
        private ArrayList<SelectImageBean> mResultList;
        private Context mContext;

        public ImageAdapter(Context context, ArrayList<SelectImageBean> list,ArrayList<SelectImageBean> mSelectList,GlideRequest<Drawable> fullRequest) {
            mData = list;
            mResultList=mSelectList;
            mContext = context;
            this.fullRequest = fullRequest;

        }


        @Override
        public ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageAdapter.ImageViewHolder(LayoutInflater.from(mContext).inflate(R
                    .layout.item_select_image, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            ImageAdapter.ImageViewHolder viewHolder = (ImageAdapter.ImageViewHolder) holder;
            final SelectImageBean item = mData.get(position);
            if (TextUtils.isEmpty(item.getPath())) { // show camera image
                viewHolder.mImageView.setBackgroundResource(R.drawable.ic_camera);
                viewHolder.mStateImageView.setVisibility(View.INVISIBLE);

            } else {

                fullRequest.load(item.getPath())
//                        .thumbnail(thumbRequest.load(item.getPath()))
                        .into(viewHolder.mImageView);
                viewHolder.mStateImageView.setVisibility(View.VISIBLE);
            }

            if (mResultList.contains(item)) {
                viewHolder.mStateImageView.setSelected(true);
            } else {
                viewHolder.mStateImageView.setSelected(false);
            }

            viewHolder.mStateImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mResultList.contains(item)) {
                        mResultList.remove(item);
                    } else {
                        if (mResultList.size() >= mMaxCount) {
                            ToastUtil.showShort(getString(R.string.toast_select_image, mMaxCount));
                            return;
                        } else {
                            mResultList.add(item);
                        }
                    }
                    notifyItemChanged(holder.getAdapterPosition(), 1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        private class ImageViewHolder extends RecyclerView.ViewHolder {

            private ImageView mImageView;
            private ImageView mStateImageView;

            public ImageViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.select_img);
                mStateImageView = itemView.findViewById(R.id.select_state_img);
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
}
