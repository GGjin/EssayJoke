package com.gg.essayjoke.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageLoader {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static class ImageLoaderHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    private ImageLoader() {
    }

    public static final ImageLoader getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }


    //直接加载网络图片
    public void displayImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
//                .placeholder(R.drawable.logo_liwushuo_grey)
//                .error(R.drawable.logo_liwushuo_grey)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .thumbnail(0.1f)
                .centerCrop()
//                .crossFade()
                .into(imageView);
    }


    //加载SD卡图片
    public void displayImage(Context context, File file, ImageView imageView) {
        Glide
                .with(context)
                .load(file)
//                .centerCrop()
                .into(imageView);

    }

    //加载SD卡图片并设置大小
    public void displayImage(Context context, File file, ImageView imageView, int width, int height) {
        GlideApp.with(context)
                .load(file)
                .override(width, height)
                .centerCrop()
                .into(imageView);
    }

    //加载网络图片并设置大小
    public void displayImage(Context context, String url, ImageView imageView, int width, int height) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .override(width, height)
//                .crossFade()
                .into(imageView);
    }

    //加载drawable图片
    public void displayImage(Context context, int resId, ImageView imageView) {
        GlideApp.with(context)
                .load(resourceIdToUri(context, resId))
//                .crossFade()
                .into(imageView);
    }

    //加载drawable图片显示为圆形图片
    public void displayCricleImage(Context context, int resId, ImageView imageView) {
        GlideApp.with(context)
                .load(resourceIdToUri(context, resId))
//                .crossFade()
//                .transform(new GlideCircleTransformTransform(context))
                .into(imageView);
    }

    //加载网络图片显示为圆形图片
    public void displayCricleImage(Context context, String url, ImageView imageView) {
        GlideApp
                .with(context)
                .load(url)
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
//                .transform(new GlideCircleTransform(context))
//                .crossFade()
                .into(imageView);
    }

    //加载SD卡图片显示为圆形图片
    public void displayCricleImage(Context context, File file, ImageView imageView) {
        GlideApp.with(context)
                .load(file)
                //.centerCrop()
//                .transform(new GlideCircleTransform(context))
                .into(imageView);

    }

    //将资源ID转为Uri
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + File.separator + resourceId);
    }

}