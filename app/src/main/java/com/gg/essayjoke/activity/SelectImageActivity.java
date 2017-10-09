package com.gg.essayjoke.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.gg.essayjoke.R;
import com.gg.essayjoke.adapter.SelectImageAdapter;
import com.gg.essayjoke.model.SelectImageBean;
import com.gg.framelibrary.BaseSkinActivity;
import com.gg.framelibrary.skin.SkinResource;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Creator:  GG
 * Time   :  2017/9/27
 * Mail   :  gg.jin.yu@gmail.com
 * Explain:
 */

public class SelectImageActivity extends BaseSkinActivity {

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
            //如果有数据变量
            if (cursor != null & cursor.getCount() > 0) {
                ArrayList<SelectImageBean> images = new ArrayList<>();

                //设置是否显示相机位置
                if (mShowCamera) {
                    SelectImageBean selectImageBean = new SelectImageBean("camera", "", 0);
                    images.add(selectImageBean);
                }


                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    XLog.e(path + " " + name + " " + dateTime);

                    if (!pathExist(path)) {
                        continue;
                    }

                    SelectImageBean selectImageBean = new SelectImageBean(name, path, dateTime);
                    images.add(selectImageBean);

                    showListData(images);
                }
            }
        }

        /**
         * 判断文件是否存在
         * @param path
         * @return
         */
        private boolean pathExist(String path) {
            if (!TextUtils.isEmpty(path)) {
                new File(path).exists();
            }
            return false;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void showListData(ArrayList<SelectImageBean> images) {
        SelectImageAdapter mSelectImageAdapter = new SelectImageAdapter(images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mImageRv.setLayoutManager(gridLayoutManager);
        mImageRv.setAdapter(mSelectImageAdapter);
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
}
